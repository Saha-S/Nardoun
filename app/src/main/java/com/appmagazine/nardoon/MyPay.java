package com.appmagazine.nardoon;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nadia on 3/2/2017.
 */

public class MyPay {
    public String created_at, description,price ,traking_code ,id, confirmation_id, type, related_id,isused ,credit ;

    public MyPay(JSONObject jsonObject) { // تابع سازنده برای دریافت مقادیر از JsonObject
        try {
            this.id = jsonObject.getString("id");
            this.confirmation_id = jsonObject.getString("confirmation_id");
            this.type = jsonObject.getString("type");
            this.related_id = jsonObject.getString("related_id");
            this.description = jsonObject.getString("description");
            this.price = jsonObject.getString("price");
            this.traking_code = jsonObject.getString("traking_code");
            this.isused = jsonObject.getString("isused");
            this.created_at = jsonObject.getString("created_at");
            this.credit = jsonObject.getString("credit");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
