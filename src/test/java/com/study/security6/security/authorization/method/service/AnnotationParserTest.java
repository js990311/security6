package com.study.security6.security.authorization.method.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationParserTest {

    @ParameterizedTest
    @MethodSource("provideSample")
    void parseAnnotation(Map<String, Object> data) {
        AnnotationParser annotationParser = new AnnotationParser();
        Map<String, Object> maps = annotationParser.parseAnnotation((String) data.get("problem"));
        maps.put("problem", data.get("problem"));
        for(String key : data.keySet()){
            assertTrue(maps.containsKey(key));
            assertEquals(data.get(key), maps.get(key));
        }
    }

    static Stream<Map<String, Object>> provideSample(){
        List<Map<String, Object>> maps = new ArrayList<>();
        maps.add(makeSampleMap("create", "boardId"));
        maps.add(makeSampleMap("update", "boardId", "userId"));
        maps.add(makeSampleMap("delete", "userId", "boardId", "commentId"));
        maps.add(makeSampleMap("read"));
        return maps.stream();
    }

    static Map<String, Object> makeSampleMap(String... strings){
        Map<String , Object> map = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<strings.length; i++){
            if(i==0){
                sb.append(strings[i]).append("(");
                map.put("method", strings[i]);
            }else {
                map.put(strings[i], null);
                if(i==1){
                    sb.append(strings[i]);
                }else {
                    sb.append(",").append(strings[i]);
                }
            }
        }
        sb.append(")");
        map.put("problem", sb.toString());
        return map;
    }

}