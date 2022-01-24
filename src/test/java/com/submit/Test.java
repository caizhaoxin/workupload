package com.submit;

import java.nio.file.Paths;

public class Test {
    public static void main(String[] args) {
        String res = Paths.get("a","b","c","d","e","f").toString();
        System.out.println(res);
    }
}
