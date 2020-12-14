package com.example.study04;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Intent intent2 = getIntent();
        String keyword1 = intent2.getStringExtra("keyword");


        mRecyclerView =  findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        queue = Volley.newRequestQueue(this);



        getNews(keyword1);
    }

    public void getNews(String Keyword) {

        String url = "https://openapi.naver.com/v1/search/news?query="+ Keyword+ "&display=10&sort=sim";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("NEWS", response);

                        try {
                            /*
                            response
                               "{key1:"--",key2:"--",
                                articles:[{title:"--",author:"--",urlToImage:"--",content:"--"...},{...},...]
                                }"
                            */
                            // JSonString => JSon
                            JSONObject jsonObj = new JSONObject(response);

                            // JSon 객체 안 의 JSon객체들을 담고있는 JSonArray 객체 get (key값으로 가져옴)
                            JSONArray arrayArticles = jsonObj.getJSONArray("items");

                            /* articles 배열 요소(JSon 객체) 는 많은 키와 데이터를 가지고 있기에 필요한
                               3가지만 사용하기 위해서 NewsData클래스를 생성
                            */
                            List<NewsData> news = new ArrayList<>();

                            for(int i = 0, j = arrayArticles.length(); i < j; i++) {
                                // articles배열 내의 각 요소(JSon 객체)들을 가져옴
                                JSONObject obj = arrayArticles.getJSONObject(i);

                                Log.d("newsss", obj.toString());

                                NewsData newsData = new NewsData();
                                newsData.setTitle(obj.getString("title").replace("&quot;","").replace("<b>","").replace("</b>",""));
                                newsData.setUrlToImage("https://s1.econotimes.com/assets/uploads/202012110237090490fbf9190_th_1024x0.jpg");
                                // newsData.setUrlToImage(obj.getString("originallink"));
                                newsData.setContent(obj.getString("description").replace("&quot;","").replace("<b>","").replace("</b>",""));

                                // ArraryList(news) : 각 뉴스들의 제목,이미지주소,내용들이 포함된 NewsData객체들의 배열
                                news.add(newsData);

                            }

                            // Adapter 연결 ! (*context는 Freso를 위해서 인자에 추가)
                            mAdapter = new MyAdapter(news,NewsActivity.this);
                            mRecyclerView.setAdapter(mAdapter);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap();
                params.put("X-Naver-Client-Id", "qriNzx6WTYH3PaWahgJ1");
                params.put("X-Naver-Client-Secret", "bhTFIAgIin");
                Log.d("getHedaer =>", "" + params);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

}