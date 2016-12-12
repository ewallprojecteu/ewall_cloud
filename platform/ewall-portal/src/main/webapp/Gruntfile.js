'use strict';

module.exports = function (grunt) {
	grunt.loadNpmTasks('grunt-bower-task');
	
	grunt.initConfig({
	  bower: {
		install: {
			options: {
				targetDir: './lib',
				layout: 'byType',
				install: true,
				verbose: false,
				cleanTargetDir: true,
				cleanBowerDir: false,
				bowerOptions: {}
			  }
		   //just run 'grunt bower:install' and you'll see files from your Bower packages in lib directory
		}
	  }
	});

	grunt.registerTask('default', [
		'bower:install'
	]);
};
