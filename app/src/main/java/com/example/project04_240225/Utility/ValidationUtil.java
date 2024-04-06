package com.example.project04_240225.Utility;

import com.example.project04_240225.View.SignUpActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {

//    역할: 입력 데이터의 유효성을 검사하는 유틸리티 클래스입니다. 이메일 주소, 비밀번호, 사용자 이름 등의 형식과 요구사항을 검증합니다.
//            기능: 정규 표현식을 사용하여 입력 형식을 검사하고, 입력 데이터가 앱의 요구사항을 충족하는지 확인합니다. 유효하지 않은 데이터에 대해선 오류 메시지를 반환합니다.

//    private static final String USERNAME_PATTERN = "^[a-zA-Z][a-zA-Z0-9]{5,11}$";
//    private static final Pattern pattern = Pattern.compile(USERNAME_PATTERN);

    public static boolean id_validate(String username) {
        String USERNAME_PATTERN = "^[a-zA-Z][a-zA-Z0-9]{5,11}$";
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        // 조건(최소 6자 최대 12자, 알파벳 대소문자와 숫자만 허용, 첫 문자는 알파벳)
        return pattern.matcher(username).matches();
    }

    public boolean pw_validate(final String password) {

//        최소 하나의 숫자가 포함되어야 합니다 (?=.*[0-9]).
//        최소 하나의 소문자 알파벳이 포함되어야 합니다 (?=.*[a-z]).
//        최소 하나의 대문자 알파벳이 포함되어야 합니다 (?=.*[A-Z]).
//        최소 하나의 특수 문자가 포함되어야 합니다 (?=.*[@#$%^&+=!]).
//        공백을 포함할 수 없습니다 ((?=\S+$)).
 //       길이는 8자 이상 16자 이하여야 합니다 (.{8,16}).
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,16}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public boolean name_validate(final String input) {
        Pattern pattern;
        Matcher matcher;

        // 정규 표현식: 한글, 영문 대소문자만 허용. 특수기호와 공백 불가.
        final String INPUT_PATTERN = "^[가-힣a-zA-Z]+$";

        pattern = Pattern.compile(INPUT_PATTERN);
        matcher = pattern.matcher(input);

        return matcher.matches();
    }


//    private static final String PHONE_NUMBER_PATTERN = "^01([0|1|6|7|8|9])-(\\d{3,4})-(\\d{4})$";
    public boolean authData_validate(int type, String input) {
        String PHONE_NUMBER_PATTERN = "^01([0|1|6|7|8|9])(\\d{3,4})(\\d{4})$";
//        String PHONE_NUMBER_PATTERN = "^01([0|1|6|7|8|9])-(\\d{3,4})-(\\d{4})$";
        String EMAIL_PATTERN =
                "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        if(SignUpActivity.EMAIL == type){
            Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            Matcher matcher = pattern.matcher(input);
            return matcher.matches();

        }
        else if(SignUpActivity.PHONE == type){
            Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
            Matcher matcher = pattern.matcher(input);
            return matcher.matches();
        }
        else {
            return false;
        }

    }


}
