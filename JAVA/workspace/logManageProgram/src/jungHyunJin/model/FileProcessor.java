package jungHyunJin.model;

import java.util.List;

public interface FileProcessor {

	// 파일 입력 메소드
	List<String> inputFileStream(String filePath);
	
	// 파일 출력 메소드
	boolean outputFileStream(String filePath, String text);
	
}
