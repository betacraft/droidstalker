#!/bin/sh

thrift --gen java:beans CPUStatsStruct.thrift
thrift --gen java:beans AndroidAppStruct.thrift
thrift --gen java:beans DeviceStruct.thrift
thrift --gen java:beans ThreadInfoStruct.thrift
thrift --gen java:beans DroidStalkerAppService.thrift
thrift --gen java:beans DroidStalkerKernelService.thrift


thrift --gen js:node CPUStatsStruct.thrift
thrift --gen js:node AndroidAppStruct.thrift
thrift --gen js:node DeviceStruct.thrift
thrift --gen js:node ThreadInfoStruct.thrift
thrift --gen js:node DroidStalkerAppService.thrift
thrift --gen js:node DroidStalkerKernelService.thrift