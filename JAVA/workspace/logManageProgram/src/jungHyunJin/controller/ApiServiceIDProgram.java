package jungHyunJin.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jungHyunJin.model.FileDataManager;


public class ApiServiceIDProgram extends FileDataManager {

	/**
	 * 프로그램을 실행합니다.
	 */
	public void start() {
//		System.out.println("[로그:정현진] ApiServiceIDProgram 들어옴");

		// 파일 찾기, 탐색할 루트 디렉토리 설정 → new File("./src")
		String rootDirectory = "./src";
		String filePath = getFilePath(new File(rootDirectory),getInputFileName()); // .\bin

		// 파일 데이터 크롤링
		List<String> fileDatas = inputFileStream(filePath);
		
		// apiServiceID 데이터 크롤링
		List<String> apiServiceIDList=new ArrayList<String>();
		apiServiceIDList = parseData(fileDatas,getSearchCondition());
//		System.out.println("[로그:정현진] apiServiceIDs : "+apiServiceIDs);
		
		// apiServiceID 중복제거 리스트 생성
		Set<String> duplicatedApiServiceIDSet = new HashSet<>(apiServiceIDList);
		List<String> duplicatedApiServiceIDList = new ArrayList<String>();
		for (String apiServiceID : duplicatedApiServiceIDSet) {
        	if(apiServiceID!=null) {
//        		System.out.print(apiServiceID+" "); // wejf anw1 23jf fwji tr8j 2jdc fqwk e3ea dcj8 jg9k
        		duplicatedApiServiceIDList.add(apiServiceID);
        	}
        }
		
		// apiServiceID 별 합계수량 계산하기
		Map<String, Integer> apiServiceIDMap = getCountData(duplicatedApiServiceIDList,apiServiceIDList);
//		System.out.println("\n[로그:정현진] apiServiceIDMap : "+apiServiceIDMap);
		
        // apiServiceIDMap 요청 수 내림차순 정렬, Java8 Stream API 활용
        List<Map.Entry<String, Integer>> sortedApiServiceIDMap = apiServiceIDMap.entrySet().stream() // Set 객체를 스트림으로 변환
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // Value값을 기준으로 스트림 요소 내림차순 정렬
                .collect(Collectors.toList()); // 정렬된 스트림을 리스트로 변환, List<Map.Entry<String,Integer>>
//      System.out.println("[로그:정현진] 호출순 sortedapiServiceIDMap : " + sortedApiServiceIDMap);
        
		// 작성할 텍스트 세팅, (호출 횟수 기준) 상위 3개 API Service ID와 각각의 요청 수
        String top3ApiServiceIDText = "상위 3개의 "+getSearchCondition()+"와 각각의 요청 수\n";
        int count = 0;
        int maxCount=3;
        for (Map.Entry<String, Integer> entry : sortedApiServiceIDMap) {
            if (count >= maxCount) {
                break;
            }
//          System.out.println("\n"+entry.getKey() + " : " + entry.getValue());
            top3ApiServiceIDText+="\n"+entry.getKey() + " : " + entry.getValue()+"\n";
            count++;
        }
        System.out.println(top3ApiServiceIDText+"\n");
        
        // 출력파일 경로 설정 후 텍스트 작성
	    String outputfilePath = getFilePath(new File(rootDirectory),getInputFileName(),getOutputFileName());
//	    System.out.println("[로그:정현진] outputfilePath : "+outputfilePath);
	    outputFileStream(outputfilePath,top3ApiServiceIDText);
	    
	}
}
