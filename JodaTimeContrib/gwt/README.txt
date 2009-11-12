Build
=====
To build this library the following software is assumed to be installed:

* java jdk (tested with 1.6.0_14-b08)
* maven (tested with 2.2.1)
* ant (tested with 1.7.1)
* gwt (tested with 1.7.0)

Build steps:
1. obtain the dependencies:
   $ mvn dependency:copy-dependencies
2. copy build.properties to build-user.properties and change the properties in build-user.properties so they fit your environment.
3. create the jar:
   $ ant jar

To test:
1. change the gwt.testSet property in build-user.properties to the set that you want to run (NOTE: running all tests will take a LOT of time!)
2. run the tests
   $ ant run-gwt-test
3. generate the test reports
   $ ant run-gwt-test-report
   
Usage
=====
Joda-time-gwt is a port of joda-time for the GWT environment. 
More specifically:
- at server side there is no difference in using joda-time or joda-time-gwt, i.e. the code you have written that depends on joda-time 
  you can also use with joda-time-gwt instead. 
- at client side the amount of code that you can still use is maximized. It emulates joda-time wherever possible.

Currently there are 3 classes excluded from use at client side: ZoneInfoCompiler, ZoneInfoProvider and JodaTimePermission.
Furthermore, for various reasons not all code in joda-time can be ported to GWT. In cases where it makes sense this code is simply left out 
(things like security and where defaults are set from system properties). In the rest of the code an UnsupportedOperationException is thrown, 
to make explicit that this code is not ported to GWT (yet).

Also joda-time-gwt introduces some new classes:
- org.joda.time.gwt.GwtTimeZone which wraps com.google.gwt.i18n.client.TimeZone and adapts it to its joda-time counterpart org.joda.time.DateTimeZone.
- org.joda.time.gwt.tz.GwtZoneInfoProvider: a GWT specific implementation of org.joda.time.tz.Provider. 

An important difference between server side and client side is regarding the default provider in DateTimeZone: on server side the first attempt
is to set it based on a system property, then it tries to create a ZoneInfoProvider and if it is then still not set then UTCProvider will be used. 
On client side UTCProvider will always be used by default (system properties and ZoneInfoProvider are not available here). 
Also GwtZoneInfoProvider is chosen not to be set by default, to minimize chances that this class is unnecessarily compiled and included as javascript.