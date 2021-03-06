package cbr_debug;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 *  Case Based Reasoning : 사례 기반 추론 기법
 *  예) http://courses.ischool.berkeley.edu/i256/f06/projects/bonniejc.pdf
 * 	debug 완료 [*]
 * 
 */
public class CBR {
	
	protected Table table;
	
	public CBR() {
		init();
	}
	
	protected void init() {
		table = new Table();
		
		//test data set
		table.add("i want a kitten", "Can we put mitten on it");		//Can we put mitten on it
		table.add("i want food", "Me too, I'm hungry");			//Me too, I'm hungry
		table.add("they had good food at the restaurant", "what kind did they have?");	//what kind did they have?
		
		System.out.println(table.calc("i want kitten"));
		
		System.out.println(table.calc("i want food"));
		
		System.out.println(table.calc("they had good food at restaurant"));
		
		
		table.debugWeight();
		
		System.out.println("i want food");
		System.out.println(table.calc("i want food"));
		
		//test
		//System.out.println(table.weightMap.get("i")[1]);
		//개발 수정사항 weightMap value를 수정할수 있게 바꿔야됨. <완료>
	}
	
	public String feedString(String input){
		return table.calc(input);
	}
	
	public void addData(String in,String out){
		table.add(in, out);
	}
}

class Table {
	/*
	 * String				유저의 입력예
	 * ArrayList<Integer> 	연결되어있는 문장의 위치
	 */
	protected Map<String, ArrayList<Integer>> wordMap; 
	protected ArrayList<Sentence> sentence;
	
	protected Table() {
		wordMap = new HashMap<String, ArrayList<Integer>>();
		sentence = new ArrayList<Sentence>();
	}
	
	/**
	 * sentence
	 */
	protected void add(String in, String out){
		String[] words = in.split(" ");
//		for(String word : words){
//			//a 같은거 빼야됌 함수 새로만드는게좋은것같음
//		}
//		System.out.println();
		
		//단어 입력
		for(String word : words){
			if(wordMap.containsKey(word)){
				wordMap.get(word).add(sentence.size());
			}else{
				ArrayList<Integer> tmpList = new ArrayList<Integer>();
				tmpList.add(sentence.size()); //주소 입력
				wordMap.put(word, tmpList);
			}
		}
		
		sentence.add(new Sentence(out, words.length));
	}
	
	protected void debugWeight(){
		
		System.out.println(":::::::: sentence weight ::::::::");
		Iterator<Sentence> it1 = sentence.iterator();
		while(it1.hasNext()){
			System.out.println(it1.next().weight());
		}
		System.out.println();
		
		System.out.println(":::::::: word link ::::::::");
		for(String key : wordMap.keySet()){
			System.out.print(key+" ");
			for(int val : wordMap.get(key)){
				System.out.print(val+" ");
			}
			System.out.println();
		}
		System.out.println();
		
		
		
	}
	
	protected String calc(String input){
		/*
		 * *1번단어 (1/문장연결수) / (1/단어가 가르키는 문장의 문장연결수) + ... +
		 * *2번단어 ... +
		 */
		
		String[] words = input.split(" ");
		
		/*
		 * Integer 문장 index
		 * Float 최종 가중치 값
		 */
		Map<Integer, Float> tmpMap = new HashMap<Integer, Float>();
		
		for(String word : words){
			if(wordMap.containsKey(word)){ // 단어선언이 되어있을경우 
				for(int i=0;i<wordMap.get(word).size();i++){
					int sentenceNum = wordMap.get(word).get(i);
					//if(!tmpMap.containsKey(sentenceNum)){ // 해당 번호의 문장이 안나왔을경우
						
						float last = tmpMap.getOrDefault(sentenceNum, (float) 0.0);
						tmpMap.put(sentenceNum, last + 
								(sentence.get(sentenceNum).weight() / wordWeightSum(word)));
						System.out.println(tmpMap.toString());
					//}
				}//end for
			}
		}//end for
		
		float maxValue = Collections.max(tmpMap.values());
		
		for(int key : tmpMap.keySet()){
			
		}
		
		for(int key : tmpMap.keySet()){
			if(tmpMap.get(key) == maxValue){
				return sentence.get(key).getSentence();
			}
		}
		
		return "";
	}
	
	// 특정 단어에게 연결되어있는 문장의 가중치의 합 구하기
	protected float wordWeightSum(String word){
		
		Iterator<Integer> it = wordMap.get(word).iterator();
		
		float sum = 0;
		
		while(it.hasNext()){
			sum += sentence.get(it.next()).weight();
		}
		
		return sum;
	}
}

/**
 * output과 연결되어있는 단어수
 */
class Sentence {
	/**
	 * weight 	연결된 단어수
	 * sentence 결과 문장
	 */
	protected int wordNum;	
	protected String sentence; 
	
	protected Sentence(String sentence, int wordNum) {
		this.wordNum = wordNum;
		this.sentence = sentence;
	}
	
	protected String getSentence(){
		return sentence;
	}
	
	protected float weight(){
		return (float)1.0/wordNum;
	}
	
	protected void plusWeight(){
		wordNum++;
	}
}