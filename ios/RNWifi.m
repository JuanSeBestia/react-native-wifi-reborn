#import "RNWifi.h"
#import <NetworkExtension/NetworkExtension.h>
#import <SystemConfiguration/CaptiveNetwork.h>
#import <CoreLocation/CoreLocation.h>

// If using official settings URL
//#import <UIKit/UIKit.h>


@interface WifiManager () <CLLocationManagerDelegate>
@property (nonatomic,strong) CLLocationManager *locationManager;
@end
@implementation WifiManager

- (instancetype)init {
  self = [super init];
  if (self) {
      NSLog(@"RNWIFI:Init");
          if (@available(iOS 13, *)) {
              self.locationManager = [[CLLocationManager alloc] init];
              self.locationManager.delegate = self;
          }
  }
  return self;
}

- (void)locationManager:(CLLocationManager *)manager didChangeAuthorizationStatus:(CLAuthorizationStatus)status {
    // TODO: implement signal
    NSLog(@"RNWIFI:statechaged %d",[CLLocationManager authorizationStatus]);
}

- (BOOL) synchronousRequestPermission
{
    [self.locationManager requestWhenInUseAuthorization];
    // TODO: implment signal with didChangeAuthorizationStatus
    while([CLLocationManager authorizationStatus] == kCLAuthorizationStatusNotDetermined){
        sleep(1);
        NSLog(@"RNWIFI:Waiting request... current state:%d",[CLLocationManager authorizationStatus]);
    }
    if ([CLLocationManager authorizationStatus] == kCLAuthorizationStatusAuthorizedWhenInUse){
        return YES;
    }
    return NO;
    
}

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
    if (@available(iOS 13, *)) {
        // for iOS 13, need permission of location
        BOOL hasPermission = [self synchronousRequestPermission];
        if(hasPermission){
            NSLog(@"RNWIFI:Location Services enabled on iOS 13");
        }
        else{
            //Location Services are available we will need software to ask to turn this On
            //The user is SOL if they refuse to turn on Location Services
            NSLog(@"RNWIFI:ERROR:Location Services not enabled");
        }
    }
    
    NSString *kSSID = (NSString*) kCNNetworkInfoKeySSID;
    
    NSArray *ifs = (__bridge_transfer id)CNCopySupportedInterfaces();
    for (NSString *ifnam in ifs) {
        NSDictionary *info = (__bridge_transfer id)CNCopyCurrentNetworkInfo((__bridge CFStringRef)ifnam);
        if (info[kSSID]) {
            resolve(info[kSSID]);
            return;
        }
    }
    NSLog(@"RNWIFI:ERROR:Cannot detect SSID");
    reject(@"cannot_detect_ssid", @"Cannot detect SSID", nil);
}

- (NSDictionary*)constantsToExport {
    // Officially better to use UIApplicationOpenSettingsURLString
    return @{
             @"settingsURL": @"App-Prefs:root=WIFI"
             };
}

@end

