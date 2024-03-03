import { ScrollView, StyleSheet, View } from 'react-native';
import { useEffect } from 'react';
import { ConnectToSSID } from './components/ConnectToSSID';
import { CurrentSSID } from './components/CurrentSSID';
import { Disconnect } from './components/Disconnect';
import Geolocation from '@react-native-community/geolocation';

const askLocationPermission = async () => {
  Geolocation.requestAuthorization((status:any) => {
    console.log('Location permission status: ', status);
  }, (error:any) => console.error('Location permission error: ', error) );
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
