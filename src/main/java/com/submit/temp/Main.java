package com.submit.temp;

import org.apache.shiro.util.StringUtils;
import sun.rmi.runtime.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;


class C {
    public C f;
}


public class Main {
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        // 2.打印调用堆栈
        System.out.println("2.打印调用堆栈");
        StackTraceElement stack[] = Thread.currentThread().getStackTrace();

        for (int i = 0; i < stack.length; i++) {
            System.out.println(stack[i].getClassName() + "." + stack[i].getMethodName());
        }
        return sb.toString();
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        System.out.println(getRandomString(13));
    }
}
