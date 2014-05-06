/**
 * Thrift definition file for DroidStalkerBackendService
 */

include "DeviceStruct.thrift"

namespace java com.rc.droid_stalker.thrift



/**
 * Kernel exception error codes
 */
enum KernelExceptionErrorCode{
    KERNEL_BOOT_FAILED = 1,
    KERNEL_CRASHED = 2,
    DEVICE_DISCONNECTED = 3,
    APP_NOT_FOUND = 4,
    APP_COULD_NOT_START = 5
}

/**
 * Exceptions that might be raised during kernel execution
 */
exception DroidStalkerKernelException{
    1: required KernelExceptionErrorCode errorCode,
    2: required string errorMessage,
}

/**
 * Session Device setup manager serviceComponent
 */
service DroidStalkerKernelService {
    /**
     * Method to start kernel
     */
    void boot(1: string adbLocation) throws (1: DroidStalkerKernelException kernelException),
    /**
     * Method to check if kernel is running or not
     */
    bool isRunning() throws (1: DroidStalkerKernelException kernelException),
    /**
     * Start app for debug
     */
    void startAppForDebug(1: string packageName) throws (1: DroidStalkerKernelException kernelException),
    /**
     * Method to start session
     */
    set<DeviceStruct.DeviceStruct> getDevices() throws (1: DroidStalkerKernelException kernelException),
}