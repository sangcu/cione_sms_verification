package com.example.ngocphong.gcm;

/**
 * Created by NgocPhong on 4/13/2015.
 */
public class UserToken {
    public String code;
    public String phone;

    @Override
    public String toString() {
        String str = code + ":" + phone;
        return str;
    }
}
