package com.android.destranger.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by ximing on 2015/5/10.
 */
public class ToolKit {
    public static String voiceToString(String voiceName){
//        StringBuilder sb = new StringBuilder();
        String sb = "";
        byte[] data = new byte[1200];
        int len;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(voiceName));
            try {
                while ((len = fis.read(data)) > 0){
                    sb += (Base64.encodeToString(data, Base64.NO_WRAP));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb;
    }

    public static String stringToVoice(String content){
        Random random = new Random();
        String pathName = "/sdcard/happyCycling/";
        String voiceName = String.valueOf(random.nextInt(100000)) + ".3gp";
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(pathName + voiceName));
            try {
                for(int i = 0; i < content.length()/1600; i++){
                    String str = content.substring(1600*i, 1600*(i+1));
                    fos.write(Base64.decode(str, Base64.NO_WRAP));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.err.println(voiceName);
        return pathName + voiceName;
    }

    public static String BitmapToString(Bitmap bitmap)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
        byte[] image = bos.toByteArray();
        return Base64.encodeToString(image,Base64.DEFAULT);
    }

    public static Bitmap StringToBitmap(String str)
    {
        byte[] image = Base64.decode(str,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        return  bitmap;
    }

    public static byte[] compressImage(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        int options = 100;
        while (bos.toByteArray().length / 1024 > 10) {
            options -= 5;
            if (options < 0)
                break;
            bos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, bos);
        }
        return bos.toByteArray();
    }
}
