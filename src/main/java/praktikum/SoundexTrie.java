package praktikum;
import java.util.*;

/**
 * Class for a Trie datastructure to store strings according to their soundex code
 * 
 * all codes need to have the same length
 * @author Franz Teichmann, mam09dgi@studserv.uni-leipzig.de
 * May 2014
 */
public class SoundexTrie {
	//length of the Soundex code
	private float length;
	
	/**
	 * constructor class
	 * @param codelength - length of the Soundex code
	 */
	public SoundexTrie(int codelength){
		length = codelength;
	}

  /*
   * Helperclass for TrieNodes
   * every node is a treemap of a char and a treemap
   */
  static class TrieNode {
    Map<Character, TrieNode> children = new TreeMap<Character, TrieNode>();
    //every leaf is a set of strings represented by the soundex code pointing to that leaf
    Set<String> leaf = new HashSet<String>();//allows several words on one leaf
  }

  TrieNode root = new TrieNode();//root-node

  /** inserts a tupel (soundexcode,word) into the trie
   *
   */
  void insert(String code, String word) {
	  TrieNode v = root;
	  for (char c : code.toCharArray()) {
		  //does a node c already exist?
		  if (!v.children.containsKey(c)) {
			  v.children.put(c, new TrieNode());
		  }
		  v = v.children.get(c);
	  }
	  v.leaf.add(word);
  }
  /**
   * Similarity Search: iterates over all nodes, returns set of Strings
   * @param code - soundex code 
   * @param threshhold - int value of how many code characters can be different
   * @param node -  node from where the search starts, default trie.root
   * @param distance - edit distance of the current node, default 0
   * @return Map<String,Float> - mapping return words and similarity measure within threshhold
   */
  public Map<String,Float> findSimilarity(String code,int threshhold,TrieNode node,int distance){
	  Map<String,Float> result = new HashMap<String,Float>();
	  if(threshhold>0){
		  for(Map.Entry<Character,TrieNode> nodeX : node.children.entrySet()){
			  if(nodeX.getKey() == code.charAt(0)){
				  result.putAll((findSimilarity(code.substring(1),threshhold,nodeX.getValue(),distance)));
			  }else{
				  result.putAll(findSimilarity(code.substring(1),threshhold-1,nodeX.getValue(),distance+1));
			  }
		  }
	  }else{
		  for(Map.Entry<Character,TrieNode> nodeX : node.children.entrySet()){
			  if(nodeX.getKey() == code.charAt(0)){
				  result.putAll((findSimilarity(code.substring(1),0,nodeX.getValue(),distance)));
			  }
		  }
	  }
	  //we reached the bottom leafs now
	  for(String word :node.leaf){
		  result.put(word,(1- distance / length));
	  }
	  return result;
  }
  /**
   * function for printing out the whole trie
   * @param node: node from where to start printing
   * @param s
   */
  public void printAll(TrieNode node, String s) {
	    for (Character ch : node.children.keySet()) {
	      printAll(node.children.get(ch), s + ch);
	    }
	    if (!node.leaf.isEmpty())
	      System.out.println(s + " - "+node.leaf.toString());
	  }
}