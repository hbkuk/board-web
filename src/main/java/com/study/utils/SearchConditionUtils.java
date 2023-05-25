package com.study.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Map;

/**
 * 매개변수 맵으로 전달된 검색조건(Search Condition)을 쿼리(Query) 또는 쿼리 스트링(Query String) 으로 변환하는 유틸 클래스
 */
@Slf4j
public class SearchConditionUtils {

    private static final String KEYWORD_CONDITION_QUERY = "b.title LIKE '%%%s%%' OR b.writer LIKE '%%%s%%' OR b.content LIKE '%%%s%%'";
    private static final String CATEGORY_CONDITION_QUERY = "c.category_idx = %d";
    private static final String START_DATE_CONDITION_QUERY = "DATE(b.regdate) >= '%s'";
    private static final String END_DATE_CONDITION_QUERY = "DATE(b.regdate) <= '%s'";

    private static final String START_DATE_PARAMETER_KEY = "start_date";
    private static final String END_DATE_PARAMETER_KEY ="end_date";
    private static final String KEYWORD_PARAMETER_KEY = "keyword";
    private static final String CATEGORY_IDX_PARAMETER_KEY = "category_idx";

    private static final int AFTER_FINAL_COMBINER_REMOVE_LENGTH = 5;

    private static final String[] SEARCH_CONDITIONS =
            {START_DATE_PARAMETER_KEY, END_DATE_PARAMETER_KEY,
                    KEYWORD_PARAMETER_KEY, CATEGORY_IDX_PARAMETER_KEY};

    /**
     * 매개변수 맵에서 쿼리 스트링(Query String)을 생성하여 StringBuilder 객체로 반환합니다.
     *
     * @param parameterMap 쿼리 스트링에 사용되는 매개변수 맵입니다.
     * @return StringBuilder 객체로 생성된 쿼리가 반환됩니다.
     */
    public static StringBuilder buildQueryString(Map<String, String[]> parameterMap) {
        StringBuilder queryBuilder = new StringBuilder();

        if (!hasLeastOneSearchCondition(parameterMap)) {
            return queryBuilder.append("");
        }

        if (isEmptyBothDate(parameterMap)) {
            isDateRange(parameterMap);
        }

        for (String key : parameterMap.keySet()) {
            log.debug("Build Query String Key : {} ", key);
            for( String searchCondition : SEARCH_CONDITIONS) {
                if( key.equals(searchCondition)) {
                    queryBuilder.append(key).append("=").append(parameterMap.get(key)[0]);
                    log.debug("Build Query String append : {} ", queryBuilder);
                    queryBuilder.append("&");
                    break;
                }
            }
        }
        queryBuilder.delete(queryBuilder.length()-1, queryBuilder.length());

        return queryBuilder;
    }

    /**
     * 매개변수 맵에서 쿼리(Query)를 생성하여 StringBuilder 객체로 반환합니다.
     *
     * @param parameterMap 쿼리 생성에 사용되는 매개변수 맵입니다.
     * @return StringBuilder 객체로 생성된 쿼리가 반환됩니다.
     */
    public static StringBuilder buildQueryCondition(Map<String, String[]> parameterMap) {
        StringBuilder queryBuilder = new StringBuilder();

        if (!hasLeastOneSearchCondition(parameterMap)) {
            return null;
        }

        if (isEmptyBothDate(parameterMap)) {
            isDateRange(parameterMap);
        }

        queryBuilder.append(" WHERE ");
        for (String key : parameterMap.keySet()) {

            if (isKeyWord(key)) {
                queryBuilder.append(
                        String.format(KEYWORD_CONDITION_QUERY,
                                parameterMap.get(key)[0], parameterMap.get(key)[0], parameterMap.get(key)[0]));
                queryBuilder.append(" AND ");
            }

            if (isCategory(key)) {
                queryBuilder.append(
                        String.format(CATEGORY_CONDITION_QUERY, Integer.parseInt(parameterMap.get(key)[0])));
                queryBuilder.append(" AND ");
            }

            if (isStartDate(key)) {
                queryBuilder.append(String.format(START_DATE_CONDITION_QUERY, parameterMap.get(key)[0]));
                queryBuilder.append(" AND ");
            }

            if (isEndDate(key)) {
                queryBuilder.append(String.format(END_DATE_CONDITION_QUERY, parameterMap.get(key)[0]));
                queryBuilder.append(" AND ");
            }
            log.debug("Query Builder Append" + queryBuilder);
        }
        queryBuilder.delete(queryBuilder.length() - AFTER_FINAL_COMBINER_REMOVE_LENGTH, queryBuilder.length());
        log.debug("Result Query Builder" + queryBuilder);
        return queryBuilder;
    }

    /**
     * 매개변수 맵이 적어도 하나의 검색 조건을 가지고 있다면 true를 반환하고, 그렇지 않은 경우 false를 반환합니다.
     *
     * @param parameterMap 쿼리 생성에 사용되는 매개변수 맵입니다.
     * @return 검색 조건을 가지고 있다면 true를 반환하고, 그렇지 않은 경우 false를 반환합니다.
     */
    private static boolean hasLeastOneSearchCondition(Map<String, String[]> parameterMap) {
        boolean isSearchCondition = false;

        for (String searchCondition : SEARCH_CONDITIONS) {
            if (parameterMap.get(searchCondition) != null && parameterMap.get(searchCondition)[0] != null) {
                isSearchCondition = true;
            }
        }
        return isSearchCondition;
    }

    /**
     * 주어진 키가 키워드를 나타내는 문자열인 경우 true를 반환하고, 그렇지 않은 경우 false를 반환합니다.
     *
     * @param key 매개변수의 키
     * @return 키가 키워드 문자열인 경우 true, 그렇지 않은 경우 false를 반환합니다.
     */
    private static boolean isKeyWord(String key) {
        return key.equals(KEYWORD_PARAMETER_KEY);
    }

    /**
     * 주어진 키가 카테고리를 나타내는 문자열인 경우 true를 반환하고, 그렇지 않은 경우 false를 반환합니다.
     * 
     * @param key 매개변수의 키
     * @return 키가 검색 문자열인 경우 true, 그렇지 않은 경우 false를 반환합니다.
     */
    private static boolean isCategory(String key) {
        return key.equals(CATEGORY_IDX_PARAMETER_KEY);
    }

    /**
     * 주어진 키가 시작 날짜 나타내는 문자열인 경우 true를 반환하고, 그렇지 않은 경우 false를 반환합니다.
     *
     * @param key 매개변수의 키
     * @return 키가 시작 날짜 문자열인 경우 true, 그렇지 않은 경우 false를 반환합니다.
     */
    private static boolean isStartDate(String key) {
        return key.equals(START_DATE_PARAMETER_KEY);
    }

    /**
     * 주어진 키가 종료 날짜 나타내는 문자열인 경우 true를 반환하고, 그렇지 않은 경우 false를 반환합니다.
     *
     * @param key 매개변수의 키
     * @return 키가 종료 날짜 문자열인 경우 true, 그렇지 않은 경우 false를 반환합니다.
     */
    private static boolean isEndDate(String key) {
        return key.equals(END_DATE_PARAMETER_KEY);
    }

    /**
     * 매개변수 맵이 시작일과 종료일을 가지는 경우 true를 반환하고, 그렇지 않은 경우 false를 반환합니다.
     *
     * @param parameterMap 쿼리 생성에 사용되는 매개변수 맵입니다.
     * @return 시작일과 종료일을 가지는 경우 true, 그렇지 않은 경우 false를 반환합니다.
     */
    private static boolean isEmptyBothDate(Map<String, String[]> parameterMap) {
        return parameterMap.get(START_DATE_PARAMETER_KEY) != null && parameterMap.get(END_DATE_PARAMETER_KEY) != null;
    }

    /**
     * 시작일이 종료일보다 큰 경우, 예외를 발생시킵니다.
     *
     * @param parameterMap 쿼리 생성에 사용되는 매개변수 맵입니다.
     */
    private static void isDateRange(Map<String, String[]> parameterMap) {
        LocalDate startDate = LocalDate.parse(parameterMap.get(START_DATE_PARAMETER_KEY)[0]);
        LocalDate endDate = LocalDate.parse(parameterMap.get(END_DATE_PARAMETER_KEY)[0]);
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작 날짜보다 종료 날짜가 클 수 없습니다.");
        }
    }
}
