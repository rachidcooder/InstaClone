package com.example.myinstaapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.myinstaapp.R;
import com.example.myinstaapp.Adapters.UserAdapter;
import com.example.myinstaapp.Moduls.UserModle;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFrahment extends Fragment {
    private EditText searchTxt;
   private RecyclerView rvsearchUsers;
   private UserAdapter adapter;
   private ArrayList<UserModle> arrUsers;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_search_frahment, container, false);
        arrUsers=new ArrayList<>();
        adapter=new UserAdapter(getContext(),arrUsers,true);
        searchTxt =v.findViewById(R.id.edtsearchID);
        rvsearchUsers=v.findViewById(R.id.rvUsersearchForID);
        readUser();
        rvsearchUsers.setLayoutManager(new LinearLayoutManager(getContext()));
        rvsearchUsers.setHasFixedSize(true);
        rvsearchUsers.setAdapter(adapter);

        searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchForUser(charSequence.toString());
                adapter.setListOfUsers(arrUsers);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
                return v;
    }


    private void searchForUser(String s){
        Query query= FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("userName").startAt(s).endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!TextUtils.isEmpty(searchTxt.getText().toString())){
                    arrUsers.clear();
                    for(DataSnapshot snapCh: snapshot.getChildren()){
                        UserModle user=snapCh.getValue(UserModle.class);
                        arrUsers.add(user);
                    }
                    adapter.setListOfUsers(arrUsers);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void readUser(){
        DatabaseReference dataRef= FirebaseDatabase.getInstance().getReference("Users");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    arrUsers.clear();
                    for(DataSnapshot snapCh: snapshot.getChildren()){
                        UserModle user=snapCh.getValue(UserModle.class);
                        arrUsers.add(user);
                    }

                    adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}