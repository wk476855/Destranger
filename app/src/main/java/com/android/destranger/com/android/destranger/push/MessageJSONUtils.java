package com.android.destranger.com.android.destranger.push;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wk on 2015/5/10.
 */
public class MessageJSONUtils {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H");

    public static String messageToJson(Message msg) {
        JSONObject json = new JSONObject();
        Class mclass = msg.getClass();
        Field[]  fields = mclass.getDeclaredFields();
        try {
            for (Field f : fields) {
                f.setAccessible(true);
                switch (f.getType().getSimpleName()) {
                    case "String":
                        String str = (String) f.get(f.getName());
                        json.put(f.getName(), str);
                        break;
                    case "Date":
                        Date date = (Date) f.get(f.getName());

                }

            }
        }catch (IllegalAccessException e) {
            throw  new RuntimeException(e);
        }catch (JSONException ee) {
            throw new RuntimeException(ee);
        }
        return null;
    }

    public Message jsonToMessage(String json) {
        return null;
    }
}
