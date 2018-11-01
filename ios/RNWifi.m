#import "RNWifi.h"
#import <NetworkExtension/NetworkExtension.h>
#import <SystemConfiguration/CaptiveNetwork.h>
// If using official settings URL
//#import <UIKit/UIKit.h>

@implementation WifiManager
  
+ (BOOL)requiresMainQueueSetup
{
  return YES;
}

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(connectToSSID:(NSString*)ssid
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    
    if (@available(iOS 11.0, *)) {
        NEHotspotConfiguration* configuration = [[NEHotspotConfiguration alloc] initWithSSID:ssid];
        configuration.joinOnce = true;
        
        [[NEHotspotConfigurationManager sharedManager] applyConfiguration:configuration completionHandler:^(NSError * _Nullable error) {
            if (error != nil) {
                reject(@"nehotspot_error", @"Error while configuring WiFi", error);
            } else {
                resolve(nil);
            }
        }];
        
    } else {
        reject(@"ios_error", @"Not supported in iOS<11.0", nil);
    }
}

RCT_EXPORT_METHOD(connectToProtectedSSID:(NSString*)ssid
                  withPassphrase:(NSString*)passphrase
                  isWEP:(BOOL)isWEP
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    
    if (@available(iOS 11.0, *)) {
        NEHotspotConfiguration* configuration = [[NEHotspotConfiguration alloc] initWithSSID:ssid passphrase:passphrase isWEP:isWEP];
        configuration.joinOnce = true;
        
        [[NEHotspotConfigurationManager sharedManager] applyConfiguration:configuration completionHandler:^(NSError * _Nullable error) {
            if (error != nil) {
                reject(@"nehotspot_error", @"Error while configuring WiFi", error);
            } else {
                resolve(nil);
            }
        }];
        
    } else {
        reject(@"ios_error", @"Not supported in iOS<11.0", nil);
    }
}

RCT_EXPORT_METHOD(disconnectFromSSID:(NSString*)ssid
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    
    if (@available(iOS 11.0, *)) {
        [[NEHotspotConfigurationManager sharedManager] getConfiguredSSIDsWithCompletionHandler:^(NSArray<NSString *> *ssids) {
            if (ssids != nil && [ssids indexOfObject:ssid] != NSNotFound) {
                [[NEHotspotConfigurationManager sharedManager] removeConfigurationForSSID:ssid];
            }
            resolve(nil);
        }];
    } else {
        reject(@"ios_error", @"Not supported in iOS<11.0", nil);
    }
    
}

RCT_REMAP_METHOD(getCurrentWifiSSID,
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject) {
    
    NSString *kSSID = (NSString*) kCNNetworkInfoKeySSID;
    
    NSArray *ifs = (__bridge_transfer id)CNCopySupportedInterfaces();
    for (NSString *ifnam in ifs) {
        NSDictionary *info = (__bridge_transfer id)CNCopyCurrentNetworkInfo((__bridge CFStringRef)ifnam);
        if (info[kSSID]) {
            resolve(info[kSSID]);
            return;
        }
    }
    
    reject(@"cannot_detect_ssid", @"Cannot detect SSID", nil);
}

- (NSDictionary*)constantsToExport {
    // Officially better to use UIApplicationOpenSettingsURLString
    return @{
             @"settingsURL": @"App-Prefs:root=WIFI"
             };
}

@end

