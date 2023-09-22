package com.webnorm.prototypever1.util;

import com.webnorm.prototypever1.exception.exceptions.BusinessLogicException;
import com.webnorm.prototypever1.exception.exceptions.MemberException;

import java.util.regex.Pattern;

public class DataPatternMatcher {

    private String data;

    public static Boolean doesMatch(String data, DataPattern type) throws BusinessLogicException {
        switch (type) {
            case EMAIL:
                if (Pattern.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$", data))
                    return true;
                else throw new BusinessLogicException(MemberException.EMAIL_PATTERN_INCORRECT);
            case NAME :
                if (Pattern.matches("^[a-zA-Z]*$",data) || Pattern.matches("^[가-힣]*$", data))
                    return true;
                else throw new BusinessLogicException(MemberException.NAME_PATTERN_INCORRECT);
            case PASSWORD:
                if (Pattern.matches("^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", data))
                    return true;
                else throw new BusinessLogicException(MemberException.PASSWORD_PATTERN_INCORRECT);
            default:
                return false;
        }
    }
}
