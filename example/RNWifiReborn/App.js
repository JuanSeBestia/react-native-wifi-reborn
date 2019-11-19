/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {useState, useEffect} from 'react';
import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  StatusBar,
  Text,
} from 'react-native';

import {Header, Colors} from 'react-native/Libraries/NewAppScreen';
import WifiManager from 'react-native-wifi-reborn';

const App = () => {
  const [connected, setConneted] = useState({connected: false, ssid: 'S4N'});
  const [ssid, setSsid] = useState('');
  const password ="tanenbaum-1981";
  const isWep = false;

  const wifi = async () => {
    try {
      const data = await WifiManager.connectToProtectedSSID(
        ssid,
        password,
        isWep,
      );
      console.log('Connected successfully!', {data});
      setConneted({connected: true, ssid});
    } catch (error) {
      setConneted({connected: false, error: error.message});
      console.log('Connection failed!', {error});
    }

    try {
      const ssid = await WifiManager.getCurrentWifiSSID();
      setSsid(ssid);
      console.log('Your current connected wifi SSID is ' + ssid);
    } catch (error) {
      setSsid('Cannot get current SSID!' + error.message);
      console.log('Cannot get current SSID!', {error});
    }
  };
  useEffect(() => {
    wifi();
  }, []);

  return (
    <>
      <StatusBar barStyle="dark-content" />
      <SafeAreaView>
        <ScrollView
          contentInsetAdjustmentBehavior="automatic"
          style={styles.scrollView}>
          <Header />
          <View style={styles.sectionContainer}>
            <Text style={styles.sectionTitle}>ssid</Text>
            <Text style={styles.sectionDescription}>
              {JSON.stringify(ssid)}
            </Text>
          </View>
          <View style={styles.sectionContainer}>
            <Text style={styles.sectionTitle}>Conencted</Text>
            <Text style={styles.sectionDescription}>
              {JSON.stringify(connected)}
            </Text>
          </View>
          <View style={styles.body}></View>
        </ScrollView>
      </SafeAreaView>
    </>
  );
};

const styles = StyleSheet.create({
  scrollView: {
    backgroundColor: Colors.lighter,
  },
  engine: {
    position: 'absolute',
    right: 0,
  },
  body: {
    backgroundColor: Colors.white,
  },
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
    color: Colors.black,
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
    color: Colors.dark,
  },
  highlight: {
    fontWeight: '700',
  },
  footer: {
    color: Colors.dark,
    fontSize: 12,
    fontWeight: '600',
    padding: 4,
    paddingRight: 12,
    textAlign: 'right',
  },
});

export default App;
