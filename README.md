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

### iOS setup

You need use enable Access WIFI Information, with correct profile

#### iOS 13

You need put "Privacy - Location When In Use Usage Description" or "Privacy - Location Always and When In Use Usage Description" in Settings -> info

### Android

Location permission (a runtime permission starting Android 6) is required for some methods (https://github.com/inthepocket/react-native-wifi-reborn#connecttoprotectedssidssid-string-password-string-iswep-boolean-promise). Make sure to request them at runtime: https://facebook.github.io/react-native/docs/permissionsandroid.

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
Used on iOS. If YES, the network is WEP Wi-Fi; otherwise it is a WPA or WPA2 personal Wi-Fi network.

#### Errors:
* `notInRange`: The WIFI network is not currently in range.
* `addOrUpdateFailed`: Could not add or update the network configuration.
* `disconnectFailed`: Disconnecting from the network failed. This is done as part of the connect flow.
* `connectNetworkFailed`: Could not connect to network.

### connectToProtectedSSIDPrefix(SSIDPrefix: string, password: string, isWep: boolean): Promise

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

* `location permission missing`: Location permission is not granted.
* `location off`: Location service is turned off.
* `failed`: Could not connect to network.

### `getCurrentWifiSSID(): Promise`

## Only iOS

The following methods work only on iOS

###  `connectToSSID(ssid: string): Promise`

###  `connectToSSIDPrefix(ssid: string): Promise`

### `disconnectFromSSID(ssid: string): Promise`

## Only Android
The following methods work only on Android

### `loadWifiList(successCallback: function, errorCallback: function)`

Method to get a list of nearby WiFI networks.

#### successCallback( wifiList:  string )

Type: `function`

Function to be called if the attempt is successful. It contains a stringified JSONArray of wifiObjects as parameter, each object containing:

* `SSID`: The network name.
* `BSSID`: The WiFi BSSID.
* `capabilities`: Describes the authentication, key management, and encryption schemes supported by the access point.
* `frequency`: The primary 20 MHz frequency (in MHz) of the channel over which the client is communicating with the access point.
* `level`: The detected signal level in dBm, also known as the RSSI.
* `timestamp`: timestamp in microseconds (since boot) when this result was last seen.

 #### errorCallback

Type: `function`

Function to be called if any error occurs during the attempt. It contains a `string` as parameter with the error message.

#### Usage

```javascript
WifiManager.loadWifiList(
	wifiList => {
		let wifiArray =  JSON.parse(wifiList);
		wifiArray.map((value, index) =>
			console.log(`Wifi ${index  +  1} - ${value.SSID}`)
		);
	},
	error =>  console.log(error)
);
/**
Result:
"Wifi 1 - Name of the network"
"Wifi 2 - Name of the network"
"Wifi 3 - Name of the network"
 ...
 */
```

### `reScanAndLoadWifiList(successCallback: function, errorCallback: function)`

This method is similar to `loadWifiList` but it forcefully starts the wifi scanning on android and in the callback fetches the list.

#### Usage

Same as `loadWifiList`.

### `isEnabled(isEnabled: function)`

Method to check if WiFi is enabled.

```javascript
WifiManager.isEnabled(isEnabled => {
	this.setState({wifiIsEnabled: isEnabled});
});
```

### `setEnabled(enabled: boolean)`

Method to set the WiFi on or off on the user's device.

```javascript
WifiManager.setEnabled(true); //set WiFi ON
WifiManager.setEnabled(false); //set WiFi OFF
```

### `connectionStatus (connectionStatusResult: function)`

Indicates whether network connectivity exists and it is possible to establish connections.

#### connectionStatusResult( isConnected: boolean )

Type: `function`

Called when the network status is resolved. It contains a boolean argument

### `disconnect`

### `getBSSID`

### `getCurrentSignalStrength`

### `getFrequency`

### `getIP`

### `isRemoveWifiNetwork`

### forceWifiUsage(useWifi: bool)

Method to force wifi usage if the user needs to send requests via wifi if it does not have internet connection.

If you want to use it, you need to add the `android.permission.WRITE_SETTINGS` permission to your AndroidManifest.xml.

```xml

<manifest  xmlns:android="http://schemas.android.com/apk/res/android">

<uses-permission  android:name="android.permission.WRITE_SETTINGS" />

</manifest>

```
