package com.example.scavengerhuntapp;

import java.security.SecureRandom;

public final class Utils {

    static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom random = new SecureRandom();

    public static String uniqueID(int len){
        StringBuilder sb = new StringBuilder(len);
        for( int i = 0; i < len; i++ )
            sb.append( CHARACTERS.charAt( random.nextInt(CHARACTERS.length()) ) );
        return sb.toString();
    }

    public static String generateHuntID(){
        String uniqueID = uniqueID(Hunt.LENGTH_OF_ID);
        StringBuilder sb = new StringBuilder(uniqueID);
        sb.insert(4, '-');
        sb.insert(9, '-');
        return sb.toString();
    }
}
