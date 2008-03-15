SET JAVA_HOME=C:\java\jdk1.4.2
call maven clean dist site
call ant -f bundle.xml