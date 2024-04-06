package com.example.project04_240225.Network.DTO;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ResponseData {

//    String response;

    JsonObject response;


    public JsonObject getJsonObject(){

        // 파싱할 JSON 문자열
//        String jsonString = "{\"name\":\"John Doe\", \"age\":30}";
//        response
        // JsonParser를 사용해 JSON 문자열을 JsonObject로 파싱
//        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
//
//        // JsonObject에서 데이터 읽기
////        String name = jsonObject.get("name").getAsString();
////        int age = jsonObject.get("age").getAsInt();
//
//
////        JsonObject jsonObject = new JsonObject();
//
////        if(response != null)
////            jsonObject.
//
//        return jsonObject;
        return response;

    }

}
