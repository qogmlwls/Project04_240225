package com.example.project04_240225;

public class Result {

    boolean result;
    String message;


    int messageColor;


    String user_id;

    public void setMessage(String message) {
        this.message = message;
    }

    public Result(boolean result, String message, int messageColor){
        this.result = result;
        this.message = message;
        this.messageColor = messageColor;
    }

    public boolean isSuccess(){
         return  result;
    }


    public String 결과메세지(){
        return message;
    }

    public int 메세지색상(){
            return messageColor;
    }

}
