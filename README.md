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

This project is based on (https://github.com/robwalkerco/react-native-wifi)

## Getting started

`$ npm install react-native-wifi-reborn --save`

### Mostly automatic installation RN <0.59

`$ react-native link react-native-wifi-reborn`

### [IOS] IMPORTANT NOTE:

You need use enable Access WIFI Information, with correct profile

#### [IOS 13 +] IMPORTANT NOTE:

You need put "Privacy - Location When In Use Usage Description" or "Privacy - Location Always and When In Use Usage Description" in Settings -> info

### Manual installation

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
