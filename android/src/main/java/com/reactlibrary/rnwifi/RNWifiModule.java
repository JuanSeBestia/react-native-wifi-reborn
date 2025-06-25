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
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.provider.Settings;
import android.os.Build;
import android.content.BroadcastReceiver;
import android.net.wifi.WifiNetworkSuggestion;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.ReadableArray;
import com.reactlibrary.rnwifi.errors.ConnectErrorCodes;
import com.reactlibrary.rnwifi.errors.DisconnectErrorCodes;
import com.reactlibrary.rnwifi.errors.ForceWifiUsageErrorCodes;
import com.reactlibrary.rnwifi.errors.GetCurrentWifiSSIDErrorCodes;
import com.reactlibrary.rnwifi.errors.IsEnabledErrorCodes;
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
    private Network joinedNetwork;
    private final WifiManager wifi;
    private final ReactApplicationContext context;
    private static String TAG = "RNWifiModule";

    private static final int TIMEOUT_MILLIS = 15000;
    private static final int TIMEOUT_REMOVE_MILLIS = 10000;

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
        if(!assertLocationPermissionGranted(promise)){
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
              // thanks to https://github.com/flutternetwork/WiFiFlutter/pull/309
              // SDK-31 If not previously in a disconnected state, select the joinedNetwork to ensure
              // the correct network is used for communications, else fallback to network manager network.
              // https://developer.android.com/about/versions/12/behavior-changes-12#concurrent-connections
              if (joinedNetwork != null) {
                selectNetwork(joinedNetwork, connectivityManager);
                promise.resolve(null);
              } else {
                final NetworkRequest.Builder networkRequestBuilder = new NetworkRequest.Builder()
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI);

                if (options != null && options.getBoolean("noInternet")) {
                    networkRequestBuilder.removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
                }

                connectivityManager.requestNetwork(networkRequestBuilder.build(), new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(@NonNull final Network network) {
                        super.onAvailable(network);
                        selectNetwork(network, connectivityManager);

                        connectivityManager.unregisterNetworkCallback(this);

                        promise.resolve(null);
                    }
                });
              }
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
        if (isAndroidTenOrLater()) {
            openWifiSettings();
        } else {
            wifi.setWifiEnabled(enabled);
        }
    }

    /**
     * Use this to open a wifi settings panel.
     * For Android Q and above.
     */
    @ReactMethod
    public void openWifiSettings() {
        Intent intent = new Intent(Settings.Panel.ACTION_WIFI);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.context.startActivity(intent);
    }


    /**
     * Use this to connect with a wifi network.
     * Example:  wifi.findAndConnect(ssid, password, false);
     * The promise will resolve with the message 'connected' when the user is connected on Android.
     *
     * @param SSID     name of the network to connect with
     * @param password password of the network to connect with
     * @param isWep    only for iOS
     * @param isHidden only for Android, use if WiFi is hidden
     * @param promise  to send success/error feedback
     */
    @ReactMethod
    public void connectToProtectedSSID(@NonNull final String SSID, @NonNull final String password, final boolean isWep, final boolean isHidden, final Promise promise) {
        if(!assertLocationPermissionGranted(promise)){
            return;
        }

        if (!wifi.isWifiEnabled() && !wifi.setWifiEnabled(true)) {
            promise.reject(ConnectErrorCodes.couldNotEnableWifi.toString(), "On Android 10, the user has to enable wifi manually.");
            return;
        }

        this.removeWifiNetwork(SSID, promise, () -> {
            connectToWifiDirectly(SSID, password, isHidden, TIMEOUT_MILLIS, promise);
        }, TIMEOUT_REMOVE_MILLIS);
    }


    /**
     * Use this to connect with a wifi network.
     * Example:  wifi.findAndConnect(ssid, password, false);
     * The promise will resolve with the message 'connected' when the user is connected on Android.
     *
     * @param options to connect with a wifi network
     * @param promise to send success/error feedback
     */
    @ReactMethod
    public void connectToProtectedWifiSSID(@NonNull ReadableMap options, final Promise promise) {
        if (!assertLocationPermissionGranted(promise)) {
            return;
        }

        if (!wifi.isWifiEnabled() && !wifi.setWifiEnabled(true)) {
            promise.reject(ConnectErrorCodes.couldNotEnableWifi.toString(), "On Android 10, the user has to enable wifi manually.");
            return;
        }

        String ssid = options.getString("ssid");
        String password = options.getString("password");
        boolean isHidden = options.hasKey("isHidden") && options.getBoolean("isHidden");
        int secondsTimeout = options.hasKey("timeout") ? options.getInt("timeout") * 1000 : TIMEOUT_MILLIS;

        this.removeWifiNetwork(ssid, promise, () -> {
            assert ssid != null;
            connectToWifiDirectly(ssid, password, isHidden, secondsTimeout, promise);
        }, TIMEOUT_REMOVE_MILLIS);
    }


    /**
     * Use this to suggest a list of Wi-Fi networks on Android.
     * The promise will resolve immediately when the suggestions are processed successfully.
     * This method only works for Android and requires a minimum SDK version of 29.
     *
     * @param networkConfigs list of network configurations containing SSID, password, WPA3 flag, and app interaction flag
     * @param promise        to send success/error feedback
     */
    @ReactMethod
    public void suggestWifiNetwork(@NonNull final ReadableArray networkConfigs, final Promise promise) {
        if (!assertLocationPermissionGranted(promise)) {
            return;
        }
        List<WifiNetworkSuggestion> suggestionsList = new ArrayList<>();
        for (int i = 0; i < networkConfigs.size(); i++) {
            ReadableMap config = networkConfigs.getMap(i);
            if (config == null) {
                continue;
            }
            String ssid = config.getString("ssid");
            String password = config.hasKey("password") ? config.getString("password") : "";
            boolean isWpa3 = config.hasKey("isWpa3") && config.getBoolean("isWpa3");
            boolean isAppInteractionRequired = config.hasKey("isAppInteractionRequired") && config.getBoolean("isAppInteractionRequired");
            WifiNetworkSuggestion.Builder builder = new WifiNetworkSuggestion.Builder()
                    .setSsid(ssid);
            if (isAppInteractionRequired) {
                builder.setIsAppInteractionRequired(true); // Conditional (Needs location permission)
            }
            if (isWpa3) {
                builder.setWpa3Passphrase(password);
            } else if (!password.isEmpty()) {
                builder.setWpa2Passphrase(password);
            }
            suggestionsList.add(builder.build());
        }
        final WifiManager wifiManager = (WifiManager) getReactApplicationContext().getSystemService(Context.WIFI_SERVICE);
        final int status = wifiManager.addNetworkSuggestions(suggestionsList);

        // Handle different status codes appropriately
        switch (status) {
            case WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS:
                promise.resolve("suggestions_added");
                break;
            case WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_ADD_DUPLICATE:
                // Treat duplicates as success since suggestions are already present
                promise.resolve("suggestions_already_added");
                break;
            case WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_ADD_EXCEEDS_MAX_PER_APP:
                promise.reject(ConnectErrorCodes.unableToConnect.toString(), "Maximum number of network suggestions exceeded for this app.");
                break;
            case WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_INTERNAL:
                promise.reject(ConnectErrorCodes.unableToConnect.toString(), "Internal error occurred while adding network suggestions.");
                break;
            case WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_APP_DISALLOWED:
                promise.reject(ConnectErrorCodes.unableToConnect.toString(), "App is not allowed to add network suggestions.");
                break;
            default:
                promise.reject(ConnectErrorCodes.unableToConnect.toString(), "Failed to add network suggestions. Status: " + status);
                break;
        }
    }

    private boolean getConnectionStatus() {
        final ConnectivityManager connectivityManager = (ConnectivityManager) getReactApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        }
        /**
         * Check if there's an existing per-app connection, otherwise check the "main" WiFi state
         */
        if (isAndroidTenOrLater() && joinedNetwork != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(joinedNetwork);
            boolean hasWifiTransport = capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
            boolean isNetworkAvailable = capabilities != null &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_SUSPENDED) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED);

            return hasWifiTransport && isNetworkAvailable;
        }

        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo == null) {
            return false;
        }
        return wifiInfo.isConnected();
    }

    /**
     * Returns if the device is currently connected to a WiFi network.
     */
    @ReactMethod
    public void connectionStatus(final Promise promise) {
        promise.resolve(this.getConnectionStatus());
        return;
    }

    /**
     * Disconnect currently connected WiFi network.
     */
    @ReactMethod
    public void disconnect(final Promise promise) {
        final int timeout = TIMEOUT_REMOVE_MILLIS;
        final Handler timeoutHandler = new Handler(Looper.getMainLooper());
        final Runnable timeoutRunnable = () -> {
            promise.reject(ConnectErrorCodes.timeoutOccurred.toString(), "Connection timeout");
            if (isAndroidTenOrLater()) {
                DisconnectCallbackHolder.getInstance().unbindProcessFromNetwork();
                DisconnectCallbackHolder.getInstance().disconnect();
            }
        };

        timeoutHandler.postDelayed(timeoutRunnable, timeout);

        WifiUtils.withContext(this.context).disconnect(new DisconnectionSuccessListener() {
            @Override
            public void success() {
                timeoutHandler.removeCallbacks(timeoutRunnable);
                promise.resolve(true);
            }

            @Override
            public void failed(@NonNull DisconnectionErrorCode errorCode) {
                timeoutHandler.removeCallbacks(timeoutRunnable);
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
        if(!assertLocationPermissionGranted(promise)){
            return;
        }

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
        removeWifiNetwork(SSID, promise, null, TIMEOUT_REMOVE_MILLIS);
    }

    private void removeWifiNetwork(final String SSID, final Promise promise, final Runnable onSuccess, final int timeout) {
        final boolean locationPermissionGranted = PermissionUtils.isLocationPermissionGranted(context);
        if (!locationPermissionGranted) {
            promise.reject(IsRemoveWifiNetworkErrorCodes.locationPermissionMissing.toString(), "Location permission (ACCESS_FINE_LOCATION) is not granted");
            return;
        }

        final Handler timeoutHandler = new Handler(Looper.getMainLooper());
        final Runnable timeoutRunnable = () -> {
            promise.reject(ConnectErrorCodes.timeoutOccurred.toString(), "Connection timeout");
            if (isAndroidTenOrLater()) {
                DisconnectCallbackHolder.getInstance().unbindProcessFromNetwork();
                DisconnectCallbackHolder.getInstance().disconnect();
            }
        };

        timeoutHandler.postDelayed(timeoutRunnable, timeout);

        WifiUtils.withContext(this.context)
                .remove(SSID, new RemoveSuccessListener() {
                    @Override
                    public void success() {
                        timeoutHandler.removeCallbacks(timeoutRunnable);
                        joinedNetwork = null;
                        if (onSuccess != null) {
                            onSuccess.run();
                            return;
                        }
                        promise.resolve(true);
                    }

                    @Override
                    public void failed(@NonNull RemoveErrorCode errorCode) {
                        timeoutHandler.removeCallbacks(timeoutRunnable);
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
        if (!assertLocationPermissionGranted(promise)) {
            return;
        }

        boolean wifiStartScan = wifi.startScan();
        Log.d(TAG, "wifi start scan: " + wifiStartScan);
        if (wifiStartScan) {
            final WifiScanResultReceiver wifiScanResultReceiver = new WifiScanResultReceiver(wifi, promise);
            getReactApplicationContext().registerReceiver(wifiScanResultReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        } else {
            Log.d(TAG, "Wifi scan rejected");
            promise.resolve("Starting Android 9, it's only allowed to scan 4 times per 2 minuts in a foreground app.");
        }
    }

    private void connectToWifiDirectly(@NonNull final String SSID, @NonNull final String password, final boolean isHidden, final int timeout, final Promise promise) {
        if (isAndroidTenOrLater()) {
            connectAndroidQ(SSID, password, isHidden, timeout, promise);
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

    private boolean selectNetwork(final Network network, final ConnectivityManager manager) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        return manager.bindProcessToNetwork(network);
      } else {
        return ConnectivityManager.setProcessDefaultNetwork(network);
      }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void connectAndroidQ(@NonNull final String SSID, @NonNull final String password, final boolean isHidden, final int timeout, final Promise promise) {
        WifiNetworkSpecifier.Builder wifiNetworkSpecifier = new WifiNetworkSpecifier.Builder()
                .setIsHiddenSsid(isHidden)
                .setSsid(SSID);

        if (!isNullOrEmpty(password)) {
            wifiNetworkSpecifier.setWpa2Passphrase(password);
        }

        NetworkRequest nr = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
                //.addCapability(NetworkCapabilities.NET_CAPABILITY_TRUSTED)
                .setNetworkSpecifier(wifiNetworkSpecifier.build())
                .build();

        // cleanup previous connections just in case
        DisconnectCallbackHolder.getInstance().disconnect();

        joinedNetwork = null;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        final Handler timeoutHandler = new Handler(Looper.getMainLooper());
        final Runnable timeoutRunnable = () -> {
            promise.reject(ConnectErrorCodes.timeoutOccurred.toString(), "Connection timeout");
            DisconnectCallbackHolder.getInstance().unbindProcessFromNetwork();
            DisconnectCallbackHolder.getInstance().disconnect();
        };

        timeoutHandler.postDelayed(timeoutRunnable, timeout);

        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                timeoutHandler.removeCallbacks(timeoutRunnable);
                joinedNetwork = network;
                DisconnectCallbackHolder.getInstance().bindProcessToNetwork(network);
                //connectivityManager.setNetworkPreference(ConnectivityManager.DEFAULT_NETWORK_PREFERENCE);
                if (!pollForValidSSID(3, SSID)) {
                    promise.reject(ConnectErrorCodes.android10ImmediatelyDroppedConnection.toString(), "Firmware bugs on OnePlus prevent it from connecting on some firmware versions.");
                    return;
                }
                promise.resolve("connected");
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
                timeoutHandler.removeCallbacks(timeoutRunnable);
                joinedNetwork = null;
                promise.reject(ConnectErrorCodes.didNotFindNetwork.toString(), "Network not found or network request cannot be fulfilled.");
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                timeoutHandler.removeCallbacks(timeoutRunnable);
                joinedNetwork = null;
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
                boolean isConnected = this.getConnectionStatus();
                if (ssid != null && ssid.equalsIgnoreCase(expectedSSID) && isConnected) {
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

    private boolean assertLocationPermissionGranted(final Promise promise) {
        final boolean locationPermissionGranted = PermissionUtils.isLocationPermissionGranted(context);
        if (!locationPermissionGranted) {
            promise.reject(ConnectErrorCodes.locationPermissionMissing.toString(), "Location permission (ACCESS_FINE_LOCATION) is not granted");
            return false;
        }

        final boolean isLocationOn = LocationUtils.isLocationOn(context);
        if (!isLocationOn) {
            promise.reject(ConnectErrorCodes.locationServicesOff.toString(), "Location service is turned off");
            return false;
        }

        return true;
    }
}
