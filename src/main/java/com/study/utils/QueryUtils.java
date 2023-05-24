package com.study.utils;

import java.time.LocalDate;
import java.util.Map;

/**
 * Map<String, String[]> 형식의 파라미터를 query로 변환하는 유틸 클래스
 */
public class QueryUtils {

    private static final String KEYWORD_CONDITION_QUERY = "title LIKE '%%%s%%' OR writer LIKE '%%%s%%' OR content LIKE '%%%s%%'";
    private static final String OTHER_CONDITION_QUERY = "%s='%s'";
    private static final String QUERY_COMBINED_AND_WITH_SPACE = " AND ";
    private static final String START_DATE_PARAMETER_KEY = "start_date";
    private static final String END_DATE_PARAMETER_KEY = "end_date";
    private static final String KEYWORD_PARAMETER_KEY = "keyword";
    private static final String CATEGORY_IDX_PARAMETER_KEY = "category_idx";
    private static final int AFTER_FINAL_COMBINER_REMOVE_LENGTH = 5;
    private static final String[] SEARCH_CONDITIONS =
            {START_DATE_PARAMETER_KEY, END_DATE_PARAMETER_KEY,
                    END_DATE_PARAMETER_KEY, KEYWORD_PARAMETER_KEY, CATEGORY_IDX_PARAMETER_KEY};

    /**
     * 매개변수 맵에서 쿼리를 생성하여 StringBuilder 객체로 반환합니다.
     *
     * @param queryMap 쿼리 생성에 사용되는 매개변수 맵입니다.
     * @return StringBuilder 객체로 생성된 쿼리가 반환됩니다.
     */
    public static StringBuilder QueryConditionSetting(Map<String, String[]> queryMap) {
        StringBuilder queryBuilder = new StringBuilder();

        if(!hasLeastOneSearchCondition(queryMap)) {
            return queryBuilder.append("");
        }

        if (isEmptyBothDate(queryMap)) {
            isDateRange(queryMap);
        }
        for (String key : queryMap.keySet()) {

            if (isKeyWordCondition(key)) {
                queryBuilder.append(
                        String.format(KEYWORD_CONDITION_QUERY,
                                queryMap.get(key)[0], queryMap.get(key)[0], queryMap.get(key)[0]));
            }

            if (!isKeyWordCondition(key)) {
                queryBuilder.append(
                        String.format(OTHER_CONDITION_QUERY, key, queryMap.get(key)[0]));
            }

            queryBuilder.append(QUERY_COMBINED_AND_WITH_SPACE);
        }
        queryBuilder.delete(queryBuilder.length()- AFTER_FINAL_COMBINER_REMOVE_LENGTH, queryBuilder.length());
        return queryBuilder;
    }

    /**
     * 매개변수 맵이 적어도 하나의 검색 조건을 가지고 있다면 true를 반환하고, 그렇지 않은 경우 false를 반환합니다.
     * @param queryMap 쿼리 생성에 사용되는 매개변수 맵입니다.
     * @return 검색 조건을 가지고 있다면 true를 반환하고, 그렇지 않은 경우 false를 반환합니다.
     */
    private static boolean hasLeastOneSearchCondition(Map<String, String[]> queryMap) {
        boolean isSearchCondition = false;

        for (String searchCondition : SEARCH_CONDITIONS) {
            if (queryMap.get(searchCondition) != null && queryMap.get(searchCondition)[0] != null) {
                isSearchCondition = true;
            }
        }
        return isSearchCondition;
    }

    /**
     * 주어진 키가 키워드 조건을 나타내는 문자열인 경우 true를 반환하고, 그렇지 않은 경우 false를 반환합니다.
     *
     * @param key 매개변수의 키
     * @return 키가 검색 조건인 경우 true, 그렇지 않은 경우 false를 반환합니다.
     */
    private static boolean isKeyWordCondition(String key) {
        return key.equals(KEYWORD_PARAMETER_KEY);
    }

    /**
     * 매개변수 맵이 시작일과 종료일을 가지는 경우 true를 반환하고, 그렇지 않은 경우 false를 반환합니다.
     *
     * @param parameters 쿼리 생성에 사용되는 매개변수 맵입니다.
     * @return 시작일과 종료일을 가지는 경우 true, 그렇지 않은 경우 false를 반환합니다.
     */
    private static boolean isEmptyBothDate(Map<String, String[]> parameters) {
        return parameters.get(START_DATE_PARAMETER_KEY) != null && parameters.get(END_DATE_PARAMETER_KEY) != null;
    }

    /**
     * 시작일이 종료일보다 큰 경우, 예외를 발생시킵니다.
     *
     * @param parameters 쿼리 생성에 사용되는 매개변수 맵입니다.
     */
    private static void isDateRange(Map<String, String[]> parameters) {
        LocalDate startDate = LocalDate.parse(parameters.get(START_DATE_PARAMETER_KEY)[0]);
        LocalDate endDate = LocalDate.parse(parameters.get(END_DATE_PARAMETER_KEY)[0]);
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작 날짜보다 종료 날짜가 클 수 없습니다.");
        }
    }
}
