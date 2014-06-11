console.log("hello...")
require('./utils.js')

console.log('======= yoo ========')
var DebugSession = require('./session.js').DebugSession

// var util = require('util')
// var thrift = require('thrift')
// var droidStalker = require('./thrift/DroidStalkerKernelService.js')
// var AndroidAppStruct = require('./thrift/AndroidAppStruct_types.js').AndroidAppStruct
// var DeviceStruct = require('./thrift/DeviceStruct_types.js').DeviceStruct
// 
// var connection = thrift.createConnection('192.168.3.137', 10000)
// 
// connection.on('error', function(err) {
//   console.log('== Error on connection -')
// 	console.log(err)
// })
// 
// var client = thrift.createClient(droidStalker, connection)

// client.getDevices(function(errs, list){
//   if(errs){
//     console.log('== Error getting devices: ')
//     console.log(errs)
//   }else{
//     console.log(list)
//     if(list.length != 0){
//       console.log('got device, starting debug session - ')
//       var device = list[0]
//       startDebugSession(device)
//     }else{
//       console.log('== No connected devices found.')
//     }
//   }
// })

var ds = new DebugSession()

// init it to start connections -
ds.init()

// get devices -
ds.getDevices(function(device){

  // once device is here, start debug session by sending app - 
  app_params = {
    packageName: 'com.obeat.advertdisplay',
    activityName: 'com.obeat.advertdisplay.activities.Boot',
    applicationName: '',
    applicationIcon: ''
  }

  // start debug session - 
  ds.start(device, app_params, function(session){
    console.log('session is here.')
    console.log(session)

    // first get cpu stat - 
    session.stats.cpu(device, ds.currentApp, function(info){
      console.log('from cpu stat: ' + info)
    })
  })
})



