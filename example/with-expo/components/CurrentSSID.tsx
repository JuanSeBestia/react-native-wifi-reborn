import { useEffect, useState } from 'react';
import { Button, Text } from 'react-native';
import WifiManager from 'react-native-wifi-reborn';
import { Section } from './Section';

export const CurrentSSID = () => {
  const [ssid, setSsid] = useState('fetching...');

  const handleFetch = () => {
    setSsid('fetching...');
    WifiManager.getCurrentWifiSSID()
      .then(setSsid)
      .catch((e) => setSsid(e.toString()));
  };

  useEffect(() => {
    handleFetch;
  }, []);

  return (
    <Section title="Current SSID">
      <Text>{ssid}</Text>
      <Button title="Refresh" onPress={handleFetch} />
    </Section>
  );
};
