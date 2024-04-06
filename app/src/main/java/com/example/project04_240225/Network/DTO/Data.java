package com.example.project04_240225.Network.DTO;

import com.google.gson.JsonObject;

public class Data{


//    static String RESPONSE="onResponse", FAILURE="onFailure";

    public boolean result;

    public String error_reason;
    public int error_code;

    public JsonObject response;


   public Data(boolean result,JsonObject data){

       this.result = result;
       response = data;

    }


//       responseCode = code;
//    public Data(String result, int code, String data){
//
//        this.result = result;
//        responseCode = code;
//        error_description = data;
//
//    }

    public Data(boolean result, String data){

        this.result = result;
        error_reason = data;
        response = null;

    }
    public Data(boolean result, String data, int code){

        this.result = result;
        error_reason = data;
        error_code = code;
        response = null;

    }
//
//    public boolean isSuccess(){
//
//        String name = response.get("result").getAsString();
//
//
//    }


}