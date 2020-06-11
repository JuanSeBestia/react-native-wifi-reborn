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
    timeoutOccurred: 'timeoutOccurred',
};

export const IS_REMOVE_WIFI_NETWORK_ERRORS = {
    couldNotGetWifiManager: 'couldNotGetWifiManager',
    couldNotGetConnectivityManager: 'couldNotGetConnectivityManager',
};

export const FORCE_WIFI_USAGE_ERRORS = {
    couldNotGetConnectivityManager: 'couldNotGetConnectivityManager',
};

export default WifiManager;
