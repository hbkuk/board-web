package com.study.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class QueryUtilsTest {

    @Test
    @DisplayName("검색 검색조건에 해당하는 파라미터가 없을 경우")
    void notting_query_string() {
        Map<String, String[]> parameters = new HashMap<>();

        StringBuilder queryBuilder = QueryUtils.QueryConditionSetting(parameters);
        assertThat(queryBuilder.toString()).isEqualTo("");
    }

    @Test
    @DisplayName("시작 날짜만 가지고 있을 경우")
    void only_start_date() {
        Map<String, String[]> parameters = new HashMap<>();
        parameters.put("start_date", new String[]{"2023-05-11"});

        StringBuilder queryBuilder = QueryUtils.QueryConditionSetting(parameters);

        assertThat(queryBuilder.toString()).isEqualTo("start_date='2023-05-11'");
    }

    @Test
    @DisplayName("검색 조건만 가지고 있을 경우")
    void only_search_query() {
        Map<String, String[]> parameters = new LinkedHashMap<>();
        parameters.put("keyword", new String[]{"소프트웨어 장인"});

        StringBuilder queryBuilder = QueryUtils.QueryConditionSetting(parameters);

        assertThat(queryBuilder.toString()).isEqualTo("title LIKE '%소프트웨어 장인%' OR writer LIKE '%소프트웨어 장인%' OR content LIKE '%소프트웨어 장인%'");
    }

    @Test
    @DisplayName("시작 날짜와 종료 날짜만 가지고 있을 경우")
    void start_date_and_end_date() {
        Map<String, String[]> parameters = new LinkedHashMap<>();
        parameters.put("start_date", new String[]{"2023-05-11"});
        parameters.put("end_date", new String[]{"2023-05-20"});

        StringBuilder queryBuilder = QueryUtils.QueryConditionSetting(parameters);

        assertThat(queryBuilder.toString()).isEqualTo("start_date='2023-05-11' AND end_date='2023-05-20'");
    }

    @Test
    @DisplayName("종료 날짜와 시작 날짜를 가지고 있는 경우")
    void end_date_and_start_date() {
        Map<String, String[]> parameters = new LinkedHashMap<>();
        parameters.put("end_date", new String[]{"2023-05-20"});
        parameters.put("start_date", new String[]{"2023-05-11"});

        StringBuilder queryBuilder = QueryUtils.QueryConditionSetting(parameters);

        assertThat(queryBuilder.toString()).isEqualTo("end_date='2023-05-20' AND start_date='2023-05-11'");
    }


    @Test
    @DisplayName("종료 날짜와 검색 조건만 가지고 있는 경우")
    void start_date_and_end_date_and_search_query() {
        Map<String, String[]> parameters = new LinkedHashMap<>();
        parameters.put("end_date", new String[]{"2023-05-20"});
        parameters.put("keyword", new String[]{"소프트웨어 장인"});

        StringBuilder queryBuilder = QueryUtils.QueryConditionSetting(parameters);

        assertThat(queryBuilder.toString()).isEqualTo("end_date='2023-05-20' AND title LIKE '%소프트웨어 장인%' OR writer LIKE '%소프트웨어 장인%' OR content LIKE '%소프트웨어 장인%'");
    }

    @Test
    @DisplayName("시작 날짜보다 종료 날짜가 크다면 예외를 던진다")
    void throw_exception_when_start_date_bigger_then_end_date() {
        Map<String, String[]> parameters = new LinkedHashMap<>();
        parameters.put("end_date", new String[]{"2023-05-11"});
        parameters.put("start_date", new String[]{"2023-05-20"});

        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    QueryUtils.QueryConditionSetting(parameters);
                }).withMessageMatching("시작 날짜보다 종료 날짜가 클 수 없습니다.");
    }
}
