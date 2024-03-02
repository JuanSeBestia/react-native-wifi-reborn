import { PropsWithChildren } from 'react';
import { View, StyleSheet, Text } from 'react-native';

type Props = PropsWithChildren<{
  title: string;
}>;

export const Section = ({ title, children }: Props) => {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>{title}</Text>
      {children}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    gap: 12,
    backgroundColor: '#fff',
    padding: 12,
    borderRadius: 10,
  },
  title: {
    fontSize: 18,
    color: '#000',
    fontWeight: 'bold',
  },
});
