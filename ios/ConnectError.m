#import <Foundation/Foundation.h>
#import "ConnectError.h"

@implementation ConnectError

+ (NSString*)code:(ConnectErrorCode)errorCode {
    switch(errorCode) {
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
        default:
            return @"UNKNOWN";
    }
}

@end
