package com.example.myinstaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myinstaapp.Adapters.CommentAdapter;
import com.example.myinstaapp.Moduls.CommentModule;
import com.example.myinstaapp.Moduls.UserModle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {
   private CircleImageView imgProfile;
   private TextView postComment;
   private EditText commentText;
  private Toolbar toolbar;
private RecyclerView rvComments;
 private String postID,autherID;
 private FirebaseUser userF;
 private ArrayList<CommentModule> listOfComments;
private CommentAdapter adapterCom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        rvComments=findViewById(R.id.rvListofCommentID);
        imgProfile=findViewById(R.id.profileyimGComment);
        postComment=findViewById(R.id.postCommentID);
        commentText=findViewById(R.id.edtTextCommentID);
        toolbar=findViewById(R.id.toolbarInCommentID);
        Intent intent=getIntent();
        postID=intent.getStringExtra("postID");
        autherID= intent.getStringExtra("autherID");
        listOfComments=new ArrayList<>();
        adapterCom=new CommentAdapter(this,listOfComments,postID);
                   GetComments();
  userF= FirebaseAuth.getInstance().getCurrentUser();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
               getImagProfile();
    postComment.setOnClickListener(v->{
        if(!TextUtils.isEmpty(commentText.getText().toString())){
              putComment();
              commentText.setText("");
        }else{
            Toast.makeText(this, "no comment added!",Toast.LENGTH_SHORT).show();
        }

        });

        LinearLayoutManager lm=new LinearLayoutManager(this);
        lm.setReverseLayout(true);
        lm.setStackFromEnd(true);
        rvComments.setLayoutManager(lm);
        rvComments.setHasFixedSize(true);
        rvComments.setAdapter(adapterCom);
    }

    private void getImagProfile() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(userF.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModle user=snapshot.getValue(UserModle.class);
                if(user.getImageUrl().equals("default")){

                }else{
                    Picasso.get().load(user.getImageUrl()).into(imgProfile);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void putComment(){
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference().child("Comments").child(postID);
        String id= dbRef.push().getKey();
        HashMap<String ,Object> map=new HashMap<>();
        map.put("id",id);
        map.put("comment",commentText.getText().toString());
        map.put("publisher",userF.getUid());
       dbRef.child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                   // Toast.makeText(CommentActivity.this, "Comment added Successful", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CommentActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void GetComments(){
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postID)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfComments.clear();
                for(DataSnapshot snap :snapshot.getChildren()){
                    CommentModule comment=snap.getValue(CommentModule.class);
                    listOfComments.add(comment);
                }
                adapterCom.setArrComments(listOfComments);
                adapterCom.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}