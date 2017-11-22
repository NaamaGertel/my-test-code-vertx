package test.project1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class WordsDB {
	// all words will be saved in an HashMap - each entry will be <word, numerical value>
	private HashMap<String, Integer> allWords;

	/**
	 * Constructor
	 */
	public WordsDB(){
		allWords = new HashMap<String, Integer>();
	}

	/**
	 * The method receives a word and return the closest word in term of total character value
	 * @param currWord
	 * @return
	 */
	public String findValue(String currWord){

		//check if the database is empty
		if(allWords.isEmpty())
			return null;

		else{
			//check if the words already exists in the database
			if (allWords.containsKey(currWord))
				return currWord;

			else{
				int val = calcNumericVal(currWord);
				//sort the the database by Value 
				Collection<Integer> allValues = allWords.values();
				List<Integer> lst = new ArrayList<Integer>(allValues);
				Collections.sort(lst);
				Iterator<Integer> itr = lst.iterator();
				int dif = Integer.MAX_VALUE;
				int saveVal = 0;

				while (itr.hasNext()) {
					Integer currVal = itr.next();

					// find the numerical difference between 2 words
					int res = val - currVal.intValue();
					if(res < dif && res > 0){
						dif = res;
						saveVal = currVal;
					}
					else{
						// check if there is a closer word and stop iterating
						if (res < 0 && -res < dif){
							saveVal = currVal;
							break;
						}
					}
				}
				// find the matching key (word) and return it
				for (Entry<String, Integer> entry : allWords.entrySet()) {
					if (entry.getValue().equals(saveVal)) 
						return(entry.getKey());
				}

			}
		}
		return null;

	}
	
	/**
	 * The method receives a word and return the closest word in term of lexical closeness
	 * @param currWord
	 * @return
	 */
	public String findLexical(String currWord) {
		String lexicalWord = null;

		//check if the database is empty
		if(allWords.isEmpty())
			return lexicalWord;

		else{
			//check if the words already exists in the database
			if (allWords.containsKey(currWord))
				return currWord;

			else{
				// sort the database by key
				List<String> mapKeys = new ArrayList<>(allWords.keySet());
				Collections.sort(mapKeys);
				Iterator<String> itr = mapKeys.iterator();
				int prev = Integer.MIN_VALUE;

				while(itr.hasNext()){
					String word = itr.next();
					int comperRes = currWord.compareTo(word);
					if(comperRes > 0){
						prev = comperRes;
						lexicalWord = word;
					}
					else{
						// if a closer word was found, save it and stop iterating
						if(comperRes < -prev){
							lexicalWord = word;
							break;
						}
						break;
					}
				}

			}
		}
		return lexicalWord;
	}

	/**
	 * This method adds a new word to the database
	 * @param currWord
	 */
	public void add (String currWord){
		allWords.put(currWord, calcNumericVal(currWord));
	}

	/**
	 * The method calculates the numerical value of a given word and returns it
	 * Assumption: a=1, b=2... and ASCII values are a=97, b=98..
	 * @param currWord
	 * @return
	 */
	private int calcNumericVal (String currWord){
		int numValue = 0;
		String lowerWord = currWord.toLowerCase();
		
		for(int i = 0; i < lowerWord.length(); i++){
			char currLetter = lowerWord.charAt(i);
			int letterVal = ((int) currLetter) - 96;
			numValue += letterVal;
		}
		/*String lowerWord = currWord.toLowerCase();
		char[] wordArr = lowerWord.toCharArray();

		for(int i = 0; i< wordArr.length; i++){
			int letterVal = (int) wordArr[i] - 96;
			numValue += letterVal;
		}*/

		return numValue;
	}

}
