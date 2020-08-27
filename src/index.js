import { NativeModules } from 'react-native';

const { WifiManager } = NativeModules;

export const CONNECT_ERRORS = {
    unavailableForOSVersion: 'unavailableForOSVersion',
    invalid: 'invalid',
    invalidSSID: 'invalidSSID',
    invalidSSIDPrefix: 'invalidSSIDPrefix',
    invalidPassphrase: 'invalidPassphrase',
    userDenied: 'userDenied',
    locationPermissionDenied: 'locationPermissionDenied',
    unableToConnect: 'unableToConnect',
    locationPermissionRestricted: 'locationPermissionRestricted',
    locationPermissionMissing: 'locationPermissionMissing',
    locationServicesOff: 'locationServicesOff',
    couldNotEnableWifi: 'couldNotEnableWifi',
    couldNotScan: 'couldNotScan',
    couldNotDetectSSID: 'couldNotDetectSSID',
    didNotFindNetwork: 'didNotFindNetwork',
    authenticationErrorOccurred: 'authenticationErrorOccurred',
    android10ImmediatelyDroppedConnection: 'android10ImmediatelyDroppedConnection',
    timeoutOccurred: 'timeoutOccurred',
};

export const DISCONNECT_ERRORS = {
    couldNotGetWifiManager: 'couldNotGetWifiManager',
};

export const IS_REMOVE_WIFI_NETWORK_ERRORS = {
    locationPermissionMissing: 'locationPermissionMissing',
    couldNotGetWifiManager: 'couldNotGetWifiManager',
    couldNotGetConnectivityManager: 'couldNotGetConnectivityManager',
    couldNotRemove: 'couldNotRemove',
};

export const FORCE_WIFI_USAGE_ERRORS = {
    couldNotGetConnectivityManager: 'couldNotGetConnectivityManager',
};

export const LOAD_WIFI_LIST_ERRORS = {
    locationPermissionMissing: 'locationPermissionMissing',
    locationServicesOff: 'locationServicesOff',
    jsonParsingException: 'jsonParsingException',
    illegalViewOperationException: 'illegalViewOperationException',
};

export default WifiManager;
