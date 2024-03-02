import { PermissionsAndroid, ScrollView, StyleSheet, View } from 'react-native';
import { useEffect } from 'react';
import { ConnectToSSID } from './components/ConnectToSSID';
import { CurrentSSID } from './components/CurrentSSID';
import { Disconnect } from './components/Disconnect';

const askLocationPermission = async () => {
  const res = await PermissionsAndroid.request(
    PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
    {
      title: 'Location permission is required for WiFi connections',
      message:
        'This app needs location permission as this is required  ' +
        'to scan for wifi networks.',
      buttonNegative: 'DENY',
      buttonPositive: 'ALLOW',
    },
  );
  console.log(res);
};

export default function App() {
  useEffect(() => {
    askLocationPermission();
  }, []);

  return (
    <ScrollView style={styles.screen} contentInsetAdjustmentBehavior="automatic">
      <View style={styles.container}>
        <CurrentSSID />
        <ConnectToSSID />
        <Disconnect />
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    backgroundColor: '#f2f2f7',
  },
  container: {
    flex: 1,
    padding: 16,
    gap: 12,
  },
});
