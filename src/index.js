import { NativeModules } from 'react-native';

const { WifiManager } = NativeModules;

export const LOAD_WIFI_LIST_ERRORS = {
    locationPermissionMissing: 'locationPermissionMissing',
    locationServicesOff: 'locationServicesOff',
    jsonParsingException: 'jsonParsingException',
    illegalViewOperationException: 'illegalViewOperationException',
};

export const IS_REMOVE_WIFI_NETWORK_ERRORS = {
    locationPermissionMissing: 'locationPermissionMissing',
};

export default WifiManager;
