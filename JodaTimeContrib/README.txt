
Additional setup for test cases
-------------------------------

Joda uses maven for the build process. Maven tries to download all dependencies from ibiblio.
Some of them are not hosted due to their licensing.

Thus you have to download them and put them in your local maven repository.
e.g. on Linux YOUR_HOME/.maven/respository


Java Transaction API:

http://java.sun.com/products/jta

# mkdir ~/.maven/repository/jta/jars
# cp jta-1_0_1B-classes.zip ~/.maven/repository/jta/jars