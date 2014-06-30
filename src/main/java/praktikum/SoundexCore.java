
package praktikum;

/**
 * Class for quick calculating soundex codes from word strings
 * @author Franz Teichmann, mam09dgi@studserv.uni-leipzig.de
 * May 2014
 */
public class SoundexCore {

	/**
     * finds code for a single char
     * @return code char, "" for every non consonant
     */
	private String getCode(char c){
		  switch(c){
		    case 'B': case 'F': case 'P': case 'V':
		      return "1";
		    case 'C': case 'G': case 'J': case 'K':
		    case 'Q': case 'S': case 'X': case 'Z':
		      return "2";
		    case 'D': case 'T':
		      return "3";
		    case 'L':
		      return "4";
		    case 'M': case 'N':
		      return "5";
		    case 'R':
		      return "6";
		    default:
		      return "";
		  }
	}
 
	/**
	 * encoding a single string
	 * @param s - string
	 * @param length - length of the soundex code
	 * @return string - code string
	 */
	public String encode(String s, int length){
	  String code, previous, soundex;
	  s = s.toUpperCase();
	  code = s.charAt(0) + "";
	  previous = "7";
	  for(int i = 1;i < s.length() && code.length() < length;i++){
	    String current = getCode(s.charAt(i));
	    if(current.length() > 0 && !current.equals(previous)){
	      code = code + current;
	    }
	    previous = current;
	  }
	  soundex = (code + "0000").substring(0, length);
	  return soundex;
	}
}
