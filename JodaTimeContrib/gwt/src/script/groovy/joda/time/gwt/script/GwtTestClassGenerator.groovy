package joda.time.gwt.script

import java.util.regex.*

public class GwtTestClassGenerator {
    
    AntBuilder ant = new AntBuilder()

    FileReplacer fileReplacer
    File branchTestRoot = new File("branch/test")
    File root = new File("src/test/gwt")

    //  (?s) enables the DOTALL setting making (.*?) also match newlines; 
    //  the last ? makes it non-greedy otherwise too much is removed
    private static final String ANYTHING_RELUCTANT = /((?s).*?)/
    private static final END_OF_METHOD = /(?m)^    \}/
    
    static Closure multiLineCommentReplacer = { Object[] it -> "/* Removed for GWT " + it[0] + " */"}
    static Closure plainMultiLineCommentReplacer = { Object[] it -> "/* " + it[0] + " */"}
    static Closure singleLineCommentReplacer = { Object[] it -> "// Removed for GWT " + it[0] }

    // Individual TestCases to remove. TestSuites don't need to be specified; they are removed by use of isSuiteFile.
    List removes = ["org.joda.time.tz.TestCompiler", "org.joda.time.format.TestDateTimeFormatStyle",
                    "org.joda.time.TestSerialization", "org.joda.time.chrono.gj.MainTest"]

    /**
     * Replacements to perform.
     * @see FileReplacer.replacements
     */
    List replacements = 
        [
         [Pattern.quote("//BEGIN GWT IGNORE") + ANYTHING_RELUCTANT + Pattern.quote("//END GWT IGNORE"), plainMultiLineCommentReplacer],
         
         [/extends TestCase/, {"extends JodaGwtTestCase"}],
         [/((protected)|(public)) void setUp\(\)(.*)\{/, "protected void gwtSetUp() throws Exception {\n        super.gwtSetUp();"],
         [/protected void tearDown\(\)(.*)\{/, "protected void gwtTearDown() throws Exception {\n        super.gwtTearDown();"],

         [/import junit.framework.TestCase;/, "import org.joda.time.gwt.JodaGwtTestCase;\nimport static org.joda.time.gwt.TestConstants.*;"],
         [/import junit.framework.TestSuite;/, "//import junit.framework.TestSuite;"],
         [/import java.lang.reflect.\w+;/, singleLineCommentReplacer],
         [/import java.security.\w+;/, singleLineCommentReplacer],
         [/import java.math.\w+;/, singleLineCommentReplacer],
         [/import java.io.FileOutputStream;/, singleLineCommentReplacer],
         [/import java.io.ByteArrayInputStream;/, singleLineCommentReplacer],
         [/import java.io.ByteArrayOutputStream;/, singleLineCommentReplacer],
         [/import java.io.FileInputStream;/, singleLineCommentReplacer],
         [/import java.text.DateFormat;/, singleLineCommentReplacer],
         [/import java.util.SimpleTimeZone;/, singleLineCommentReplacer],
         [/import org.joda.time.JodaTimePermission;/, singleLineCommentReplacer],
         
         [method(Pattern.quote("public static void main(String[] args) {")), multiLineCommentReplacer],
         [method(Pattern.quote("public static TestSuite suite() {")), multiLineCommentReplacer],
         [method(/public Test(\w*)\(String name\) \{/), multiLineCommentReplacer],
         
         [/private static final DateTimeZone /, singleLineCommentReplacer],
         [/private static final (\w*)Chronology/, singleLineCommentReplacer],
         [Pattern.quote("private static final DateTimeZone[] ZONES") + ANYTHING_RELUCTANT + Pattern.quote("};"), multiLineCommentReplacer],
         [Pattern.quote("private static final int OFFSET_PARIS = PARIS.getOffset"), singleLineCommentReplacer],
         [Pattern.quote("private static final int OFFSET_LONDON = LONDON.getOffset"), singleLineCommentReplacer],
         [Pattern.quote("private static final int OFFSET_MOSCOW = MOSCOW.getOffset"), singleLineCommentReplacer]
        ]
    
    
    static String method(String firstPartPattern) {
        firstPartPattern + ANYTHING_RELUCTANT + END_OF_METHOD
    }
    
    public GwtTestClassGenerator() {
        fileReplacer = new FileReplacer(replacements:replacements)
    }
             
    private boolean isSvn(File file) {
        file.absolutePath.contains(".svn")
    }
             
    private boolean isTestFile(File file) {
        return file.name.startsWith("Test")
    }
    
    private boolean isSuiteFile(File file) {
        return (file.name in ["TestAll.java","TestAllPackages.java"])
    }
    
    private void removeFiles() {
        removes.each() { className ->
            String fileName = root.absolutePath + File.separator + className.replaceAll('\\.', File.separator) + ".java"
            println("deleting $fileName")
            new File(fileName).delete()
        }
    }
    
    private copy() {
        ant.copy(todir:root.absolutePath, overwrite:true) {
            fileset(dir:branchTestRoot.absolutePath) { include(name:"**/*.java") }
        }
    }
    
    public void generate() {
        copy()
        removeFiles()
        root.eachFileRecurse() {file ->
            if (!isSvn(file)) {
                if (isSuiteFile(file)) { file.delete() }
                else if (isTestFile(file)) { fileReplacer.replace(file) }
            }
        }
    }
    
	static void main(String[] args) {
	    new GwtTestClassGenerator().generate()
	}
}
