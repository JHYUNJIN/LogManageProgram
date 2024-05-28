package jungHyunJin;

import jungHyunJin.controller.ApiKeyProgram;
import jungHyunJin.controller.ApiServiceIDProgram;
import jungHyunJin.controller.WebBrowserProgram;

public class Client {

	public static void main(String[] args) {
		
		final String INPUT_FILE_NAME = "input.log";
		String outputFileName = "정현진.log";

		final String API_KEY = "API KEY";
		final String API_SERVICE_ID = "API Service ID";
		final String WEB_BROWSER = "웹브라우저";
		final String SUCCESS_URL = "http://apis.daum.net/search/";
		
		int statusCode = 200;
		
		// java 8버전, 함수형 인터페이스 & 람다식 구현
		Startable app = () -> {
			
			// 최다 호출 APIKEY (호출 횟수는 상태코드가 200으로 정상인 경우에만 카운트 해야함) 
			ApiKeyProgram apiKeyProgram = new ApiKeyProgram();
			// 파일 I/O 설정
			apiKeyProgram.setInputFileName(INPUT_FILE_NAME);
			apiKeyProgram.setOutputFileName(outputFileName);
			// 로그 분석 데이터 설정
			apiKeyProgram.setSearchCondition(API_KEY);
			apiKeyProgram.setSuccessUrl(SUCCESS_URL);
			apiKeyProgram.setStatusCode(statusCode);
			// 실행
			apiKeyProgram.start();
			
			// 문제2. (호출 횟수 기준) 상위 3개의 API Service ID와 각각의 요청 수
			ApiServiceIDProgram apiServiceIDProgram = new ApiServiceIDProgram();
			apiServiceIDProgram.setInputFileName(INPUT_FILE_NAME);
			apiServiceIDProgram.setOutputFileName(outputFileName);
			apiServiceIDProgram.setSearchCondition(API_SERVICE_ID);
			apiServiceIDProgram.setSuccessUrl(SUCCESS_URL);
			apiServiceIDProgram.setStatusCode(statusCode);
			apiServiceIDProgram.start();
			
			// 문제3. 웹브라우저별 사용비율 (정상 요청인 경우, 요청 브라우저의 사용 비율)
			WebBrowserProgram webBrowserProgram = new WebBrowserProgram();
			webBrowserProgram.setInputFileName(INPUT_FILE_NAME);
			webBrowserProgram.setOutputFileName(outputFileName);
			webBrowserProgram.setSearchCondition(WEB_BROWSER);
			webBrowserProgram.setSuccessUrl(SUCCESS_URL);
			webBrowserProgram.setStatusCode(statusCode);
			webBrowserProgram.start();
        };

        // java 8버전, 함수형 인터페이스 메서드 호출
        app.start();

	}
}

