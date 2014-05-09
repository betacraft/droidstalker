#!/bin/sh

thrift --gen java:beans AndroidAppStruct.thrift
thrift --gen java:beans DeviceStruct.thrift
thrift --gen java:beans ThreadInfoStruct.thrift
thrift --gen java:beans DroidStalkerAppService.thrift
thrift --gen java:beans DroidStalkerKernelService.thrift