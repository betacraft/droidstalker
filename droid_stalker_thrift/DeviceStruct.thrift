/**
 * Thrift definition file for Device struct
 */
namespace java com.rc.droid_stalker.thrift

/**
 * Device Structure
 */
struct DeviceStruct {
    1: required string serialNumber,
    2: required string avdName,
    3: required string deviceState,
    4: required bool isEmulator,
    5: required i16 batteryPercentage,
}