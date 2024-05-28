# LogManageProgram
로그관리 프로그램 

※ 문제해결 방법

1. 문제 의도 파악

	1) java 8 기능
		- 람다 표현식 
		- 스트림 API (병렬처리) 
		- 함수형 인터페이스 
		- java.time 패키지활용

	2) 코딩 컨벤션
		- 코드 블록구조를 명확히 함 
		- 네이밍 규칙 
-		 주석을 통해 코드의 의도와 작동방식을 명확히 함 
		- 단일 책임원칙 (1함수 1기능) 
		- 파일구조 

	3) 자료구조
		- Array, List, Map, Set

	4) 객체지향 설계능력
		- 캡슐화 (private)
		- 상속 (메소드강제)
		- 다형성  (오버로딩, 오버라이딩)
		- 추상화  (인터페이스, 추상클래스)
		- 객체지향 SOLID 원칙

2. 문제 분석 및 해석

	1) 문제별로 프로그램을 작성하시오. → 프로그램 3개가 필요함

		☆ 로그 파일 분석 결과를 파일로 출력하는 프로그램 구현
		- 문제1. 최다호출 APIKEY
			- 1. APIKEY 파싱 메소드 ☆ 
			- 2. APIKEY 카운트 메소드 ☆
			- 3. APIKEY 최다 호출 메소드
			- 4. 파일 입출력 ★ 
			- 5. 파일 경로 반환 ★

		- 문제2. 상위 3개의 API Service ID와 각각의 요청 수
			- 1. API Service ID 파싱 메소드 ☆
			- 2. API Service ID 카운트 메소드 ☆
			- 3. API Service ID 상위 3개 메소드
			- 4. 파일 입출력 ★
			- 5. 파일 경로 반환 ★

		- 문제3. 웹브라우저별 사용 비율
			- 1. 웹브라우저 파싱 메소드 ☆
			- 2. 웹브라우저 카운트 메소드 ☆
			- 3. 웹브라우저 사용 비율 메소드 
			- 4. 파일 입출력 ★
			- 5. 파일 경로 반환 ★
		
		☆(데이터), ★(파일) 중복되는 메소드  인터페이스 메소드 시그니처 선언 → 메소드 구현 강제
		(SOLID원칙, 인터페이스 분리 원칙 적용) 객체의 유연성 ↑, 확장성 ↑

			☆ DataProcessor (메소드 시그니처)
				- 데이터 파싱 : List<String> getParsingData(List<String> lines, String searchCondition);
				- 데이터 카운트 : Map<String,Integer> getCountData(List<String> dataList, List<String> uniqueDataList);

			★ FileProcessor (메소드 시그니처)
				- 파일 입력 : List<String> inputStream(String filePath);
				- 파일 출력 : boolean outputStream(String text, String filePath);		

		☆ 웹 로그 확인 (input)
		[200][http://apis.daum.net/search/knowledge?apikey=jg9k&q=daum][IE][2012-06-10 10:11:12]
		...


		
		☆ 출력 포맷 (output) - 예시
			☆ *호출 횟수는 상태코드가 200으로 정상인 경우에만 카운트
				최다호출 APIKEY 
				f83e
	
			상위 3개의 API Service ID와 각각의 *요청 수 
				blog : 1224
				vclip : 871
				image : 705

			웹브라우저별 사용 비율 : 정상 *요청인 경우, 요청 브라우저의 사용 비율
				IE : 60%
				Chrome : 20%
				Safari : 10%
				Firefox : 7%
				Opera : 3%
			
				☆ HTTP 응답 코드(StatusCode)
				10 : apikey나 q가 파라미터에 없는 오류
				200 : 성공	
				404 : 페이지 없음
				
3. 해결방법

	1) 문제 풀이
		
		문제 의도를 파악하고 분석한 내용을 토대로 인터페이스와 구현클래스 그리고 구현클래스를 상속받는 프로그램 클래스로 분류하고
           	가장 먼저 최다호출 1번 문제를 해결함

		☆ 1번 문제 해결방법 - 최다호출 APIKEY

			1. ApiKeyProgram 클래스에 문제를 해결하기 위한 로직을 순서대로 모두 작성 후 결과 확인
		 	2. 작성한 비즈니스 로직을 구현클래스(FileDataManager)로 모듈화 후 메소드 호출을 통해 프로그램 실행 및 결과 확인
			3. 내부 로직 설명
				1. 루트. 디렉토리(./src)를 설정하여 해당 폴더부터 재귀함수를 사용하여 입력 파일(input.log)의 경로를 찾습니다.
				2. 입력파일로부터 로그 데이터를 크롤링하여 List타입 변수에 데이터를 저장합니다. → fileData
				3. 조건(API KEY)에 따라 로그 데이터를 크롤링하여 원하는 데이터를 List타입 변수에 저장합니다. → apikeyList
				4. Set타입 변수를 선언하여 apikeyList에서 중복된 데이터를 제거 후 저장합니다. → duplicatedApikeySet		
				5. duplicatedApikeySet 데이터를 duplicatedApikeyList에 초기화합니다.
				6. duplicatedApikeyList(key)와 apikeyDatas(value)를 맵핑하여 apikey 별 호출 수 합계를 계산합니다. → apiKeyCountMap  
				7. apiKeyCountMap 데이터에서 value값이 가장 큰 key를 반환합니다. → mostCalledApiKeyList
				8. 최다호출 API KEY 텍스트를 초기화합니다. → mostCalledApiKeyText
				9. 재귀함수를 사용하여 출력경로를 반환합니다. → getFilePath(new File(rootDirectory),getInputFileName(),getOutputFileName())
				10. mostCalledApiKeyText를 출력합니다. → outputFileStream(outputfilePath,mostCalledApiKeyText)

		☆ 2번 문제 해결방법 - 상위 3개의 API Service ID와 각각의 요청 수

			1. ApiServiceIDProgram 클래스에 문제를 해결하기 위한 로직을 순서대로 모두 작성 후 결과 확인
			2. 1번과 중복되는 로직 메소드 호출 및 모듈화
			3. 내부 로직 설명
				1. ApiKeyProgram 1~6번까지 동일한 로직을 수행합니다. → apiServiceIDMap
				2. Java8버전부터 제공하는 Stream API를 활용하여 
				   List<Map.Entry<String, Integer>> 타입 변수에 정렬된 데이터를 초기화합니다. → sortedApiServiceIDMap
				3. for문을 활용하여 sortedApiServiceIDMap 데이터로 부터 상위 3개 데이터 key 와 value를
				   top3ApiServiceIDText 문자열로 작성합니다.
				4. 재귀함수를 사용하여 출력경로를 반환합니다. → getFilePath(new File(rootDirectory),getInputFileName(),getOutputFileName())
				5. top3ApiServiceIDText를  출력합니다. → outputFileStream(outputfilePath,top3ApiServiceIDText)


		☆ 3번 문제 해결방법
			1. 2번과 같은 방식으로 해결
			2. 내부 로직 설명
				1. ApiKeyProgram 1~6번까지 동일한 로직을 수행합니다. → webBrowserMap
				2. Java8버전부터 제공하는 Stream API를 활용하여 
				   List<Map.Entry<String, Integer>> 타입 변수에 정렬된 데이터를 초기화합니다. → sortedWebBrowserMap
				3. 웹브라우저의 합계수량을 계산합니다. → totalCount
				4. for문을 활용하여 sortedWebBrowserMap 데이터로부터 웹브라우저별 사용 비율을 계산하여 webBrowserRatioText에 저장합니다.
				5. 재귀함수를 사용하여 출력경로를 반환합니다. → getFilePath(new File(rootDirectory),getInputFileName(),getOutputFileName())
				6. webBrowserRatioText를 출력합니다. → outputFileStream(outputFilePath,webBrowserRatioText)


	2) 모든 프로그램의 흐름을 정리하고 중복되는 코드 제거 및 모듈화 그리고 하드코딩 제거

	3) 클라이언트의 요청에 따라 프로그램을 실행하기 위해 구현클래스(FileDataManager)에 멤버변수 선언 후 접근메소드 추가 → 캡슐화
		private String inputFileName;
		private String outputFileName;
		private int statusCode;
		private String successUrl;
		private String searchCondition;

	4) 최종적으로 프로그램을 실행하는 클라이언트에서 Java 8 버전부터 제공하는 함수형 인터페이스와 람다식을 사용하여 프로그램 실행
		


4. 설계
	
	1) 2개의 인터페이스를 상속받는 클래스 생성 -> FileDataManager (Model 역할, 비즈니스 로직 수행)
		☆ FileDataManager의 멤버변수
			- String inputFileName → "input.log"
			- String outputFileName → "정현진.log"
			- int statusCode →  200, 404, 10
			- String successUrl → "http://apis.daum.net/search/"
			- String searchCondition → apikey, apiServiceID, webBrowser

		☆ FileDataManager의 메소드
			- 멤버변수 접근자 (getter, setter)
			=== 파일관련 메소드 ===
			- inputFileStream(String filePath) → 입력파일(input.log) 데이터를 읽어옴, @Override
			- outputFileStream(String filePath,String text) → 출력파일(filePath)에 text 작성 @Override
			- getFilePath(File file,String inputFileName) → inputFileName의 경로 반환함수
			- getFilePath(File file,String inputFileName,String outputFileName) → outputFileName의 경로 반환함수
			=== 데이터관련 메소드 === 
			- parseData(List<String> fileData,String seachConditin) → 조건(API KEY, API Service ID, 웹브라우저)에 따라 파일데이터 반환  				- extractDataFromUrl(String url,String seachCondition) → 
	2) 각 문제별로 프로그램 생성(3문제) → 컨트롤러 역할

	3) 프로그램을 실행 할 메인 클래스 생성 → 클라이언트 역할


5. 모든 파일, 변수, 주요 메소드 정리

	인터페이스 : FileProcessor, DataProcessor
	함수형 인터페이스 : Startable
	클래스 : FileDataManager, ApiKeyProgram, ApiServiceIDProgram, WebBrowserProgram, Client
	파일 관련 메소드 : inputFileStream(), outputFileStream(), getFilePath(), findFile()
	데이터 관련 메소드 : parseData(), extractDataFromUrl(), parseQueryString(), getCountDatas(), getMostCalledDatas()
	멤버변수 : inputFileName, outputFileName, searchCondition, successUrl, statusCode
	지역변수 : rootDirectory, filePath
 	








