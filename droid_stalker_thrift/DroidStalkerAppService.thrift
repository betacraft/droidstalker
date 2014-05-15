/**
 * Thrift definition file for DroidStalkerBackendService
 */

include "AndroidAppStruct.thrift"
include "CPUStatsStruct.thrift"

namespace java com.rc.droid_stalker.thrift
namespace js js.droid_stalker.thrift


/**
 * Kernel exception error codes
 */
enum AppExceptionErrorCode{
    ERROR_WHILE_EXECUTING_CODE = 1
}

/**
 * Exceptions that might be raised during kernel execution
 */
exception DroidStalkerAppException{
    1: required AppExceptionErrorCode errorCode,
    2: required string errorMessage,
}

/**
 *  DroidStalkerAppService that will run inside a service in our Android Application
 */
service DroidStalkerAppService {
    /**
     * Method to get installed apps from device
     */
    set<AndroidAppStruct.AndroidAppStruct> getInstalledApps() throws (1: DroidStalkerAppException appException),
    /**
     * Method to get CPU stats associated with a pid
     **/
    CPUStatsStruct.CPUStatsStruct getCPUStatsFor(1: i32 pid, 2: i32 span) throws (1: DroidStalkerAppException
    appException),
}