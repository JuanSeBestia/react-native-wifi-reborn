package com.reactlibrary.rnwifi.errors;

public enum IsRemoveWifiNetworkErrorCodes {
    /**
     * Starting android 6, location permission needs to be granted for wifi scanning.
     */
    locationPermissionMissing,
    /**
     * Could not get the WifiManager.
     * https://developer.android.com/reference/android/net/wifi/WifiManager?hl=en
     */
    couldNotGetWifiManager,
    /**
     * Could not get the ConnectivityManager.
     * https://developer.android.com/reference/android/net/ConnectivityManager?hl=en
     */
    couldNotGetConnectivityManager,
}
