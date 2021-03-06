package com.tedkim.thttprequest.requestclient;

import android.content.Context;

import com.tedkim.thttprequest.manager.APIManager;
import com.tedkim.thttprequest.manager.TokenManager;
import com.tedkim.thttprequest.utils.APIUtils;

import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * Created by Ted
 */
public class RequestHeaders {

    public static final String CLIENT_ID = "Client-ID";
    public static final String CLIENT_SECRET = "Client-Secret";
    public static final String AUTHORIZATION = "Authorization";
    public static final String APP_VERSION = "X-APP-VER";
    public static final String TZ_OFFSET = "X-TZ-OFFSET";
    public static final String COUNTRY = "X-COUNTRY";
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json; charset=utf-8";
    public static final String CACHE_NO_CACHE = "no-cache";
    public static final String CONTENT_TYPE_FILE = "multipart/form-data";
    public static final String BASIC = "Basic ";
    public static final String BEARER = "Bearer ";

    private static RequestHeaders mInstance;
    private Context mContext;

    public static RequestHeaders getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestHeaders();
        }
        mInstance.mContext = context;
        return mInstance;
    }

    /**
     * Header that use a Base
     *
     * @param chain Interceptor Chain
     * @return Request client
     */
    public Request getBasicHeader(Interceptor.Chain chain) {
        Request.Builder builder = chain.request().newBuilder();
        builder.header(AUTHORIZATION, BASIC + TokenManager.getBasicToken(mContext));
        builder.header(CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON);
        builder.header(CACHE_CONTROL, CACHE_NO_CACHE);
        builder.header(APP_VERSION, String.valueOf(APIUtils.getVersionCode(mContext, mContext.getPackageName())));
        builder.header(TZ_OFFSET, APIUtils.getCurrentTimezoneOffset());
        builder.header(COUNTRY, APIUtils.getDeviceCountryCode());
        builder.header(ACCEPT_LANGUAGE, APIUtils.getDeviceLanguage());

        return builder.build();
    }

    /**
     * Common Header
     *
     * @param chain Interceptor Chain
     * @return Request client
     */
    public Request getHeader(Interceptor.Chain chain) {
        String token;
        String accessToken = TokenManager.getAccessToken(mContext);
        if (accessToken == null)
            token = BASIC + TokenManager.getBasicToken(mContext);
        else
            token = BEARER + accessToken;


        Request.Builder builder = chain.request().newBuilder();
        builder.header(AUTHORIZATION, token);
        builder.header(CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON);
        builder.header(CACHE_CONTROL, CACHE_NO_CACHE);
        builder.header(APP_VERSION, String.valueOf(APIUtils.getVersionCode(mContext, mContext.getPackageName())));
        builder.header(TZ_OFFSET, APIUtils.getCurrentTimezoneOffset());
        builder.header(COUNTRY, APIUtils.getDeviceCountryCode());
        builder.header(ACCEPT_LANGUAGE, APIUtils.getDeviceLanguage());

        return builder.build();
    }

    /**
     * Header that use the File Upload
     *
     * @param chain Interceptor Chain
     * @return Request client
     */
    public Request getFileUploadHeader(Interceptor.Chain chain) {
        String accessToken = TokenManager.getAccessToken(mContext);

        Request.Builder builder = chain.request().newBuilder();
        builder.header(CONTENT_TYPE, CONTENT_TYPE_FILE);
        builder.header(CACHE_CONTROL, CACHE_NO_CACHE);
        builder.header(APP_VERSION, String.valueOf(APIUtils.getVersionCode(mContext, mContext.getPackageName())));
        builder.header(TZ_OFFSET, APIUtils.getCurrentTimezoneOffset());
        builder.header(COUNTRY, APIUtils.getDeviceCountryCode());
        builder.header(ACCEPT_LANGUAGE, APIUtils.getDeviceLanguage());

        if (accessToken != null) {
            builder.header(AUTHORIZATION, BEARER + accessToken);
        } else {
            builder.header(CLIENT_ID, APIManager.getClientID(mContext));
            builder.header(CLIENT_SECRET, APIManager.getClientSecret(mContext));
        }

        return builder.build();
    }

}
