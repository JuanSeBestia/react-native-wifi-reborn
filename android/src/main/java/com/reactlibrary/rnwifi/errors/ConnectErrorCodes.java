package com.reactlibrary.rnwifi.errors;

public enum ConnectErrorCodes {
    /**
     * Starting android 6, location permission needs to be granted for wifi scanning.
     */
    locationPermissionMissing,
    /**
     * Starting Android 6, location services needs to be on to scan for wifi networks.
     */
    locationServicesOff,
    /**
     * Starting Android 10, apps are no longer allowed to enable wifi.
     * User has to manually do this.
     */
    couldNotEnableWifi,
    /**
     * Starting Android 9, it's only allowed to scan 4 times per 2 minuts in a foreground app.
     * https://developer.android.com/guide/topics/connectivity/wifi-scan
     */
    couldNotScan,
    /**
     * If the wifi network is not in range, the security type is unknown and WifiUtils doesn't support
     * connecting to the network.
     */
    didNotFindNetwork,
    /**
     * Authentication error occurred while trying to connect.
     * The password could be incorrect or the user could have a saved network configuration with a
     * different password!
     */
    authenticationErrorOccurred,
    /**
     * Could not connect in the timeout window.
     */
    timeoutOccurred,
    /**
     * On Android 10, the user cancelled connecting (via System UI).
     */
    userDenied,
    /**
     * Firmware bugs on OnePlus prevent it from connecting on some firmware versions.
     * More info: https://github.com/ThanosFisherman/WifiUtils/issues/63
     */
    android10ImmediatelyDroppedConnection,
    unableToConnect,
}
