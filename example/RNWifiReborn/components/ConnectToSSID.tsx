import { useState } from 'react';
import { Button, TextInput, StyleSheet, Text, ActivityIndicator } from 'react-native';
import WifiManager from 'react-native-wifi-reborn';
import { Section } from './Section';

export const ConnectToSSID = () => {
  const [ssid, setSsid] = useState('SSID');
  const [pass, setPass] = useState('password');
  const [error, setError] = useState('');
  const [response, setResponse] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleConnect = () => {
    setError('');
    setResponse('');
    setIsLoading(true);
    WifiManager.connectToProtectedSSID(ssid, pass, false, false)
      .then((r) => setResponse(JSON.stringify(r, null, 2)))
      .catch((e) => setError(e.toString()))
      .finally(() => setIsLoading(false));
  };

  return (
    <Section title="Connect to SSID">
      <TextInput style={styles.textInput} value={ssid} onChangeText={setSsid} />
      <TextInput style={styles.textInput} value={pass} onChangeText={setPass} />
      <Button title="Connect" onPress={handleConnect} disabled={isLoading} />
      {isLoading ? (
        <ActivityIndicator />
      ) : (
        <>
          <Text style={styles.error}>{error}</Text>
          <Text style={styles.text}>{response}</Text>
        </>
      )}
    </Section>
  );
};

const styles = StyleSheet.create({
  container: {
    gap: 12,
    backgroundColor: '#fff',
    padding: 12,
    borderRadius: 10,
  },
  textInput: {
    borderWidth: 1,
    borderColor: '#e5e5ea',
    padding: 4,
    borderRadius: 4,
    color: '#000',
  },
  error: {
    color: 'red',
  },
  text: {    
    color: '#000',
  },
});
