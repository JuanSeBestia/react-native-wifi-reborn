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

    /**
     * Connects to a WiFi network. Rejects with an error if it couldn't connect.
     *
     * @param password `null` for open networks.
     * @param isWep Used on iOS. If `true`, the network is WEP Wi-Fi; otherwise it is a WPA or WPA2 personal Wi-Fi network.
     */
    export function connectToProtectedSSID(
        SSID: string,
        password: string,
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
    /**
     * Returns a list of nearby WiFI networks.
     *
     * @param callback Called if the attempt is successful. It contains a stringified JSONArray of `WiFiObject` as parameter.
     * @param error Called if any error occurs during the attempt.
     * @example
     * WifiManager.loadWifiList(
            wifiList => {
                let wifiArray =  JSON.parse(wifiList);
                wifiArray.map((value, index) =>
                    console.log(`Wifi ${index  +  1} - ${value.SSID}`)
                );
            },
            error => console.log(error)
        );
     */
    export function loadWifiList(
        callback: (wifiList: string) => void,
        error: (err: string) => void
    ): void;

    /**
     * Similar to `loadWifiList` but it forcefully starts the WiFi scanning on android and in the callback fetches the list.
     */
    export function reScanAndLoadWifiList(
        callback: (wifiList: string) => void,
        error: (err: string) => void
    ): void;

    export function isEnabled(callback: (enabled: boolean) => void): void;

    export function setEnabled(enabled: boolean): void;

    /**
     * Indicates whether network connectivity exists and it is possible to establish connections.
     * @param Called when the network status is resolved.
     */
    export function connectionStatus(callback: (isConnected: boolean) => void): void;

    export function disconnect(): void;

    /**
     * Remove the network with SSID from configirued networks. 
     * When their is a wrong authed network in configured network, the connectToProtectedSSID will fail.
     * So please use this function to delete it first. 
     * @param SSID 
     * @returns true: the network has been deleted. false: the network is created by others, you can not delete it.
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
