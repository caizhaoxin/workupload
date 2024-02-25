package com.submit.temp;

import com.alibaba.druid.sql.visitor.functions.Char;

import java.util.*;

public class Tmp {

    public static void main(String[] args) throws InterruptedException {
        Map<Character, Integer> mapA = new HashMap<>();
        Map<Character, Integer> mapB = new HashMap<>();
        Integer i1 = 5;
        Integer i2 = 5;
        System.out.println(i1==i2);
        mapA.put('a', i1);
        mapB.put('a', i2);
        System.out.println(mapA.get('a')==mapB.get('a'));
    }
}
