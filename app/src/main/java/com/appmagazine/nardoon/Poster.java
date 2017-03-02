package com.appmagazine.nardoon;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nadia on 3/2/2017.
 */

public class Poster {
    public int id;
    public String title, price, location , image;

    public Poster(JSONObject jsonObject) { // تابع سازنده برای دریافت مقادیر از JsonObject
        try {
            this.id = jsonObject.getInt("id");
            this.title = jsonObject.getString("title");
            this.price = jsonObject.getString("price");
            this.location = jsonObject.getString("location");
            this.image = jsonObject.getString("image");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
