package com.example.jhjun.firebasebbs;

import android.content.ContentResolver;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jhjun.firebasebbs.domain.Bbs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhjun on 2017-07-04.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.Holder>{

    List<Bbs> data = new ArrayList<>();
    LayoutInflater inflater;

    public ListAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_item,parent,false);
        return new Holder(view);
    }

    public void setData(List<Bbs> data){
        this.data = data;
    }


    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Bbs bbs = data.get(position);
        holder.setTitle(bbs.title);
        holder.setCount(bbs.count);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private TextView textTitle;
    private TextView textCount;

    public class Holder extends RecyclerView.ViewHolder { // 홀더 클래스는 한 셀에 해당하는 뷰 안의 위젯들을 컨트롤하기 위한 연결통로

        public Holder(View v) {
            super(v); // super() 붙는 이유 아직도 잘 모르겠슈

            textTitle = (TextView) v.findViewById(R.id.textTitle);
            textCount = (TextView) v.findViewById(R.id.textCount);
        }

        public void setTitle(String title){
            textTitle.setText(title);
        }

        public void setCount(Long count){
            textCount.setText(count+"");
        }
    }

}
