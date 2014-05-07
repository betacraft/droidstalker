/**
 * Thrift definition file for Device struct
 */
namespace java com.rc.droid_stalker.thrift

/**
 * ThreadInfo structure
 */
struct ThreadInfoStruct {
    1: required i32 threadId,
    2: required string name,
    3: required i32 status,
    4: required i32 timeId,
    5: required i32 upTime,
    6: required i32 startTime,
    7: required bool isDaemon,
    8: required i64 traceTime
}