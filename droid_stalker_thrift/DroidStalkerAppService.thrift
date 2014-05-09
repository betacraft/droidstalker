/**
 * Thrift definition file for DroidStalkerBackendService
 */

include "AndroidAppStruct.thrift"

namespace java com.rc.droid_stalker.thrift



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
    set<AndroidAppStruct.AndroidAppStruct> getInstalledApps() throws (1: DroidStalkerAppException kernelException),

}