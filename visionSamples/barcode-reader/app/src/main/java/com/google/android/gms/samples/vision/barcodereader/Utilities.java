package com.google.android.gms.samples.vision.barcodereader;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.os.Environment.getExternalStorageDirectory;
import static java.io.File.separator;

public class Utilities {

    public static final String API_URL = "http://imptn.eastasia.cloudapp.azure.com:5000/"; //"http://10.103.76.45:5000/";

    public static String getPhotoTime(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyMMdd_hhmmss");
        return sdf.format(new Date());
    }

    public static String capturePic(String barcodeValue, byte[] bytes) {
        try {
            String mainpath = getExternalStorageDirectory() + separator + "MaskIt" + separator + "images" + separator;
            File basePath = new File(mainpath);
            if (!basePath.exists())
                Log.d("CAPTURE_BASE_PATH", basePath.mkdirs() ? "Success": "Failed");

            String filePath = mainpath + "photo_" + Utilities.getPhotoTime() + "_" + barcodeValue + ".jpg";
            File captureFile = new File(filePath);
            if (!captureFile.exists())
                Log.d("CAPTURE_FILE_PATH", captureFile.createNewFile() ? "Success": "Failed");
            FileOutputStream stream = new FileOutputStream(captureFile);
            stream.write(bytes);
            stream.flush();
            stream.close();
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static void execMultipartPost(String filePath) throws Exception {
        File file = new File(filePath);
        String contentType = file.toURL().openConnection().getContentType();

        Log.d("UPLOAD", "file: " + file.getPath());
        Log.d("UPLOAD", "contentType: " + contentType);

        RequestBody fileBody = RequestBody.create(MediaType.parse(contentType), file);

        //final String filename = "file_" + System.currentTimeMillis() / 1000L;
        final String filename = file.getName();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", "1")
                .addFormDataPart("group_id", "1")
                .addFormDataPart("token", "NVbk9J_eE@ux2v?3")
                .addFormDataPart("image", filename, fileBody)
                .build();

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
               /* runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //et_response.setText(e.getMessage());
                        // Toast.makeText(MainActivity.this, "nah", Toast.LENGTH_SHORT).show();

                        // TODO handle error
                    }
                });*/
            }

            @Override
            public void onResponse(Call call, final Response response) {
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        *//*try {
                            et_response.setText(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*//*

                        // TODO: perform action
                    }
                });*/
            }
        });
    }
}
