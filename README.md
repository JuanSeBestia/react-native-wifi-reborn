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

You need use enable Access WIFI Information, with correct profile

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

#### When using Wix React Native Navigation

##### Android

While the library is included (via settings.gradle) and added (via build.gradle), you still need to manually added to your MainApplication.

```java
import com.reactlibrary.RNWifiPackage;

public class MainApplication extends NavigationApplication {
@Override
	public List<ReactPackage> createAdditionalReactPackages() {
	return Arrays.asList(
		...,
		new RNWifiPackage());
	}
}
```

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
* `location permission missing`: The location permission (ACCESS_FINE_LOCATION) is not granted (android 6+).
* `location off`: The location service needs to be turned on (android 6+).
* `failed`: Could not connect to the network. Could be due to multiple reasons; not in rang or wrong password.

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

* `notInRange`: The WIFI network is not currently in range.

* `addOrUpdateFailed`: Could not add or update the network configuration.

* `disconnectFailed`: Disconnecting from the network failed. This is done as part of the connect flow.

* `connectNetworkFailed`: Could not connect to network.

## Only Android
The following methods work only on Android

<details>
<summary>
loadWifiList(): `Promise<Array<WifiEntry>>`</summary>
Returns a list of nearby WiFI networks.
* `SSID`: The network name.
* `BSSID`: The WiFi BSSID.
* `capabilities`: Describes the authentication, key management, and encryption schemes supported by the access point.
* `frequency`: The primary 20 MHz frequency (in MHz) of the channel over which the client is communicating with the access point.
* `level`: The detected signal level in dBm, also known as the RSSI.
* `timestamp`: timestamp in microseconds (since boot) when this result was last seen.
</details>

<details>
<summary>reScanAndLoadWifiList(): Promise<Array<string>></summary>
Similar to `loadWifiList` but it forcefully starts a new WiFi scan and only passes the results when the scan is done.
</details>

<details>
<summary>isEnabled(): Promise<boolean></summary>
Method to check if WiFi is enabled.

```javascript
const enabled = await WifiManager.isEnabled();
this.setState({wifiIsEnabled: enabled});
```
</details>

### `setEnabled(enabled: boolean)`

Method to set the WiFi on or off on the user's device.

```javascript
WifiManager.setEnabled(true); //set WiFi ON
WifiManager.setEnabled(false); //set WiFi OFF
```

<details>
<summary>connectionStatus(): Promise<boolean></summary>
Returns if the device is currently connected to a WiFi network.
</details>

<details>
<summary>disconnect()</summary>
Disconnect currently connected WiFi network.
</details>

<details>
<summary>getBSSID(): Promise<string></summary>
Returns the BSSID (basic service set identifier) of the currently connected WiFi network.
</details>

<details>
<summary>getCurrentSignalStrength(): Promise<number></summary>
Returns the RSSI (received signal strength indicator) of the currently connected WiFi network.
</details>

<details>
<summary>getFrequency(): Promise<number></summary>
Returns the frequency of the currently connected WiFi network.
</details>

<details>
<summary>getIP(): Promise<number></summary>
Returns the IP of the currently connected WiFi network.
</details>

<details>
<summary>isRemoveWifiNetwork(ssid: String): Promise<boolean></summary>
This method will remove the wifi network configuration.
If you are connected to that network, it will disconnect.
</details>

<details>
<summary>forceWifiUsage(useWifi: boolean): Promise</summary>

 Use this to execute api calls to a wifi network that does not have internet access.
 Useful for commissioning IoT devices.
 This will route all app network requests to the network (instead of the mobile connection).
 It is important to disable it again after using as even when the app disconnects from the wifi network it will keep on routing everything to wifi.
</details>

## Conventions

* Anuglar JS Git Commit conventions are used, read more: https://gist.github.com/stephenparish/9941e89d80e2bc58a153#recognizing-unimportant-commits