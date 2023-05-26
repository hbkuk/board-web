package com.study.ebsoft.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 웹 브라우저가 전송한 파리미터에서 검색조건(Search Condition) 파라미터를 추출해서
 * SQL 쿼리(Query) 또는 쿼리 스트링(Query String) 으로 반환하는 유틸 클래스
 */
@Slf4j
public class SearchConditionUtils {

    /**
     * 파라미터 맵에서 검색조건에 해당하는 파라미터 이름(name)
     */
    private static final String START_DATE_PARAMETER_KEY = "start_date";
    private static final String END_DATE_PARAMETER_KEY = "end_date";
    private static final String CATEGORY_IDX_PARAMETER_KEY = "category_idx";
    private static final String KEYWORD_PARAMETER_KEY = "keyword";

    /**
     * 각각의 검색조건에 해당하는 SQL query Format
     */
    private static final String START_DATE_CONDITION_QUERY = "DATE(b.regdate) >= '%s'";
    private static final String END_DATE_CONDITION_QUERY = "DATE(b.regdate) <= '%s'";
    private static final String CATEGORY_CONDITION_QUERY = "c.category_idx = %d";
    private static final String KEYWORD_CONDITION_QUERY = "b.title LIKE '%%%s%%' OR b.writer LIKE '%%%s%%' OR b.content LIKE '%%%s%%'";

    private static final int REMOVE_LENGTH_WHEN_COMPLETE_QUERY = 5;
    private static final String[] SEARCH_CONDITIONS =
            {START_DATE_PARAMETER_KEY, END_DATE_PARAMETER_KEY,
                    KEYWORD_PARAMETER_KEY, CATEGORY_IDX_PARAMETER_KEY};
    
    private static final int DEFAULT_MINUS_YEARS = 1;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 파라미터 맵에서 쿼리 스트링(Query String)을 생성하여 문자열을 반환합니다.
     *
     * @param parameterMap 쿼리 스트링에 사용되는 매개변수 맵입니다.
     */
    public static String buildQueryString(Map<String, String[]> parameterMap) {
        StringBuilder queryBuilder = new StringBuilder();

        if (!hasLeastOneSearchCondition(parameterMap)) {
            return "";
        }

        if (isEmptyBothDate(parameterMap)) {
            isDateRange(parameterMap);
        }

        for (String key : parameterMap.keySet()) {
            log.debug("Build Query String Key : {} ", key);
            for (String searchCondition : SEARCH_CONDITIONS) {
                if (key.equals(searchCondition)) {
                    queryBuilder.append(key).append("=").append(parameterMap.get(key)[0]);
                    log.debug("Build Query String append : {} ", queryBuilder);
                    queryBuilder.append("&");
                    break;
                }
            }
        }
        queryBuilder.delete(queryBuilder.length() - 1, queryBuilder.length());

        return queryBuilder.toString();
    }

    /**
     * 파라미터 맵에서 쿼리(Query)를 생성하여 문자열을 반환합니다.
     *
     * @param parameterMap 쿼리 생성에 사용되는 매개변수 맵입니다.
     */
    public static String buildQueryCondition(Map<String, String[]> parameterMap) {
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
        queryBuilder.delete(queryBuilder.length() - REMOVE_LENGTH_WHEN_COMPLETE_QUERY, queryBuilder.length());
        log.debug("Result Query Builder" + queryBuilder);
        return queryBuilder.toString();
    }

    /**
     * 파라미터 맵이 적어도 하나의 검색 조건을 가지고 있다면 true를 반환하고, 그렇지 않은 경우 false를 반환합니다.
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
     * 파라미터 맵이 시작일과 종료일을 가지는 경우 true를 반환하고, 그렇지 않은 경우 false를 반환합니다.
     *
     * @param parameterMap 쿼리 생성에 사용되는 파라미터 맵입니다.
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

    /**
     * 요청 파라미터에 시작 날짜가 있는 경우 그 값을, 아니라면 현재 날짜를 {yyyy-mm-dd} 형태의 문자열로 반환한다.
     *
     * @param request 요청 정보를 담고있는 객체
     * @return 시작 날짜가 있는 경우 그 값을, 아니라면 현재 날짜를 반환
     */
    public static String hasParamStartDate(HttpServletRequest request) {
        LocalDate defaultStartDate = LocalDate.now().minusYears(DEFAULT_MINUS_YEARS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String startDate = defaultStartDate.format(formatter);

        if (request.getParameter(START_DATE_PARAMETER_KEY) != null) {
            startDate = request.getParameter(START_DATE_PARAMETER_KEY);
        }
        return startDate;
    }

    /**
     * 요청 파라미터에 종료 날짜가 있는 경우 그 값을, 아니라면 현재 날짜를 {yyyy-mm-dd} 형태의 문자열로 반환한다.
     *
     * @param request 요청 정보를 담고있는 객체
     * @return 종료 날짜가 있는 경우 그 값을, 아니라면 현재 날짜를 반환
     */
    public static String hasParamEndDate(HttpServletRequest request) {
        LocalDate defaultStartDate =  LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String end_date = defaultStartDate.format(formatter);

        if (request.getParameter(END_DATE_PARAMETER_KEY) != null) {
            end_date = request.getParameter(END_DATE_PARAMETER_KEY);
        }
        return end_date;
    }

    /**
     * 요청 파라미터에 검색 키워드가 있는 경우 그 값을, 아니라면 빈 문자열("")을 반환한다.
     * 
     * @param request 요청 정보를 담고있는 객체
     * @return 검색 키워드가 있는 경우 그 값을, 아니라면 빈 문자열("")을 반환
     */
    public static String hasParamKeyword(HttpServletRequest request) {
        if (request.getParameter(KEYWORD_PARAMETER_KEY) != null) {
            return request.getParameter(KEYWORD_PARAMETER_KEY);
        }
        return "";
    }

    /**
     * 요청 파라미터에 카테고리 번호가 있는 경우 그 값을, 아니라면 기본 번호(0)을 반환한다.
     *
     * @param request 요청 정보를 담고있는 객체
     * @return 카테고리 번호가 있는 경우 그 값을, 아니라면 기본 번호(0)을 반환
     */
    public static int hasParamCategoryIdx(HttpServletRequest request) {
        if (request.getParameter(CATEGORY_IDX_PARAMETER_KEY) != null) {
            return Integer.parseInt(request.getParameter(CATEGORY_IDX_PARAMETER_KEY));
        }
        return 0;
    }
}
