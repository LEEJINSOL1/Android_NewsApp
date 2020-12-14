package com.example.study04;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<NewsData> mDataset;

    /* 리스트 내의 각 항목들을 관리하는 ViewHolder class*/

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView TextView_title;
        public TextView TextView_content;
        // Fresco에서 사용하는 전용 이미지 component
        public SimpleDraweeView ImageView_news;


        /* 가져오는 layout에서 id값을 통해 해당 Component를 가져와 변수에 저장 */
        public MyViewHolder(View v) {
            super(v);
            TextView_title = v.findViewById(R.id.TextView_title);
            TextView_content = v.findViewById(R.id.TextView_content);
            ImageView_news = v.findViewById(R.id.ImageView_news);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    /* NewsActivity에서 Adapter와 매칭시 사용하는 사용자이다
       이 때 NewsActivity의 데이터들이 데이터배열을 통해 전달되어진다
    */

    public MyAdapter(List<NewsData> myDataset,Context context) {

        mDataset = myDataset;
        Fresco.initialize(context);     // Fresco 사용을 위한 초기화
    }
    /*
          mAdapter = new MyAdapter(news,NewsActivity.this);
          mRecyclerView.setAdapter(mAdapter);
           => news에 대한 데이터들은 mDataset에 저장되고 RecyclerView와 Adapter매칭
    */


    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        /* 리스트의 각 항목에 입혀줄 디자인을 생성한다 즉, create a new view
           setContentView(R.layout.xxx) 처럼 xml 매칭, 하지만 RecycleView의 특정 부분만 변경시킬 때는
           효율성을 위해 inflate함수를 사용
           사용할 xml(row_news)의 최상위 layout을 이용
         * */
        androidx.constraintlayout.widget.ConstraintLayout v
                = (androidx.constraintlayout.widget.ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_news, parent, false);

        /* row_news에서 components를 가져와 MyViewHolder객체의 변수들에 저장된다*/
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        NewsData news = mDataset.get(position);     // mDataset리스트에서 순서대로 NewsData객체 가져옴

        // newsData클래스의 getter로 값 획득 후 row_news.xml의 component에 값 셋팅
        holder.TextView_title.setText(news.getTitle());
        holder.TextView_content.setText(news.getContent());

        Uri uri = Uri.parse(news.getUrlToImage());      // Fresco
        holder.ImageView_news.setImageURI(uri);         // 이미지 셋팅
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}