package com.zsyc.common;

import java.util.UUID;

public class Uuid {

    public static String getUid() {

            //注意replaceAll前面的是正则表达式
            String uuid = UUID.randomUUID().toString().replaceAll("-","");

            return uuid;
    }


    public static Long getLongUid(){

        Long l = UUID.randomUUID().getMostSignificantBits();

        return l;
    }

}
