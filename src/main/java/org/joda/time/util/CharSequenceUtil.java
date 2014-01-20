package org.joda.time.util;

public class CharSequenceUtil {

	public static boolean regionMatches(CharSequence first, boolean ignoreCase,
            int toffset,
            CharSequence other,
            int ooffset,
            int len){
		
		int counter=0;
		char charA,charB;
		try{
		if (ignoreCase){
		while(counter<len){
			charA=first.charAt(counter+toffset);
			charB=other.charAt(counter+ooffset);
			if (Character.toLowerCase(charA)!=Character.toLowerCase(charB)) return false; 
			if (Character.toUpperCase(charA)!=Character.toUpperCase(charB)) return false; 
		}
		}else{
			while(counter<len){
				charA=first.charAt(counter+toffset);
				charB=other.charAt(counter+ooffset);
				if (charA!=charB) return false; 
			}
		}
		}catch(IndexOutOfBoundsException ex){
			return false;
		}
		return true;		
		
	}
	
	
	 public static boolean startsWith(CharSequence first, CharSequence prefix, int toffset) {
			int to = toffset;
			int po = 0;
			int pc = prefix.length();
			if ((toffset < 0) || (toffset > first.length() - pc)) {
			    return false;
			}
			while (--pc >= 0) {
			    if (first.charAt(to++)!= prefix.charAt(po++)) {
			        return false;
			    }
			}
			return true;
		    }
	
	 public static boolean startsWith(CharSequence first, CharSequence prefix) {
			return startsWith(first,prefix,0);
		    }
	
	 
	 
	 public static final int parseInt(CharSequence s)
				throws NumberFormatException{
		 return parseInt(s,10,0,s.length());
	 }
	 
	 
	 public static int parseInt(CharSequence s, int radix, int start,int end)
				throws NumberFormatException
			    {
			        if (s == null) {
			            throw new NumberFormatException("null");
			        }

				if (radix < Character.MIN_RADIX) {
				    throw new NumberFormatException("radix " + radix +
								    " less than Character.MIN_RADIX");
				}

				if (radix > Character.MAX_RADIX) {
				    throw new NumberFormatException("radix " + radix +
								    " greater than Character.MAX_RADIX");
				}

				int result = 0;
				boolean negative = false;
				int i = start, max = end;
				int limit;
				int multmin;
				int digit;

				if (max > 0) {
				    if (s.charAt(0) == '-') {
					negative = true;
					limit = Integer.MIN_VALUE;
					i++;
				    } else {
					limit = -Integer.MAX_VALUE;
				    }
				    multmin = limit / radix;
				    if (i < max) {
					digit = Character.digit(s.charAt(i++),radix);
					if (digit < 0) {
					    throw new NumberFormatException(s.toString());
					} else {
					    result = -digit;
					}
				    }
				    while (i < max) {
					// Accumulating negatively avoids surprises near MAX_VALUE
					digit = Character.digit(s.charAt(i++),radix);
					if (digit < 0) {
					    throw new NumberFormatException(s.toString());
					}
					if (result < multmin) {
					    throw new NumberFormatException(s.toString());
					}
					result *= radix;
					if (result < limit + digit) {
					    throw new NumberFormatException(s.toString());
					}
					result -= digit;
				    }
				} else {
				    throw new NumberFormatException(s.toString());
				}
				if (negative) {
				    if (i > 1) {
					return result;
				    } else {	/* Only got "-" */
					throw new NumberFormatException(s.toString());
				    }
				} else {
				    return -result;
				}
			    }
	 
	 
}
