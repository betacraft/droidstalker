
var util = require('util')
var thrift = require('thrift')
var droidStalker = require('./thrift/DroidStalkerKernelService.js')
var AndroidAppStruct = 
      require('./thrift/AndroidAppStruct_types.js').AndroidAppStruct
var DeviceStruct = require('./thrift/DeviceStruct_types.js').DeviceStruct



DebugSession = function(){
  this.session = {}

  // thrift objects - 
  this.connection = undefined
  this.client = undefined
  this.devices = []
  this.currentDevice = {}
  this.apps = []
  this.currentApp = {}

  this.init = function(){
    this.connection = thrift.createConnection('192.168.3.137', 10000)

    this.connection.on('error', function(err) {
      console.log('== Error on connection -')
      console.log(err)
    })

    this.client = thrift.createClient(droidStalker, this.connection)
  }

  this.getDevices = function(callback){
    this.client.getDevices(function(errs, list){
      if(errs){
        console.log('== Error getting devices: ')
        console.log(errs)
      }else{
        console.log(list)
        if(list.length != 0){
          console.log('got device, starting debug session - ')
          var device = list[0]
          //startDebugSession(device)
          callback(device)
        }else{
          console.log('== No connected devices found.')
        }
      }
    })
  }

  this.setDevice = function(device){
  }

  this.getApps = function(){
  }

  this.setApp = function(app){}

  this.start = function(device, app_params, callback){
    var app =  new AndroidAppStruct(app_params)
    this.currentApp = app
    // console.log(app)
    // var device = new DeviceStruct(device_attrs)

    this.client.startDebugSessionFor(device, app, function(err, debugSession){
      if(err){
        console.log(err)
      }else{
        console.log('upto debug session..')
        console.log(debugSession)
        this.session = debugSession
        callback(this.session)
      }
    })
  }

  // get stats  - 
  this.stats = {
    cpu: function(device, app, callback){
      this.session.getCPUStatsFor(device, app, function(err, data){
        console.log('cpu err: ' + err)
        console.log('cpu data: ' + data)
      })
    }
  }
}

exports.DebugSession = DebugSession
