package com.reactlibrary.rnwifi;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.reactlibrary.rnwifi.errors.IsRemoveWifiNetworkErrorCodes;
import com.reactlibrary.rnwifi.errors.LoadWifiListErrorCodes;
import com.reactlibrary.rnwifi.receivers.WifiScanResultReceiver;
import com.reactlibrary.utils.LocationUtils;
import com.reactlibrary.utils.PermissionUtils;
import com.thanosfisherman.wifiutils.WifiUtils;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionErrorCode;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener;
import com.thanosfisherman.wifiutils.wifiRemove.RemoveErrorCode;
import com.thanosfisherman.wifiutils.wifiRemove.RemoveSuccessListener;

import java.util.List;

import static com.reactlibrary.mappers.WifiScanResultsMapper.mapWifiScanResults;

public class RNWifiModule extends ReactContextBaseJavaModule {
    private final WifiManager wifi;
    private final ReactApplicationContext context;

    RNWifiModule(ReactApplicationContext context) {
        super(context);

        // TODO: get when needed
        wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        this.context = context;
    }

    @NonNull
    @Override
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

    /**
     * Use this to execute api calls to a wifi network that does not have internet access.
     * Function is overloading to allow user to specifiy if the connection has internet access 
     * in order to prevent mobile data routing.
     *
     * Useful for commissioning IoT devices.  hasInternet will default to false since this is the 
     * common use case for IoT devices.
     *
     * This will route all app network requests to the network (instead of the mobile connection).
     * It is important to disable it again after using as even when the app disconnects from the wifi
     * network it will keep on routing everything to wifi.
     *
     * @param useWifi boolean to force wifi off or on
     */
    @ReactMethod
    public void forceWifiUsage(final boolean useWifi, final Promise promise)
    {
        this(useWifi, false, promise);
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
     * @param hasInternet boolean to remove the internet capabilitiy to prevent mobile data routing
     */
    @ReactMethod
    public void forceWifiUsage(final boolean useWifi, final boolean hasInternet, final Promise promise) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            promise.reject(ForceWifiUsageErrorCodes.couldNotGetConnectivityManager.toString(), "Failed to get the ConnectivityManager.");
            return;
        }

        if (useWifi) {
            NetworkRequest networkRequest = new NetworkRequest.Builder();
            
            networkRequest.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
            
            if (!hasInternet) {
                networkRequest.removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            }

            networkRequest.build();

            connectivityManager.requestNetwork(networkRequest, new ConnectivityManager.NetworkCallback() {
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
            promise.reject("location permission missing", "Location permission (ACCESS_FINE_LOCATION) is not granted");
            return;
        }

        final boolean isLocationOn = LocationUtils.isLocationOn(context);
        if (!isLocationOn) {
            promise.reject("location off", "Location service is turned off");
            return;
        }

        WifiUtils.withContext(context).connectWith(SSID, password).onConnectionResult(new ConnectionSuccessListener() {
            @Override
            public void success() {
                promise.resolve("connected");
            }

            @Override
            public void failed(@NonNull ConnectionErrorCode errorCode) {
                promise.reject("failed", "Could not connect to network");
            }
        }).start();
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
    public void disconnect() {
        wifi.disconnect();
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

        WifiUtils.withContext(context).remove(SSID, new RemoveSuccessListener() {
            @Override
            public void success() {
                promise.resolve(true);
            }

            @Override
            public void failed(@NonNull RemoveErrorCode errorCode) {
                promise.resolve(false);
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

    private static String formatWithBackslashes(final String value) {
        return String.format("\"%s\"", value);
    }

    /**
     * @return true if the current sdk is above or equal to Android M
     */
    private static boolean isAndroidLollipopOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * @return true if the current sdk is above or equal to Android Q
     */
    private static boolean isAndroid10OrLater() {
        return false; // TODO: Compatibility with Android 10
        // return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }
}