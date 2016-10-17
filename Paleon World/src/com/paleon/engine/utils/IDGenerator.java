package com.paleon.engine.utils;

/**
 * Created by Rick on 14.10.2016.
 */
public class IDGenerator {

    private static int id;

    public static int generate() {
        return id++;
    }

}
