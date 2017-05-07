package com.appmagazine.nardoon;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nadia on 3/2/2017.
 */

public class Cat {
    public int id;
    public String title, price, location , image,created_at ,validity , comment , special;

    public Cat(JSONObject jsonObject) { // تابع سازنده برای دریافت مقادیر از JsonObject
        try {
            this.id = jsonObject.getInt("id");
            this.title = jsonObject.getString("title");
            this.price = jsonObject.getString("price");
            this.location = jsonObject.getString("location");
            this.created_at = jsonObject.getString("created_at");
            this.special = jsonObject.getString("special");
            this.image = jsonObject.getString("image");

            // this.validity = jsonObject.getString("validity");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
