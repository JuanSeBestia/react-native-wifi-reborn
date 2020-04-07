@interface ConnectError : NSObject

typedef enum {
    UnavailableForOSVersion,
    Invalid,
    InvalidSSID,
    InvalidSSIDPrefix,
    InvalidPassphrase,
    UserDenied,
    UnableToConnect,
    LocationPermissionDenied,
    LocationPermissionRestricted,
    DidNotFindNetwork,
    CouldNotDetectSSID,
    Unknown
} ConnectErrorCode;

+ (NSString*)code:(ConnectErrorCode)errorCode;

@end
