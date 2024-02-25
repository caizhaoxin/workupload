package com.submit.temp;


import okhttp3.*;
import okio.BufferedSink;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.alibaba.fastjson.JSON;

class MyTask implements Callable<String> {
    String filtUrl, oriUrl;

    public MyTask(String filtUrl, String oriUrl) {
        this.filtUrl = filtUrl;
        this.oriUrl = oriUrl;
    }

    @Override
    public String call() throws Exception {
        String res = DownloadUtil.filterUrlByOkHttp(filtUrl, oriUrl);
        String filtedUrl = ((Map<String, String>) JSON.parse(res)).get("url");
        return filtedUrl;
    }
}

public class DownloadUtil {
    public static void main(String[] args) throws JSONException, ExecutionException, InterruptedException {
        Map<String, Map<String, Object>> result = null;
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(2, TimeUnit.SECONDS)//设置读取超时时间
                    .build();
            MultipartBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("package_name", "com.cns.mc.activity")
                    .build();
            Request request = new Request.Builder()
                    .url("http://127.0.0.1:5000/get_sink_by_package_name")
                    .post(formBody)
                    .build();
            Response response = client.newCall(request).execute();
            result = (Map<String, Map<String, Object>>) JSON.parse(response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        if (result != null) {
            for (Map.Entry<String, Map<String, Object>> hookData : result.entrySet()) {
                com.alibaba.fastjson.JSONObject sourceData = (com.alibaba.fastjson.JSONObject) hookData.getValue().get("source");
                com.alibaba.fastjson.JSONObject sinkData = (com.alibaba.fastjson.JSONObject) hookData.getValue().get("sink");
                if (sourceData == null || sinkData == null) continue;
//                System.out.println("sourceData");
//                System.out.println(sourceData);
//                System.out.println("sinkData");
//                System.out.println(sinkData);
                if (sourceData.get("targetClass") != null) {
                    String sourceMethod = sourceData.get("targetClass") + "." + sourceData.get("targetMethod") + listToString((List<Object>) sourceData.get("targetArguments"));
                    System.out.println(sourceMethod);
                }

                //                if (sourceData == null || sinkData == null) continue;
//                String methodSign = hookData.getKey();
//                String sourceMethod = null;
//                String sinkMethod = null;
//                if (sourceData.get("targetClass") != null) {
//                    sourceMethod = sourceData.get("targetClass") + "." + sourceData.get("targetMethod");
//                }
//                if (sinkData.get("targetClass") != null) {
//                    sinkMethod = sinkData.get("targetClass") + "." + sinkData.get("targetMethod");
//                }
//                System.out.println(sinkData.getString("targetClass"));
//                String methodName = sinkData.getString("targetMethod");
//                List<Object> methodParameter = JSON.parseArray(Objects.requireNonNull(sinkData.get("targetArguments")).toString());
            }
        }

    }

    public static String listToString(List<Object> list) {
        StringBuilder sb = new StringBuilder("(");
        if (list == null || list.size() == 0) return sb.append(")").toString();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i != list.size() - 1) sb.append(",");
        }
        return sb.append(")").toString();
    }

    public static Map<String, Map<String, Map<String, List<List<Object>>>>> getSinkByOkHttp(String url) {
        Map<String, Map<String, Map<String, List<List<Object>>>>> result = null;
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(2, TimeUnit.SECONDS)//设置读取超时时间
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            result = (Map<String, Map<String, Map<String, List<List<Object>>>>>) JSON.parse(response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return result;
    }

    public static String filterUrlByOkHttp(String url, String oriUrl) {
        String result = "";
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(2, TimeUnit.SECONDS)//设置读取超时时间
                    .build();
            MultipartBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("ori_url", oriUrl)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return result;
    }

    // JSON字符串下载
    public static String downloadJSON(String path) throws JSONException {
        URL url = null;
        InputStream input = null;
        HttpURLConnection conn = null;
        byte data[] = null;
        try {
            url = new URL(path);// 1.将网址封装到URL中
            conn = (HttpURLConnection) url.openConnection();// 2.打开http连接
            conn.connect();// 3.开始连接
            input = conn.getInputStream();// 4.获取网络输入流，用于读取要下载内容
            ByteArrayOutputStream bos = new ByteArrayOutputStream();// 5.获取字节转换流
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = input.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            data = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String jsonStr = new String(data);
        //第一种方式
        Map<String, Map<String, Map<String, List<List<Object>>>>> maps = (Map<String, Map<String, Map<String, List<List<Object>>>>>) JSON.parse(jsonStr);
        System.out.println(maps);
        return new String(data);
    }

    //下载图片
    public static void downloadPhoto(String photo, String dest) {
        URL url = null;
        InputStream input = null;
        HttpURLConnection conn = null;
        OutputStream out = null;
        byte date[] = null;
        try {
            out = new FileOutputStream(dest);
            url = new URL(photo);// 1.将网址封装到URL中
            conn = (HttpURLConnection) url.openConnection();// 2.打开http连接
            conn.connect();// 3.开始连接
            input = conn.getInputStream();// 4.获取网络输入流，用于读取要下载内容
            ByteArrayOutputStream bos = new ByteArrayOutputStream();// 5.获取字节转换流
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = input.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            date = bos.toByteArray();
            out.write(date);
            System.out.println(dest + "图片下载成功，以写入本地磁盘..");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
