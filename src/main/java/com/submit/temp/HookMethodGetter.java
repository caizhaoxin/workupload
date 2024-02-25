package com.submit.temp;

import java.util.ArrayList;
import java.util.HashMap;

public class HookMethodGetter {
    public static HashMap<String, HashMap<String, HashMap<String, ArrayList<Object>>>> getHookMethod(){
        HashMap<String, HashMap<String, ArrayList<Object>>> appMp = new HashMap<>();
        ArrayList<Object> list = new ArrayList<>();
        list.add("java.lang.String");
        list.add("java.lang.Integer");
        list.add("int");
        list.add("[C");
        HashMap<String, ArrayList<Object>> tmp = new HashMap<>();
        tmp.put("hello", list);
        appMp.put("gosec.xpose_victim.MainActivity", tmp);
        HashMap<String, HashMap<String, HashMap<String, ArrayList<Object>>>> mp = new HashMap<>();
        mp.put("gosec.xpose_victim", appMp);
        return mp;
    }
}