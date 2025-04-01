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
    [self connectToProtectedSSID:ssid withPassphrase:@"" isWEP:false isHidden:false resolver:resolve rejecter:reject];
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

RCT_EXPORT_METHOD(connectToProtectedSSIDPrefix:(NSString*)ssidPrefix
                  withPassphrase:(NSString*)passphrase
                  isWEP:(BOOL)isWEP
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {

    [self connectToProtectedSSIDPrefixOnce:ssidPrefix withPassphrase:passphrase isWEP:isWEP joinOnce:false resolver:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(connectToProtectedSSIDPrefixOnce:(NSString*)ssidPrefix
                  withPassphrase:(NSString*)passphrase
                  isWEP:(BOOL)isWEP
                  joinOnce:(BOOL)joinOnce
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {

    if (@available(iOS 13.0, *)) {
        NEHotspotConfiguration* configuration = [[NEHotspotConfiguration alloc] initWithSSIDPrefix:ssidPrefix passphrase:passphrase isWEP:isWEP];
        configuration.joinOnce = joinOnce;

        [[NEHotspotConfigurationManager sharedManager] applyConfiguration:configuration completionHandler:^(NSError * _Nullable error) {
            if (error != nil) {
                reject([self parseError:error], @"Error while configuring WiFi", error);
            } else {
                // Verify SSID connection
                [self getWifiSSID:^(NSString* result) {
                    if ([result hasPrefix:ssidPrefix]){
                        resolve(nil);
                    } else {
                        reject([ConnectError code:UnableToConnect], [NSString stringWithFormat:@"%@/%@", @"Unable to connect to Wi-Fi with prefix ", ssidPrefix], nil);
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
                  isHidden:(BOOL)isHidden
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [self connectToProtectedSSIDOnce:ssid withPassphrase:passphrase isWEP:isWEP joinOnce:false resolver:resolve rejecter:reject];
}

RCT_EXPORT_METHOD(connectToProtectedWifiSSID:(NSDictionary *)params
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSString *ssid = params[@"ssid"];
    NSString *passphrase = params[@"password"];
    BOOL isWEP = [params[@"isWEP"] boolValue];

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
        if ([ssid isEqualToString:resultSSID]) {
            resolve(nil);
            return;
        }

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
                    // It's not guaranteed that the connection is established before completionHandler is called, so
                    // we check the connected WiFi network repeatedly until it's the one we requested.
                    // The original code here checked immediately, but sometimes this completionHandler is called before
                    // the connection is successful.
                    __block int tries = 0;
                    __block int maxTries = 20;
                    __block BOOL hasCompleted = NO; // Add a flag to track completion status, fixes #429
                    double intervalSeconds = 0.5;

                    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
                    dispatch_source_t dispatchSource = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue);
                    dispatch_time_t startTime = dispatch_time(DISPATCH_TIME_NOW, 0);
                    uint64_t intervalTime = (int64_t)(intervalSeconds * NSEC_PER_SEC);
                    dispatch_source_set_timer(dispatchSource, startTime, intervalTime, 0);
                    dispatch_source_set_event_handler(dispatchSource, ^{
                        // Check if we've already completed
                        if (hasCompleted) {
                            dispatch_source_cancel(dispatchSource);
                            return;
                        }
                        [self getWifiSSID:^(NSString* newSSID) {
                            // Return early if we've already completed
                            if (hasCompleted) {
                                return;
                            }
                            bool success = [ssid isEqualToString:newSSID];
                            tries++;

                            if (success) {
                                hasCompleted = YES;
                                dispatch_suspend(dispatchSource);
                                resolve(nil);
                            } else if (tries > maxTries) {
                                hasCompleted = YES;
                                dispatch_suspend(dispatchSource);
                                reject([ConnectError code:UnableToConnect], [NSString stringWithFormat:@"%@/%@", @"Unable to connect to ", ssid], nil);
                            } else {
                                // We aren't connected yet, keep trying
                            }
                        }];
                    });

                    // Start the timer
                    dispatch_resume(dispatchSource);
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
