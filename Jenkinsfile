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
                script{
                    //def result = getCommitName()
                    //String s = result.toString()
                    //demo()
                    csv()
                          
            }
        }
    }
}
}

def csv()
{

    def newFile = new File("D:\\Test.csv")
    def exists = fileExists 'D:\\Test.csv'
    println exists

if (!exists) {


    //newFile.createNewFile()
    newFile.append("HashCode, Random HashCode 1, Random HashCode 2, Diff. between two commits, Code change category, Test case type,Total no. of test cases, No. of succeeed test, No. of failed tests, \n")
    
   // newFile.append("demo1, demo2 \n")
    
}
    
   /* newFile.append("demo3, demo4 \n")
    newFile.append("demo5, demo6 \n")
    newFile.append("demo7, demo8 \n")
    newFile.append("demo9, demo10 \n")
    newFile.append("demo11, demo12 \n")*/


def a = bat (script: '@git log -1 --pretty=%%H',returnStdout: true).trim()
def b = bat (script: '@git log -1 --pretty=%%H',returnStdout: true).trim()

newFile.append("${a}, ${b} \n")
  
}


def demo(){
    def commitCode = bat (script: 'git log --format=format:"%%H"', returnStdout: true).trim()
    String[] hashCode = null;

    hashCode = commitCode.split("\n")
    println Arrays.toString(hashCode)

    Random r = new Random()
    int a = r.nextInt(hashCode.size())
    println a
    int b = r.nextInt(hashCode.size())
    println b
    
    println "First hashcode"+hashCode[a+1] 
    println "Second hashcode"+hashCode[b+1]

    def firstCommit = hashCode[a+1]
    def secondCommit = hashCode[b+1]
 

    def result = bat (script: "@git diff $firstCommit $secondCommit",returnStdout: true).trim()
    println(result)

    String diff = result.toString().toLowerCase()
    println diff
    String[] diffArray = null;
	String[] keywords = ["Runtime", "New", "gc", "System"];
	      
	int count =0;
	         
	        
	        diffArray = diff.split(" ");
	        for(int i=0 ;i< diffArray.length ;i++) {
	        	for(int j=0 ;j < keywords.length ; j++ )
	        	{
	        	 if((diffArray[i].contains(keywords[j])))//|| (diffArray[i].startsWith(keywords[j])))
	        	{
	        		count++;
	        	}
	        }
	        }
	        if(count > 0) {
	         bat "mvn -Dsuite=PerformanceTests test"
                        post{
                            always{
                                junit "**/ /*target/surefire-reports/TEST-org.joda.time.TestAllPackages.xml"
                               // csv()
                                 }
                            }	
	        }
	        else{
                bat "mvn -Dsuite=FunctionalTests test"
                        post{
                            always{
                                junit "**/ /*target/surefire-reports/TEST-org.joda.time.TestAllPackages.xml"
                                //csv()
                                  }
                            }
            }

            return result
        		   
	         
	}
