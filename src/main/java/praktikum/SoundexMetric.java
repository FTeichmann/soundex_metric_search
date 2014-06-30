package praktikum;
import java.util.Map;

/**
 * Class implementing the soundex metric for usage within the semantic Web context * 
 * @author Franz Teichmann, mam09dgi@studserv.uni-leipzig.de
 * May 2014
 */
public class SoundexMetric {
	
	//length of the soundex code
	private int length;
	//trie data structure to store all values
	private SoundexTrie trie;
	//core object to calculate soundex codes
	private SoundexCore core;
	
	/**
	 * constructor
	 */
	public SoundexMetric(int codelength){
		length = codelength;
		trie = new SoundexTrie(codelength);
		core = new SoundexCore();
	}
	/**
	 * calculates Soundex Similarity of two codes via simple string comparison
	 */
	public float getSimpleSimilarity(String string1, String string2){
		float distance = 0;
        String code1 = core.encode(string1,length);
        String code2 = core.encode(string1,length);
		for(int i = 0; i< length; i++){
			if(code1.charAt(i) != code2.charAt(i)){
				distance = distance +1;
			}
		}
		return (1- distance / length);
	}
	
	/**
	 * adds a word string to the internal trie
	 * @param word - word string
	 */
	public void add(String word){
		trie.insert(core.encode(word,length),word);
	}
	/**
	 * searches for all words in the trie with a similarity measure > threshold
	 * @param word - word string
	 * @param threshhold - threshhold value 0<=t<=0
	 * @return Map - Map<word, similarity measure>
	 */
	public Map<String,Float> TrieSearch(String word,float threshhold){
		String code = core.encode(word,length);
		int possibleMismatches = (int) Math.floor(((1-threshhold)*length));
		//trie.printAll(trie.root, "");
		return trie.findSimilarity(code, possibleMismatches, trie.root, 0);
	}

}
