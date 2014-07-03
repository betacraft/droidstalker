/**
 * Thrift definition file for CPU stats struct
 */
namespace java com.rc.droid_stalker.thrift
namespace js js.droid_stalker.thrift

/**
 * CPU stats Structure
 */
struct CPUStatsStruct {
    1: required i32 packetId,
    2: required double totalCPU,
    3: required double pidCPU,
    4: required i32 pid,
    5: required string startTimestamp,
    6: required string endTimestamp
}