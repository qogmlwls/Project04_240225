package com.example.project04_240225.Network.DTO;

public class Error {


    public static final int 사용자_정보_가져오기_에러_코드 = 404; // 상수 이름은 좀 더 명확하게

    int code;
    String message;



    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
