package com.reactlibrary.rnwifi;

import static com.reactlibrary.rnwifi.mappers.WifiScanResultsMapper.mapWifiScanResults;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.reactlibrary.rnwifi.errors.ConnectErrorCodes;
import com.reactlibrary.rnwifi.errors.DisconnectErrorCodes;
import com.reactlibrary.rnwifi.errors.GetCurrentWifiSSIDErrorCodes;
import com.reactlibrary.rnwifi.errors.IsRemoveWifiNetworkErrorCodes;
import com.reactlibrary.rnwifi.errors.LoadWifiListErrorCodes;
import com.reactlibrary.rnwifi.receivers.WifiScanResultReceiver;
import com.reactlibrary.rnwifi.utils.LocationUtils;
import com.reactlibrary.rnwifi.utils.PermissionUtils;
import com.thanosfisherman.wifiutils.WifiUtils;
import com.thanosfisherman.wifiutils.wifiConnect.DisconnectCallbackHolder;
import com.thanosfisherman.wifiutils.wifiDisconnect.DisconnectionErrorCode;
import com.thanosfisherman.wifiutils.wifiDisconnect.DisconnectionSuccessListener;
import com.thanosfisherman.wifiutils.wifiRemove.RemoveErrorCode;
import com.thanosfisherman.wifiutils.wifiRemove.RemoveSuccessListener;

import java.util.List;

public class RNWifiModule extends ReactContextBaseJavaModule {
    private final WifiManager wifi;
    private final ReactApplicationContext context;

    RNWifiModule(ReactApplicationContext context) {
        super(context);

        // TODO: get when needed
        wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        this.context = context;
    }

    @Override
    @NonNull
    public String getName() {
        return "WifiManager";
    }

    /**
     * Returns a list of nearby WiFI networks.
     */
    @ReactMethod
    public void loadWifiList(final Promise promise) {
        final boolean locationPermissionGranted = PermissionUtils.isLocationPermissionGranted(context);
        if (!locationPermissionGranted) {
            promise.reject(LoadWifiListErrorCodes.locationPermissionMissing.toString(), "Location permission (ACCESS_FINE_LOCATION) is not granted");
            return;
        }

        final boolean isLocationOn = LocationUtils.isLocationOn(context);
        if (!isLocationOn) {
            promise.reject(LoadWifiListErrorCodes.locationServicesOff.toString(), "Location service is turned off");
            return;
        }

        try {
            final List<ScanResult> scanResults = wifi.getScanResults();
            final WritableArray results = mapWifiScanResults(scanResults);
            promise.resolve(results);
        } catch (final Exception exception) {
            promise.reject(LoadWifiListErrorCodes.exception.toString(), exception.getMessage());
        }
    }

    @Deprecated
    @ReactMethod
    public void forceWifiUsage(final boolean useWifi, final Promise promise) {
        forceWifiUsageWithOptions(useWifi, null, promise);
    }

    /**
     * Use this to execute api calls to a wifi network that does not have internet access.
     * <p>
     * Useful for commissioning IoT devices.
     * <p>
     * This will route all app network requests to the network (instead of the mobile connection).
     * It is important to disable it again after using as even when the app disconnects from the wifi
     * network it will keep on routing everything to wifi.
     *
     * @param useWifi boolean to force wifi off or on
     * @param options `noInternet` to indicate that the wifi network does not have internet connectivity
     */
    @ReactMethod
    public void forceWifiUsageWithOptions(final boolean useWifi, @Nullable final ReadableMap options, final Promise promise) {
        boolean canWriteFlag = false;

        try {
            if (useWifi) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    canWriteFlag = Settings.System.canWrite(context);
                    int networkStatePermission = context.checkCallingOrSelfPermission(Manifest.permission.CHANGE_NETWORK_STATE);

                    if (!canWriteFlag && networkStatePermission != PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        intent.setData(Uri.parse("package:" + context.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            }

            final ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivityManager == null) {
                promise.reject(ForceWifiUsageErrorCodes.couldNotGetConnectivityManager.toString(), "Failed to get the ConnectivityManager.");
                return;
            }

            if (useWifi) {
                final NetworkRequest.Builder networkRequestBuilder = new NetworkRequest.Builder()
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI);

                if (options != null && options.getBoolean("noInternet")) {
                    networkRequestBuilder.removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
                }

                connectivityManager.requestNetwork(networkRequestBuilder.build(), new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(@NonNull final Network network) {
                        super.onAvailable(network);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            connectivityManager.bindProcessToNetwork(network);
                        } else {
                            ConnectivityManager.setProcessDefaultNetwork(network);
                        }

                        connectivityManager.unregisterNetworkCallback(this);

                        promise.resolve(null);
                    }
                });
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    connectivityManager.bindProcessToNetwork(null);
                } else {
                    ConnectivityManager.setProcessDefaultNetwork(null);
                }

                promise.resolve(null);
            }
        } catch (Exception ex) {
            promise.reject("", ex.getMessage());
        }
    }

    /**
     * Method to check if wifi is enabled.
     */
    @ReactMethod
    public void isEnabled(final Promise promise) {
        if (this.wifi == null) {
            promise.reject(IsEnabledErrorCodes.couldNotGetWifiManager.toString(), "Failed to initialize the WifiManager.");
            return;
        }

        promise.resolve(wifi.isWifiEnabled());
    }

    /**
     * Method to set the WiFi on or off on the user's device.
     *
     * @param enabled to enable/disable wifi
     */
    @ReactMethod
    public void setEnabled(final boolean enabled) {
        wifi.setWifiEnabled(enabled);
    }

    /**
     * Use this to connect with a wifi network.
     * Example:  wifi.findAndConnect(ssid, password, false);
     * The promise will resolve with the message 'connected' when the user is connected on Android.
     *
     * @param SSID     name of the network to connect with
     * @param password password of the network to connect with
     * @param isWep    only for iOS
     * @param promise  to send success/error feedback
     */
    @ReactMethod
    public void connectToProtectedSSID(@NonNull final String SSID, @NonNull final String password, final boolean isWep, final Promise promise) {
        final boolean locationPermissionGranted = PermissionUtils.isLocationPermissionGranted(context);
        if (!locationPermissionGranted) {
            promise.reject(ConnectErrorCodes.locationPermissionMissing.toString(), "Location permission (ACCESS_FINE_LOCATION) is not granted");
            return;
        }

        final boolean isLocationOn = LocationUtils.isLocationOn(context);
        if (!isLocationOn) {
            promise.reject(ConnectErrorCodes.locationServicesOff.toString(), "Location service is turned off");
            return;
        }

        if (!wifi.isWifiEnabled() && !wifi.setWifiEnabled(true)) {
            promise.reject(ConnectErrorCodes.couldNotEnableWifi.toString(), "On Android 10, the user has to enable wifi manually.");
            return;
        }


        this.removeWifiNetwork(SSID, promise, () -> {
            connectToWifiDirectly(SSID, password, promise);
        });
    }


    /**
     * Returns if the device is currently connected to a WiFi network.
     */
    @ReactMethod
    public void connectionStatus(final Promise promise) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) getReactApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            promise.resolve(false);
            return;
        }

        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo == null) {
            promise.resolve(false);
            return;
        }

        promise.resolve(wifiInfo.isConnected());
    }

    /**
     * Disconnect currently connected WiFi network.
     */
    @ReactMethod
    public void disconnect(final Promise promise) {
        WifiUtils.withContext(this.context).disconnect(new DisconnectionSuccessListener() {
            @Override
            public void success() {
                promise.resolve(true);
            }

            @Override
            public void failed(@NonNull DisconnectionErrorCode errorCode) {
                switch (errorCode) {
                    case COULD_NOT_GET_WIFI_MANAGER: {
                        promise.reject(DisconnectErrorCodes.couldNotGetWifiManager.toString(), "Could not get WifiManager.");
                    }
                    case COULD_NOT_GET_CONNECTIVITY_MANAGER: {
                        promise.reject(DisconnectErrorCodes.couldNotGetConnectivityManager.toString(), "Could not get Connectivity Manager.");
                    }
                    default:
                    case COULD_NOT_DISCONNECT: {
                        promise.resolve(false);
                    }
                }
            }
        });
    }

    /**
     * This method will return current SSID
     *
     * @param promise to send error/result feedback
     */
    @ReactMethod
    public void getCurrentWifiSSID(final Promise promise) {
        String ssid = getWifiSSID();
        if (ssid == null) {
            promise.reject(GetCurrentWifiSSIDErrorCodes.couldNotDetectSSID.toString(), "Not connected or connecting.");
            return;
        }

        promise.resolve(ssid);
    }

    /**
     * Returns the BSSID (basic service set identifier) of the currently connected WiFi network.
     */
    @ReactMethod
    public void getBSSID(final Promise promise) {
        final WifiInfo info = wifi.getConnectionInfo();
        final String bssid = info.getBSSID();
        promise.resolve(bssid.toUpperCase());
    }

    /**
     * Returns the RSSI (received signal strength indicator) of the currently connected WiFi network.
     */
    @ReactMethod
    public void getCurrentSignalStrength(final Promise promise) {
        final int linkSpeed = wifi.getConnectionInfo().getRssi();
        promise.resolve(linkSpeed);
    }

    /**
     * Returns the frequency of the currently connected WiFi network.
     */
    @ReactMethod
    public void getFrequency(final Promise promise) {
        final WifiInfo info = wifi.getConnectionInfo();
        final int frequency = info.getFrequency();
        promise.resolve(frequency);
    }

    /**
     * Returns the IP of the currently connected WiFi network.
     */
    @ReactMethod
    public void getIP(final Promise promise) {
        final WifiInfo info = wifi.getConnectionInfo();
        final String stringIP = longToIP(info.getIpAddress());
        promise.resolve(stringIP);
    }

    /**
     * This method will remove the wifi network configuration.
     * If you are connected to that network, it will disconnect.
     *
     * @param SSID wifi SSID to remove configuration for
     */
    @ReactMethod
    public void isRemoveWifiNetwork(final String SSID, final Promise promise) {
        removeWifiNetwork(SSID, promise, null);
    }

    private void removeWifiNetwork(final String SSID, final Promise promise, final Runnable onSuccess) {
        final boolean locationPermissionGranted = PermissionUtils.isLocationPermissionGranted(context);
        if (!locationPermissionGranted) {
            promise.reject(IsRemoveWifiNetworkErrorCodes.locationPermissionMissing.toString(), "Location permission (ACCESS_FINE_LOCATION) is not granted");
            return;
        }

        WifiUtils.withContext(this.context)
                .remove(SSID, new RemoveSuccessListener() {
                    @Override
                    public void success() {
                        if (onSuccess != null) {
                            onSuccess.run();
                            return;
                        }
                        promise.resolve(true);
                    }

                    @Override
                    public void failed(@NonNull RemoveErrorCode errorCode) {
                        switch (errorCode) {
                            case COULD_NOT_GET_WIFI_MANAGER: {
                                promise.reject(IsRemoveWifiNetworkErrorCodes.couldNotGetWifiManager.toString(), "Could not get WifiManager.");
                            }
                            case COULD_NOT_GET_CONNECTIVITY_MANAGER: {
                                promise.reject(IsRemoveWifiNetworkErrorCodes.couldNotGetConnectivityManager.toString(), "Could not get Connectivity Manager.");
                            }
                            default:
                            case COULD_NOT_REMOVE: {
                                if (onSuccess != null) {
                                    onSuccess.run();
                                    return;
                                }
                                promise.resolve(false);
                            }
                        }
                    }
                });
    }

    /**
     * Similar to `loadWifiList` but it forcefully starts a new WiFi scan and only passes the results when the scan is done.
     */
    @ReactMethod
    public void reScanAndLoadWifiList(final Promise promise) {
        final WifiScanResultReceiver wifiScanResultReceiver = new WifiScanResultReceiver(wifi, promise);
        getReactApplicationContext().registerReceiver(wifiScanResultReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifi.startScan();
    }

    private void connectToWifiDirectly(@NonNull final String SSID, @NonNull final String password, final Promise promise) {
        if (isAndroidTenOrLater()) {
            connectAndroidQ(SSID, password, promise);
        } else {
            connectPreAndroidQ(SSID, password, promise);
        }
    }

    private void connectPreAndroidQ(@NonNull final String SSID, @NonNull final String password, final Promise promise) {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = formatWithBackslashes(SSID);

        if (!isNullOrEmpty(password)) {
            stuffWifiConfigurationWithWPA2(wifiConfig, password);
        } else {
            stuffWifiConfigurationWithoutEncryption(wifiConfig);
        }

        int netId = wifi.addNetwork(wifiConfig);
        if (netId == -1) {
            promise.reject(ConnectErrorCodes.unableToConnect.toString(), String.format("Could not add or update network configuration with SSID %s", SSID));
            return;
        }
        if (!wifi.enableNetwork(netId, true)) {
            promise.reject(ConnectErrorCodes.unableToConnect.toString(), String.format("Failed to enable network with %s", SSID));
            return;
        }
        if (!wifi.reconnect()) {
            promise.reject(ConnectErrorCodes.unableToConnect.toString(), String.format("Failed to reconnect with %s", SSID));
            return;
        }
        if (!pollForValidSSID(10, SSID)) {
            promise.reject(ConnectErrorCodes.unableToConnect.toString(), String.format("Failed to connect with %s", SSID));
            return;
        }
        promise.resolve("connected");
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void connectAndroidQ(@NonNull final String SSID, @NonNull final String password, final Promise promise) {
        WifiNetworkSpecifier.Builder wifiNetworkSpecifier = new WifiNetworkSpecifier.Builder()
                .setSsid(SSID);

        if (!isNullOrEmpty(password)) {
            wifiNetworkSpecifier.setWpa2Passphrase(password);
        }

        NetworkRequest nr = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .setNetworkSpecifier(wifiNetworkSpecifier.build())
                .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
                .build();

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        ConnectivityManager.NetworkCallback networkCallback = new
                ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        super.onAvailable(network);
                        DisconnectCallbackHolder.getInstance().bindProcessToNetwork(network);
                        connectivityManager.setNetworkPreference(ConnectivityManager.DEFAULT_NETWORK_PREFERENCE);
                        if (!pollForValidSSID(3, SSID)) {
                            promise.reject(ConnectErrorCodes.android10ImmediatelyDroppedConnection.toString(), "Firmware bugs on OnePlus prevent it from connecting on some firmware versions.");
                            return;
                        }
                        promise.resolve("connected");
                    }

                    @Override
                    public void onUnavailable() {
                        super.onUnavailable();
                        promise.reject(ConnectErrorCodes.userDenied.toString(), "On Android 10, the user cancelled connecting (via System UI).");
                    }

                    @Override
                    public void onLost(@NonNull Network network) {
                        super.onLost(network);
                        DisconnectCallbackHolder.getInstance().unbindProcessFromNetwork();
                        DisconnectCallbackHolder.getInstance().disconnect();
                    }
                };
        DisconnectCallbackHolder.getInstance().addNetworkCallback(networkCallback, connectivityManager);
        DisconnectCallbackHolder.getInstance().requestNetwork(nr);
    }

    private static String longToIP(int longIp) {
        StringBuilder sb = new StringBuilder();
        String[] strip = new String[4];
        strip[3] = String.valueOf((longIp >>> 24));
        strip[2] = String.valueOf((longIp & 0x00FFFFFF) >>> 16);
        strip[1] = String.valueOf((longIp & 0x0000FFFF) >>> 8);
        strip[0] = String.valueOf((longIp & 0x000000FF));
        sb.append(strip[0]);
        sb.append(".");
        sb.append(strip[1]);
        sb.append(".");
        sb.append(strip[2]);
        sb.append(".");
        sb.append(strip[3]);
        return sb.toString();
    }

    private boolean pollForValidSSID(int maxSeconds, String expectedSSID) {
        try {
            for (int i = 0; i < maxSeconds; i++) {
                String ssid = this.getWifiSSID();
                if (ssid != null && ssid.equalsIgnoreCase(expectedSSID)) {
                    return true;
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            return false;
        }
        return false;
    }

    private String getWifiSSID() {
        WifiInfo info = wifi.getConnectionInfo();

        // This value should be wrapped in double quotes, so we need to unwrap it.
        String ssid = info.getSSID();
        if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
            ssid = ssid.substring(1, ssid.length() - 1);
        }

        // Android returns `<unknown ssid>` when it is not connected or still connecting
        if (ssid.equals("<unknown ssid>")) {
            ssid = null;
        }

        return ssid;
    }

    /**
     * @return true if the current sdk is above or equal to Android Q
     */
    private boolean isAndroidTenOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    /**
     * @return true if the value is null or empty
     */
    private boolean isNullOrEmpty(final String value) {
        return value == null || value.trim().isEmpty();
    }

    private void stuffWifiConfigurationWithWPA2(final WifiConfiguration wifiConfiguration, final String password) {
        if (password.matches("[0-9A-Fa-f]{64}")) {
            wifiConfiguration.preSharedKey = password;
        } else {
            wifiConfiguration.preSharedKey = formatWithBackslashes(password);
        }

        wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

        wifiConfiguration.status = WifiConfiguration.Status.ENABLED;

        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);


        wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
    }

    private void stuffWifiConfigurationWithoutEncryption(final WifiConfiguration wifiConfiguration) {
        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
    }

    private String formatWithBackslashes(final String value) {
        return String.format("\"%s\"", value);
    }
}
