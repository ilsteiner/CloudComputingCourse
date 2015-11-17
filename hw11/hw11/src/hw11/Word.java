package hw11;

public class Word implements Comparable<Word>{
	String word;
	int count;
	
	public Word(String word, int count) {
		this.word = word;
		this.count = count;
	}
	
	public Word(String word){
		this.word = word;
		this.count = 1;
	}

	@Override
	public int compareTo(Word otherWord) {
		if(this.count < otherWord.count){
			return -1;
		}
		else if(this.count > otherWord.count){
			return 1;
		}
		else{
			return 0;
		}
	}

	@Override
	public String toString() {
		return word + ": " + count;
	}
}
