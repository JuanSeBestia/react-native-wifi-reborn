import { NativeModules } from 'react-native';

const { WifiManager } = NativeModules;

export const LOAD_WIFI_LIST_ERRORS = {
    locationPermissionMissing: 'locationPermissionMissing',
    locationServicesOff: 'locationServicesOff',
    jsonParsingException: 'jsonParsingException',
    illegalViewOperationException: 'illegalViewOperationException',
};

export default WifiManager;
