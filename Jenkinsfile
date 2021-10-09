def name;
pipeline {
    agent any
    stages {
        stage('Build Stage') {
            steps {
                script{
                bat "mvn -B -DskipTests clean package"
                  }
            }
        }
                stage('Testing Stage') {
		steps {
			script {
				def count = demo()
				//println "count above"+count
				
				if(count > 0) 
				{
					bat "mvn -Dsuite=PerformanceTests test"
					
                   		 }
				else
				{
					bat "mvn -Dsuite=FunctionalTests test"
					
				}
			}
			
		}
		post{
                          always{
                              	junit "**/target/surefire-reports/TEST-org.joda.time.TestAllPackages.xml"
				log()
                        
                                }
                     }
	}
    }
}

def log(){
	
    def inputFile = new File("C:\\Users\\palla\\.jenkins\\workspace\\JodaTime_Github\\target\\surefire-reports\\TEST-org.joda.time.TestAllPackages.xml")
    def XMLDATA  = new XmlParser().parse(inputFile)
    if(!inputFile.exists())
    {
       println "file not found"
    }
    else
    {
        //Read and parse XML file and store it into a variable
	    int total= XMLDATA.attribute("tests")
	    int fail = XMLDATA.attribute("failures")
	    int success = (total - fail)
	    
	    def newFile = new File("D:\\TestDemo.csv")
	    newFile.append(",${XMLDATA.attribute("tests")}, ${success}, ${XMLDATA.attribute("failures")}")
}
}
def demo(){
    def commitCode = bat (script: 'git log --format=format:"%%H"', returnStdout: true).trim()
    String[] hashCode = null;

    hashCode = commitCode.split("\n")
    //println Arrays.toString(hashCode)

    Random r = new Random()
    int n1 = r.nextInt(hashCode.size())
    println n1
    int n2 = n1+1
    println n2
    
   //println "First hashcode"+hashCode[n1+1] 
    //println "Second hashcode"+hashCode[n2+1]

    def firstCommit = hashCode[n1+1]
    def secondCommit = hashCode[n2+1]
	
	//def result = bat (script: "git diff -u $firstCommit $secondCommit | grep -E '^\\+'",returnStdout: true).trim()
	def result = bat (script: "git diff -a -m $firstCommit $secondCommit",returnStdout: true).trim()
	
	String repl = result.replaceAll("(\\r|\\n|\\r\\n|\\r|,|\\r\n)+", "\\\\n")																						   
	println(repl)
	String s = Regex.Replace(result, "[^0-9a-zA-Z:,]+", "")
	println(s)
	

    String diff = result.toString().toLowerCase()
    String[] diffArray = null;
    String[] keywords = ["runtime", "new", "gc", "system"];
    int count =0;
	
	        diffArray = diff.split(" ");
	        for(int i=5 ;i< diffArray.length ;i++) {
	        	for(int j=0 ;j < keywords.length ; j++ )
	        	{
	        	 if((diffArray[i].contains(keywords[j])))
	        	{
	        		count++;
	        	}
	        }
	        }
	
//CSV code start
    def newFile = new File("D:\\TestDemo.csv")
    def exists = fileExists 'D:\\TestDemo.csv'

if (!exists) {
    newFile.append("HashCode, Random HashCode 1, Random HashCode 2, Diff. between two commits, Code change category, Test case type,Total no. of test cases, No. of succeeed test, No. of failed tests, \n")
    
}
def currentHashcode = bat (script: '@git log -1 --pretty=%%H',returnStdout: true).trim()

	// for print code change category
	def codeChangeCategory 
	def testCaseType
	if(count >= 1)
	{
		codeChangeCategory = "Memory Management"
		testCaseType = "Performance Test"
	}
	else
	{
		codeChangeCategory = "Functional"
		testCaseType = "Functional Test"
	}
	newFile.append("\n")
	newFile.append("${currentHashcode}, ${firstCommit}, ${secondCommit}, ${repl}, ${codeChangeCategory}, ${testCaseType}")
	//csv code end
	       return count
}
