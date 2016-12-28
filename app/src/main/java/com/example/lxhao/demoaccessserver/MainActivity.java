package com.example.lxhao.demoaccessserver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    /**
     * 服务器地址
     */
    private final String URL = "http://emsysnet.cn:80/IntellCity/CommnityInfo/AppComActivity";
    private final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        receiveInfo();
    }


    /**
     * 从服务器读取数据
     * AsyncHttp 异步通信框架在app/build.gradle中引入
     */

    private void receiveInfo() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        //最大超时5000ms， 最多重试2次
        asyncHttpClient.setMaxRetriesAndTimeout(2, 5000);

        //get的方式从服务器获取数据
        asyncHttpClient.get(this.URL, new AsyncHttpResponseHandler() {

            /**
             * 访问成功的回调
             * @param statusCode
             * @param headers
             * @param response
             */
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                String result;
                try {
                    result = new String(response, "utf-8");
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                    Log.i(TAG, "服务器返回的数据：" + result);
                    Log.i(TAG, "测试json解析");
                    //服务器返回的是一个数组,所以用Json数组的方式解析
                    JSONArray jsonArray = new JSONArray(result);
                    //读取数组信息
                    for (int i = 0; i < jsonArray.length(); i++) {
                        //每一个元素其实都是JSONObject
                        JSONObject element = (JSONObject) jsonArray.get(i);
                        //得到活动地址
                        System.out.println(element.getString("activityAddress"));
                    }
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "不支持的编码:" + e);
                } catch (JSONException e) {
                    Log.e(TAG, "Json解析错误:" + e);
                }
            }

            // 发送失败
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable e) {
                Toast.makeText(MainActivity.this, "访问服务器失败", Toast.LENGTH_LONG).show();
                Log.e(TAG, "访问服务器出错:" + e.getMessage());
            }

            /**
             * 访问前要做的操作
             */
            @Override
            public void onStart() {
                Log.i(TAG, "准备访问服务器");
            }

            /**
             * 访问完成要做的操作
             */
            @Override
            public void onFinish() {
                Log.i(TAG, "访问服务器完成");
            }

        });
    }
}
