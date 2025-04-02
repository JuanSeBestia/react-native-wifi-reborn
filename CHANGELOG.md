## [4.13.5](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.13.4...v4.13.5) (2025-04-02)


### Bug Fixes

* Update README.md ([59f92a5](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/59f92a52336f411b60aec0009c0049683e32287c))

## [4.13.4](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.13.3...v4.13.4) (2025-01-23)


### Bug Fixes

* **types:** Align types, index and native module interfaces ([#416](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/416)) ([f15187e](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/f15187e0cbbb6da940d40603b35149c4f49d284d))

## [4.13.3](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.13.2...v4.13.3) (2025-01-20)


### Bug Fixes

* **ios:** prevent crash when canceling WiFi connection ([#425](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/425)) ([e398406](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/e398406e87a15fc452bb230ce204d5c7382b3af4)), closes [#424](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/424)

## [4.13.2](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.13.1...v4.13.2) (2025-01-18)


### Bug Fixes

* **android:** connection status support for per-app wifi connections ([#420](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/420)) ([a0b8fb6](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/a0b8fb67cf51950b1dca5bce4c100d2e2fae0521)), closes [#419](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/419)

## [4.13.1](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.13.0...v4.13.1) (2024-12-30)


### Bug Fixes

* **Android:** properly check on pollForValidSSID if network connected ([#399](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/399)) ([0371acd](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/0371acd87a627cf30c0ddc72c4d28aeed4a74be1))

# [4.13.0](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.12.1...v4.13.0) (2024-09-17)


### Bug Fixes

* add semicolon ([561ea0a](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/561ea0a12a57cf7bde6caf573258f1e82dbcc43e))
* fix error argument is marked as nullable but React requires that all NSNumber arguments are nonnull ([b3f4e84](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/b3f4e8480ed7146e0300117d6b2f9208c0a3b4e8))
* use node 20 for semantic-release ([f516e8d](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/f516e8dbf39aea549f281d23253535e25ff41ecb))


### Features

* **iOS:** Add new method to connect to a SSIDPrefix protected only once, Android: Try to address issue [#303](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/303) ([d98ccfc](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/d98ccfc42844826a45270f97567fd8bdba430fec))

## [4.12.1](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.12.0...v4.12.1) (2024-03-28)


### Bug Fixes

* crash on Android when isHidden params not passed ([e3d7dcc](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/e3d7dcc0910a26d50f8689e38acb7441f4343927))

# [4.12.0](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.11.0...v4.12.0) (2024-03-26)


### Bug Fixes

* **android:** re-add onLost method ([be1d872](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/be1d872d3be8c9ed84a92ea9f7c0ab20935ea388))
* IOS method ([b5bdb35](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/b5bdb35f5c51c35e2b8a4d50a9474cd396e40407))
* removed assert password != null ([93d737a](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/93d737aef7dd5a1b1d2dcb0e5bbf2bc796c05138))
* use enum for error ([7b7587c](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/7b7587c27ff8b4544a4b05cb62e93740188cce2c))


### Features

* add new method connectToProtectedWifiSSID ([1813073](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/1813073561a837e3a7a5eedd413fa76f003702ae))
* add nullable password ([75ff05f](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/75ff05fd37185c2e2b5302a2c1e373447e0e4f2d))
* **android:** add timeout parameter ([9426913](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/94269130b3ac7589accd653b8ac2948083618d72))
* update README ([8970ba4](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/8970ba47d368e189e9f6af4d2eb060617ecfb872))

# [4.11.0](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.10.1...v4.11.0) (2024-02-12)


### Features

* **iOS, Loop patch:** check the connected WiFi network repeatedly until it's the one we requested ([#301](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/301)) ([e7088be](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/e7088be14546c2da79e225aad1295e32728bb558))

## [4.10.1](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.10.0...v4.10.1) (2023-11-06)


### Bug Fixes

* **npm:** remove engines from package.json ([8bd1385](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/8bd13854ad02350c7a2477279e551a892062ab76))

# [4.10.0](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.9.0...v4.10.0) (2023-10-27)


### Features

* **android:** 🌟 add support for React Native 0.73 ([#312](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/312)) ([451b92c](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/451b92c53a0e173cd48f95b38f0bcaba2c5b63a8))

# [4.9.0](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.8.3...v4.9.0) (2023-10-27)


### Bug Fixes

* **Android:** Open the Wifi settings panel on Android 10 and above. ([#319](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/319)) ([bd89902](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/bd89902d10a22ad3fc3a5f81534d6a817fae5cc6))


### Features

* add with-expo example ([#314](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/314)) ([f572a81](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/f572a81f4a45f5efa77bfca0b7103d372fbada7c))

## [4.8.3](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.8.2...v4.8.3) (2023-05-26)


### Bug Fixes

* **iOS:** add missing isHidden flag to connectToProtectedSSID ([#306](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/306)) ([11a703d](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/11a703d4ece723fb9fce90e0dc0e02f865613190)), closes [#287](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/287)

## [4.8.2](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.8.1...v4.8.2) (2023-05-26)


### Bug Fixes

* prefer not to mutate the ScanResult object ([#268](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/268)) ([840c80d](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/840c80de870d91238c50842ab53827334d380ec2))

## [4.8.1](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.8.0...v4.8.1) (2023-05-26)


### Bug Fixes

* update android native dependencies ([472896d](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/472896d51c90b67ac6ba7329eb34929acde50155))

# [4.8.0](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.7.1...v4.8.0) (2023-05-26)


### Features

* **Android:** added feature to connect to hidden wifi on android 10 ([#287](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/287)) ([32b5db2](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/32b5db2e1ebcdc757a4dec39f1441a4c9586a827))

## [4.7.1](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.7.0...v4.7.1) (2023-05-26)


### Bug Fixes

* **Android:** * add location permission check for getCurrentWifiSSID and reScanAndLoadWifiList and change error message for clarity ([#264](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/264)) ([c3ba4e5](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/c3ba4e532fc937cf757937448140af50e360988c))

# [4.7.0](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.6.0...v4.7.0) (2022-07-12)


### Features

* **Android:** Allows to show hidden WiFI AP while scanning ([#255](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/255)) ([29d364a](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/29d364ac7a8eef143363eb9c2b0ecd457dc02cea))

# [4.6.0](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.5.1...v4.6.0) (2022-06-09)


### Features

* Minor bug fix, iOS 15 getWifiSSID fix, connect to Android Wifi without scanning (because of the limit) ([#249](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/249)) ([3389baf](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/3389baf1775e400107797acabc5491d422cdd9b1))

## [4.5.1](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.5.0...v4.5.1) (2022-06-09)


### Bug Fixes

* Use enum types instead of const in the TypeScript declaration file ([5e6dfeb](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/5e6dfebb8586a3f981893a4aa7e0a5e7bfabeca4))

# [4.5.0](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.4.2...v4.5.0) (2021-12-16)


### Features

* Add Expo config plugin ([#226](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/226)) ([d328a87](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/d328a8723169c7ca7615677c9c1150256002e187))

## [4.4.2](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.4.1...v4.4.2) (2021-12-16)


### Bug Fixes

* **iOS:** Revert wrong removal of promise resolution ([#225](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/225)) ([89eda2d](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/89eda2d126546fcabbe85ad2de8bbee9ba0e793c))

## [4.4.1](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.4.0...v4.4.1) (2021-11-04)


### Bug Fixes

* **Android:** Replace jCenter with mavenCentral ([#216](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/216)) ([b047208](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/b04720822c62498a47eb8f671dd79c2d557f4d24))
* **iOS:** Fix disconnectFromSSID having no effect ([#214](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/214)) ([0d55c08](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/0d55c086798b4c4bfc2216056138b67f25ed8173))
* **iOS:** Fix prefix check in connectToProtectedSSIDPrefix ([#220](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/220)) ([d94f356](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/d94f356dedc357a68f397fe1dab5177e41ba0903))

# [4.4.0](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.3.8...v4.4.0) (2021-09-29)


### Bug Fixes

* Fix return type for forceWifiUsageWithOptions ([#170](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/170)) ([6de4204](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/6de42043d01cee4c00b80b22f86d0e9545fbfd2b))


### Features

* **iOS:** allow joinOnce property to be set for iOS devices ([#187](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/187)) ([991f9cc](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/991f9ccca28fe82bfcc5f23be135e6ec2c68dc1f))

## [4.3.8](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.3.7...v4.3.8) (2021-05-26)


### Bug Fixes

* **iOS:** Reject after not connecting to WiFi with SSID-prefix ([#183](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/183)) ([f255caa](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/f255caad992826324eaa6790451c304271faa7b4)), closes [#182](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/182)

## [4.3.7](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.3.6...v4.3.7) (2021-01-14)


### Bug Fixes

* **Android:** Replace Kotlin files with Java ([#154](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/154)) ([1e15eac](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/1e15eac83244056fcac8ee727e4772588d096fa1))

## [4.3.6](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.3.5...v4.3.6) (2021-01-10)


### Bug Fixes

* **Android:** getCurrentWifiSSID return null if '<unknown ssid>' ([#148](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/148)) ([336668a](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/336668a665db05fac141f6f7f0f240cdd43b23c5))

## [4.3.5](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.3.4...v4.3.5) (2021-01-07)


### Bug Fixes

* **Android:** Add elvis dependency to build.gradle ([#152](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/152)) ([1ab284a](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/1ab284ab771e1bd66156d17805dac24d3febd744))

## [4.3.4](https://github.com/JuanSeBestia/react-native-wifi-reborn/compare/v4.3.3...v4.3.4) (2020-12-19)


### Bug Fixes

* **Android:** Fix BSSID type ([#146](https://github.com/JuanSeBestia/react-native-wifi-reborn/issues/146)) ([a769618](https://github.com/JuanSeBestia/react-native-wifi-reborn/commit/a76961872638858275727ddceda04a5d5ab6d1bf))

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
