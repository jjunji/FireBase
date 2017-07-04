package com.example.jhjun.firebasebbs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.jhjun.firebasebbs.domain.Bbs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ListAdapter adapter;

    FirebaseDatabase database;
    DatabaseReference bbsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        database = FirebaseDatabase.getInstance();
        bbsRef = database.getReference("message");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new ListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadData();
    }

    public void loadData(){
        bbsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                List<Bbs> list = new ArrayList<Bbs>();
                try {
                    // json 데이터를 Bbs 인스턴스로 변환오류 발생 가능성 있음.
                    // 그래서 예외처리가 필요.
                    for (DataSnapshot item : data.getChildren()) {
                        Bbs bbs = item.getValue(Bbs.class);
                        list.add(bbs);
                    }
                }catch(Exception e){
                    Log.e("Firebase", e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void refreshList(List<Bbs> data){
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

    public void postData(View view){
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
    }
}
