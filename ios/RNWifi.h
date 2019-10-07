// Created by Rutger Bresjer on 10/10/2017

// Notes:
// - Be sure to enable "Hotspot Configuration" capability for the iOS target
// - Make sure the NetworkExtension framework is linked to the target

#import <Foundation/Foundation.h>
#if __has_include(<React/RCTBridgeModule.h>) // React Native >= 0.40
#import <React/RCTBridgeModule.h>
#else // React Native < 0.40
#import "RCTBridgeModule.h"
#endif

@interface WifiManager : NSObject <RCTBridgeModule>

@end

