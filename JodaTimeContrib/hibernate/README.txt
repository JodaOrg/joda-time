
Joda-Time Contributions area - Hibernate support
================================================
Joda-Time is a date and time library that vastly improves on the JDK.
This release provides additional support for Hibernate database persistence.
See http://www.hibernate.org/ for more details on Hibernate.

Additional setup for test cases
-------------------------------
Joda-Time supports the use of maven for the build process.
Maven tries to download all dependencies from ibiblio.
Some of them are not hosted due to their licensing.

Thus you have to download them and put them in your local maven repository.
eg. on Linux, YOUR_HOME/.maven/respository
eg. on Windows, Documents and Settings/YOUR_USER/.maven/respository

For the hibernate code, you need the Java Transaction API:
http://java.sun.com/products/jta

# mkdir ~/.maven/repository/jta/jars
# cp jta-1_0_1B-classes.zip ~/.maven/repository/jta/jars
