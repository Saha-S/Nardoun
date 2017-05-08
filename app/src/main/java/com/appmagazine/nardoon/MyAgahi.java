package com.appmagazine.nardoon;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nadia on 3/2/2017.
 */

public class MyAgahi {
    public int id;
    public String title, price, location , image,created_at ,validity,permission ,comment ,factors,category_id;

    public MyAgahi(JSONObject jsonObject) { // تابع سازنده برای دریافت مقادیر از JsonObject
        try {
            this.id = jsonObject.getInt("id");
            this.title = jsonObject.getString("title");
            this.price = jsonObject.getString("price");
            this.validity = jsonObject.getString("validity");
            this.location = jsonObject.getString("location");
            this.created_at = jsonObject.getString("created_at");
            this.image = jsonObject.getString("image");
            this.permission = jsonObject.getString("permission");
            this.category_id = jsonObject.getString("category_id");
            this.comment = jsonObject.getString("comment");
            this.factors = jsonObject.getString("factors");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
