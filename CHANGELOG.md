## [4.3.3](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.3.2...v4.3.3) (2020-10-19)


### Bug Fixes

* Add compileOptions JavaVersion.VERSION_1_8 ([#133](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/133)) ([f28a8dc](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/f28a8dcf94aab49a5498f5354ca44f0534dd8892))

## [4.3.2](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.3.1...v4.3.2) (2020-10-01)


### Bug Fixes

* Fix Xcode 12 compatibility ([db9994a](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/db9994adeb11c225f1bab782db404153cd2b3d59)), closes [facebook/react-native#29633](https://github.com/facebook/react-native/issues/29633)

## [4.3.1](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.3.0...v4.3.1) (2020-09-22)


### Bug Fixes

* **Android:** Fix WifiUtils dependencies ([#122](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/122)) ([caf9a7e](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/caf9a7ec82e47bf41947443373577cf256879cfb))

# [4.3.0](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.2.2...v4.3.0) (2020-09-15)


### Features

* **Android:** Add forceWifiUsageWithOptions & add noInternet option ([#115](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/115)) ([df1b73a](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/df1b73addc9075472687887d786de5cc6d63a0c7))

## [4.2.2](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.2.1...v4.2.2) (2020-08-27)


### Bug Fixes

* Fix reScanAndLoadWifiList() return type ([#108](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/108)) ([431f624](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/431f6243bbfc6f2c34ec4ba73bec4f0f5b35b645))

## [4.2.1](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.2.0...v4.2.1) (2020-08-25)


### Bug Fixes

* loadWifiList and reScanAndLoadWifiList not returning a JSON array but a string ([#104](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/104)) ([9dce618](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/9dce6189eb3176fe2288ba8f85dbaf746aa3b4e6))

# [4.2.0](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.1.0...v4.2.0) (2020-06-08)


### Features

* Add isRemoveWifiNetwork() method ([#88](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/88)) ([aa195f1](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/aa195f1df9893b2cf82df30ffcb1dd2e47e5f19e)), closes [#77](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/77)

# [4.1.0](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.0.2...v4.1.0) (2020-05-27)


### Features

* Add loadWifiList meaningfull Exceptions ([#81](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/81)) ([cc8f7c1](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/cc8f7c13438a1782258c9500135e8a2edb17928c))

## [4.0.2](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.0.1...v4.0.2) (2020-05-23)


### Bug Fixes

* **iOS:** Consistent joinOnce=false configuration ([#72](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/72)) ([50cfc20](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/50cfc2008889b5d3f8c678f26839dd5bbddee440))

## [4.0.1](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.0.0...v4.0.1) (2020-05-23)


### Bug Fixes

* Fix Android 10 no-internet support ([#82](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/82)) ([45d626c](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/45d626c277fa79b13838e642eb3afb6fc86b9d91))

# [4.0.0](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v3.1.2...v4.0.0) (2020-05-01)


* Merge pull request #56 from inthepocket/feature/android-use-promises ([83e30cd](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/83e30cd749f9743771484a4d064bdf150780f538)), closes [#56](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/56)


### Features

* **promise:** change isEnabled from callback to promise. ([5631fe8](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/5631fe8e9982701cc5d9771b859d99e7b902584e))
* **promise:** return the current signal strength as a promise ([7ef9a40](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/7ef9a405ec58d9fae0f0ffe9e667063ebbac225a))
* **promise:** use a promise instead of callsbacks for isRemoveWifiNetwork ([abb3be2](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/abb3be272bc4d63fe8e0d35c079638b5e4fdac50))
* **promise:** use a promise to resolve the frequency of the currently connected WiFi network ([79dc3bf](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/79dc3bf94cc2b9c7e148f25b1679f1cffb3e1a7e))
* **promise:** use a promise to return results for reScanAndLoadWifiList() ([dcdeb0c](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/dcdeb0c8ce853483cf912b57b405b71ddae521ea))
* **promise:** use a promise to return the bssid of the currenlty connected network ([8d39c67](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/8d39c67d69387061ab7833754775dc9cffbe6683))
* **promise:** use a promise to return the loadWifiList results ([e0fe2b9](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/e0fe2b9c0653b1a6e180f9f7b8676cc6441084f8))


### BREAKING CHANGES

* Use promises instead of callbacks

## [3.1.2](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v3.1.1...v3.1.2) (2020-04-16)


### Bug Fixes

* Add null type to password on connectToProtectedSSID ([#60](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/60)) ([9ad28cc](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/9ad28cc3642d3b2abf62978e8ed552cf13151f23))

## [3.1.1](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v3.1.0...v3.1.1) (2020-03-22)


### Bug Fixes

* add location permission explanation on the readme ([12810b8](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/12810b8d3cedf1b0bdb980a56ce2075f48e785b8))

# [3.1.0](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v3.0.0...v3.1.0) (2020-03-22)


### Features

* **forceWifiUsage:** remove need of the WRITE_SETTINGS. Api calls to a wifi network without internet access can perfectly be done without ([7c9daef](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/7c9daef69d0c38536d087b1489e753673a8b89fd))

# [3.0.0](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v2.4.0...v3.0.0) (2020-03-17)


* Merge pull request #46 from inthepocket/feature/use-wifiutils-for-android-with-semantics ([eeea1af](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/eeea1afc1e42a78ce785a42dae3ff626cea3fe33)), closes [#46](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/46)


### BREAKING CHANGES

* Use WifiUtils to connect with a wifi network

# [2.4.0](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v2.3.4...v2.4.0) (2020-03-04)


### Features

* Add TypeScript declaration ([#44](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/44)) ([610fc32](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/610fc32bc01959518b6d4345a3ddab2e1138f085))

## [2.3.4](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v2.3.3...v2.3.4) (2020-03-04)


### Performance Improvements

* Reduce package size ([#43](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/43)) ([d108823](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/d108823989538a5d102b034f393bfada21619b75))

## [2.3.3](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v2.3.2...v2.3.3) (2020-02-27)


### Bug Fixes

* **iOS:** Fix joinOnce for connectToSSIDPrefix ([#40](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/40)) ([61185a9](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/61185a960b06fe621af3c4c8c8d114036b7ff042))

## [2.3.2](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v2.3.1...v2.3.2) (2020-01-21)


### Bug Fixes

* **android:** Verify connection after enabling network ([#31](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/31)) ([9fa86ee](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/9fa86ee0141535bd142f0e10ff4fd698ccda3533))

## [2.3.1](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v2.3.0...v2.3.1) (2020-01-16)


### Bug Fixes

* **iOS:** Use joinOnce as mentioned https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/2. ([#33](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/33)) ([6c71618](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/6c7161857b17866594ed49cc96b6bda49b74fb40))

# [2.3.0](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v2.2.4...v2.3.0) (2020-01-16)


### Features

* **iOS:** Connect to SSID prefix functions. ([#25](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/25)) ([5b52c97](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/5b52c97461753cb2fb7574964a1415ddaa23ba29))

## [2.2.4](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v2.2.3...v2.2.4) (2019-12-18)


### Performance Improvements

* **test:** test perf semantic-release ([0d42570](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/0d425709f01b84a6882d27e2698d8e92783efb43))

## [2.2.3](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v2.2.2...v2.2.3) (2019-12-12)


### Bug Fixes

* **iOS:** getCurrentWifiSSID when is deneided or  restrited ([1c6d1fd](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/1c6d1fd0a30840695201e38c6b1db1e6833cf1bf))

## [2.2.2](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v2.2.1...v2.2.2) (2019-12-11)


### Bug Fixes

* **Android:** Bad marge ([b6a3cf5](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/b6a3cf5acdf701857137d76af98317e20d73cb12))

## [2.2.1](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v2.2.0...v2.2.1) (2019-12-10)


### Bug Fixes

* **android:** Added network compatibility ([331a40f](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/331a40f2016773a87f5e4d134d4ff1fed9f62867))

# [2.2.0](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v2.1.1...v2.2.0) (2019-11-19)


### Features

* **Android:** Update all library ([f4cc526](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/f4cc526ba7c7417fe883c58188dea212bb2b5d20))

## [2.1.1](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v2.1.0...v2.1.1) (2019-11-15)


### Bug Fixes

* **Android:** Rename package to avoid colisions ([17da67e](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/17da67e6bbe2675dd307071414a63bf1b24c58b6))

# [2.1.0](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v2.0.4...v2.1.0) (2019-11-03)


### Features

* **Android:** Update gradle configuration ([475cc30](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/475cc3029fdb1f9406e04894ff9eaee64844464d)), closes [#5](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/5)

## [2.0.4](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v2.0.3...v2.0.4) (2019-11-03)


### Bug Fixes

* **ios:** Change settingsURL value so Apple accepts it. Referencing comments from: ([653330e](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/653330e7676cf21859f7a0a982c22ae85530c807))

## [2.0.3](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v2.0.2...v2.0.3) (2019-10-25)


### Bug Fixes

* **IOS:** Live Reload ([aa44375](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/aa443754a6188d61af797c5406a631b1811a603e))

## [2.0.2](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v2.0.1...v2.0.2) (2019-10-24)


### Bug Fixes

* **semantic-release:** Add more extensions ([40f0e01](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/40f0e012cf69adeabfc665eb1382a49fbdc8fbbf))
