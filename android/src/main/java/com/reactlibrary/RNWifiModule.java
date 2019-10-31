package com.reactlibrary;

import com.facebook.react.uimanager.*;
import com.facebook.react.bridge.*;

import android.provider.Settings;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.NetworkCapabilities;
import android.net.Network;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RNWifiModule extends ReactContextBaseJavaModule {
	private WifiManager wifi;
	private ReactApplicationContext context;

	RNWifiModule(ReactApplicationContext context) {
		super(context);

		wifi = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		this.context = context;
	}

	@Override
	public String getName() {
		return "WifiManager";
	}

	/**
	 * Method to load wifi list into string via Callback. Returns a stringified JSONArray
	 *
	 * @param successCallback
	 * @param errorCallback
	 */
	@ReactMethod
	public void loadWifiList(Callback successCallback, Callback errorCallback) {
		try {
			List < ScanResult > results = wifi.getScanResults();
			JSONArray wifiArray = new JSONArray();

			for (ScanResult result: results) {
				JSONObject wifiObject = new JSONObject();
				if(!result.SSID.equals("")){
					try {
						wifiObject.put("SSID", result.SSID);
						wifiObject.put("BSSID", result.BSSID);
						wifiObject.put("capabilities", result.capabilities);
						wifiObject.put("frequency", result.frequency);
						wifiObject.put("level", result.level);
						wifiObject.put("timestamp", result.timestamp);
					} catch (JSONException e) {
          				errorCallback.invoke(e.getMessage());
					}
					wifiArray.put(wifiObject);
				}
			}
			successCallback.invoke(wifiArray.toString());
		} catch (IllegalViewOperationException e) {
			errorCallback.invoke(e.getMessage());
		}
	}

	//

	/**
	 * Method to force wifi usage if the user needs to send requests via wifi
	 * if it does not have internet connection. Useful for IoT applications, when
	 * the app needs to communicate and send requests to a device that have no
	 * internet connection via wifi.
	 *
	 * Receives a boolean to enable forceWifiUsage if true, and disable if false.
	 * Is important to enable only when communicating with the device via wifi
	 * and remember to disable it when disconnecting from device.
	 * @param useWifi
	 */
	@ReactMethod
	public void forceWifiUsage(boolean useWifi) {
        boolean canWriteFlag = false;
		
        if (useWifi) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    canWriteFlag = Settings.System.canWrite(context);

                    if (!canWriteFlag) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        intent.setData(Uri.parse("package:" + context.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        context.startActivity(intent);
                    }
                }


                if (((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && canWriteFlag) || ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) && !(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M))) {
                    final ConnectivityManager manager = (ConnectivityManager) context
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkRequest.Builder builder;
                    builder = new NetworkRequest.Builder();
                    //set the transport type do WIFI
                    builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);


                    manager.requestNetwork(builder.build(), new ConnectivityManager.NetworkCallback() {
                        @Override
                        public void onAvailable(Network network) {
                            // FIXME: should this be try catched?
                        	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                manager.bindProcessToNetwork(network);
                            } else {
                                //This method was deprecated in API level 23
                                ConnectivityManager.setProcessDefaultNetwork(network);
                            }
                            manager.unregisterNetworkCallback(this);
                        }
                    });
                }


            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ConnectivityManager manager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                manager.bindProcessToNetwork(null);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ConnectivityManager.setProcessDefaultNetwork(null);
            }
        }
    }

	/**
	 * Method to check if wifi is enabled
	 *
	 * @param isEnabled
	 */
	@ReactMethod
	public void isEnabled(Callback isEnabled) {
		isEnabled.invoke(wifi.isWifiEnabled());
	}

	/**
	 * Method to connect/disconnect wifi service
	 *
	 * @param enabled
	 */
	@ReactMethod
	public void setEnabled(Boolean enabled) {
		wifi.setWifiEnabled(enabled);
	}

	/**
	 * Send the SSID and password of a Wifi network into this to connect to the network.
	 * Example:  wifi.findAndConnect(ssid, password);
	 * After 10 seconds, a post telling you whether you are connected will pop up.
	 * Callback returns true if ssid is in the range
	 *
	 * @param SSID name of the network to connect with
	 * @param password password of the network to connect with
	 * @param isWep required for iOS
	 * @param promise
	 */
	@ReactMethod
	public void connectToProtectedSSID(@NonNull final String SSID, @NonNull final String password, final boolean isWep, final Promise promise) {
		final List <ScanResult> results = wifi.getScanResults();
		for (ScanResult result: results) {
			String resultString = "" + result.SSID;
			if (SSID.equals(resultString)) {
				connectTo(result, SSID, password, promise);
			}
		}

		promise.reject("notInRange", String.format("Not in range of the provided SSID: %s ", SSID));
	}



	/**
	 * Use this method to check if the device is currently connected to Wifi.
	 *
	 * @param connectionStatusResult
	 */
	@ReactMethod
	public void connectionStatus(Callback connectionStatusResult) {
		ConnectivityManager connManager = (ConnectivityManager) getReactApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (mWifi.isConnected()) {
			connectionStatusResult.invoke(true);
		} else {
			connectionStatusResult.invoke(false);
		}
	}

	/**
	 * Method to connect to WIFI Network
	 *
	 * @param result wifi scan result
	 * @param password
	 * @param SSID
	 * @return
	 */
	//
	void connectTo(final ScanResult result, final String SSID, final String password, final Promise promise) {
		//Make new configuration
		final WifiConfiguration conf = new WifiConfiguration();
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        	conf.SSID = SSID;
		} else {
			conf.SSID = "\"" + SSID + "\"";
		}

		String capabilities = result.capabilities;
		
		if (capabilities.contains("WPA")  || 
          capabilities.contains("WPA2") || 
          capabilities.contains("WPA/WPA2 PSK")) {

	    // appropriate cipher is need to set according to security type used,
	    // if not added it will not be able to connect
	    conf.preSharedKey = "\"" + password + "\"";
	    
	    conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
	    
		conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
	    
	    conf.status = WifiConfiguration.Status.ENABLED;
	    
	    conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
	    conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
	    
	    
	    conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
	    conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
	    
		}	else if (capabilities.contains("WEP")) {
			conf.wepKeys[0] = "\"" + password + "\"";
			conf.wepTxKeyIndex = 0;
			conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);

		} else {
			conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		}

		//Remove the existing configuration for this network
		List<WifiConfiguration> mWifiConfigList = wifi.getConfiguredNetworks();

		int updateNetwork = -1;

		for(WifiConfiguration wifiConfig : mWifiConfigList){
			if(wifiConfig.SSID.equals(conf.SSID)){
				conf.networkId = wifiConfig.networkId;
				updateNetwork = wifi.updateNetwork(conf);
			}
		}

    	// If network not already in configured networks add new network
		if (updateNetwork == -1) {
			updateNetwork = wifi.addNetwork(conf);
			wifi.saveConfiguration();
		}

		if (updateNetwork == -1) {
		  promise.reject("addOrUpdateFailed", String.format("Could not add or update network configuration with SSID %s.", SSID));
		}

    	boolean disconnect = wifi.disconnect();
		if ( !disconnect ) {
			promise.reject("disconnectFailed", String.format("Disconnecting network with SSID %s failed (before connect).", SSID));
		}

		final boolean enableNetwork = wifi.enableNetwork(updateNetwork, true);
		if (enableNetwork) {
			promise.resolve(null);
			return;
		}
		promise.reject("connectNetworkFailed", String.format("Could not connect to network with SSID: %s", SSID));

	}

	/**
	 * Disconnect current Wifi.
	 */
	@ReactMethod
	public void disconnect() {
		wifi.disconnect();
	}

	/**
	 * This method will return current SSID
	 *
	 * @param promise
	 */
	@ReactMethod
	public void getCurrentWifiSSID(final Promise promise) {
		WifiInfo info = wifi.getConnectionInfo();

		// This value should be wrapped in double quotes, so we need to unwrap it.
		String ssid = info.getSSID();
		if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
			ssid = ssid.substring(1, ssid.length() - 1);
		}

		promise.resolve(ssid);
	}

	/**]
	 * This method will return the basic service set identifier (BSSID) of the current access point
	 * @param callback
	 */
	@ReactMethod
	public void getBSSID(final Callback callback) {
		WifiInfo info = wifi.getConnectionInfo();

		String bssid = info.getBSSID();

		callback.invoke(bssid.toUpperCase());
	}

	/**
	 * This method will return current wifi signal strength
	 *
	 * @param callback
	 */
	@ReactMethod
	public void getCurrentSignalStrength(final Callback callback) {
		int linkSpeed = wifi.getConnectionInfo().getRssi();
		callback.invoke(linkSpeed);
	}

	/**
	 * This method will return current wifi frequency
	 *
	 * @param callback
	 */
	@ReactMethod
	public void getFrequency(final Callback callback) {
		WifiInfo info = wifi.getConnectionInfo();
		int frequency = info.getFrequency();
		callback.invoke(frequency);
	}

	/**
	 * This method will return current IP
	 *
	 * @param callback
	 */
	@ReactMethod
	public void getIP(final Callback callback) {
		WifiInfo info = wifi.getConnectionInfo();
		String stringip = longToIP(info.getIpAddress());
		callback.invoke(stringip);
	}

	/**
	 * This method will remove the wifi network as per the passed SSID from the device list
	 *
	 * @param ssid
	 * @param callback
	 */
	@ReactMethod
	public void isRemoveWifiNetwork(String ssid, final Callback callback) {
    List<WifiConfiguration> mWifiConfigList = wifi.getConfiguredNetworks();
    for (WifiConfiguration wifiConfig : mWifiConfigList) {
				String comparableSSID = ('"' + ssid + '"'); //Add quotes because wifiConfig.SSID has them
				if(wifiConfig.SSID.equals(comparableSSID)) {
					wifi.removeNetwork(wifiConfig.networkId);
					wifi.saveConfiguration();
					callback.invoke(true);
					return;
				}
    }
		callback.invoke(false);
	}

	/**
	 * This method is similar to `loadWifiList` but it forcefully starts the wifi scanning on android and in the callback fetches the list
	 *
	 * @param successCallback
	 * @param errorCallback
	 */
	@ReactMethod
	public void reScanAndLoadWifiList(Callback successCallback, Callback errorCallback) {
		WifiReceiver receiverWifi = new WifiReceiver(wifi, successCallback, errorCallback);
   	getReactApplicationContext().getCurrentActivity().registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    wifi.startScan();
	}

	static String longToIP(int longIp){
		StringBuffer sb = new StringBuffer("");
		String[] strip=new String[4];
		strip[3]=String.valueOf((longIp >>> 24));
		strip[2]=String.valueOf((longIp & 0x00FFFFFF) >>> 16);
		strip[1]=String.valueOf((longIp & 0x0000FFFF) >>> 8);
		strip[0]=String.valueOf((longIp & 0x000000FF));
		sb.append(strip[0]);
		sb.append(".");
		sb.append(strip[1]);
		sb.append(".");
		sb.append(strip[2]);
		sb.append(".");
		sb.append(strip[3]);
		return sb.toString();
	}

	//region WifiReceiver hey

	class WifiReceiver extends BroadcastReceiver {

		private Callback successCallback;
		private Callback errorCallback;
		private WifiManager wifi;

		public WifiReceiver(final WifiManager wifi, Callback successCallback, Callback errorCallback) {
			super();
			this.successCallback = successCallback;
			this.errorCallback = errorCallback;
			this.wifi = wifi;
		}

		// This method call when number of wifi connections changed
		public void onReceive(Context c, Intent intent) {
			// LocalBroadcastManager.getInstance(c).unregisterReceiver(this);
			c.unregisterReceiver(this);
			// getReactApplicationContext().getCurrentActivity().registerReceiver
			try {
				List < ScanResult > results = this.wifi.getScanResults();
				JSONArray wifiArray = new JSONArray();

				for (ScanResult result: results) {
					JSONObject wifiObject = new JSONObject();
					if(!result.SSID.equals("")){
						try {
				wifiObject.put("SSID", result.SSID);
				wifiObject.put("BSSID", result.BSSID);
				wifiObject.put("capabilities", result.capabilities);
				wifiObject.put("frequency", result.frequency);
				wifiObject.put("level", result.level);
				wifiObject.put("timestamp", result.timestamp);
						} catch (JSONException e) {
				this.errorCallback.invoke(e.getMessage());
							return;
						}
						wifiArray.put(wifiObject);
					}
				}
				this.successCallback.invoke(wifiArray.toString());
			} catch (IllegalViewOperationException e) {
				this.errorCallback.invoke(e.getMessage());
			}
		}
	}
}
