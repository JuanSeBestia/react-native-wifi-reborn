package com.reactlibrary.rnwifi.mappers;

import android.net.wifi.ScanResult;

import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;

import java.util.List;

public class WifiScanResultsMapper {
    private WifiScanResultsMapper() {
    }

    private static String parseSSID(final ScanResult scanResult) {
        if (scanResult.SSID.equals("")) {
            return "(hidden SSID)";
        }

        return scanResult.SSID;
    }

    public static WritableArray mapWifiScanResults(final List<ScanResult> scanResults) {
        final WritableArray wifiArray = new WritableNativeArray();

        for (ScanResult result : scanResults) {
            final WritableMap wifiObject = new WritableNativeMap();
            wifiObject.putString("SSID", parseSSID(result));
            wifiObject.putString("BSSID", result.BSSID);
            wifiObject.putString("capabilities", result.capabilities);
            wifiObject.putInt("frequency", result.frequency);
            wifiObject.putInt("level", result.level);
            wifiObject.putDouble("timestamp", result.timestamp);
            wifiArray.pushMap(wifiObject);
        }

        return wifiArray;
    }
}
