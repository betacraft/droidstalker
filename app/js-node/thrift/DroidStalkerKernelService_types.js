//
// Autogenerated by Thrift Compiler (0.9.1)
//
// DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
//
var Thrift = require('thrift').Thrift;
var DeviceStruct_ttypes = require('./DeviceStruct_types')
var ThreadInfoStruct_ttypes = require('./ThreadInfoStruct_types')
var AndroidAppStruct_ttypes = require('./AndroidAppStruct_types')
var CPUStatsStruct_ttypes = require('./CPUStatsStruct_types')


var ttypes = module.exports = {};
if (typeof js === 'undefined') {
  js = {};
}
if (typeof js.droid_stalker === 'undefined') {
  js.droid_stalker = {};
}
if (typeof js.droid_stalker.thrift === 'undefined') {
  js.droid_stalker.thrift = {};
}
ttypes.KernelExceptionErrorCode = {
'KERNEL_ERROR' : 0,
'KERNEL_BOOT_FAILED' : 1,
'KERNEL_CRASHED' : 2,
'DEVICE_DISCONNECTED' : 3,
'APP_NOT_FOUND' : 4,
'APP_COULD_NOT_START' : 5,
'NO_SUCH_DEBUG_SESSION_RUNNING' : 6,
'DEVICE_NOT_FOUND_CONNECTED' : 7,
'FAILED_TO_START_APP' : 8
};
js.droid_stalker.thrift.DroidStalkerKernelException = module.exports.DroidStalkerKernelException = function(args) {
  Thrift.TException.call(this, "js.droid_stalker.thrift.DroidStalkerKernelException")
  this.name = "js.droid_stalker.thrift.DroidStalkerKernelException"
  this.errorCode = null;
  this.errorMessage = null;
  if (args) {
    if (args.errorCode !== undefined) {
      this.errorCode = args.errorCode;
    }
    if (args.errorMessage !== undefined) {
      this.errorMessage = args.errorMessage;
    }
  }
};
Thrift.inherits(js.droid_stalker.thrift.DroidStalkerKernelException, Thrift.TException);
js.droid_stalker.thrift.DroidStalkerKernelException.prototype.name = 'DroidStalkerKernelException';
js.droid_stalker.thrift.DroidStalkerKernelException.prototype.read = function(input) {
  input.readStructBegin();
  while (true)
  {
    var ret = input.readFieldBegin();
    var fname = ret.fname;
    var ftype = ret.ftype;
    var fid = ret.fid;
    if (ftype == Thrift.Type.STOP) {
      break;
    }
    switch (fid)
    {
      case 1:
      if (ftype == Thrift.Type.I32) {
        this.errorCode = input.readI32();
      } else {
        input.skip(ftype);
      }
      break;
      case 2:
      if (ftype == Thrift.Type.STRING) {
        this.errorMessage = input.readString();
      } else {
        input.skip(ftype);
      }
      break;
      default:
        input.skip(ftype);
    }
    input.readFieldEnd();
  }
  input.readStructEnd();
  return;
};

js.droid_stalker.thrift.DroidStalkerKernelException.prototype.write = function(output) {
  output.writeStructBegin('DroidStalkerKernelException');
  if (this.errorCode !== null && this.errorCode !== undefined) {
    output.writeFieldBegin('errorCode', Thrift.Type.I32, 1);
    output.writeI32(this.errorCode);
    output.writeFieldEnd();
  }
  if (this.errorMessage !== null && this.errorMessage !== undefined) {
    output.writeFieldBegin('errorMessage', Thrift.Type.STRING, 2);
    output.writeString(this.errorMessage);
    output.writeFieldEnd();
  }
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

