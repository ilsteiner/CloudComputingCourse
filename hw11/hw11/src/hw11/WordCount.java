package hw11;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WordCount {
	public static void main(String[] args) {
		File file = new File(args[0]);
		
		ArrayList<Word> mappedList = map(file);
		
		ArrayList<Word> reducedList = reduce(mappedList);
		
		for(Word word : reducedList){
			System.out.println(word);
		}
	}
	
	private static ArrayList<Word> map(File file){
		ArrayList<Word> words = new ArrayList<Word>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null){
				line = line.replaceAll("[^a-zA-Z ]","").toLowerCase();
				for(String word : line.split("\\s+")){
					//I don't quite understand why it needs to be a String,1 matrix
					//This creates duplicate entries if there are duplicate words
					//If that wasn't a requirement I would use a Map construct here
					words.add(new Word(word,1));					
				}
			}
			
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return words;
	}
	
	private static ArrayList<Word> reduce(ArrayList<Word> wordCounts){
		HashMap<String,Integer> aggregateMap = new HashMap<>();
				
		for(Word word : wordCounts){
			if(aggregateMap.containsKey(word.word)){
				aggregateMap.put(word.word, aggregateMap.get(word.word) + word.count);
			}
			else{
				aggregateMap.put(word.word,word.count);
			}
		}
		
		ArrayList<Word> wordList = new ArrayList<Word>();
		
		for(Map.Entry<String, Integer> word : aggregateMap.entrySet()){
			wordList.add(new Word(word.getKey(),word.getValue()));
		}
		
		Collections.sort(wordList);
		Collections.reverse(wordList);
		
		return wordList;
	}
}
