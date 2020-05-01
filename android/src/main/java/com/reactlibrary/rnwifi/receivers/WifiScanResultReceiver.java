package com.reactlibrary.rnwifi.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.facebook.react.bridge.Promise;
import com.facebook.react.uimanager.IllegalViewOperationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;

public class WifiScanResultReceiver extends BroadcastReceiver {
    private final WifiManager wifiManager;
    private final Promise promise;

    public WifiScanResultReceiver(@NonNull final WifiManager wifiManager, @NonNull final Promise promise) {
        super();
        this.promise = promise;
        this.wifiManager = wifiManager;
    }

    // This method call when number of wifi connections changed
    public void onReceive(final Context context, final Intent intent) {
        context.unregisterReceiver(this);
        try {
            final List<ScanResult> results = this.wifiManager.getScanResults();
            final JSONArray wifiArray = new JSONArray();

            for (ScanResult result : results) {
                JSONObject wifiObject = new JSONObject();
                if (!result.SSID.equals("")) {
                    try {
                        wifiObject.put("SSID", result.SSID);
                        wifiObject.put("BSSID", result.BSSID);
                        wifiObject.put("capabilities", result.capabilities);
                        wifiObject.put("frequency", result.frequency);
                        wifiObject.put("level", result.level);
                        wifiObject.put("timestamp", result.timestamp);
                    } catch (final JSONException jsonException) {
                        promise.reject("jsonException", jsonException.getMessage());
                        return;
                    }
                    wifiArray.put(wifiObject);
                }
            }
            promise.resolve(wifiArray.toString());
        } catch (IllegalViewOperationException illegalViewOperationException) {
            promise.reject("exception", illegalViewOperationException.getMessage());
        }
    }

}
