package com.vitalityactive.va.register.entity;

import android.support.annotation.NonNull;

import java.util.regex.Pattern;

public class Password extends CredentialImpl {
    private static final Pattern[] patterns = new Pattern[]{
            Pattern.compile(".*[A-Z].*"),
            Pattern.compile(".*[a-z].*"),
            Pattern.compile(".*\\d.*")
    };

    public Password(@NonNull CharSequence text) {
        super(text);
    }

    @Override
    public boolean isValid() {

        if (text.length() < 7) {
            return false;
        }

        for (Pattern pattern : patterns) {
            if (!pattern.matcher(text).matches())
                return false;
        }

        if(!isSingleByte()){
            return false;
        }

        return true;
    }

//    public boolean isLengthValid(){
//        if (text.length() < 7) {
//            return false;
//        }
//        return true;
//    }
//
    public boolean isPatternValid(String text){
        for (Pattern pattern : patterns) {
            if (!pattern.matcher(text).matches())
                return false;
        }
        return true;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Password password = (Password) o;

        return getText().toString().equals(password.getText().toString());

    }

    @Override
    public int hashCode() {
        return getText().hashCode();
    }

    // Allow only single byte characters
    private boolean isSingleByte() {
        for (int i=0; i<text.length(); i++) {
            if (String.valueOf(text.charAt(i)).getBytes().length != 1) {
                return false;
            }
        }
        return true;
    }
}
