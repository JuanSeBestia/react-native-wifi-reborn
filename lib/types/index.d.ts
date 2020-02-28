declare module 'react-native-wifi-reborn' {
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

    export function connectToProtectedSSID(
        SSID: string,
        password: string,
        isWEP: boolean
    ): Promise<void>;

    export function connectToProtectedSSIDPrefix(
        SSIDPrefix: string,
        password: string,
        isWEP: boolean
    ): Promise<void>;

    export function forceWifiUsage(force: boolean): Promise<void>;

    export function getCurrentWifiSSID(): Promise<string>;

    //#region iOS only
    export function connectToSSID(SSID: string): Promise<void>;
    export function connectToSSIDPrefix(SSIDPrefix: string): Promise<void>;
    export function disconnectFromSSID(SSIDPrefix: string): Promise<void>;
    //#endregion

    //#region Android only
    export function loadWifiList(
        // Function to be called if the attempt is successful. It contains a stringified JSONArray of wifiObjects as parameter, each object containing:
        callback: (wifiList: string) => void,
        // Function to be called if any error occurs during the attempt. It contains a string as parameter with the error message.
        error: (err: string) => void
    ): void;
    export function reScanAndLoadWifiList(
        // Function to be called if the attempt is successful. It contains a stringified JSONArray of wifiObjects as parameter, each object containing:
        callback: (wifiList: string) => void,
        // Function to be called if any error occurs during the attempt. It contains a string as parameter with the error message.
        error: (err: string) => void
    ): void;
    export function isEnabled(callback: (enabled: boolean) => void): void;
    export function setEnabled(enabled: boolean): void;
    export function connectionStatus(callback: (isConnected: boolean) => void): void;
    export function disconnect(): void;
    export function isRemoveWifiNetwork(SSID: string): Promise<void>;
    //#endregion
}
