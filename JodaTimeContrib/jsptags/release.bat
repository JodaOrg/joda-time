call maven site:sshdeploy
cd target
pscp joda-time-jsptags-1.0.2-bundle.jar scolebourne@shell.sourceforge.net:/home/groups/j/jo/joda-time/htdocs
cd ..
svn mkdir "https://joda-time.svn.sourceforge.net/svnroot/joda-time/tags/JSPTAGS_v1_0_2" -m "Release 1.0.2"
svn copy https://joda-time.svn.sourceforge.net/svnroot/joda-time/trunk/JodaTimeContrib/jsptags https://joda-time.svn.sourceforge.net/svnroot/joda-time/tags/JSPTAGS_v1_0_2/jsptags -m "Release 1.0.2"