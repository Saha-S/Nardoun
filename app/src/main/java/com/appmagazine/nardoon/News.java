package com.appmagazine.nardoon;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nadia on 3/2/2017.
 */

public class News {
    public String id, subject, content,image, updated_at , create_at;

    public News(JSONObject jsonObject) { // تابع سازنده برای دریافت مقادیر از JsonObject
        try {
            this.id = jsonObject.getString("id");
            this.subject = jsonObject.getString("subject");
            this.image = jsonObject.getString("image");
            this.updated_at = jsonObject.getString("updated_at");
            this.create_at = jsonObject.getString("create_at");




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
