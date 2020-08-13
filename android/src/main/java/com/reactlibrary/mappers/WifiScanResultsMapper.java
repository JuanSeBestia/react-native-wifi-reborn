package com.reactlibrary.mappers;

import android.net.wifi.ScanResult;

import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;

import java.util.List;

public class WifiScanResultsMapper {
    private WifiScanResultsMapper() {
    }

    public static WritableArray mapWifiScanResults(final List<ScanResult> scanResults) {
        final WritableArray wifiArray = new WritableNativeArray();

        for (ScanResult result : scanResults) {
            final WritableMap wifiObject = new WritableNativeMap();
            if (!result.SSID.equals("")) {
                wifiObject.putString("SSID", result.SSID);
                wifiObject.putString("BSSID", result.BSSID);
                wifiObject.putString("capabilities", result.capabilities);
                wifiObject.putInt("frequency", result.frequency);
                wifiObject.putInt("level", result.level);
                wifiObject.putDouble("timestamp", result.timestamp);
                wifiArray.pushMap(wifiObject);
            }
        }

        return wifiArray;
    }
}
