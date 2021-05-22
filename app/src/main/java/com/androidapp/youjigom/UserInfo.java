package com.androidapp.youjigom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.Person;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserInfo extends FragmentActivity {

    public ArrayList<UserInfo_item> listData;
    public RecyclerViewAdapter reAdapter;
    public RecyclerView recycle;
    public LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_item);

        init();
        getData();

    }

    private void init() {
        recycle = (RecyclerView)findViewById(R.id.recyclerView);

        linearLayoutManager = new LinearLayoutManager(this);
        recycle.setLayoutManager(linearLayoutManager);

        reAdapter = new RecyclerViewAdapter();
        recycle.setAdapter(reAdapter);
    }

    private void getData() {
        List<Integer> imageList = Arrays.asList(
                R.drawable.korea,
                R.drawable.japan,
                R.drawable.china
        );
        List<String> nameList = Arrays.asList("손흥민", "나카무라", "동팡저우");
        List<String> countryList = Arrays.asList(
                "대한민국",
                "일본",
                "중국");

        for ( int i = 0; i < nameList.size(); i++ ){
            UserInfo_item data = new UserInfo_item();
            data.setProfileImage(imageList.get(i));
            data.setName(nameList.get(i));
            data.setCountry(countryList.get(i));

            reAdapter.addItem(data);
        }

        reAdapter.notifyDataSetChanged();
    }

}