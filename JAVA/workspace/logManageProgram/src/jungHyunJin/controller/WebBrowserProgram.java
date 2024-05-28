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

public class WebBrowserProgram extends FileDataManager{
	
    /**
     * 프로그램을 실행합니다.
     */
    public void start() {
    	
		// 파일 찾기, 탐색할 루트 디렉토리 설정 → new File("./src")
    	String rootDirectory = "./src";
		String filePath = getFilePath(new File(rootDirectory),getInputFileName()); // .\bin, X
		
		// 파일 데이터 크롤링
		List<String> fileDatas = inputFileStream(filePath);
		
		// webBrowser 데이터 크롤링
		List<String> webBrowserList=new ArrayList<String>();
		webBrowserList = parseData(fileDatas,getSearchCondition());
//		System.out.println("[로그:정현진] webBrowsers : "+webBrowsers);
		
		// 중복제거 리스트 생성
		Set<String> duplicatedWebBrowserSet = new HashSet<>(webBrowserList); // 중복제거
		List<String> duplicatedWebBrowserList = new ArrayList<String>();
		for (String duplicatedWebBrowser : duplicatedWebBrowserSet) {
			if(duplicatedWebBrowser!=null) {
//				System.out.println("[로그:정현진] : "+duplicatedWebBrowser+" ");
				duplicatedWebBrowserList.add(duplicatedWebBrowser);
			}
		}
		
		// webBrowser 별 합계수량 계산하기
		Map<String, Integer> webBrowserMap = getCountData(duplicatedWebBrowserList,webBrowserList);
		
		// webBrowser 별 합계수량 내림차순 정렬
//	    List<Map.Entry<String, Integer>> sortedwebBrowserMap = new ArrayList<>(webBrowserMap.entrySet());
//	    sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
//	    System.out.println(sortedEntries);
	    
		// webBrowserMap 내림차순 정렬, Java8 Stream API 활용
		List<Map.Entry<String, Integer>> sortedWebBrowserMap = webBrowserMap.entrySet().stream() // Set객체를 스트림으로 변환
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // Value값을 기준으로 스트림 요소 내림차순 정렬
				.collect(Collectors.toList()); // 정렬된 스트림을 리스트로 변환, List<Map.Entry<String,Integer>>
//		System.out.println("[로그:정현진] sortedWebBrowserMap : "+sortedWebBrowserMap);
	    
		// value 합계 수량 계산하기
		int totalCount=0;
		for (Map.Entry<String, Integer> data : webBrowserMap.entrySet()) {
			totalCount+=data.getValue();
		}
		
	    // 정렬된 결과 출력
//	    String webBrowserRatioText="웹브라우저별 사용 비율\n";
		String webBrowserRatioText = getSearchCondition()+"별 사용 비율\n";
	    for (Map.Entry<String, Integer> entry : sortedWebBrowserMap) {
//	    	System.out.println("[로그:정현진] value : "+(double)entry.getValue()); // 4050
//	    	System.out.println("[로그:정현진] totalCount : "+totalCount); // 4747
//	        double webBrowserRatio = (double)entry.getValue() / totalCount * 100; // 0.85 * 100
//	        System.out.println("[로그:정현진] webBrowserRatio : "+webBrowserRatio); // 85.317...
//    	    System.out.println("\n"+entry.getKey() + " : " + Math.round(webBrowserRatio) + "%");
	        webBrowserRatioText+="\n"+entry.getKey() + " : " + Math.round((double)entry.getValue() / totalCount * 100) + "%\n"; // 반올림
	    }
	    System.out.println(webBrowserRatioText+"\n");
	
	    // 출력파일 경로 설정 후 텍스트 작성
	    String outputFilePath = getFilePath(new File(rootDirectory),getInputFileName(),getOutputFileName());
//	    System.out.println("[로그:정현진] outputfilePath : "+outputFilePath);
	    outputFileStream(outputFilePath,webBrowserRatioText);
	    
	}
}
