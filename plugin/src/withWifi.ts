import {
    AndroidConfig,
    ConfigPlugin,
    createRunOncePlugin,
    withEntitlementsPlist,
} from '@expo/config-plugins';
// eslint-disable-next-line @typescript-eslint/no-var-requires
const pkg = require('../../package.json');

export type Props = {
    fineLocationPermission?: false;
};

/**
 * Adds `HotSpotConfiguration` and `wifi-info` to the `entitlements.plist`.
 *
 * @returns
 */
// eslint-disable-next-line @typescript-eslint/no-unused-vars
const withIosPermissions: ConfigPlugin<Props> = (c, _) => {
    return withEntitlementsPlist(c, (config) => {
        config.modResults['com.apple.developer.networking.HotspotConfiguration'] = true;

        config.modResults['com.apple.developer.networking.wifi-info'] = true;

        return config;
    });
};

/**
 * Adds the following to the `AndroidManifest.xml`: `<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />`
 */
const withAndroidPermissions: ConfigPlugin<Props> = (config, props) => {
    const fineLocationPermission =
        props.fineLocationPermission === false ? [] : ['android.permission.ACCESS_FINE_LOCATION'];
    return AndroidConfig.Permissions.withPermissions(config, fineLocationPermission);
};

const withWifi: ConfigPlugin<Props> = (config, props = {}) => {
    config = withIosPermissions(config, props);
    config = withAndroidPermissions(config, props);
    return config;
};

export default createRunOncePlugin(withWifi, pkg.name, pkg.version);
