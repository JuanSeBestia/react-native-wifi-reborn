# react-native-wifi-reborn

<p align="center">
  <a href="./LICENSE">
    <img src="https://img.shields.io/badge/license-ISC-blue.svg" alt="ISC license" />
  </a>
  <a href="https://npmjs.org/package/react-native-wifi-reborn">
    <img src="http://img.shields.io/npm/v/react-native-wifi-reborn.svg" alt="Current npm package version" />
  </a>
  <a href="https://github.com/JuanSeBestia/react-native-wifi-reborn/graphs/commit-activity">
    <img src="https://img.shields.io/badge/Maintained%3F-yes-brightgreen.svg" alt="Maintenance" />
  </a>
  <a href="https://github.com/semantic-release/semantic-release#how-does-it-work">
    <img src="https://img.shields.io/badge/%20%20%F0%9F%93%A6%F0%9F%9A%80-semantic--release-e10079.svg" alt="Semantic Release" />
  </a>
  <a href="https://npmjs.org/package/react-native-wifi-reborn">
    <img src="http://img.shields.io/npm/dm/react-native-wifi-reborn.svg" alt="Downloads" />
  </a>
  <a href="https://npmjs.org/package/react-native-wifi-reborn">
    <img src="http://img.shields.io/npm/dt/react-native-wifi-reborn.svg?label=total%20downloads" alt="Total downloads" />
  </a>
  <a href="https://twitter.com/intent/follow?screen_name=JuanSeBestia">
    <img src="https://img.shields.io/twitter/follow/JuanSeBestia.svg?label=Follow%20@JuanSeBestia" alt="Follow @JuanSeBestia" />
  </a>
</p>

This project is based on the no longer maintained https://github.com/robwalkerco/react-native-wifi.

## Getting started

`$ npm install react-native-wifi-reborn --save`

### iOS

You need use enable `Access WIFI Information`, with correct profile. `Hotspot Configuration` is required in order to connect to networks.

#### iOS 13

You need put "Privacy - Location When In Use Usage Description" or "Privacy - Location Always and When In Use Usage Description" in Settings -> info

### Android

#### `ACCESS_FINE_LOCATION` permission

Since [Android 6](https://developer.android.com/about/versions/marshmallow), you must request the [`ACCESS_FINE_LOCATION`](https://developer.android.com/reference/android/Manifest.permission#ACCESS_FINE_LOCATION) permission at runtime to use the device's Wi-Fi scanning and managing capabilities. In order to accomplish this, you can use the [PermissionsAndroid API](https://reactnative.dev/docs/permissionsandroid) or [React Native Permissions](https://github.com/react-native-community/react-native-permissions).

Example:
```javascript
import { PermissionsAndroid } from 'react-native';

const granted = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
      {
        title: 'Location permission is required for WiFi connections',
        message:
          'This app needs location permission as this is required  ' +
          'to scan for wifi networks.',
        buttonNegative: 'DENY',
        buttonPositive: 'ALLOW',
      },
);
if (granted === PermissionsAndroid.RESULTS.GRANTED) {
    // You can now use react-native-wifi-reborn
} else {
    // Permission denied
}
```

### Autolinking (React Native 60+)

This library is correctly autolinked on React Native 60+ 🎉.

### React Native Link (for React Native 0.59 and below)

`$ react-native link react-native-wifi-reborn`

### Manual linking

#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-wifi-reborn` and add `RNWifi.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNWifi.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`

- Add `import com.reactlibrary.RNWifiPackage;` to the imports at the top of the file
- Add `new RNWifiPackage()` to the list returned by the `getPackages()` method

2. Append the following lines to `android/settings.gradle`:
   ```
   include ':react-native-wifi-reborn'
   project(':react-native-wifi-reborn').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-wifi-reborn/android')
   ```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      implementation project(':react-native-wifi-reborn')
  	```

## Usage

```javascript
import WifiManager from "react-native-wifi-reborn";

WifiManager.connectToProtectedSSID(ssid, password, isWep).then(
  () => {
    console.log("Connected successfully!");
  },
  () => {
    console.log("Connection failed!");
  }
);

WifiManager.getCurrentWifiSSID().then(
  ssid => {
    console.log("Your current connected wifi SSID is " + ssid);
  },
  () => {
    console.log("Cannot get current SSID!");
  }
);
```

# Methods


_The api documentation is in progress._


## Android & iOS

The following methods work on both Android and iOS

### `connectToProtectedSSID(SSID: string, password: string, isWEP: boolean): Promise`

Returns a promise that resolves when connected or rejects with the error when it couldn't connect to the wifi network.

#### SSID

Type: `string`

The SSID of the wifi network to connect with.

#### password

Type: `string`

The password of the wifi network to connect with.

#### isWep
Type: `boolean`
Used on iOS. If true, the network is WEP Wi-Fi; otherwise it is a WPA or WPA2 personal Wi-Fi network.

#### Errors:
* iOS:
  * `unavailableForOSVersion`: Starting from iOS 11, NEHotspotConfigurationError is available.
  * `invalid`: If an unknown error is occurred.
  * `invalidSSID`: If the SSID is invalid.
  * `invalidSSIDPrefix`: If the SSID prefix is invalid.
  * `invalidPassphrase`: If the passphrase is invalid.
  * `userDenied`: If the user canceled the request to join the asked network.
  * `locationPermissionDenied`: Starting from iOS 13, location permission is denied.
  * `locationPermissionRestricted`: Starting from iOS 13, location permission is restricted.
  * `couldNotDetectSSID`: If the SSID couldn't be detected.
* Android:
  * `locationPermissionMissing`: Starting android 6, location permission needs to be granted for wifi scanning.
  * `locationServicesOff`: Starting Android 6, location services needs to be on to scan for wifi networks.
  * `couldNotEnableWifi`: Starting Android 10, apps are no longer allowed to enable wifi. User has to manually do this.
  * `couldNotScan`: Starting Android 9, it's only allowed to scan 4 times per 2 minuts in a foreground app.
  * `didNotFindNetwork`: If the wifi network is not in range, the security type is unknown and WifiUtils doesn't support connecting to the network.
  * `authenticationErrorOccurred`: Authentication error occurred while trying to connect. The password could be incorrect or the user could have a saved network configuration with a different password!
  * `android10ImmediatelyDroppedConnection` : Firmware bugs on OnePlus prevent it from connecting on some firmware versions. More info: https://github.com/ThanosFisherman/WifiUtils/issues/63.
  * `timeoutOccurred`: Could not connect in the timeout window.
* Both:
  * `unableToConnect`: When an unknown error occurred.

### `getCurrentWifiSSID(): Promise`

## Only iOS

The following methods work only on iOS

###  `connectToSSID(ssid: string): Promise`

###  `connectToSSIDPrefix(ssid: string): Promise`

### `disconnectFromSSID(ssid: string): Promise`

### `connectToProtectedSSIDPrefix(SSIDPrefix: string, password: string, isWep: boolean): Promise`

Use this function when you want to match a known SSID prefix, but don’t have a full SSID. If the system finds multiple Wi-Fi networks whose SSID string matches the given prefix, it selects the network with the greatest signal strength.

#### SSIDPrefix
Type: `string`
A prefix string to match the SSID of a Wi-Fi network.

#### password
Type: `string`
The password of the wifi network to connect with.

#### isWep
Type: `boolean`
Used on iOS. If YES, the network is WEP Wi-Fi; otherwise it is a WPA or WPA2 personal Wi-Fi network.


#### Errors:
* `unavailableForOSVersion`: Starting from iOS 11, NEHotspotConfigurationError is available.
* `invalid`: If an unknown error is occurred.
* `invalidSSID`: If the SSID is invalid.
* `invalidSSIDPrefix`: If the SSID prefix is invalid.
* `invalidPassphrase`: If the passphrase is invalid.
* `userDenied`: If the user canceled the request to join the asked network.
* `locationPermissionDenied`: Starting from iOS 13, location permission is denied.
* `locationPermissionRestricted`: Starting from iOS 13, location permission is restricted.
* `couldNotDetectSSID`: If the SSID couldn't be detected.
* `unableToConnect`: When an unknown error occurred.

## Only Android
The following methods work only on Android

### `loadWifiList(): Promise<Array<WifiEntry>>`

Returns a list of nearby WiFI networks.
* `SSID`: The network name.
* `BSSID`: The WiFi BSSID.
* `capabilities`: Describes the authentication, key management, and encryption schemes supported by the access point.
* `frequency`: The primary 20 MHz frequency (in MHz) of the channel over which the client is communicating with the access point.
* `level`: The detected signal level in dBm, also known as the RSSI.
* `timestamp`: timestamp in microseconds (since boot) when this result was last seen.

#### Errors:
* `locationPermissionMissing`: Starting android 6, location permission needs to be granted for wifi 
* `locationServicesOff`: Starting Android 6, location services needs to be on to scan for wifi networks.
* `exception`: Any other caught exception.

### `reScanAndLoadWifiList(): Promise<Array<WifiEntry>>`
Similar to `loadWifiList` but it forcefully starts a new WiFi scan and only passes the results when the scan is done.

### `isEnabled(): Promise<boolean>`
Method to check if WiFi is enabled.

```javascript
const enabled = await WifiManager.isEnabled();
this.setState({wifiIsEnabled: enabled});
```

### `setEnabled(enabled: boolean)`

Method to set the WiFi on or off on the user's device.

```javascript
WifiManager.setEnabled(true); //set WiFi ON
WifiManager.setEnabled(false); //set WiFi OFF
```

### `connectionStatus(): Promise<boolean>`

Returns if the device is currently connected to a WiFi network.

### `disconnect()`
Disconnect currently connected WiFi network.

### `getBSSID(): Promise<string>`
Returns the BSSID (basic service set identifier) of the currently connected WiFi network.

### `getCurrentSignalStrength(): Promise<number>`
Returns the RSSI (received signal strength indicator) of the currently connected WiFi network.


### `getFrequency(): Promise<number>`
Returns the frequency of the currently connected WiFi network.

### `getIP(): Promise<number>`
Returns the IP of the currently connected WiFi network.

### `isRemoveWifiNetwork(ssid: String): Promise<boolean>`
This method will remove the wifi network configuration.
If you are connected to that network, it will disconnect.

#### Errors:
* `locationPermissionMissing`: Starting android 6, location permission needs to be granted for wifi 

### `forceWifiUsage(useWifi: boolean): Promise`
Deprecated; see forceWifiUsageWithOptions.

### `forceWifiUsageWithOptions(useWifi: boolean, options<Record<string, unknown>)`

Use this to execute api calls to a wifi network that does not have internet access.
Useful for commissioning IoT devices.
This will route all app network requests to the network (instead of the mobile connection).
It is important to disable it again after using as even when the app disconnects from the wifi network it will keep on routing everything to wifi.

#### options
* `noInternet: Boolean`: Indicate the wifi network does not have internet connectivity.

## Conventions

* Anuglar JS Git Commit conventions are used, read more: https://gist.github.com/stephenparish/9941e89d80e2bc58a153#recognizing-unimportant-commits
