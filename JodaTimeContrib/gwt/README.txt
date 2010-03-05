Build
=====
To build this library the following software is assumed to be installed:

* java jdk (tested with 1.6.0_14-b08)
* maven (tested with 2.2.1)
* ant (tested with 1.7.1)
* gwt (tested with 1.7.0)

Build steps:
1. make sure you have java jdk, maven, ant and gwt installed
2. obtain the dependencies:
   $ mvn dependency:copy-dependencies
3. copy build.properties to build-user.properties and change the properties in build-user.properties so they fit your environment.
4. create the jar:
   $ mvn package
   or
   $ ant jar

To test:
1. change the gwt.testSet property in build-user.properties to the set that you want to run (NOTE: running all tests will take a LOT of time!)
2. run the tests
   $ ant run-gwt-test
3. generate the test reports
   $ ant run-gwt-test-report
   
Misc:
Maven is used for the other tasks, so standard maven commands like "mvn site" and "mvn install" can be used.

Usage
=====
See http://joda-time.sourceforge.net/contrib/gwt/userguide.html