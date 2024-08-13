package com.study.security6.security.authorization.method.service;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class AnnotationParser {
    public String getDynamicValue(String[] paramNames, Object[] args, String annotationValue){
        return null;
    }

    public Map<String, Object> parseAnnotation(String annotationValue){
        Map<String, Object> map = new HashMap<>();
        StringTokenizer st = new StringTokenizer(annotationValue,"(");
        map.put("method", st.nextToken());
        String params = st.nextToken();
        params = params.substring(0, params.length()-1); // ) 제거
        st = new StringTokenizer(params, ",");
        while (st.hasMoreTokens()){
            map.put(st.nextToken(), null);
        }
        return map;
    }
}
