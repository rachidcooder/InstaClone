package com.example.myinstaapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.UserDictionary;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myinstaapp.ActivitytoEditPorfile;
import com.example.myinstaapp.Adapters.PHotoAdapter;
import com.example.myinstaapp.Moduls.PostModule;
import com.example.myinstaapp.Moduls.UserModle;
import com.example.myinstaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {
private TextView userName,fullName,editprofile,postnumber,followersNumber,follwingNumber;
private ImageView imgProfile,options;
private ImageButton savedPost,menuPosts;
private RecyclerView rvPosts;
FirebaseUser fUser;
private String profileID;
private PHotoAdapter adapter;
private PHotoAdapter adapterSavedPost;
private ArrayList<PostModule> listOfPhots;
private ArrayList<PostModule> listOfPhotsSved;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     View v=   inflater.inflate(R.layout.fragment_profile, container, false);

     String data=getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).
             getString("profileID","none");
     if(data.equals("none")){
         profileID =fUser.getUid();
     }
     else{
         profileID=data;
     }


     fUser= FirebaseAuth.getInstance().getCurrentUser();
     listOfPhots=new ArrayList<>();
     listOfPhotsSved=new ArrayList<>();
   adapter=new PHotoAdapter(getContext(),listOfPhots);
    adapterSavedPost=new PHotoAdapter(getContext(),listOfPhotsSved);
     userName=v.findViewById(R.id.UserNameinProfileID);
     fullName=v.findViewById(R.id.fullNId);
     editprofile=v.findViewById(R.id.editProfileTxtID);
     postnumber=v.findViewById(R.id.postNUmberID);
     followersNumber=v.findViewById(R.id.followersNumberID);
     follwingNumber=v.findViewById(R.id.followingNumberID);
     imgProfile=v.findViewById(R.id.imgInProfileFrag);
     savedPost=v.findViewById(R.id.savedPostID);
     options=v.findViewById(R.id.OptionsID);
     menuPosts=v.findViewById(R.id.menuPhotopostID);
     rvPosts=v.findViewById(R.id.rvImgPosts);

     if(profileID.equals(fUser.getUid())){
         editprofile.setText("Edit Profile");
     }
     else {
         CheckFollowinStats();  }
    editprofile.setOnClickListener(view->{
    if(editprofile.getText().equals("Edit Profile")){
      getContext().startActivity(new Intent(getContext(), ActivitytoEditPorfile.class));
    }else{
      if(editprofile.getText().equals("follow"))  {
   FirebaseDatabase.getInstance().getReference().child("Follow").child(fUser.getUid())
                  .child("following").child(profileID).setValue(true);
   FirebaseDatabase.getInstance().getReference().child("Follow").
          child(profileID).child("followers").child(fUser.getUid()).setValue(true);
      }
      else{
          FirebaseDatabase.getInstance().getReference().child("Follow").child(fUser.getUid())
                  .child("following").child(profileID).removeValue();
          FirebaseDatabase.getInstance().getReference().child("Follow").
                  child(profileID).child("followers").child(fUser.getUid()).removeValue();
      }
    }
});
GetMyPostsphoto();
        GridLayoutManager lm=new GridLayoutManager(getContext(),3);
        lm.setReverseLayout(true);
        rvPosts.setLayoutManager(lm);
        rvPosts.setHasFixedSize(true);

        rvPosts.setAdapter(adapter);
     GetUserInfo();
     GetPostCount();
     GetFollowersCount();
     GetFollowingCount();

     menuPosts.setOnClickListener(view->{
         GetMyPostsphoto();
         rvPosts.setAdapter(adapter);

     });
     savedPost.setOnClickListener(view->{
        ShowPostPhotoSaved();
         rvPosts.setAdapter(adapterSavedPost);
     });

        return v;
    }

    private void ShowPostPhotoSaved() {
         List<String> listOfposidSaved=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Saves").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfposidSaved.clear();
                for(DataSnapshot snap: snapshot.getChildren()){
                    listOfposidSaved.add(snap.getKey());
                }
                getPhotopostSaved(listOfposidSaved);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getPhotopostSaved(List<String> listofpostIDsaved) {
       FirebaseDatabase.getInstance().getReference().child("Posts")
               .addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               listOfPhotsSved.clear();
               for(DataSnapshot snap: snapshot.getChildren()){
                   PostModule postPHoto=snap.getValue(PostModule.class);
                   for(String postID:listofpostIDsaved){
                   if(postPHoto.getPostid().equals(postID)){
                       listOfPhotsSved.add(postPHoto);
                   }
                   }
               }
               adapterSavedPost.setListOfPHoto(listOfPhotsSved);
               adapterSavedPost.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }

    private void GetMyPostsphoto() {
        FirebaseDatabase.getInstance().getReference().child("Posts").
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfPhots.clear();
                for(DataSnapshot snap: snapshot.getChildren()){
                    PostModule postPHoto=snap.getValue(PostModule.class);
                    if(postPHoto.getPublisher().equals(fUser.getUid())){
                        listOfPhots.add(postPHoto);
                    }
                }
                adapter.setListOfPHoto(listOfPhots);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void CheckFollowinStats() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(fUser.getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(profileID).exists()){
                    editprofile.setText("following");
                }else{
                    editprofile.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void GetPostCount() {
        FirebaseDatabase.getInstance().getReference().child("Posts").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int counter = 0;
                        for(DataSnapshot snap: snapshot.getChildren()){
                            PostModule posts= snap.getValue(PostModule.class);
                          if(posts.getPublisher().equals(profileID)){
                              counter++;
                          }
                        }
                        postnumber.setText(counter+"");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void GetFollowingCount() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileID).
                child("following").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        follwingNumber.setText(""+snapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void GetFollowersCount() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileID).
                child("Followers").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       followersNumber.setText(""+snapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void GetUserInfo() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(profileID).
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModle user =snapshot.getValue(UserModle.class);
                Picasso.get().load(user.getImageUrl()).into(imgProfile);
                userName.setText(user.getUserName());
                fullName.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}