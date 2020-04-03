@interface ConnectError : NSObject

typedef enum {
    UnavailableForOSVersion,
    Invalid,
    InvalidSSID,
    InvalidSSIDPrefix,
    InvalidPassphrase,
    UserDenied,
    UnableToConnect,
    CouldNotDetectSSID,
    LocationPermissionDenied,
    LocationPermissionRestricted,
    
    LocationPermissionMissing,
    LocationServicesOff,
    CouldNotEnabledWifi,
    CouldNotScan,
    DidNotFindNetworkByScanning,
    AuthenticationErrorOccurred,
    TimeoutOccurred,
    CouldNotConnect,
    Unknown
} ConnectErrorCode;

+ (NSString*)code:(ConnectErrorCode)errorCode;

@end
