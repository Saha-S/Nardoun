package com.appmagazine.nardoon;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nadia on 3/2/2017.
 */

public class Nini {
    public String id, confirmation_id, age,name, image , point, pointm ,created_at ,validity ,comment;

    public Nini(JSONObject jsonObject) { // تابع سازنده برای دریافت مقادیر از JsonObject
        try {
            this.id = jsonObject.getString("id");
            this.name = jsonObject.getString("name");
            this.image = jsonObject.getString("image");
            this.age = jsonObject.getString("age");

            this.point = jsonObject.getString("point");
            this.pointm = jsonObject.getString("pointm");
            this.validity = jsonObject.getString("validity");
            this.created_at = jsonObject.getString("created_at");
            this.confirmation_id = jsonObject.getString("confirmation_id");
            this.comment = jsonObject.getString("comment");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
