package com.reactlibrary.rnwifi.errors;

public enum DisconnectErrorCodes {
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
