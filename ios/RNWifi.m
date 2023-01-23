#import "RNWifi.h"
#import <NetworkExtension/NetworkExtension.h>
#import <SystemConfiguration/CaptiveNetwork.h>
#import <CoreLocation/CoreLocation.h>
#import <UIKit/UIKit.h>

#import "ConnectError.h"


@interface WifiManager () <CLLocationManagerDelegate>
@property (nonatomic,strong) CLLocationManager *locationManager;
@property (nonatomic) BOOL solved;
@end
@implementation WifiManager

- (instancetype)init {
  self = [super init];
  if (self) {
      NSLog(@"RNWIFI:Init");
      self.solved = YES;
          if (@available(iOS 13, *)) {
              self.locationManager = [[CLLocationManager alloc] init];
              self.locationManager.delegate = self;
          }
  }
  return self;
}

- (void)locationManager:(CLLocationManager *)manager didChangeAuthorizationStatus:(CLAuthorizationStatus)status {
    NSLog(@"RNWIFI:statechaged %d", status);
    [[NSNotificationCenter defaultCenter]
     postNotificationName:@"RNWIFI:authorizationStatus" object:nil userInfo:nil];
}

- (void)getWifiSSID:(void (^)(NSString *))completionHandler {
    if (@available(iOS 14.0, *)) {
        [NEHotspotNetwork fetchCurrentWithCompletionHandler:^(NEHotspotNetwork * _Nullable currentNetwork) {
            NSString  *strSSID = [currentNetwork SSID];
            completionHandler(strSSID);
        }];
    } else {
        NSString *kSSID = (NSString*) kCNNetworkInfoKeySSID;

        NSArray *ifs = (__bridge_transfer id)CNCopySupportedInterfaces();
        for (NSString *ifnam in ifs) {
            NSDictionary *info = (__bridge_transfer id)CNCopyCurrentNetworkInfo((__bridge CFStringRef)ifnam);
            if (info[kSSID]) {
                completionHandler(info[kSSID]);
                return;
            }
        }
        completionHandler(nil);
    }
}

+ (BOOL)requiresMainQueueSetup
{
  return YES;
}

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(connectToSSID:(NSString*)ssid
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [self connectToProtectedSSID:ssid withPassphrase:@"" isWEP:false resolver:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(connectToSSIDPrefix:(NSString*)ssid
                   resolver:(RCTPromiseResolveBlock)resolve
                   rejecter:(RCTPromiseRejectBlock)reject) {

     if (@available(iOS 13.0, *)) {
         NEHotspotConfiguration* configuration = [[NEHotspotConfiguration alloc] initWithSSIDPrefix:ssid];
         configuration.joinOnce = false;

         [[NEHotspotConfigurationManager sharedManager] applyConfiguration:configuration completionHandler:^(NSError * _Nullable error) {
             if (error != nil) {
                 reject([self parseError:error], @"Error while configuring WiFi", error);
             } else {
                 resolve(nil);
             }
         }];

     } else {
         reject([ConnectError code:UnavailableForOSVersion], @"Not supported in iOS<13.0", nil);
     }
 }

RCT_EXPORT_METHOD(connectToProtectedSSIDPrefix:(NSString*)ssid
                  withPassphrase:(NSString*)passphrase
                  isWEP:(BOOL)isWEP
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {

    if (@available(iOS 13.0, *)) {
        NEHotspotConfiguration* configuration = [[NEHotspotConfiguration alloc] initWithSSIDPrefix:ssid passphrase:passphrase isWEP:isWEP];
        configuration.joinOnce = false;

        [[NEHotspotConfigurationManager sharedManager] applyConfiguration:configuration completionHandler:^(NSError * _Nullable error) {
            if (error != nil) {
                reject([self parseError:error], @"Error while configuring WiFi", error);
            } else {
                // Verify SSID connection
                [self getWifiSSID:^(NSString* result) {
                    if ([result hasPrefix:ssid]){
                        resolve(nil);
                    } else {
                        reject([ConnectError code:UnableToConnect], [NSString stringWithFormat:@"%@/%@", @"Unable to connect to Wi-Fi with prefix ", ssid], nil);
                    }
                }];
            }
        }];

    } else {
        reject([ConnectError code:UnavailableForOSVersion], @"Not supported in iOS<13.0", nil);
    }
}

RCT_EXPORT_METHOD(connectToProtectedSSID:(NSString*)ssid
                  withPassphrase:(NSString*)passphrase
                  isWEP:(BOOL)isWEP
                  forIOT:(BOOL)forIOT
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [self connectToProtectedSSIDOnce:ssid withPassphrase:passphrase isWEP:isWEP joinOnce:false resolver:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(connectToProtectedSSIDOnce:(NSString*)ssid
                  withPassphrase:(NSString*)passphrase
                  isWEP:(BOOL)isWEP
                  joinOnce:(BOOL)joinOnce
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    // Prevent NEHotspotConfigurationManager error when connecting to an already connected network
    [self getWifiSSID:^(NSString* resultSSID) {
 //       if ([ssid isEqualToString:resultSSID]) {
//             resolve(nil);
//             return;
//         }
        
        if (@available(iOS 11.0, *)) {
            NEHotspotConfiguration* configuration;
            // Check if open network
            if (passphrase == (id)[NSNull null] || passphrase.length == 0 ) {
                configuration = [[NEHotspotConfiguration alloc] initWithSSID:ssid];
            } else {
                configuration = [[NEHotspotConfiguration alloc] initWithSSID:ssid passphrase:passphrase isWEP:isWEP];
            }
            configuration.joinOnce = joinOnce;

            [[NEHotspotConfigurationManager sharedManager] applyConfiguration:configuration completionHandler:^(NSError * _Nullable error) {
                if (error != nil) {
                    reject([self parseError:error], [error localizedDescription], error);
                } else {
                    // Verify SSID connection
                    [self getWifiSSID:^(NSString* newSSID) {
                        if ([ssid isEqualToString:newSSID]){
                            resolve(nil);
                        } else {
                            reject([ConnectError code:UnableToConnect], [NSString stringWithFormat:@"%@/%@", @"Unable to connect to ", ssid], nil);
                        }
                    }];
                }
            }];

        } else {
            reject([ConnectError code:UnavailableForOSVersion], @"Not supported in iOS<11.0", nil);
        }
    }];    
}

RCT_EXPORT_METHOD(disconnectFromSSID:(NSString*)ssid
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {

    if (@available(iOS 11.0, *)) {
        [[NEHotspotConfigurationManager sharedManager] removeConfigurationForSSID:ssid];
        resolve(nil);
    } else {
        reject([ConnectError code:UnavailableForOSVersion], @"Not supported in iOS<11.0", nil);
    }
}


RCT_REMAP_METHOD(getCurrentWifiSSID,
                 resolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject) {

    if (@available(iOS 13, *)) {
        // Reject when permission had rejected
        if([CLLocationManager authorizationStatus] == kCLAuthorizationStatusDenied){
            NSLog(@"RNWIFI:ERROR:Cannot detect SSID because LocationPermission is Denied ");
            reject([ConnectError code:LocationPermissionDenied], @"Cannot detect SSID because LocationPermission is Denied", nil);
        }
        if([CLLocationManager authorizationStatus] == kCLAuthorizationStatusRestricted){
            NSLog(@"RNWIFI:ERROR:Cannot detect SSID because LocationPermission is Restricted ");
            reject([ConnectError code:LocationPermissionRestricted], @"Cannot detect SSID because LocationPermission is Restricted", nil);
        }
    }

    BOOL hasLocationPermission = [CLLocationManager authorizationStatus] == kCLAuthorizationStatusAuthorizedWhenInUse ||
    [CLLocationManager authorizationStatus] == kCLAuthorizationStatusAuthorizedAlways;
    if (@available(iOS 13, *) && hasLocationPermission == NO) {
        // Need request LocationPermission or HotSpot or have VPN connection
        // https://forums.developer.apple.com/thread/117371#364495
        [self.locationManager requestWhenInUseAuthorization];
        self.solved = NO;
        [[NSNotificationCenter defaultCenter] addObserverForName:@"RNWIFI:authorizationStatus" object:nil queue:nil usingBlock:^(NSNotification *note)
        {
            if(self.solved == NO){
                if ([CLLocationManager authorizationStatus] == kCLAuthorizationStatusAuthorizedWhenInUse ||
                    [CLLocationManager authorizationStatus] == kCLAuthorizationStatusAuthorizedAlways){
                    [self getWifiSSID:^(NSString* SSID) {
                        if (SSID) {
                            resolve(SSID);
                            return;
                        }
                        NSLog(@"RNWIFI:ERROR:Cannot detect SSID");
                        reject([ConnectError code:CouldNotDetectSSID], @"Cannot detect SSID", nil);
                    }];
                } else{
                    reject([ConnectError code:LocationPermissionDenied], @"Permission not granted", nil);
                }
            }
            // Avoid call when live-reloaded app
            self.solved = YES;
        }];
    }else{
        [self getWifiSSID:^(NSString* SSID) {
            if (SSID){
                resolve(SSID);
                return;
            }
            NSLog(@"RNWIFI:ERROR:Cannot detect SSID");
            reject([ConnectError code:CouldNotDetectSSID], @"Cannot detect SSID", nil);
        }];
    }
}

- (NSString *)parseError:(NSError *)error {
    if (@available(iOS 11, *)) {
        
        if (!error) {
            return [ConnectError code:UnableToConnect];
        };
        
        /*
         NEHotspotConfigurationErrorInvalid                         = 0,
         NEHotspotConfigurationErrorInvalidSSID                     = 1,
         NEHotspotConfigurationErrorInvalidWPAPassphrase            = 2,
         NEHotspotConfigurationErrorInvalidWEPPassphrase            = 3,
         NEHotspotConfigurationErrorInvalidEAPSettings              = 4,
         NEHotspotConfigurationErrorInvalidHS20Settings             = 5,
         NEHotspotConfigurationErrorInvalidHS20DomainName           = 6,
         NEHotspotConfigurationErrorUserDenied                      = 7,
         NEHotspotConfigurationErrorInternal                        = 8,
         NEHotspotConfigurationErrorPending                         = 9,
         NEHotspotConfigurationErrorSystemConfiguration             = 10,
         NEHotspotConfigurationErrorUnknown                         = 11,
         NEHotspotConfigurationErrorJoinOnceNotSupported            = 12,
         NEHotspotConfigurationErrorAlreadyAssociated               = 13,
         NEHotspotConfigurationErrorApplicationIsNotInForeground    = 14,
         NEHotspotConfigurationErrorInvalidSSIDPrefix               = 15
         */

        switch (error.code) {
            case NEHotspotConfigurationErrorInvalid:
                return [ConnectError code:Invalid];
            case NEHotspotConfigurationErrorInvalidSSID:
                return [ConnectError code:InvalidSSID];
            case NEHotspotConfigurationErrorInvalidSSIDPrefix:
                return [ConnectError code:InvalidSSIDPrefix];
            case NEHotspotConfigurationErrorInvalidWPAPassphrase:
            case NEHotspotConfigurationErrorInvalidWEPPassphrase:
                return [ConnectError code:InvalidPassphrase];
            case NEHotspotConfigurationErrorUserDenied:
                return [ConnectError code:UserDenied];
            default:
                return [ConnectError code:UnableToConnect];
        }
    }
    return [ConnectError code:UnavailableForOSVersion];
}

- (NSDictionary*)constantsToExport {
    // Officially better to use UIApplicationOpenSettingsURLString
    return @{
        @"settingsURL": UIApplicationOpenSettingsURLString
    };
}

@end
