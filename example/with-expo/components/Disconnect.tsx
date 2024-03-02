import { useState } from 'react';
import { Button, StyleSheet, Text } from 'react-native';
import WifiManager from 'react-native-wifi-reborn';
import { Section } from './Section';

export const Disconnect = () => {
  const [error, setError] = useState('');
  const [response, setResponse] = useState('');

  const handleDisconnect = () => {
    setError('');

    WifiManager.disconnect()
      .then((r) => setResponse(JSON.stringify(r, null, 2)))
      .catch((e) => setError(e.toString()));
  };

  return (
    <Section title="Disconnect">
      <Button title="Disconnect" onPress={handleDisconnect} />
      <Text>{response}</Text>
      <Text style={styles.error}>{error}</Text>
    </Section>
  );
};

const styles = StyleSheet.create({
  error: {
    color: 'red',
  },
});
