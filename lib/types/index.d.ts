declare module 'react-native-wifi-reborn' {
    export type WiFiObject = {
        SSID: string;
        BSSID: string;
        capabilities: string;
        frequency: number;
        level: number;
        timestamp: number;
    };

    export type SuggestedNetworkConfig = {
        ssid: string;
        password?: string;
        isWpa3?: boolean;
        isAppInteractionRequired?: boolean;
    };

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
         * Firmware bugs on OnePlus prevent it from connecting on some firmware versions.
         * More info: https://github.com/ThanosFisherman/WifiUtils/issues/63
         */
        android10ImmediatelyDroppedConnection = 'android10ImmediatelyDroppedConnection',
        /**
         * Could not connect in the timeout window.
         */
        timeoutOccurred = 'timeoutOccurred',
    }

    /**
     * Options parameter object for use with the connectToProtectedWifiSSID function.
     */
    type ConnectToProtectedSSIDParams = {
        /**
         * Wifi name.
         */
        ssid: string;
        /**
         * `null` for open networks.
         */
        password: string | null;
        /**
         * Used on iOS. If `true`, the network is WEP Wi-Fi; otherwise it is a WPA or WPA2 personal Wi-Fi network.
         */
        isWEP?: boolean;
        /**
         * only for Android, use if Wi-Fi is hidden.
         */
        isHidden?: boolean;
        /**
         * only for Android, timeout in seconds. If the connection is not established in this time, it will reject. Default is 15 seconds.
         */
        timeout?: number;
    };

    export enum GET_CURRENT_WIFI_SSID_ERRRORS {
        /**
         * Not connected or connecting.
         */
        CouldNotDetectSSID = 'CouldNotDetectSSID',
    }

    export interface WifiEntry {
        SSID: string;
        BSSID: string;
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

    export enum DISCONNECT_ERRORS {
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
    }

    export enum IS_REMOVE_WIFI_NETWORK_ERRORS {
        /**
         * Starting android 6, location permission needs to be granted for wifi scanning.
         */
        locationPermissionMissing = 'locationPermissionMissing',
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
    }

    export enum FORCE_WIFI_USAGE_ERRORS {
        /**
         * Could not get the ConnectivityManager.
         * https://developer.android.com/reference/android/net/ConnectivityManager?hl=en
         */
        couldNotGetConnectivityManager = 'couldNotGetConnectivityManager',
    }

    /**
     * Interface to the WifiManager native module.
     */
    interface WifiManager {
        /**
         * Connects to a WiFi network. Rejects with an error if it couldn't connect.
         *
         * @param SSID Wifi name.
         * @param password `null` for open networks.
         * @param isWep Used on iOS. If `true`, the network is WEP Wi-Fi; otherwise it is a WPA or WPA2 personal Wi-Fi network.
         * @param isHidden only for Android, use if Wi-Fi is hidden.
         */
        connectToProtectedSSID(
            SSID: string,
            password: string | null,
            isWEP: boolean,
            isHidden: boolean
        ): Promise<void>;

        /**
         * Suggests a list of Wi-Fi networks on Android. Resolves with 'connected' when the suggestions are added successfully.
         * Only works for Android and requires a minimum SDK version of 29.
         *
         * @param networkConfigs List of network configurations containing SSID, password, WPA3 flag, and app interaction flag.
         * @returns Promise that resolves with 'connected' on success, or rejects with an error message on failure.
         */
        suggestWifiNetwork(networkConfigs: SuggestedNetworkConfig[]): Promise<string>;

        /**
         * Connects to a WiFi network. Rejects with an error if it couldn't connect.
         *
         * @param options Connection options object containing, SSID, password, isWep, isHidden, timeout
         */
        connectToProtectedWifiSSID(options: ConnectToProtectedSSIDParams): Promise<void>;

        /**
         * Returns the name of the currently connected WiFi. When not connected, the promise will be or null when not connected.
         */
        getCurrentWifiSSID(): Promise<string>;

        //#region iOS only

        connectToSSID(SSID: string): Promise<void>;
        connectToSSIDPrefix(SSIDPrefix: string): Promise<void>;
        disconnectFromSSID(SSIDPrefix: string): Promise<void>;

        /**
         * Connects to a WiFi network, with option to limit connection lifetime. Rejects with an error if it couldn't connect.
         *
         * @param SSID Wifi name.
         * @param password `null` for open networks.
         * @param isWep Used on iOS. If `true`, the network is WEP Wi-Fi; otherwise it is a WPA or WPA2 personal Wi-Fi network.
         * @param joinOnce Used on iOS. If `true`, restricts the lifetime of a configuration to the operating status of the app that created it.
         */
        connectToProtectedSSIDOnce(
            SSID: string,
            password: string | null,
            isWEP: boolean,
            joinOnce: boolean
        ): Promise<void>;

        /**
         * Connects to a WiFi network that start with SSIDPrefix. Rejects with an error if it couldn't connect.
         *
         * @param SSIDPrefix Wifi name prefix.
         * @param password `null` for open networks.
         * @param isWep Used on iOS. If `true`, the network is WEP Wi-Fi; otherwise it is a WPA or WPA2 personal Wi-Fi network.
         */
        connectToProtectedSSIDPrefix(
            SSIDPrefix: string,
            password: string,
            isWEP: boolean
        ): Promise<void>;

        /**
         * Connects to a WiFi network that start with SSIDPrefix, with option to limit connection lifetime. Rejects with an error if it couldn't connect.
         *
         * @param SSIDPrefix Wifi name prefix.
         * @param password `null` for open networks.
         * @param isWep Used on iOS. If `true`, the network is WEP Wi-Fi; otherwise it is a WPA or WPA2 personal Wi-Fi network.
         * @param joinOnce Used on iOS. If `true`, restricts the lifetime of a configuration to the operating status of the app that created it.
         */
        connectToProtectedSSIDPrefixOnce(
            SSIDPrefix: string,
            password: string | null,
            isWEP: boolean,
            joinOnce: boolean
        ): Promise<void>;

        //#endregion

        //#region Android only

        /**
         * Returns a list of nearby WiFI networks.
         */
        loadWifiList(): Promise<Array<WifiEntry>>;

        /**
         * Similar to `loadWifiList` but it forcefully starts a new WiFi scan and only passes the results when the scan is done.
         */
        reScanAndLoadWifiList(): Promise<Array<WifiEntry>>;

        /**
         * Method to check if wifi is enabled.
         */
        isEnabled(): Promise<boolean>;

        setEnabled(enabled: boolean): void;

        /**
         * Returns if the device is currently connected to a WiFi network.
         */
        connectionStatus(): Promise<boolean>;

        disconnect(): Promise<boolean>;

        /**
         * Returns the BSSID (basic service set identifier) of the currently connected WiFi network.
         */
        getBSSID(): Promise<string>;

        /**
         * Returns the RSSI (received signal strength indicator) of the currently connected WiFi network.
         */
        getCurrentSignalStrength(): Promise<number>;

        /**
         * Returns the frequency of the currently connected WiFi network.
         */
        getFrequency(): Promise<number>;

        /**
         * Returns the IP of the currently connected WiFi network.
         */
        getIP(): Promise<string>;

        /**
         * This method will remove the wifi network configuration.
         * If you are connected to that network, it will disconnect.
         *
         * @param SSID wifi SSID to remove configuration for
         */
        isRemoveWifiNetwork(SSID: string): Promise<boolean>;

        /**
         * @deprecated Use forceWifiUsageWithOptions.
         */
        forceWifiUsage(useWifi: boolean): Promise<void>;

        /**
         * Use this to route all app network requests to the wifi network (instead of the mobile connection).
         * It is important to disable it again after using as even when the app disconnects from the wifi
         * network it will keep on routing everything to wifi.
         *
         * Useful for commissioning IoT devices. If the wifi access point has no internet you can indicate so with
         * the option `noInternet`.
         *
         * @param useWifi Force wifi usage on or off.
         * @param options `noInternet` To indicate the access point has no internet. Usefull as some
         * phone vendor customizations will switch back to mobile when the wifi access point has no internet.
         */
        forceWifiUsageWithOptions(
            useWifi: boolean,
            options: { noInternet: boolean }
        ): Promise<void>;

        //#endregion
    }

    const _default: WifiManager;
    export default _default;
}
