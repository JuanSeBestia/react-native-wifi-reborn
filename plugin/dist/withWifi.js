"use strict";
exports.__esModule = true;
var config_plugins_1 = require("@expo/config-plugins");
// eslint-disable-next-line @typescript-eslint/no-var-requires
var pkg = require('../../package.json');
/**
 * Adds `HotSpotConfiguration` and `wifi-info` to the `entitlements.plist`.
 *
 * @returns
 */
// eslint-disable-next-line @typescript-eslint/no-unused-vars
var withIosPermissions = function (c, _) {
    return config_plugins_1.withEntitlementsPlist(c, function (config) {
        config.modResults['com.apple.developer.networking.HotspotConfiguration'] = true;
        config.modResults['com.apple.developer.networking.wifi-info'] = true;
        return config;
    });
};
/**
 * Adds the following to the `AndroidManifest.xml`: `<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />`
 */
var withAndroidPermissions = function (config, props) {
    var fineLocationPermission = props.fineLocationPermission === false ? [] : ['android.permission.ACCESS_FINE_LOCATION'];
    return config_plugins_1.AndroidConfig.Permissions.withPermissions(config, fineLocationPermission);
};
var withWifi = function (config, props) {
    if (props === void 0) { props = {}; }
    config = withIosPermissions(config, props);
    config = withAndroidPermissions(config, props);
    return config;
};
exports["default"] = config_plugins_1.createRunOncePlugin(withWifi, pkg.name, pkg.version);
