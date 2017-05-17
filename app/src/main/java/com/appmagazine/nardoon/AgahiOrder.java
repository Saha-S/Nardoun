package com.appmagazine.nardoon;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nadia on 3/2/2017.
 */

public class AgahiOrder {
    public String created_at, confirmation_mobile ,agahi_mobile ,id, confirmation_id, order, price,isdone,agahi_id ,address;

    public AgahiOrder(JSONObject jsonObject) { // تابع سازنده برای دریافت مقادیر از JsonObject
        try {
            this.id = jsonObject.getString("id");
            this.order = jsonObject.getString("order");
            this.isdone = jsonObject.getString("isdone");

            this.created_at = jsonObject.getString("created_at");
            this.agahi_id = jsonObject.getString("agahi_id");
            this.price = jsonObject.getString("price");

            this.agahi_mobile = jsonObject.getString("agahi_mobile");
            this.confirmation_id = jsonObject.getString("confirmation_id");
            this.confirmation_mobile = jsonObject.getString("confirmation_mobile");
            this.address = jsonObject.getString("address");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
