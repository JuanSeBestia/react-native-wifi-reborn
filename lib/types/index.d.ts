declare module 'react-native-wifi-reborn' {
    export type WiFiObject = {
        SSID: string;
        BSSID: string;
        capabilities: string;
        frequency: number;
        level: number;
        timestamp: number;
    };

    export type Errors = Partial<{
        // The WIFI network is not currently in range.
        notInRange: boolean;
        // Could not add or update the network configuration.
        addOrUpdateFailed: boolean;
        // Disconnecting from the network failed. This is done as part of the connect flow
        disconnectFailed: boolean;
        // Could not connect to network
        connectNetworkFailed: boolean;
    }>;

    export enum CONNECT_ERRORS {
        /**
         * Starting from iOS 11, NEHotspotConfigurationError is available
         */
        unavailableForOSVersion = 'unavailableForOSVersion',
        /**
         * If an unknown error is occurred (iOS)
         */
        invalid = 'invalid',
        /**
         * If the SSID is invalid
         */
        invalidSSID = 'invalidSSID',
        /**
         * If the SSID prefix is invalid
         */
        invalidSSIDPrefix = 'invalidSSIDPrefix',
        /**
         * If the passphrase is invalid
         */
        invalidPassphrase = 'invalidPassphrase',
        /**
         * If the user canceled the request to join the asked network
         */
        userDenied = 'userDenied',
        /**
         * Starting from iOS 13, location permission is denied (iOS)
         */
        locationPermissionDenied = 'locationPermissionDenied',
        /**
         * When an unknown error occurred
         */
        unableToConnect = 'unableToConnect',
        /**
         * Starting from iOS 13, location permission is restricted (iOS)
         */
        locationPermissionRestricted = 'locationPermissionRestricted',
        /**
         * Starting android 6, location permission needs to be granted for wifi scanning.
         */
        locationPermissionMissing = 'locationPermissionMissing',
        /**
         * Starting Android 6, location services needs to be on to scan for wifi networks.
         */
        locationServicesOff = 'locationServicesOff',
        /**
         * Starting Android 10, apps are no longer allowed to enable wifi.
         * User has to manually do this.
         */
        couldNotEnableWifi = 'couldNotEnableWifi',
        /**
         * Starting Android 9, it's only allowed to scan 4 times per 2 minuts in a foreground app.
         * https://developer.android.com/guide/topics/connectivity/wifi-scan
         */
        couldNotScan = 'couldNotScan',
        /**
         * If the SSID couldn't be detected
         */
        couldNotDetectSSID = 'couldNotDetectSSID',
        /**
         * If the wifi network is not in range, the security type is unknown and WifiUtils doesn't support
         * connecting to the network.
         */
        didNotFindNetwork = 'didNotFindNetwork',
        /**
         * Authentication error occurred while trying to connect.
         * The password could be incorrect or the user could have a saved network configuration with a
         * different password!
         */
        authenticationErrorOccurred = 'authenticationErrorOccurred',
        /**
         * Could not connect in the timeout window.
         */
        timeoutOccurred = 'timeoutOccurred',
    }

    /**
     * Connects to a WiFi network. Rejects with an error if it couldn't connect.
     *
     * @param SSID Wifi name.
     * @param password `null` for open networks.
     * @param isWep Used on iOS. If `true`, the network is WEP Wi-Fi; otherwise it is a WPA or WPA2 personal Wi-Fi network.
     */
    export function connectToProtectedSSID(
        SSID: string,
        password: string | null,
        isWEP: boolean
    ): Promise<void>;

    export function getCurrentWifiSSID(): Promise<string>;

    //#region iOS only

    export function connectToSSID(SSID: string): Promise<void>;
    export function connectToSSIDPrefix(SSIDPrefix: string): Promise<void>;
    export function disconnectFromSSID(SSIDPrefix: string): Promise<void>;
    export function connectToProtectedSSIDPrefix(
        SSIDPrefix: string,
        password: string,
        isWEP: boolean
    ): Promise<void>;

    //#endregion

    //#region Android only

    export interface WifiEntry {
        SSID: string;
        BSSID: number;
        capabilities: string;
        frequency: number;
        level: number;
        timestamp: number;
    }

    export enum LOAD_WIFI_LIST_ERRORS {
        /**
         * Starting android 6, location permission needs to be granted for wifi scanning.
         */
        locationPermissionMissing = 'locationPermissionMissing',
        /**
         * Starting Android 6, location services needs to be on to scan for wifi networks.
         */
        locationServicesOff = 'locationServicesOff',
        /**
         * Json parsing exception while parsing the result.
         */
        jsonParsingException = 'jsonParsingException',
        /**
         * An exception caused by JS requesting the UI manager to perform an illegal view operation.
         */
        illegalViewOperationException = 'illegalViewOperationException',
    }

    /**
     * Returns a list of nearby WiFI networks.
     *
     * @example
     * const results = await WifiManager.loadWifiList();
        results => {
            let wifiArray =  JSON.parse(results);
            wifiArray.map((value, index) =>
                console.log(`Wifi ${index  +  1} - ${value.SSID}`)
            );
        },
     */
    export function loadWifiList(): Promise<Array<WifiEntry>>;

    /**
     * Similar to `loadWifiList` but it forcefully starts a new WiFi scan and only passes the results when the scan is done.
     */
    export function reScanAndLoadWifiList(): Promise<Array<string>>;

    /**
     * Method to check if wifi is enabled.
     */
    export function isEnabled(): Promise<boolean>;

    export function setEnabled(enabled: boolean): void;

    /**
     * Returns if the device is currently connected to a WiFi network.
     */
    export function connectionStatus(): Promise<boolean>;

    export const DISCONNECT_ERRORS = {
        /**
         * Could not get the WifiManager.
         * https://developer.android.com/reference/android/net/wifi/WifiManager?hl=en
         */
        couldNotGetWifiManager = 'couldNotGetWifiManager',
        /**
         * Could not get the ConnectivityManager.
         * https://developer.android.com/reference/android/net/ConnectivityManager?hl=en
         */
        couldNotGetConnectivityManager = 'couldNotGetConnectivityManager',
    };

    export function disconnect(): Promise<boolean>;

    /**
     * Returns the BSSID (basic service set identifier) of the currently connected WiFi network.
     */
    export function getBSSID(): Promise<string>;

    /**
     * Returns the RSSI (received signal strength indicator) of the currently connected WiFi network.
     */
    export function getCurrentSignalStrength(): Promise<number>;

    /**
     * Returns the frequency of the currently connected WiFi network.
     */
    export function getFrequency(): Promise<number>;

    /**
     * Returns the IP of the currently connected WiFi network.
     */
    export function getIP(): Promise<string>;

    export const IS_REMOVE_WIFI_NETWORK_ERRORS = {
        /**
         * Starting android 6, location permission needs to be granted for wifi scanning.
         */
        locationPermissionMissing = 'locationPermissionMissing',
        couldNotGetWifiManager = 'couldNotGetWifiManager',
        couldNotGetConnectivityManager = 'couldNotGetConnectivityManager',
    };

    /**
     * This method will remove the wifi network configuration.
     * If you are connected to that network, it will disconnect.
     *
     * @param SSID wifi SSID to remove configuration for
     */
    export function isRemoveWifiNetwork(SSID: string): Promise<boolean>;

    export enum FORCE_WIFI_USAGE_ERRORS {
        couldNotGetConnectivityManager = 'couldNotGetConnectivityManager',
    }

    /**
     * Use this to execute api calls to a wifi network that does not have internet access.
     *
     * Useful for commissioning IoT devices.
     *
     * This will route all app network requests to the network (instead of the mobile connection).
     * It is important to disable it again after using as even when the app disconnects from the wifi
     * network it will keep on routing everything to wifi.
     *
     * @param useWifi boolean to force wifi off or on
     */
    export function forceWifiUsage(useWifi: boolean): Promise<void>;

    //#endregion
}
