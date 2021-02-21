package com.rafaelwillen.util;

public class Validator {

    /**
     * Regex for email validation. Email like: name123@domain.foo
     */
    private static final String EMAIL_REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    /**
     * Regex for phone number validation. Accepts a string with 9 numbers
     */
    private static final String PHONE_NUMBER_REGEX = "\\d{9}";
    /**
     * Regex for username ad email validation. Accepts a string with more than 7 characters
     */
    private static final String USERNAME_PASSWORD_REGEX = "^\\w{8,}";
    private static final String DECIMAL_NUMBER_REGEX = "^\\d*(,\\d{1,2})?$";

    private Validator() {
    }

    public static boolean isEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

    public static boolean isPhoneNumber(String phone) {
        return phone.matches(PHONE_NUMBER_REGEX);
    }

    public static boolean isUsernameOrPassword(String str) {
        return str.matches(USERNAME_PASSWORD_REGEX);
    }

    public static boolean isDecimalNumber(String number) {
        return number.matches(DECIMAL_NUMBER_REGEX);
    }

    public static boolean isEmpty(String value) {
        return value.isEmpty() || value.isBlank();
    }

}

