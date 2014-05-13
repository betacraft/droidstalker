/**
 * Thrift definition file for CPU stats struct
 */
namespace java com.rc.droid_stalker.thrift

/**
 * CPU stats Structure
 */
struct CPUStatsStruct {
    1: required double totalCPU,
    2: required double pidCPU,
    3: required i32 pid,
}