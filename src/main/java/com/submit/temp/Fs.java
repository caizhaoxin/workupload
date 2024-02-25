package com.submit.temp;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SFTPInputStream;
import ch.ethz.ssh2.SFTPv3Client;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Fs {
    public static Class<?> getClassFromRemote(String url) {
        // 模拟远程读取class的文件内容 开始
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        byte[] result = null;
        try {
            Response response = client.newCall(request).execute();
            result = Objects.requireNonNull(response.body()).bytes();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Class<?> obj = new RemoteClassLoad().load(result, result.length);
        return obj;
    }


    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        // 模拟远程读取class的文件内容 开始
        String sinkUrl = "http://192.168.43.62:5000/sink_class";
        Class<?> obj = getClassFromRemote(sinkUrl);
        Object instance = obj.newInstance();
        Method method = obj.getMethod("handler", Object.class);
        method.setAccessible(true);
        String str = (String) method.invoke(instance, "hahah");
        System.out.println(str);
    }
}

class RemoteClassLoad extends ClassLoader {
    public Class<?> load(byte[] data, int length) {
        return super.defineClass(null, data, 0, length);
    }
}