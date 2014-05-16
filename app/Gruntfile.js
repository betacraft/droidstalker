module.exports = function(grunt) {

  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    nodewebkit: {
      options: {
          build_dir: '/home/appsurfer/projects', // Where the build version of my node-webkit app is saved
          mac: false, // We want to build it for mac
          win: false, // We want to build it for win
          linux32: false, // We don't need linux32
          linux64: true // We don't need linux64
      },
      src: ['./**/*'] // Your node-webkit app
    },
  });

  grunt.loadNpmTasks('grunt-node-webkit-builder');

  // register task - 
  grunt.registerTask('default', ['nodewebkit'])
};
