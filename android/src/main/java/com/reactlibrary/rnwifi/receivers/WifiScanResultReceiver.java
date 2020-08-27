package com.reactlibrary.rnwifi.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableArray;

import java.util.List;

import static com.reactlibrary.mappers.WifiScanResultsMapper.mapWifiScanResults;

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
            final List<ScanResult> scanResults = this.wifiManager.getScanResults();
            final WritableArray results = mapWifiScanResults(scanResults);
            promise.resolve(results);
        } catch (Exception exception) {
            promise.reject("exception", exception.getMessage());
        }
     }
}
