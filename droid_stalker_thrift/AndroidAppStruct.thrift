/**
 * Thrift definition file for Device struct
 */
namespace java com.rc.droid_stalker.thrift

/**
 * Device Structure
 */
struct AndroidAppStruct {
    1: required string packageName,
    2: required string activityName,
    3: required string applicationName,
    4: required string applicationIcon
}