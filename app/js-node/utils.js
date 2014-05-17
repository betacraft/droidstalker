var fs = require('fs')

fs.readdir('/home/appsurfer/projects/droid_stalker/', function (err, files) {
	alert(files)
})