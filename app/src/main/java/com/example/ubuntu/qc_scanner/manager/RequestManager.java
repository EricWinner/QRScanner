/*
 * Created by Storm Zhang, Feb 11, 2014.
 */

package com.example.ubuntu.qc_scanner.manager;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Request.Priority;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ubuntu.qc_scanner.application.ItLanbaoLibApplication;
import com.example.ubuntu.qc_scanner.http.HttpResponeCallBack;
import com.example.ubuntu.qc_scanner.mode.ApiParams;
import com.example.ubuntu.qc_scanner.util.NetworkUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


/**
 * @author itlanbao
 * <p>
 * 网络请求处理
 */
public class RequestManager {
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;

    private synchronized static void initRequestQueue() {
        if (mRequestQueue == null) {
            //创建一个请求队列
            mRequestQueue = Volley.newRequestQueue(ItLanbaoLibApplication.getInstance());
        }
    }


    /**
     * 添加请求到请求队列中
     * @param request
     * @param tag
     */
    private static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }

    /**
     * post 请求数据
     *
     * @param app_url     公共的接口前缀 http://www.itlanbao.com/api/app/
     * @param tag_url     接口名称，eg:users/user_register_Handler.ashx(注册接口)
     * @param parameter  请求参数封装对象
     * @param clazz      返回数据封装对象，如果传null，则直接返回String
     * @param callback   接口回调监听
     */
    public static <T> void post(final String app_url, final String tag_url, final HashMap<String, String> parameter, Class<T> clazz,
                                final HttpResponeCallBack callback) {
        //发送post请求服务器
        post(app_url, tag_url, parameter, clazz, callback, Priority.NORMAL);
    }


    /**
     * post 请求数据
     *
     * @param app_url    路径
     * @param url        接口名称
     * @param parameter  请求参数封装对象
     * @param clazz      返回数据封装对象，如果传null，则直接返回String
     * @param callback   接口回调监听
     * @param priority   指定接口请求线程优先级
     */
    public static <T> void post(final String app_url, final String url, final HashMap<String, String> parameter, final Class<T> clazz,
                                final HttpResponeCallBack callback, Priority priority) {
        if (callback != null) {
            callback.onResponeStart(url);//回调请求开始
        }

        initRequestQueue();

        //将公共的接口前缀和接口名称拼接
        //eg:拼接成注册的接口  http://www.itlanbao.com/api/app/users/user_register_Handler.ashx
        StringBuilder builder = new StringBuilder(app_url);
        builder.append(url);

        // 检查当前网络是否可用
        final NetworkUtils networkUtils = new NetworkUtils(ItLanbaoLibApplication.getInstance());

        if (!networkUtils.isNetworkConnected() && android.os.Build.VERSION.SDK_INT > 10) {
            if (callback != null) {
                callback.onFailure(url, null, 0, "网络出错");//回调请求失败
                return;
            }
        }

        /**
         * 使用Volley框架真正去请求服务器
         * Method.POST：请求方式为post
         * builder.toString()：请求的链接
         * Listener<String>：监听
         */
        StringRequest request = new StringRequest(Method.POST, builder.toString(),
                new Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        //这个位置先公共解析处理共同异常
                        try {
                            if (response != null && callback != null) {
                                Gson gson = new Gson();
                                //回调请求成功，同时url和解析的对象
                                callback.onSuccess(url, gson.fromJson(response, clazz));
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                            if (callback != null) {
                                //回调请求失败--解析异常
                                callback.onFailure(url, e, 0, "解析异常");
                                return;
                            }
                        }
                    }
                }, new ErrorListener() {
            //请求出错的监听
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callback != null) {
                    if (error != null) {
                        callback.onFailure(url, error.getCause(), 0,
                                error.getMessage());
                    } else {
                        callback.onFailure(url, null, 0, "");
                    }
                }
            }
        }) {
            //post请求的参数信息
            protected Map<String, String> getParams() {
                return getPostApiParmes(parameter);
            }
        };

        //添加请求到请求队列中
        addRequest(request, url);
    }


    /*
     * post参数
	 * ts:时间戳 sign: 接口签名 parms = 按文档参数拼接 parm[0]+ … + parm[n-1] sign =
	 * md5(parms+"双方平台约定公钥")
	 */
    private static ApiParams getPostApiParmes(final HashMap<String, String> parameter) {
        ApiParams api = new ApiParams();
        for (Entry<String, String> entry : parameter.entrySet()) {
            api.with(entry.getKey(), entry.getValue());
        }
        return api;
    }


}
