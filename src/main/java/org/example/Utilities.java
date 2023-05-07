package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Utilities {

    public String ConvertMapToJson(Map<String, Object> map) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(map);
    }

    public String ReqBodyToByteArray(InputStream requestBody) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead = -1;
        while ((bytesRead = requestBody.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }

        String responseBodyString = baos.toString(StandardCharsets.UTF_8);

        return responseBodyString;
    }

    public Map QueryParamsToMap(String query) {
        // parsing query parameters
        Map<String, String> queryParamsMap = new HashMap<>();
        if (query != null) {
            String[] queryParams = query.split("&");
            for (String param : queryParams) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    queryParamsMap.put(keyValue[0], keyValue[1]);
                }
            }
        }

        return queryParamsMap;
    }

    public Map QueryHeadersToMap(Headers requestHeaders) {
        Map<String, String> headerParamsMap = new HashMap<>();
        for (String header : requestHeaders.keySet()) {
            headerParamsMap.put(header, requestHeaders.get(header).get(0));
        }

        return headerParamsMap;
    }

    public String GetJSONResponse(String requestMethod, Headers requestHeaders, String responseBodyString, String query, String path) throws JsonProcessingException {
        // Mapping each element to a key
        Map<String, Object> parsedBodyMap = new HashMap<>();

        // parsing header parameters
        Map headerParamsMap = QueryHeadersToMap(requestHeaders);

        // parsing query parameters
        Map queryParamsMap = QueryParamsToMap(query);

        // adding elements to parsedBodyMap
        parsedBodyMap.put("request method", requestMethod);
        parsedBodyMap.put("request body", responseBodyString);
        parsedBodyMap.put("header params", headerParamsMap);
        parsedBodyMap.put("query params", queryParamsMap);
        parsedBodyMap.put("query path", path);

        // converting parsedBodyMap to JSON string
        String jsonStr = ConvertMapToJson(parsedBodyMap);

        return jsonStr;
    }
}
