package com.transation.demo.utils;

public class Panic {
    public static void panicForRollBack() {
        throw new RuntimeException("exception for rll back");
    }
}
