package com.gz.family.Network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gz.family.application.Constant;
import com.gz.family.utils.LogUtil;

import java.util.Map;

/**
 * user for http request
 * Created by host on 2015/11/5.
 */
public class Net {
    private static RequestQueue mQueue;
    private static boolean isInit = false;
    private static Context context;

    public static RequestQueue getmQueue() throws Exception {
        if (isInit) {
            return mQueue;
        } else {
            throw new Exception("volley has not init");
        }
    }

    public static void init(Context context) {
        mQueue = Volley.newRequestQueue(context);
        Net.context = context;
        isInit = true;
    }

    public static void post(String httpurl, final Map<String, String> params, final NetworkListener networkListener) {
        StringRequest stringRequest = new StringRequest8(Request.Method.POST, Net.context, httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtil.i("volley", "response -> " + response);
                        networkListener.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.i("volley error", error.getMessage() + "");
                networkListener.onFail(error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                return params;
            }
        };
        mQueue.add(stringRequest);
    }

    public static void put(String httpurl, final Map<String, String> params, final NetworkListener networkListener) {
        StringRequest stringRequest = new StringRequest8(Request.Method.PUT, Net.context, httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtil.i("volley", "response -> " + response);
                        networkListener.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.i("volley error", error.getMessage() + "");
                networkListener.onFail(error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                return params;
            }
        };
        mQueue.add(stringRequest);
    }

    public static void delete(String httpurl, final Map<String, String> params, final NetworkListener networkListener) {
        httpurl += "?";
        for (Map.Entry<String, String> entry : params.entrySet()) {
            httpurl += entry.getKey() + "=" + entry.getValue();
        }
        StringRequest stringRequest = new StringRequest8(Request.Method.DELETE, Net.context, httpurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtil.i("volley", "response -> " + response);
                        networkListener.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.i("volley error", error.getMessage() + "");
                networkListener.onFail(error.getMessage());
            }
        });
        mQueue.add(stringRequest);
    }

    public static void get(String url , final Map<String,String> params, final NetworkListener networkListener) {
        StringRequest stringRequest = new StringRequest8(Request.Method.GET, Net.context, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtil.i("volley", "response -> " + response);
                        networkListener.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.i("volley error", error.getMessage() + "");
                networkListener.onFail(error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                return params;
            }
        };
        mQueue.add(stringRequest);
    }

    public static void login(final Map<String, String> params, final NetworkListener networkListener) {
        StringRequest stringRequest = new CookieStoreRequest(Request.Method.POST, Net.context, Constant.URL.Login,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtil.i("volley", "response -> " + response);
                        networkListener.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.i("volley error", error.getMessage() + "");
                networkListener.onFail(error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //在这里设置需要post的参数
                return params;
            }
        };
        mQueue.add(stringRequest);
    }


    public interface NetworkListener {
        void onSuccess(String response);

        void onFail(String error);
    }
}
