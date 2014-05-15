/**
 * Thrift definition file for DroidStalkerBackendService
 */

include "DeviceStruct.thrift"
include "ThreadInfoStruct.thrift"
include "AndroidAppStruct.thrift"
include "CPUStatsStruct.thrift"

namespace java com.rc.droid_stalker.thrift
namespace js js.droid_stalker.thrift



/**
 * Kernel exception error codes
 */
enum KernelExceptionErrorCode{
    KERNEL_ERROR =0,
    KERNEL_BOOT_FAILED = 1,
    KERNEL_CRASHED = 2,
    DEVICE_DISCONNECTED = 3,
    APP_NOT_FOUND = 4,
    APP_COULD_NOT_START = 5,
    NO_SUCH_DEBUG_SESSION_RUNNING = 6,
    DEVICE_NOT_FOUND_CONNECTED = 7,
    FAILED_TO_START_APP = 8
}

/**
 * Exceptions that might be raised during kernel execution
 */
exception DroidStalkerKernelException{
    1: required KernelExceptionErrorCode errorCode,
    2: required string errorMessage,
}

/**
 * DroidStalker Kernel Service
 */
service DroidStalkerKernelService {
    /**
     * Method to get the list of devices
     */
    set<DeviceStruct.DeviceStruct> getDevices() throws (1: DroidStalkerKernelException kernelException),
    /**
     * Method to get installed apps from device
     */
    set<AndroidAppStruct.AndroidAppStruct> getInstalledAppsOn(1: DeviceStruct.DeviceStruct device)
    throws (1: DroidStalkerKernelException kernelException),
    /**
     * Start app for debug
     * Returns debug session id
     */
    string startDebugSessionFor(1: DeviceStruct.DeviceStruct device, 2: AndroidAppStruct.AndroidAppStruct app)
    throws (1: DroidStalkerKernelException kernelException),
    /**
     * Method to get all running threads in a debug session
     */
    set<ThreadInfoStruct.ThreadInfoStruct> getThreadsRunningIn(1: string debugSession) throws
    (1: DroidStalkerKernelException kernelException),
    /**
     * Method to get CPU stats associated with debug session
     **/
    CPUStatsStruct.CPUStatsStruct getCPUStatsFor(1: string debugSession, 2: i32 span) throws
    (1: DroidStalkerKernelException kernelException),
}