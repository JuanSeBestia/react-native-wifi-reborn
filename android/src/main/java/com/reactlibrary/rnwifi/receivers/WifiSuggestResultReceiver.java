package com.reactlibrary.rnwifi.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;

public class WifiSuggestResultReceiver extends BroadcastReceiver {
  private final WifiManager wifiManager;
  private final Promise promise;

  public WifiSuggestResultReceiver(@NonNull final WifiManager wifiManager, @NonNull final Promise promise) {
      super();
      this.promise = promise;
      this.wifiManager = wifiManager;
  }

  // This method call when number of wifi connections changed
  public void onReceive(final Context context, final Intent intent) {
      context.unregisterReceiver(this);
      
      if (!intent.getAction().equals(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION)) {
          promise.reject("exception", "Unable to connect to SSID");
      }else {
          promise.resolve(null);
      }
   }
}
