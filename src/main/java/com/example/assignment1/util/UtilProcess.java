package com.example.assignment1.util;

import java.util.UUID;

public class UtilProcess {
    public static String generateId(){
        return UUID.randomUUID().toString();
    }

}