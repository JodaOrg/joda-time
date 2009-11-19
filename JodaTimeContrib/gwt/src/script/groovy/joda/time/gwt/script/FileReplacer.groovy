package joda.time.gwt.script

import java.io.*
import java.util.regex.*

class FileReplacer {
  
  // Replacements to perform. This is a list of pairs.
  // First element in the pair is the pattern to look for
  // Second element specifies the replacement. This can be either plain text or a Closure 
  // and works in the same way as documented in groovy's String.replaceAll(String regex, Closure closure)
  List replacements

  private String replaceText(text, replacement) {
    String pattern = replacement[0]
    def subst = replacement[1]          
    //Matcher matcher = (text =~ pattern)
    String outtext = text.replaceAll(pattern, subst)
    return outtext
  }
  
  private String replaceText(text) {
    String outtext = text
    replacements.each() { replacement -> outtext = replaceText(outtext, replacement) }
    return outtext
  }

  void replace(File file) {
    println("Replacing: ${file.absolutePath}")
    String text = file.getText()
    Writer out = new FileWriter(file)
    out << replaceText(text)
    out.close()
  }

}