eWALL Portal
=========
In order to automatically update the Javascript libraries used by this web application, we used [bower](http://bower.io/) and
[grunt](http://gruntjs.com/).

Install
---------
* install [NodeJS and NPM](http://nodejs.org/)
* run `npm install` in your CLI, this will install the dependencies listed in the `package.json` file
* run `grunt` to update the libraries

Update of the Javascript libraries
---------
Once you did the installation, then you can just run `grunt` for future updates.

If you get any error by running `grunt` you can try to run `bower install` and follow the instructions to solve the issues.

Adding other libraries
---------
To add a new library you have to see whether it is registered in the bower repository, if not just add the library in the `js` folder.
Do not add it in the `lib` folder because it must be used only by bower.

If the library is supported by bower you can install it with `bower install library_name --save`

Since by default bower will download all the repository of the installed library, we used a grunt plugin to put in the `lib` folder
only the needed files. Therefore, you have also to edit the `bower.json` file in order to specify which files you want to keep.
In particular, you may need to add your configuration in the `"exportsOverride"` section of the file.

Once you did it simply run `grunt` as usual.

Possible problem in Eclipse
---------
This setup will create two folders: `bower_components` and `node_modules`. Eclipse might want to parse also this folders causing
high CPU load and Memory usage. In order to avoid it you have to exclude these folders in the project settings of Eclipse: `Resource -> Resource Filters`.