package com.appmagazine.nardoon;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by nadia on 3/2/2017.
 */
public class NetUtilsCatsAgahi {
   // private static final String BASE_URL = "http://nardoun.ir/api/categories/"; // آدرس اصلی api

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

   // private static String getAbsoluteUrl(String relativeUrl) {
    //    return BASE_URL + relativeUrl;
  // }
}
