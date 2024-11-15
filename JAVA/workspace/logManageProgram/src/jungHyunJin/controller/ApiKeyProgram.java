package jungHyunJin.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jungHyunJin.model.FileDataManager;

public class ApiKeyProgram extends FileDataManager {
	
	/*
	 * FileDataManager를 상속받은 이유
	 * 확장성, Client에서 프로그램 실행하기 위함, 사용자가 요청할 데이터들은 클라이언트에서 설정가능
	 * 캡슐화, 클라이언트에서 원하는 정보만을 설정하여 프로그램을 실행 시킬 수 있음
	 * 해당 프로그램이 어떻게 구현되었는지 클라이언트는 알 수 없기 때문에 프로그램의 내부로직에 영향을 끼칠 수 없으므로 보안강화
	 */
	
	/**
	 * 프로그램을 실행합니다.
	 */
	public void start() {
		
		// 파일 찾기, 탐색할 루트 디렉토리 설정 → new File("./src")
		String rootDirectory = "./src";
		String filePath = getFilePath(new File(rootDirectory),getInputFileName()); // .\bin
		
		// 파일 데이터 크롤링
		List<String> fileDatas = inputFileStream(filePath);
		
		// apikey 데이터 크롤링
		List<String> apikeyList=new ArrayList<String>();
		apikeyList=parseData(fileDatas,getSearchCondition());
//		System.out.println("[로그:정현진] apikeyList : "+apikeyList);
	
		// apikey 중복제거 리스트 생성
		Set<String> duplicatedApikeySet = new HashSet<>(apikeyList);
//		System.out.println("[로그:정현진] duplicatedApikeyList : "+duplicatedApikeyList);
		List<String> duplicatedApikeyList = new ArrayList<>();
		for (String apiKey : duplicatedApikeySet) {
        	if(apiKey!=null) {
//        		System.out.print(apiKey+" "); // wejf anw1 23jf fwji tr8j 2jdc fqwk e3ea dcj8 jg9k
        		duplicatedApikeyList.add(apiKey);
        	}
        }
//		System.out.println("[로그:정현진] duplicatedApikeyList : "+duplicatedApikeyList);
		// apikey 별 합계수량 계산하기
		Map<String, Integer> apiKeyCountMap = getCountData(duplicatedApikeyList,apikeyList);
		// 최다 호출된 apiKey 값(들)을 구하기
        List<String> mostCalledApiKeyList = getMostCalledDatas(apiKeyCountMap);
        
        // 작성할 텍스트 세팅, 최다호출 API KEY
        String mostCalledApiKeyText = "최다호출 "+getSearchCondition()+"\n";
        for (String data : mostCalledApiKeyList) {
        	mostCalledApiKeyText+="\n"+data+"\n";
		}
        System.out.println(mostCalledApiKeyText+"\n");
        
        // 출력파일 경로 설정 후 텍스트 작성
	    String outputfilePath = getFilePath(new File(rootDirectory),getInputFileName(),getOutputFileName());
//	    System.out.println("[로그:정현진] outputfilePath : "+outputfilePath);
	    outputFileStream(outputfilePath,mostCalledApiKeyText);
        
	}
}

