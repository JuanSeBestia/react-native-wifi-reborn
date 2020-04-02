@interface ConnectError : NSObject

typedef enum {
    LocationPermissionMissing = 0,
    LocationServicesOff,
    CouldNotEnabledWifi,
    CouldNotScan,
    DidNotFindNetworkByScanning,
    AuthenticationErrorOccurred,
    TimeoutOccurred,
    CouldNotConnect
} ConnectErrorCode;

+ (NSString*)code:(ConnectErrorCode)errorCode;

@end
