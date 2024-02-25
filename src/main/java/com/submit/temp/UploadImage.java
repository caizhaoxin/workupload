package com.submit.temp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class UploadImage {
//    public static String uploadImage(String url, String imagePath) throws IOException, JSONException {
//        OkHttpClient okHttpClient = new OkHttpClient();
//        File file = new File(imagePath);
//        System.out.println(file);
//        RequestBody image = RequestBody.create(MediaType.parse("image/jpg"), file);
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("image", imagePath, image)
//                .build();
//        Request request = new Request.Builder()
//                .url(url)
//                .post(requestBody)
//                .build();
//        Response response = okHttpClient.newCall(request).execute();
//        JSONObject jsonObject = new JSONObject(response.body().string());
//        return jsonObject.optString("image");
//    }

    public static String uploadImage(String url, String imagePath) {
        File imageFile = new File(imagePath);
        RequestBody fileBody = RequestBody.create(imageFile, MediaType.parse("image/jpg"));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                //可以根据自己的接口需求在这里添加上传的参数
                .addFormDataPart("image", "avatar", fileBody)
                .build();

        Request request;
        Call call;
        Response response;
        try {
            request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Type", "multipart/form-data")
                    .post(requestBody)
                    .build();

            call = new OkHttpClient().newCall(request);
            response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String res = uploadImage("http://211.66.130.46:12401/predict", "E:\\adv_data_set\\data\\manual_data_set\\VOC2007\\20220522235934.jpg");
        System.out.println(res);
//        Json jsonData =
        JSONObject jsonObject =  JSON.parseObject(res);
        List<List<BigDecimal>> boxesArr = (List<List<BigDecimal>>) jsonObject.get("boxes");

        for(List<BigDecimal> list: boxesArr){
            System.out.println(list);
            int d = list.get(0).intValue();
            System.out.println(d);
//            double d = Double.parseDouble(list.get(0));
        }

//        System.out.println(boxesArr);
    }
}
