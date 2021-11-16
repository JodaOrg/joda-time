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
				if(count == 0) 
				{
					bat "mvn -Dsuite=FunctionalTests test"
                   		 }
				else
				{
					bat "mvn -Dsuite=PerformanceTests test"
					
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

    def firstCommit = hashCode[n1+1]
    def secondCommit = hashCode[n2+1]
	
    	def result = bat (script: "git diff $firstCommit $secondCommit| grep ^+",returnStdout: true).trim()
	def a = result.replaceAll("//.*|/\\*((.|\\n)(?!=*/))+\\*/", "")
	def s = a.replaceAll("[^a-zA-Z0-9 ]+"," ")
	def t = s.replaceAll(/\s+/, ' ')
	def p = t.drop(143)
    String diff = p.toString().toLowerCase()
    String[] diffArray = null;
    String[] keywords = ["runtime", "new", "gc", "system"];
    int count =0;
	
	        diffArray = diff.split(" ");
	        for(int i=0 ;i< diffArray.length ;i++) {
	        	for(int j=0 ;j < keywords.length ; j++ )
	        	{
	        	 if((diffArray[i].equals(keywords[j])))
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
	if(count == 0)
	{
		codeChangeCategory = "Functional"
		testCaseType = "Functional Test"
		
	}
	else
	{
		codeChangeCategory = "Memory Management"
		testCaseType = "Performance Test"
	}
	newFile.append("\n")
	newFile.append("${currentHashcode}, ${firstCommit}, ${secondCommit}, ${p}, ${codeChangeCategory}, ${testCaseType}")
	//csv code end
	println "count below"+count
	       return count
}
