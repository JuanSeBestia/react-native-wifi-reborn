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

export default WifiManager;
