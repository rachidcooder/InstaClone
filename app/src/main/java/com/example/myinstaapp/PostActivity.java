package com.example.myinstaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {
    private static final int CODE_RES = 5;
    ImageView close,imgAdded;
    EditText discription;
    TextView post;
    String imGUrl;
    Uri imgUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        close=findViewById(R.id.closeID);
        imgAdded=findViewById(R.id.addphotoID);
        discription=findViewById(R.id.descriptionID);
        post=findViewById(R.id.postID);

        LoadPhoto();
        close.setOnClickListener(v->{
            startActivity(new Intent(this,HomeActivity.class));
        });

        post.setOnClickListener(v->{
            LoadImgTstorage();
        });
    }

    private void LoadImgTstorage() {
        ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        if (imgUri != null) {


        StorageReference storageRef= FirebaseStorage.getInstance().getReference()
                  .child("Posts").child(System.currentTimeMillis()+"."+getExtention(imgUri));
     StorageTask uploadTask=storageRef.putFile(imgUri);
        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if(!task.isSuccessful()){
                  throw task.getException();
                }

                Toast.makeText(PostActivity.this, "the uploadTask successful", Toast.LENGTH_SHORT).show();
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri downloadUri=task.getResult();
                imGUrl= downloadUri.toString();

            DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Posts");
            String  postID=dbRef.push().getKey();

            HashMap<String,Object> map=new HashMap<>();
            map.put("postid",postID);
            map.put("imagUrl",imGUrl);
            map.put("description",discription.getText().toString());
            map.put("publisher",FirebaseAuth.getInstance().getCurrentUser().getUid());

            dbRef.child(postID).setValue(map);

                pd.dismiss();
            startActivity(new Intent(PostActivity.this,HomeActivity.class));
            finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
      pd.dismiss();
                Toast.makeText(PostActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    else{
            Toast.makeText(this, "the imageUrl is null!", Toast.LENGTH_SHORT).show();
    }
    }



    private String getExtention(Uri uri) {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        String Extention =mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        return Extention ;
    }

    private void LoadPhoto() {
        Intent intent=new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,CODE_RES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CODE_RES&&resultCode==RESULT_OK&&data.getData()!=null)
        {
            imgUri=data.getData();
            imgAdded.setImageURI(data.getData());

        }

    }
}