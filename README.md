
# react-native-wifi-reborn
[![semantic-release](https://img.shields.io/badge/%20%20%F0%9F%93%A6%F0%9F%9A%80-semantic--release-e10079.svg)](https://github.com/semantic-release/semantic-release)


This project is based on the no longer maintained https://github.com/robwalkerco/react-native-wifi.


## Getting started

`$ npm install react-native-wifi-reborn --save`

### iOS setup

You need use enable Access WIFI Information, with correct profile 

#### iOS 13

You need put "Privacy - Location When In Use Usage Description" or "Privacy - Location Always and When In Use Usage Description" in Settings -> info

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
2. Go to `node_modules` ➜ `react-native-wifi` and add `RNWifi.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNWifi.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNWifiPackage;` to the imports at the top of the file
  - Add `new RNWifiPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-wifi-reborn'
  	project(':react-native-wifi-reborn').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-wifi/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      implementation project(':react-native-wifi-reborn')
  	```

## Usage
```javascript
import WifiManager from 'react-native-wifi-reborn';

WifiManager.connectToProtectedSSID(ssid, password, isWep)
.then(() => {
	console.log('Connected successfully!')
}, () => {
	console.log('Connection failed!')
})

WifiManager.getCurrentWifiSSID()
.then((ssid) => {
	console.log("Your current connected wifi SSID is " + ssid)
}, () => {
	console.log('Cannot get current SSID!')
})
```

## API

This work is in progress.

### connectToProtectedSSID(SSID: string, password: string, isWep: boolean): Promise

Returns a promise that resolves when connected or rejects with the reason why it couldn't connect to the WIFI network.

#### SSID
Type: `string`
The SSID of the wifi network to connect with.

#### password
Type: `string`
The password of the wifi network to connect with.

#### isWeb
Type: `boolean`
Used on iOS. FIXME: why?

#### Errors:
* `notInRange`: The WIFI network is not currently in range.
* `addOrUpdateFailed`: Could not add or update the network configuration.
* `disconnectFailed`: Disconnecting from the network failed. This is done as part of the connect flow.
* `enableNetworkFailed`: Could not connect to network.

### getCurrentWifiSSID
### forceWifiUsage(useWifi: bool) [Android]

Method to force wifi usage if the user needs to send requests via wifi if it does not have internet connection.
If you want to use it, you need to add the `android.permission.WRITE_SETTINGS` permission to your AndroidManifest.xml.

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <uses-permission android:name="android.permission.WRITE_SETTINGS" />
</manifest>
```
