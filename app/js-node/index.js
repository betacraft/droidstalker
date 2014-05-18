console.log("hello...")
require('./utils.js')

console.log('======= yoo ========')

var util = require('util')
var thrift = require('thrift')
var droidStalker = require('./thrift/DroidStalkerKernelService.js')
var DeviceStruct_ttypes = require('./thrift/DeviceStruct_types')
var connection = thrift.createConnection('192.168.3.137', 10000)

connection.on('error', function(err) {
	console.log(err)	
})

var client = thrift.createClient(droidStalker, connection)

var output = client.getDevices(function(errs, list){
	console.log("====== INSIDE getDevices calblack..")
	console.log(errs)
	console.log(list)
})

console.log('output: ')
console.log(output)
