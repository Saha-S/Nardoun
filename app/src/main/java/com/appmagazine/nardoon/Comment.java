package com.appmagazine.nardoon;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nadia on 3/2/2017.
 */

public class Comment {
    public int news_id;
    public String confirmation_id, content, validity,name  ;

    public Comment(JSONObject jsonObject) { // تابع سازنده برای دریافت مقادیر از JsonObject
        try {
            this.news_id = jsonObject.getInt("news_id");
            this.confirmation_id = jsonObject.getString("confirmation_id");
            this.content = jsonObject.getString("content");
            this.validity = jsonObject.getString("validity");
            this.name = jsonObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
