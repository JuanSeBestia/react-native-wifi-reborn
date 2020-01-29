package com.reactlibrary.rnwifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.IllegalViewOperationException;
import com.reactlibrary.utils.LocationUtils;
import com.reactlibrary.utils.PermissionUtils;
import com.thanosfisherman.wifiutils.WifiUtils;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public String getName() {
        return "WifiManager";
    }

    /**
     * Method to load wifi list into string via Callback. Returns a stringified JSONArray
     *
     * @param successCallback
     * @param errorCallback
     */
    @ReactMethod
    public void loadWifiList(Callback successCallback, Callback errorCallback) {
        try {
            List<ScanResult> results = wifi.getScanResults();
            JSONArray wifiArray = new JSONArray();

            for (ScanResult result : results) {
                JSONObject wifiObject = new JSONObject();
                if (!result.SSID.equals("")) {
                    try {
                        wifiObject.put("SSID", result.SSID);
                        wifiObject.put("BSSID", result.BSSID);
                        wifiObject.put("capabilities", result.capabilities);
                        wifiObject.put("frequency", result.frequency);
                        wifiObject.put("level", result.level);
                        wifiObject.put("timestamp", result.timestamp);
                    } catch (JSONException e) {
                        errorCallback.invoke(e.getMessage());
                    }
                    wifiArray.put(wifiObject);
                }
            }
            successCallback.invoke(wifiArray.toString());
        } catch (IllegalViewOperationException e) {
            errorCallback.invoke(e.getMessage());
        }
    }

    /**
     * Method to force wifi usage if the user needs to send requests via wifi
     * if it does not have internet connection. Useful for IoT applications, when
     * the app needs to communicate and send requests to a device that have no
     * internet connection via wifi.
     * <p>
     * Receives a boolean to enable forceWifiUsage if true, and disable if false.
     * Is important to enable only when communicating with the device via wifi
     * and remember to disable it when disconnecting from device.
     *
     * @param useWifi
     */
    @ReactMethod
    public void forceWifiUsage(boolean useWifi) {
        boolean canWriteFlag = false;

        if (useWifi) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    canWriteFlag = Settings.System.canWrite(context);

                    if (!canWriteFlag) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        intent.setData(Uri.parse("package:" + context.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intent);
                    }
                }


                if (((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && canWriteFlag) || ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) && !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M))) {
                    final ConnectivityManager manager = (ConnectivityManager) context
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkRequest.Builder builder;
                    builder = new NetworkRequest.Builder();
                    //set the transport type to WIFI
                    builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);


                    manager.requestNetwork(builder.build(), new ConnectivityManager.NetworkCallback() {
                        @Override
                        public void onAvailable(@NonNull final Network network) {
                            // FIXME: should this be try catch?
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                manager.bindProcessToNetwork(network);
                            } else {
                                //This method was deprecated in API level 23
                                ConnectivityManager.setProcessDefaultNetwork(network);
                            }
                            manager.unregisterNetworkCallback(this);
                        }
                    });
                }


            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ConnectivityManager manager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                manager.bindProcessToNetwork(null);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ConnectivityManager.setProcessDefaultNetwork(null);
            }
        }
    }

    /**
     * Method to check if wifi is enabled
     *
     * @param isEnabled
     */
    @ReactMethod
    public void isEnabled(Callback isEnabled) {
        isEnabled.invoke(wifi.isWifiEnabled());
    }

    /**
     * Method to connect/disconnect wifi service
     *
     * @param enabled
     */
    @ReactMethod
    public void setEnabled(Boolean enabled) {
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
            promise.reject("location permission missing", "Location permission is not granted");
            return;
        }

        final boolean isLocationOn = LocationUtils.isLocationOn(context);
        if (!isLocationOn) {
            promise.reject("location off", "Location service is turned off");
            return;
        }

        WifiUtils.withContext(context).connectWith(SSID, password).onConnectionResult(new ConnectionSuccessListener() {
            @Override
            public void isSuccessful(boolean isSuccess) {
                if (isSuccess) {
                    promise.resolve("connected");
                } else {
                    promise.reject("failed", "Could not connect to network");
                }
            }
        }).start();
    }

    /**
     * Use this method to check if the device is currently connected to Wifi.
     *
     * @param connectionStatusResult
     */
    @ReactMethod
    public void connectionStatus(Callback connectionStatusResult) {
        ConnectivityManager connManager = (ConnectivityManager) getReactApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            connectionStatusResult.invoke(true);
        } else {
            connectionStatusResult.invoke(false);
        }
    }

    /**
     * Disconnect current Wifi.
     */
    @ReactMethod
    public void disconnect() {
        wifi.disconnect();
    }

    /**
     * This method will return current SSID
     *
     * @param promise
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
     * ]
     * This method will return the basic service set identifier (BSSID) of the current access point
     *
     * @param callback
     */
    @ReactMethod
    public void getBSSID(final Callback callback) {
        WifiInfo info = wifi.getConnectionInfo();

        String bssid = info.getBSSID();

        callback.invoke(bssid.toUpperCase());
    }

    /**
     * This method will return current wifi signal strength
     *
     * @param callback
     */
    @ReactMethod
    public void getCurrentSignalStrength(final Callback callback) {
        int linkSpeed = wifi.getConnectionInfo().getRssi();
        callback.invoke(linkSpeed);
    }

    /**
     * This method will return current wifi frequency
     *
     * @param callback
     */
    @ReactMethod
    public void getFrequency(final Callback callback) {
        WifiInfo info = wifi.getConnectionInfo();
        int frequency = info.getFrequency();
        callback.invoke(frequency);
    }

    /**
     * This method will return current IP
     *
     * @param callback
     */
    @ReactMethod
    public void getIP(final Callback callback) {
        WifiInfo info = wifi.getConnectionInfo();
        String stringIP = longToIP(info.getIpAddress());
        callback.invoke(stringIP);
    }

    /**
     * This method will remove the wifi network as per the passed SSID from the device list
     *
     * @param ssid
     * @param callback
     */
    @ReactMethod
    public void isRemoveWifiNetwork(String ssid, final Callback callback) {
        List<WifiConfiguration> mWifiConfigList = wifi.getConfiguredNetworks();
        for (WifiConfiguration wifiConfig : mWifiConfigList) {
            String comparableSSID = ('"' + ssid + '"'); //Add quotes because wifiConfig.SSID has them
            if (wifiConfig.SSID.equals(comparableSSID)) {
                wifi.removeNetwork(wifiConfig.networkId);
                wifi.saveConfiguration();
                callback.invoke(true);
                return;
            }
        }
        callback.invoke(false);
    }

    /**
     * This method is similar to `loadWifiList` but it forcefully starts the wifi scanning on android and in the callback fetches the list
     *
     * @param successCallback
     * @param errorCallback
     */
    @ReactMethod
    public void reScanAndLoadWifiList(Callback successCallback, Callback errorCallback) {
        WifiReceiver receiverWifi = new WifiReceiver(wifi, successCallback, errorCallback);
        getReactApplicationContext().registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
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

    class WifiReceiver extends BroadcastReceiver {

        private final Callback successCallback;
        private final Callback errorCallback;
        private final WifiManager wifi;

        public WifiReceiver(final WifiManager wifi, Callback successCallback, Callback errorCallback) {
            super();
            this.successCallback = successCallback;
            this.errorCallback = errorCallback;
            this.wifi = wifi;
        }

        // This method call when number of wifi connections changed
        public void onReceive(Context c, Intent intent) {
            c.unregisterReceiver(this);
            try {
                List<ScanResult> results = this.wifi.getScanResults();
                JSONArray wifiArray = new JSONArray();

                for (ScanResult result : results) {
                    JSONObject wifiObject = new JSONObject();
                    if (!result.SSID.equals("")) {
                        try {
                            wifiObject.put("SSID", result.SSID);
                            wifiObject.put("BSSID", result.BSSID);
                            wifiObject.put("capabilities", result.capabilities);
                            wifiObject.put("frequency", result.frequency);
                            wifiObject.put("level", result.level);
                            wifiObject.put("timestamp", result.timestamp);
                        } catch (JSONException e) {
                            this.errorCallback.invoke(e.getMessage());
                            return;
                        }
                        wifiArray.put(wifiObject);
                    }
                }
                this.successCallback.invoke(wifiArray.toString());
            } catch (IllegalViewOperationException e) {
                this.errorCallback.invoke(e.getMessage());
            }
        }
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
