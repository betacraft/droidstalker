//
// Autogenerated by Thrift Compiler (0.9.1)
//
// DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
//
var Thrift = require('thrift').Thrift;
var AndroidAppStruct_ttypes = require('./AndroidAppStruct_types')
var CPUStatsStruct_ttypes = require('./CPUStatsStruct_types')


var ttypes = require('./DroidStalkerAppService_types');
//HELPER FUNCTIONS AND STRUCTURES

js.droid_stalker.thrift.DroidStalkerAppService_getInstalledApps_args = function(args) {
};
js.droid_stalker.thrift.DroidStalkerAppService_getInstalledApps_args.prototype = {};
js.droid_stalker.thrift.DroidStalkerAppService_getInstalledApps_args.prototype.read = function(input) {
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
    input.skip(ftype);
    input.readFieldEnd();
  }
  input.readStructEnd();
  return;
};

js.droid_stalker.thrift.DroidStalkerAppService_getInstalledApps_args.prototype.write = function(output) {
  output.writeStructBegin('DroidStalkerAppService_getInstalledApps_args');
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

js.droid_stalker.thrift.DroidStalkerAppService_getInstalledApps_result = function(args) {
  this.success = null;
  this.appException = null;
  if (args instanceof ttypes.DroidStalkerAppException) {
    this.appException = args;
    return;
  }
  if (args) {
    if (args.success !== undefined) {
      this.success = args.success;
    }
    if (args.appException !== undefined) {
      this.appException = args.appException;
    }
  }
};
js.droid_stalker.thrift.DroidStalkerAppService_getInstalledApps_result.prototype = {};
js.droid_stalker.thrift.DroidStalkerAppService_getInstalledApps_result.prototype.read = function(input) {
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
      case 0:
      if (ftype == Thrift.Type.SET) {
        var _size0 = 0;
        var _rtmp34;
        this.success = [];
        var _etype3 = 0;
        _rtmp34 = input.readSetBegin();
        _etype3 = _rtmp34.etype;
        _size0 = _rtmp34.size;
        for (var _i5 = 0; _i5 < _size0; ++_i5)
        {
          var elem6 = null;
          elem6 = new AndroidAppStruct_ttypes.AndroidAppStruct();
          elem6.read(input);
          this.success.push(elem6);
        }
        input.readSetEnd();
      } else {
        input.skip(ftype);
      }
      break;
      case 1:
      if (ftype == Thrift.Type.STRUCT) {
        this.appException = new ttypes.DroidStalkerAppException();
        this.appException.read(input);
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

js.droid_stalker.thrift.DroidStalkerAppService_getInstalledApps_result.prototype.write = function(output) {
  output.writeStructBegin('DroidStalkerAppService_getInstalledApps_result');
  if (this.success !== null && this.success !== undefined) {
    output.writeFieldBegin('success', Thrift.Type.SET, 0);
    output.writeSetBegin(Thrift.Type.STRUCT, this.success.length);
    for (var iter7 in this.success)
    {
      if (this.success.hasOwnProperty(iter7))
      {
        iter7 = this.success[iter7];
        iter7.write(output);
      }
    }
    output.writeSetEnd();
    output.writeFieldEnd();
  }
  if (this.appException !== null && this.appException !== undefined) {
    output.writeFieldBegin('appException', Thrift.Type.STRUCT, 1);
    this.appException.write(output);
    output.writeFieldEnd();
  }
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

js.droid_stalker.thrift.DroidStalkerAppService_getCPUStatsFor_args = function(args) {
  this.pid = null;
  this.span = null;
  if (args) {
    if (args.pid !== undefined) {
      this.pid = args.pid;
    }
    if (args.span !== undefined) {
      this.span = args.span;
    }
  }
};
js.droid_stalker.thrift.DroidStalkerAppService_getCPUStatsFor_args.prototype = {};
js.droid_stalker.thrift.DroidStalkerAppService_getCPUStatsFor_args.prototype.read = function(input) {
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
        this.pid = input.readI32();
      } else {
        input.skip(ftype);
      }
      break;
      case 2:
      if (ftype == Thrift.Type.I32) {
        this.span = input.readI32();
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

js.droid_stalker.thrift.DroidStalkerAppService_getCPUStatsFor_args.prototype.write = function(output) {
  output.writeStructBegin('DroidStalkerAppService_getCPUStatsFor_args');
  if (this.pid !== null && this.pid !== undefined) {
    output.writeFieldBegin('pid', Thrift.Type.I32, 1);
    output.writeI32(this.pid);
    output.writeFieldEnd();
  }
  if (this.span !== null && this.span !== undefined) {
    output.writeFieldBegin('span', Thrift.Type.I32, 2);
    output.writeI32(this.span);
    output.writeFieldEnd();
  }
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

js.droid_stalker.thrift.DroidStalkerAppService_getCPUStatsFor_result = function(args) {
  this.success = null;
  this.appException = null;
  if (args instanceof ttypes.DroidStalkerAppException) {
    this.appException = args;
    return;
  }
  if (args) {
    if (args.success !== undefined) {
      this.success = args.success;
    }
    if (args.appException !== undefined) {
      this.appException = args.appException;
    }
  }
};
js.droid_stalker.thrift.DroidStalkerAppService_getCPUStatsFor_result.prototype = {};
js.droid_stalker.thrift.DroidStalkerAppService_getCPUStatsFor_result.prototype.read = function(input) {
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
      case 0:
      if (ftype == Thrift.Type.STRUCT) {
        this.success = new CPUStatsStruct_ttypes.CPUStatsStruct();
        this.success.read(input);
      } else {
        input.skip(ftype);
      }
      break;
      case 1:
      if (ftype == Thrift.Type.STRUCT) {
        this.appException = new ttypes.DroidStalkerAppException();
        this.appException.read(input);
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

js.droid_stalker.thrift.DroidStalkerAppService_getCPUStatsFor_result.prototype.write = function(output) {
  output.writeStructBegin('DroidStalkerAppService_getCPUStatsFor_result');
  if (this.success !== null && this.success !== undefined) {
    output.writeFieldBegin('success', Thrift.Type.STRUCT, 0);
    this.success.write(output);
    output.writeFieldEnd();
  }
  if (this.appException !== null && this.appException !== undefined) {
    output.writeFieldBegin('appException', Thrift.Type.STRUCT, 1);
    this.appException.write(output);
    output.writeFieldEnd();
  }
  output.writeFieldStop();
  output.writeStructEnd();
  return;
};

js.droid_stalker.thrift.DroidStalkerAppServiceClient = exports.Client = function(output, pClass) {
    this.output = output;
    this.pClass = pClass;
    this._seqid = 0;
    this._reqs = {};
};
js.droid_stalker.thrift.DroidStalkerAppServiceClient.prototype = {};
js.droid_stalker.thrift.DroidStalkerAppServiceClient.prototype.seqid = function() { return this._seqid; }
js.droid_stalker.thrift.DroidStalkerAppServiceClient.prototype.new_seqid = function() { this._seqid += 1; }
js.droid_stalker.thrift.DroidStalkerAppServiceClient.prototype.getInstalledApps = function(callback) {
  this._seqid = this.new_seqid();
  this._reqs[this.seqid()] = callback;
  this.send_getInstalledApps();
};

js.droid_stalker.thrift.DroidStalkerAppServiceClient.prototype.send_getInstalledApps = function() {
  var output = new this.pClass(this.output);
  output.writeMessageBegin('getInstalledApps', Thrift.MessageType.CALL, this.seqid());
  var args = new js.droid_stalker.thrift.DroidStalkerAppService_getInstalledApps_args();
  args.write(output);
  output.writeMessageEnd();
  return this.output.flush();
};

js.droid_stalker.thrift.DroidStalkerAppServiceClient.prototype.recv_getInstalledApps = function(input,mtype,rseqid) {
  var callback = this._reqs[rseqid] || function() {};
  delete this._reqs[rseqid];
  if (mtype == Thrift.MessageType.EXCEPTION) {
    var x = new Thrift.TApplicationException();
    x.read(input);
    input.readMessageEnd();
    return callback(x);
  }
  var result = new js.droid_stalker.thrift.DroidStalkerAppService_getInstalledApps_result();
  result.read(input);
  input.readMessageEnd();

  if (null !== result.appException) {
    return callback(result.appException);
  }
  if (null !== result.success) {
    return callback(null, result.success);
  }
  return callback('getInstalledApps failed: unknown result');
};
js.droid_stalker.thrift.DroidStalkerAppServiceClient.prototype.getCPUStatsFor = function(pid, span, callback) {
  this._seqid = this.new_seqid();
  this._reqs[this.seqid()] = callback;
  this.send_getCPUStatsFor(pid, span);
};

js.droid_stalker.thrift.DroidStalkerAppServiceClient.prototype.send_getCPUStatsFor = function(pid, span) {
  var output = new this.pClass(this.output);
  output.writeMessageBegin('getCPUStatsFor', Thrift.MessageType.CALL, this.seqid());
  var args = new js.droid_stalker.thrift.DroidStalkerAppService_getCPUStatsFor_args();
  args.pid = pid;
  args.span = span;
  args.write(output);
  output.writeMessageEnd();
  return this.output.flush();
};

js.droid_stalker.thrift.DroidStalkerAppServiceClient.prototype.recv_getCPUStatsFor = function(input,mtype,rseqid) {
  var callback = this._reqs[rseqid] || function() {};
  delete this._reqs[rseqid];
  if (mtype == Thrift.MessageType.EXCEPTION) {
    var x = new Thrift.TApplicationException();
    x.read(input);
    input.readMessageEnd();
    return callback(x);
  }
  var result = new js.droid_stalker.thrift.DroidStalkerAppService_getCPUStatsFor_result();
  result.read(input);
  input.readMessageEnd();

  if (null !== result.appException) {
    return callback(result.appException);
  }
  if (null !== result.success) {
    return callback(null, result.success);
  }
  return callback('getCPUStatsFor failed: unknown result');
};
js.droid_stalker.thrift.DroidStalkerAppServiceProcessor = exports.Processor = function(handler) {
  this._handler = handler
}
js.droid_stalker.thrift.DroidStalkerAppServiceProcessor.prototype.process = function(input, output) {
  var r = input.readMessageBegin();
  if (this['process_' + r.fname]) {
    return this['process_' + r.fname].call(this, r.rseqid, input, output);
  } else {
    input.skip(Thrift.Type.STRUCT);
    input.readMessageEnd();
    var x = new Thrift.TApplicationException(Thrift.TApplicationExceptionType.UNKNOWN_METHOD, 'Unknown function ' + r.fname);
    output.writeMessageBegin(r.fname, Thrift.MessageType.Exception, r.rseqid);
    x.write(output);
    output.writeMessageEnd();
    output.flush();
  }
}

js.droid_stalker.thrift.DroidStalkerAppServiceProcessor.prototype.process_getInstalledApps = function(seqid, input, output) {
  var args = new js.droid_stalker.thrift.DroidStalkerAppService_getInstalledApps_args();
  args.read(input);
  input.readMessageEnd();
  this._handler.getInstalledApps(function (err, result) {
    var result = new js.droid_stalker.thrift.DroidStalkerAppService_getInstalledApps_result((err != null ? err : {success: result}));
    output.writeMessageBegin("getInstalledApps", Thrift.MessageType.REPLY, seqid);
    result.write(output);
    output.writeMessageEnd();
    output.flush();
  })
}

js.droid_stalker.thrift.DroidStalkerAppServiceProcessor.prototype.process_getCPUStatsFor = function(seqid, input, output) {
  var args = new js.droid_stalker.thrift.DroidStalkerAppService_getCPUStatsFor_args();
  args.read(input);
  input.readMessageEnd();
  this._handler.getCPUStatsFor(args.pid, args.span, function (err, result) {
    var result = new js.droid_stalker.thrift.DroidStalkerAppService_getCPUStatsFor_result((err != null ? err : {success: result}));
    output.writeMessageBegin("getCPUStatsFor", Thrift.MessageType.REPLY, seqid);
    result.write(output);
    output.writeMessageEnd();
    output.flush();
  })
}

