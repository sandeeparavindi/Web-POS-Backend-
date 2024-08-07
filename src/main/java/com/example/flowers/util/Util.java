package com.example.flowers.util;

import java.util.UUID;

public class Util {
    private static int counter = 0;
    public static String idGenerate() {
        counter++;
        return String.format("C%03d", counter);
    }

}
