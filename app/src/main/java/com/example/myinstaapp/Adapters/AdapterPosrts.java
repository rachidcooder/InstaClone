package com.example.myinstaapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myinstaapp.CommentActivity;
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

public class AdapterPosrts extends RecyclerView.Adapter<AdapterPosrts.Vholder> {
FirebaseUser firebaseUser;
    Context mCxt;
    List<PostModule> arrPosts=new ArrayList<>();

    public AdapterPosrts(Context mCxt, List<PostModule> arrPosts) {
        this.mCxt = mCxt;
        this.arrPosts = arrPosts;
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    }

    public void setArrPosts(List<PostModule> arrPosts) {
        this.arrPosts = arrPosts;
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vpost= LayoutInflater.from(mCxt).inflate(R.layout.postitem,parent,false);
        return new Vholder(vpost);
    }

    @Override
    public void onBindViewHolder(@NonNull Vholder holder, int position) {

        PostModule posts= arrPosts.get(position);
        holder.descriptiontxt.setText(posts.getDescription());
   IsSaved(posts.getPostid(),holder.save);
    Picasso.get().load(posts.getImagUrl()).into(holder.postImG);

    // get User name and User icon:
        FirebaseDatabase.getInstance().getReference().child("Users").child(posts.getPublisher())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModle userModle=snapshot.getValue(UserModle.class);
                if(userModle.getImageUrl()=="default"){
                    holder.ProfileImg.setImageResource(R.mipmap.ic_launcher);
                }else {
                   //Picasso.get().load(userModle.getImagUrl()).placeholder(R.mipmap.ic_launcher).into(holder.ProfileImg);
                }

                holder.userName.setText(userModle.getUserName());
                holder.posterName.setText(userModle.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
// methods
  isLiked(posts.getPostid(), holder.like);
  likesNumber(posts.getPostid(), holder.likeNum);
        CommentNumber(posts.getPostid(), holder.commentNum);

       holder.like.setOnClickListener(view -> {
            if(holder.like.getTag().equals("like")){
                FirebaseDatabase.getInstance().getReference().child("Likes")
                        .child(posts.getPostid()).child(firebaseUser.getUid()).setValue(true);
            }else {
                FirebaseDatabase.getInstance().getReference().child("Likes")
                        .child(posts.getPostid()).child(firebaseUser.getUid()).removeValue();
            }

        });
       holder.comment.setOnClickListener(view -> {
           Intent intent=new Intent(mCxt, CommentActivity.class);
           intent.putExtra("postID",posts.getPostid());
           intent.putExtra("autherID",posts.getPublisher());
          mCxt.startActivity(intent);
        });
       holder.commentNum.setOnClickListener(view -> {
           Intent intent=new Intent(mCxt,CommentActivity.class);
           intent.putExtra("postID",posts.getPostid());
           intent.putExtra("autherID",posts.getPublisher());
           mCxt.startActivity(intent);
       });
       holder.save.setOnClickListener(view -> {
           if(holder.save.getTag().equals("save")) {
               FirebaseDatabase.getInstance().getReference().child("Saves").
                       child(firebaseUser.getUid()).child(posts.getPostid()).setValue(true);
           }else{
               FirebaseDatabase.getInstance().getReference().child("Saves").
                       child(firebaseUser.getUid()).child(posts.getPostid()).removeValue();
           }
       });

    }

    private void IsSaved(String postid, ImageView imagSave) {
        FirebaseDatabase.getInstance().getReference().child("Saves").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postid).exists()){
                   imagSave.setImageResource(R.drawable.ic_bookmarksave);
                   imagSave.setTag("saved");
                }else{
                    imagSave.setImageResource(R.drawable.ic_bookmarkempty);
                    imagSave.setTag("save");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrPosts.size();
    }

    class Vholder extends RecyclerView.ViewHolder {
         TextView posterName, descriptiontxt,publisherUsername,userName;
        TextView likeNum,commentNum;
        ImageView postImG,ProfileImg;
        ImageView like,comment,save;
        public Vholder(@NonNull View itemView) {
            super(itemView);
            descriptiontxt=itemView.findViewById(R.id.textDescrptionID);
            publisherUsername=itemView.findViewById(R.id.usernameInpostID);
            postImG=itemView.findViewById(R.id.postImgId);
  userName=itemView.findViewById(R.id.usernameInpostID);
posterName=itemView.findViewById(R.id.PosterNameID);
            like=itemView.findViewById(R.id.LikeInpostID);
            comment=itemView.findViewById(R.id.CommentID);
           likeNum=itemView.findViewById(R.id.txtLikednumberID);
            commentNum=itemView.findViewById(R.id.nu_of_comentID);
            save=itemView.findViewById(R.id.saveID);
        }
    }
    private void isLiked(String postID,final ImageView imageView){
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postID)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(firebaseUser.getUid()).exists()){
                    imageView.setImageResource(R.drawable.ic_baseline_favorite_24);
                    imageView.setTag("liked");

                }else{
                    imageView.setImageResource(R.drawable.ic_like);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void likesNumber(String postId,TextView TxtLikesNum){
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId).
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TxtLikesNum.setText(snapshot.getChildrenCount()+" likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void CommentNumber(String postid,TextView txtCommentNum){
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postid)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txtCommentNum.setText("View all "+snapshot.getChildrenCount()+" Comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
