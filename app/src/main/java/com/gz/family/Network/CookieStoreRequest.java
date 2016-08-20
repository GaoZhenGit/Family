package com.gz.family.Network;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.gz.family.application.Constant;
import com.gz.family.utils.LogUtil;
import com.gz.family.utils.SpUtils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * make a request with cookies store in share preference
 * Created by host on 2015/10/30.
 */
public class CookieStoreRequest extends StringRequest {
    private String mHeader;
    public String cookieFromResponse;
    private Context context;

    public CookieStoreRequest(int method, Context context, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.context = context;
    }

    public CookieStoreRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        mHeader = response.headers.toString();
        LogUtil.i("LOG", "get headers in parseNetworkResponse " + response.headers.toString());
        //使用正则表达式从reponse的头中提取cookie内容的子串
        Pattern pattern = Pattern.compile("Set-Cookie.*?;");
        Matcher m = pattern.matcher(mHeader);
        if (m.find()) {
            cookieFromResponse = m.group();
            LogUtil.i("LOG", "cookie from server " + cookieFromResponse);
            //去掉cookie末尾的分号
            cookieFromResponse = cookieFromResponse.substring(11, cookieFromResponse.length() - 1);
            LogUtil.i("LOG", "cookie substring " + cookieFromResponse);

            SpUtils spUtils = new SpUtils(context);
            spUtils.setValue(Constant.DataKey.SESS, cookieFromResponse);
        }


        String str = null;
        try {
            str = new String(response.data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Response.success(str, HttpHeaderParser.parseCacheHeaders(response));
    }
}