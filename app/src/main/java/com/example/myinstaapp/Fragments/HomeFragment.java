package com.example.myinstaapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.myinstaapp.Adapters.AdapterPosrts;
import com.example.myinstaapp.Moduls.PostModule;
import com.example.myinstaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
private RecyclerView rvPosts,rvStory;
private ImageView imgInbox;
 private AdapterPosrts adapPost;
 private List<PostModule> arrPosts;
 private List<String> followingList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    View v=inflater.inflate(R.layout.fragment_home, container, false);
    rvPosts =v.findViewById(R.id.RvPostsID);
    rvStory =v.findViewById(R.id.RvStoryID);

    followingList=new ArrayList<String>();
    arrPosts=new ArrayList<>();
        FollWersUser();
    adapPost=new AdapterPosrts(getContext(),arrPosts);

    LinearLayoutManager lm=new LinearLayoutManager(getContext());
    // من الاحدات الى الاقدام
    lm.setStackFromEnd(true);
    lm.setReverseLayout(true);
    rvPosts.setLayoutManager(lm);
    rvPosts.setHasFixedSize(true);
    rvPosts.setAdapter(adapPost);
        return v;
    }

    void FollWersUser(){
        //following
       FirebaseDatabase.getInstance().getReference().child("Follow").
               child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following").addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                     followingList.clear();
                     for(DataSnapshot snap: snapshot.getChildren()) {
                     followingList.add(snap.getKey());
                     }
                     if(!followingList.isEmpty()){
                      ReadPostsFromFbase(followingList);
                     }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {

                  }
               });
    };
    private void ReadPostsFromFbase(List<String> listOffollowing) {
      FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
             arrPosts.clear();
               for(DataSnapshot snap: snapshot.getChildren()){
                   PostModule postofFollwing=snap.getValue(PostModule.class);
                for(String id:listOffollowing){

                   if(postofFollwing.getPublisher().equals(id)) {
                   arrPosts.add(postofFollwing);
               }}}

                adapPost.setArrPosts(arrPosts);
                adapPost.notifyDataSetChanged();

          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
       });
    }

}