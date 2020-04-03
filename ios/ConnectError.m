#import <Foundation/Foundation.h>
#import "ConnectError.h"

@implementation ConnectError

+ (NSString*)code:(ConnectErrorCode)errorCode {
    switch(errorCode) {
        case UnavailableForOSVersion:
            return @"unavailableForOSVersion";
        case Invalid:
            return @"invalid";
        case InvalidSSID:
            return @"invalidSSID";
        case InvalidSSIDPrefix:
            return @"invalidSSIDPrefix";
        case InvalidPassphrase:
            return @"invalidPassphrase";
        case UserDenied:
            return @"userDenied";
        case CouldNotDetectSSID:
            return @"couldNotDetectSSID";
        case LocationPermissionDenied:
            return @"locationPermissionDenied";
        case LocationPermissionRestricted:
            return @"locationPermissionRestricted";
        case LocationPermissionMissing:
            return @"LocationPermissionMissing";
        case LocationServicesOff:
            return @"LocationServicesOff";
        case CouldNotEnabledWifi:
            return @"CouldNotEnabledWifi";
        case CouldNotScan:
            return @"CouldNotScan";
        case DidNotFindNetworkByScanning:
            return @"DidNotFindNetworkByScanning";
        case AuthenticationErrorOccurred:
            return @"AuthenticationErrorOccurred";
        case TimeoutOccurred:
            return @"TimeoutOccurred";
        case CouldNotConnect:
            return @"CouldNotConnect";
        case Unknown:
        default:
            return @"Unknown";
    }
}

@end
