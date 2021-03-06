package kakao_parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cbr.TrainData;

public class KakaoParser {
	
	File file;
	ArrayList<String> list;
	BufferedReader in;
	String[] name;
	String date;
	
	
	public KakaoParser(String resourcesName) {
		try {
			file = new File("./resources/"+resourcesName+".txt");
			try {
				in = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
				list = new ArrayList<String>();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		init();
	}

	public void init() {
		String stmp = null; // 여러줄 문자열 처리를위해 2개로 (현제라인)
		String atmp = null; // (다음라인)
		try {
			name = in.readLine().split(", ");
			
			date = in.readLine();
			in.readLine(); //맨앞 3줄 제거
			atmp = in.readLine();
			while((stmp = in.readLine()) != null){
				/*
				 * replace list
				 * 날자 : --------------- 2017년 5월 8일 월요일 ---------------
				 */
				if(stmp.matches("")){
					continue;
				}else if(stmp.matches("--------------- [0-9]{4}년 [0-9]{1,2}월 [0-9]{1,2}일 [가-힣]{3} ---------------")){
					continue;
				}
				
				String[] split = stmp.split("[\\[\\]]");
				if(split.length < 4){
					atmp += stmp;
					continue;
				}else {
					atmp = split[4];
					list.add(atmp.trim());
				}
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> getList() {
		return list;
	}
}
