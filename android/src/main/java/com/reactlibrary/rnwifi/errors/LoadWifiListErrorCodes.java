package com.reactlibrary.rnwifi.errors;

public enum LoadWifiListErrorCodes {
    /**
     * Starting android 6, location permission needs to be granted for wifi scanning.
     */
    locationPermissionMissing,
    /**
     * Starting Android 6, location services needs to be on to scan for wifi networks.
     */
    locationServicesOff,
    /**
     * Json parsing exception while parsing the result.
     */
    jsonParsingException,
    /**
     * An exception caused by JS requesting the UI manager to perform an illegal view operation.
     */
    illegalViewOperationException,
}
