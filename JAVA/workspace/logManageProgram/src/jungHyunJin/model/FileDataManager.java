package jungHyunJin.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class FileDataManager implements DataProcessor, FileProcessor { // 다중구현
	
	// 멤버변수 캡슐화, 외부의 직접적인 접근을 제한하고 특정 메소드를 통해 접근가능하도록 함 → 개방폐쇄의 원칙
	private String inputFileName; // "input.log"
	private String outputFileName; // "정현진.log"
	private String searchCondition; // apikey, apiServiceID, webBrowser
	private String successUrl; // "http://apis.daum.net/search/"
	private int statusCode; // [성공 200, 페이지 없음 404, apiKey나 q가 파라미터에 없는 오류 10,]
	/*
	 * 200 : 정상 요청
	 * 404 : (Not Found) : 페이지 없음
	 * 10 : apikey나 q가 파라미터에 없는 오류
	 */
	
	public String getInputFileName() {
		return inputFileName;
	}
	
	/**
	 * @param inputFileName 입력 파일명
	 */
	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
	}
	
	public String getOutputFileName() {
		return outputFileName;
	}
	
	/**
	 * @param outputFileName 출력 파일명
	 */
	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	
	/**
	 * @param statusCode 성공 200, 페이지 없음 404, apiKey나 q가 파라미터에 없는 오류 10
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public String getSuccessUrl() {
		return successUrl;
	}
	
	/**
	 * @param successUrl 성공 URL, "http://apis.daum.net/search/"
	 */
	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}
	
	/**
	 * @return searchCondition [ API KEY || API Service ID || 웹브라우저 ]
	 */
	public String getSearchCondition() {
		return searchCondition;
	}
	
	/**
	 * @param searchCondition [ API KEY || API Service ID || 웹브라우저 ]
	 */
	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}

	
	
    // ===== 파일 관련 메소드 ===============================================================
	
	/**
	 * 이 메소드는 파일 데이터를 읽어옵니다.
	 * @param filePath 파일 경로
	 */
	@Override
	public List<String> inputFileStream(String filePath) {
//		System.out.println("[로그:정현진] FileDataProcessor, inputStream 들어옴");
//		System.out.println("[로그:정현진] filePath : "+filePath);

		List<String> fileDataList = new ArrayList<>();
		try {
			String[] fileData = null;
			String line = null;
			FileInputStream fileInputStream = new FileInputStream(filePath); // 파일 데이터를 바이트 단위로 변환하는 객체, 인코더
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream); // 바이트를 문자로 변환하는 객체, 디코더
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader); // 텍스트 읽어오는 객체
			
			while((line = bufferedReader.readLine()) != null) {
//				System.out.println("[로그:정현진] line : "+line); // 파일 로그 데이터
				fileData = line.split("[\\[\\]\\s]+"); 
//				System.out.println("[로그:정현진] fileData : "+Arrays.toString(fileData));
//				System.out.println("[로그:정현진] statusCode : "+this.statusCode);
				int statusCodeOfLog = Integer.parseInt(fileData[1]); // 문자열 비교는 인트형 비교보다 성능이 떨어지기에 int형으로 변환
//				System.out.println("[로그:정현진] 로그의 상태코드 : "+compareStatusCode);
				if(statusCodeOfLog!=this.statusCode) { // 사용자가 요청한 상태코드와 로그의 상태코드가 같지 않다면
					if(this.statusCode!=0) {
//						System.out.println("[로그:정현진] 상태코드 : "+this.statusCode+", 크롤링 취소");
						continue;
					}
//					System.out.println("[로그:정현진] 상태코드 : "+this.statusCode);
				}
//				System.out.println("[로그:정현진] 상태코드 : "+this.statusCode);
				fileDataList.add(line); // 사용자가 요청한 상태코드와 로그의 상태코드가 같을 경우
			} // while
			
			// 자원해제, 메모리 누수 방지
            bufferedReader.close();
            inputStreamReader.close(); 
			
		} catch (FileNotFoundException e) {
			System.out.println("[로그:정현진] 파일 찾기 실패");
		} catch (IOException e) {
			System.out.println("[로그:정현진] 파일 읽어오기 실패");
		}
//		System.out.println("[로그:정현진] fileDataList : "+fileDataList);
		return fileDataList;
	}
	
	/**
	 * 이 메소드는 텍스트 파일을 작성합니다.
	 * @param filePath 파일 경로
	 * @param text 작성할 내용
	 */
	@Override
	public boolean outputFileStream(String filePath,String text) { 
		// 매개변수 true 생략 시 덮어쓰기, true 추가 시 이어쓰기 실행
		try {
			OutputStream outputStream = new FileOutputStream(filePath, true);
			text += "\n\n";
			byte[] bytes = text.getBytes();
        	outputStream.write(bytes);
        	
        	outputStream.close(); // 메모리 누수 방지
        	
        } catch (IOException e) {
//        	System.err.println("파일 작성 에러 : " + e.getMessage());
        	return false;
        }
//		System.out.println("파일 작성 성공");
		return true;
	}
	
	/**
     * 이 메소드는 입력 파일의 경로를 반환합니다.
     * @param file 루트 디렉토리
     * @param inputFileName 입력 파일명
     */
    public String getFilePath(File file,String inputFileName) {
    	
        File inputFile = findFile(file, inputFileName);
//      System.out.println("[로그:정현진] inputFile : "+inputFile);
        
        String path="";
        if (inputFile != null) {
            path = parseRelativePath(inputFile.getAbsolutePath());
//          System.out.println("[로그:정현진] 파일 경로 : " + path);
        } 
//      System.out.println("[로그:정현진] "+inputFileName+" 파일 찾기 실패");
        return path;
    }
	
	/**
     * 이 메소드는 출력 파일의 경로를 반환합니다. (오버로딩)
     * @param file 루트 디렉토리
     * @param inputFileName 입력 파일명
     * @param outputFileName 출력 파일명
     */ 
    public String getFilePath(File file,String inputFileName,String outputFileName) {
    	/*
    	 * 오버로딩 이유
    	 * 인터페이스의 메소드 강제를 최소화 하기 위함
    	 * 입력파일 경로를 반환하는 함수와 출력파일 경로를 반환하는 함수 모두 경로를 반환한다는 동일한 목적을 띄고 있기에
    	 * 인터페이스를 간결하게 하면서 확장성을 고려했습니다.
    	 * 
    	 * 기본적으로 getFilePath함수는 시스템의 루트디렉토리를 기준으로 매개변수로 받아온 파일명을 재귀함수를 통해 찾게됩니다.
    	 * 이 때, 만약 출력파일이 시스템 어디에도 존재하지 않는다면 입력파일의 위치를 기준으로 
    	 * 경로를 가공하여 같은 폴더 내에 출력파일을 저장하기 위해 사용된 함수입니다.
    	 */
    	
//      System.out.println("[로그:정현진] getOutputFilePath 들어옴");
        File outputFilePath = findFile(file, outputFileName); // 재귀함수 findFile()
        
        String path="";
        if (outputFilePath == null) { // 
//        	System.out.println("[로그:정현진] "+outputFileName+" 파일 찾기 실패");
            String inputFilePath = getFilePath(file,inputFileName);
            if (inputFilePath.isEmpty()) {
//            	System.out.println("[로그:정현진] input.log 파일 찾기 실패");
            	return "";
            } 
        	int num = inputFilePath.lastIndexOf(inputFileName);
//        	System.out.println("[로그:정현진] inputFilePath : "+inputFilePath);
//        	System.out.println("[로그:정현진] num : "+num); // 15
        	path = inputFilePath.substring(0, num) + outputFileName; // src\fileModule\ + outputFileName
//        	System.out.println("[로그:정현진] outputFilePath : " + path);
        	return path;
        } 
//    	System.out.println("[로그:정현진] "+outputFileName+" 파일 찾음");
        path = parseRelativePath(outputFilePath.getAbsolutePath());
//      System.out.println("[로그:정현진] outputFilePath : " + path);
        return path;
    }
    
    // 파일 탐색 함수 (재귀함수 활용)
    /*
     * findFile함수 실행
     * → for문, /src루트 디렉토리부터 폴더 안 파일 탐색 시작
     * → if(data.isDirectory()) 탐색 할 파일이 폴더일 경우 true반환
     * → 재귀함수, 폴더 안 파일탐색 재실행
     * → if(data.isDirectory()) 탐색 할 파일이 파일일 경우 false반환
     * → else if(탐색한 파일의 절대경로에 매개변수로 받아온 fileName이 포함되어있다면) 418 라인, 재귀함수의 결과로 data 반환
     * → if(result != null) 파일이 존재한다면 해당 파일 최종적으로 반환
     */
    private File findFile(File file, String fileName) {
//    	System.out.println("[로그:정현진] findFile() 함수 들어옴");
        File[] fileList = file.listFiles(); // .src 하위 파일 데이터 반환
        if (fileList != null) { // 탐색할 파일이 존재한다면
            for(File data : fileList) { // 파일 탐색 시작
//            	System.out.println("[로그:정현진] @boolean : "+data.isDirectory());
                if(data.isDirectory()) { // 폴더인 경우 폴더 탐색 
//                	System.out.println("[로그:정현진] @@boolean : "+data.isDirectory());
//                	System.out.println("[로그:정현진] data : "+data);
                    File result = findFile(data, fileName); // 재귀 함수, 해당 폴더 안 파일 탐색
//                  System.out.println("[로그:정현진] @result : "+result); // null 또는 .\src\fileModule\output.log
                    if(result != null) { // 파일을 찾았다면
//                    	System.out.println("[로그:정현진] 파일 찾기 성공 result : "+result); //.\src\fileModule\output.log
                        return result; // 최종적으로 호출된 함수가 완료되는 구문
                    }
                }
                else if(data.getAbsolutePath().contains(fileName)) { // 파일인 경우 파일명 확인, 탐색한 파일의 절대경로가 찾으려는 파일의 이름을 가지고 있다면 파일찾기 성공, 해당 파일 반환
//                	System.out.println("[로그:정현진] data.getAbsolutePath() : "+data.getAbsolutePath());
//                	System.out.println("[로그:정현진] 파일 찾기 성공 data : "+data);
                    return data; // 418 라인, 재귀함수의 결과로 반환됨
                }
//              System.out.println("[로그:정현진] data != fileName 다음 반복 실행");
            }// for
        }// if (fileList != null)
        return null;
    }

    // 상대 경로 가공 함수
    private String parseRelativePath(String absolutePath) {
        int num = absolutePath.lastIndexOf("src");
        String forwardUrlParts = absolutePath.substring(0, num);
        return absolutePath.replace(forwardUrlParts, "");
    }
    
    
    
    //===== 데이터 관련 메소드 ===============================================================
	
	/**
	 * 이 메소드는 데이터 별 합계 수량을 반환합니다.
	 * @param dataList 데이터 리스트, 수량카운트
	 * @param duplicatedDataList 기준 데이터
	 */
	@Override
	public Map<String, Integer> getCountData(List<String> duplicatedDataList, List<String> dataList) {
		Map<String, Integer> dataCountMap = new HashMap<>();
	
        // duplicatedDataList에 포함된 각 data 값 0으로 초기화
        for (String data : duplicatedDataList) {
            dataCountMap.put(data, 0);
//          System.out.println("[로그:정현진] dataCountMap : "+dataCountMap);
            // {fwji=0, dcj8=0, e3ea=0, tr8j=0, anw1=0, 23jf=0, wejf=0, jg9k=0, 2jdc=0, fqwk=0}
        }

        // dataList를 순회하면서 각 data 값 +1
        for (String data : dataList) {
            if (dataCountMap.containsKey(data)) {
                int count = dataCountMap.get(data);
                dataCountMap.put(data, count + 1);
//              System.out.println("[로그:정현진] @@dataCountMap : "+dataCountMap);
                
            }
        }
//      System.out.println("[로그:정현진] @@@dataCountMap : "+dataCountMap);
        // {fwji=445, dcj8=473, e3ea=493, tr8j=467, anw1=475, 23jf=483, wejf=473, jg9k=474, 2jdc=488, fqwk=476}
        return dataCountMap;
	}

	
	/**
	 * 이 메소드는 파일데이터에서 원하는 정보를 반환합니다.
	 * @param fileData 파일데이터
	 * @param searchCondition 원하는 정보 [apikey, apiServiceID, ,webBrowser]
	 */
	@Override
	public List<String> parseData(List<String> fileData,String seachConditin) {
//		System.out.println("[로그:정현진] seachConditin : "+seachConditin);
		List<String> parseDatas=new ArrayList<String>();
		for (String data : fileData) {
//			System.out.println("[로그:정현진] data : "+data);
			String[] parts = data.split("\\]\\[");
//			System.out.println("[로그:정현진] parts : "+Arrays.toString(parts));
//			각 부분의 시작과 끝에 있는 대괄호([]) 제거 
			for (int i = 0; i < parts.length; i++) {
				parts[i] = parts[i].replaceAll("\\[", "").replaceAll("\\]", "");
			}
//			System.out.println("[로그:정현진] parts : "+Arrays.toString(parts));
			
			if(this.searchCondition.equals("웹브라우저")) {
				// seachCondition이 webBrowser일 경우
				parseDatas.add(parts[2]);
			}
			else { // webBrowser가 아니라는건 apikey 또는 apiServiceID 라는 것, 해당 값들은 url에 포함되어 있음
//				System.out.println("[로그:정현진] parts : "+Arrays.toString(parts));
				parseDatas.add(extractDataFromUrl(parts[1],this.searchCondition));
			}
		} // for
		
//		System.out.println("[로그:정현진] parseDatas : "+parseDatas);
		return parseDatas;
	}
	
	// dataCrawling 함수
	public String extractDataFromUrl(String url,String seachCondition) {
		try {
//		    System.out.println("[로그:정현진] url : "+url);
			URL apiUrl = new URL(url);
//			System.out.println("[로그:정현진] apiUrl"+apiUrl);
			String query = apiUrl.getQuery(); // URL에서 쿼리 부분 추출, 쿼리란 url에서 "?" 이후 부분을 의미함
//			String query = ""; // URL에서 쿼리 부분 추출, 쿼리란 url에서 "?" 이후 부분을 의미함
//		    System.out.println("[로그:정현진] query : "+query); // apikey=jg9k&q=daum
			if(seachCondition.replaceAll("\\s", "").toLowerCase().equals("apikey")) {
				if(query==null||query.isEmpty()) { // 쿼리 추출 실패시 인덱싱으로 apikey 찾기
					String apiUrlStr = apiUrl.toString();
//					System.out.println("[로그:정현진] apiUrlStr : "+apiUrlStr);
					int num = apiUrlStr.indexOf("apikey");
					apiUrlStr = apiUrlStr.substring(num);
//					System.out.println("[로그:정현진] apiUrlStr : "+apiUrlStr);
					query=apiUrlStr;
				}
				Map<String, String> params = parseQueryString(query); // 쿼리 파라미터 파싱, {q=daum, apikey=jg9k}
//				System.out.println("[로그:정현진] params : "+params);
				return params.get(seachCondition.replaceAll("\\s", "").toLowerCase()); // apikey 값 반환
			}
			else if(seachCondition.equals("API Service ID")) {
				
		        int prefixLength = this.successUrl.length();
//		        System.out.println("[로그:정현진] prefixLength : "+prefixLength); // 28

		        // url에서 successUrl부분 잘라내기
		        int startIndex = url.indexOf(this.successUrl);
//		        System.out.println("[로그:정현진] startIndex : "+startIndex); // 0
		        if (startIndex != -1) { // 문자열에 successUrl이 없을경우 -1이 반환됨
		            // serviceIdPrefix 이후의 부분을 가져오기
		            String apiServiceIDSubstring = url.substring(startIndex + prefixLength); // knowledge?apikey=fqwk&q=daum
//		            System.out.println("[로그:정현진] apiServiceIDSubstring : "+serviceIdSubstring);

		            // apiServiceIDSubstring에서 '?' 이전 부분 반환하기 => API Service ID
		            int endIndex = apiServiceIDSubstring.indexOf('?');
		            if (endIndex != -1) { // 문자열에 "?"가 없을 경우 -1이 반환됨
		                return apiServiceIDSubstring.substring(0, endIndex);
		            }
		        } // if(startIndex != -1)
			}// else if
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 쿼리스트링으로 부터 값 추출, apikey=jg9k&q=daum -> {q=daum, apikey=jg9k}
	public Map<String, String> parseQueryString(String queryString) {
        Map<String, String> paramMap = new TreeMap<>();
        if (queryString != null) { // 쿼리스트링이 존재한다면
            String[] params = queryString.split("&"); // & 제거
            for (String param : params) {
                String[] keyValue = param.split("="); // [apikey, dcj8], [q, daum]
//                System.out.println("[로그:정현진] @@keyValue : "+Arrays.toString(keyValue));
                if (keyValue.length == 2) {
                    paramMap.put(keyValue[0], keyValue[1]); 
//                  System.out.println("[로그:정현진] keyValue[0] : "+keyValue[0]); // apikey
//                  System.out.println("[로그:정현진] keyValue[1] : "+keyValue[1]); // 2jdc
//                  System.out.println("[로그:정현진] paramMap : "+paramMap); // {apikey=2jdc}
                }
            }// for
        }// if(queryString != null)
//		System.out.println("[로그:정현진] paramMap : "+paramMap);
        return paramMap;
    }
	
	
    /**
     * 이 메소드는 최다 호출된 데이터를 찾는 함수입니다.
     * @param dataAggregationMap Map타입의 집계 데이터
     */
    public List<String> getMostCalledDatas(Map<String, Integer> dataAggregationMap) {
    	// 최다 호출된 apiKey 값 찾는 함수, 값이 여러개 일 수 있으므로 List로 반환
        List<String> mostCalledData = new ArrayList<>();
        int maxCount = 0;
       
        // 맵을 순회하면서 최대값 찾기
        for (Map.Entry<String, Integer> entry : dataAggregationMap.entrySet()) {
            String key = entry.getKey();
//	        System.out.println("[로그:정현진] apiKey : "+apiKey);
            int count = entry.getValue();
//	        System.out.println("[로그:정현진] count : "+count);

            if (count > maxCount) { // 최대값 변경
                maxCount = count;
                mostCalledData.clear(); // 리스트 비우기
                mostCalledData.add(key); // 최대값 초기화
            }
            else if (count == maxCount) { // 최대값이 같은 경우 계속해서 추가
            	mostCalledData.add(key);
            }
        }
        return mostCalledData;
        /*
         * 참고) entrySet() 함수 설명
         * Map인터페이스에서 제공하는 
         * keySet() 함수와 values() 함수를 사용하는 경우, 
         * 반환되는 요소의 타입을 명시적으로 지정할 수 없기 때문에
         * Map.Entry<key,value> 인터페이스가 제공하는 entrySet() 함수를 사용하여 
         * key와 value 값 쌍의 타입을 명시적으로 지정하여 한번에 가져올 수 있도록 함
         * 결론적으로, Map의 요소를 효율적으로 관리하고 조작할 수 있음
         * 
         */
   }
    

    



}
