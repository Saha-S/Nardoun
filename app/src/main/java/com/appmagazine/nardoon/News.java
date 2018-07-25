package com.appmagazine.nardoon;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nadia on 3/2/2017.
 */

public class News {
    public String id,title, subject, content,image, updated_at , create_at ,commentcount ,point ,pointm , like , dislike ,validity;

    public News(JSONObject jsonObject) { // تابع سازنده برای دریافت مقادیر از JsonObject
        try {
            this.id = jsonObject.getString("id");
            this.subject = jsonObject.getString("subject");
            this.content = jsonObject.getString("content");
            this.title = jsonObject.getString("title");
            this.image = jsonObject.getString("image");
            this.updated_at = jsonObject.getString("updated_at");
            this.create_at = jsonObject.getString("create_at");
            this.commentcount = jsonObject.getString("commentcount");
            this.point = jsonObject.getString("point");
            this.pointm = jsonObject.getString("pointm");
            this.like = jsonObject.getString("like");
            this.dislike = jsonObject.getString("dislike");
            try {
                this.validity = jsonObject.getString("validity");
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
