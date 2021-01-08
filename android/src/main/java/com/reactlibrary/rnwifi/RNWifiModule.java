package com.reactlibrary.rnwifi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.reactlibrary.rnwifi.errors.ConnectErrorCodes;
import com.reactlibrary.rnwifi.errors.DisconnectErrorCodes;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.reactlibrary.rnwifi.errors.GetCurrentWifiSSIDErrorCodes;
import com.reactlibrary.rnwifi.errors.IsRemoveWifiNetworkErrorCodes;
import com.reactlibrary.rnwifi.errors.LoadWifiListErrorCodes;
import com.reactlibrary.rnwifi.receivers.WifiScanResultReceiver;
import com.reactlibrary.rnwifi.utils.LocationUtils;
import com.reactlibrary.rnwifi.utils.PermissionUtils;
import com.thanosfisherman.wifiutils.WifiUtils;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionErrorCode;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener;
import com.thanosfisherman.wifiutils.wifiDisconnect.DisconnectionErrorCode;
import com.thanosfisherman.wifiutils.wifiDisconnect.DisconnectionSuccessListener;
import com.thanosfisherman.wifiutils.wifiRemove.RemoveErrorCode;
import com.thanosfisherman.wifiutils.wifiRemove.RemoveSuccessListener;

import java.util.List;

import static com.reactlibrary.rnwifi.mappers.WifiScanResultsMapper.mapWifiScanResults;

public class RNWifiModule extends ReactContextBaseJavaModule {
    private final WifiManager wifi;
    private final ReactApplicationContext context;

    final long CONNECT_TIMEOUT_IN_MILLISECONDS = 45000;

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
        forceWifiUsageWithOptions(useWifi, null , promise);
    }

    /**
     * Use this to execute api calls to a wifi network that does not have internet access.
     *
     * Useful for commissioning IoT devices.
     *
     * This will route all app network requests to the network (instead of the mobile connection).
     * It is important to disable it again after using as even when the app disconnects from the wifi
     * network it will keep on routing everything to wifi.
     *
     * @param useWifi boolean to force wifi off or on
     * @param options `noInternet` to indicate that the wifi network does not have internet connectivity
     */
    @ReactMethod
    public void forceWifiUsageWithOptions(final boolean useWifi, @Nullable final ReadableMap options, final Promise promise) {
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

        WifiUtils.withContext(context)
                .connectWith(SSID, password)
                .setTimeout(CONNECT_TIMEOUT_IN_MILLISECONDS)
                .onConnectionResult(new ConnectionSuccessListener() {
                    @Override
                    public void success() {
                        promise.resolve("connected");
                    }

                    @SuppressLint("DefaultLocale")
                    @Override
                    public void failed(@NonNull ConnectionErrorCode errorCode) {
                        switch (errorCode) {
                            case COULD_NOT_ENABLE_WIFI: {
                                promise.reject(ConnectErrorCodes.couldNotEnableWifi.toString(), "On Android 10, the user has to enable wifi manually.");
                            }
                            case COULD_NOT_SCAN: {
                                promise.reject(ConnectErrorCodes.couldNotScan.toString(), "Starting Android 9, apps are only allowed to scan wifi networks a few times.");
                            }
                            case DID_NOT_FIND_NETWORK_BY_SCANNING: {
                                promise.reject(ConnectErrorCodes.didNotFindNetwork.toString(), "Wifi network is not in range or not seen.");
                            }
                            case AUTHENTICATION_ERROR_OCCURRED: {
                                promise.reject(ConnectErrorCodes.authenticationErrorOccurred.toString(), "Authentication error, wrong password or a saved wifi configuration with a different password / security type.");
                            }
                            case TIMEOUT_OCCURRED: {
                                promise.reject(ConnectErrorCodes.timeoutOccurred.toString(), String.format("Could not connect in %d milliseconds ", CONNECT_TIMEOUT_IN_MILLISECONDS));
                            }
                            case USER_CANCELLED: {
                                promise.reject(ConnectErrorCodes.userDenied.toString(), "On Android 10, the user cancelled connecting (via System UI).");
                            }
                            case ANDROID_10_IMMEDIATELY_DROPPED_CONNECTION: {
                                promise.reject(ConnectErrorCodes.android10ImmediatelyDroppedConnection.toString(), "Firmware bugs on OnePlus prevent it from connecting on some firmware versions.");
                            }
                            default:
                            case COULD_NOT_CONNECT: {
                                promise.reject(ConnectErrorCodes.unableToConnect.toString(), String.format("Failed to connect with %s", SSID));
                            }
                        }
                    }
                })
                .start();
    }

    /**
     * Returns if the device is currently connected to a WiFi network.
     */
    @ReactMethod
    public void connectionStatus(final Promise promise) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) getReactApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager == null) {
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
        WifiInfo info = wifi.getConnectionInfo();

        // This value should be wrapped in double quotes, so we need to unwrap it.
        String ssid = info.getSSID();
        if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
            ssid = ssid.substring(1, ssid.length() - 1);
        }

        // Android returns `<unknown ssid>` when it is not connected or still connecting
        if (ssid.equals("<unknown ssid>")) {
            promise.reject(GetCurrentWifiSSIDErrorCodes.CouldNotDetectSSID.toString(), "Not connected or connecting.");
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
        final boolean locationPermissionGranted = PermissionUtils.isLocationPermissionGranted(context);
        if (!locationPermissionGranted) {
            promise.reject(IsRemoveWifiNetworkErrorCodes.locationPermissionMissing.toString(), "Location permission (ACCESS_FINE_LOCATION) is not granted");
            return;
        }

        WifiUtils.withContext(this.context)
                .remove(SSID, new RemoveSuccessListener() {
                    @Override
                    public void success() {
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
}
