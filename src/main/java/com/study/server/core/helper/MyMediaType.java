package com.study.server.core.helper;

import org.springframework.http.MediaType;

public class MyMediaType {

    public static final String UTF_8 = "charset=UTF-8";

    public static final String APPLICATION_JSON_VALUE = MediaType.APPLICATION_JSON_VALUE + ";" + UTF_8;

}
