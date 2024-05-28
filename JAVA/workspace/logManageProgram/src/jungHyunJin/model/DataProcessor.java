package jungHyunJin.model;

import java.util.List;
import java.util.Map;

public interface DataProcessor {
	
	// 데이터 파싱 메소드
	List<String> parseData(List<String> lines, String searchCondition);

	// 데이터 카운트 메소드  
	Map<String,Integer> getCountData(List<String> dataList, List<String> duplicatedDataList);

}
