package com.example.myinstaapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myinstaapp.Moduls.UserModle;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivitytoEditPorfile extends AppCompatActivity {

    private static final int CODE_RES = 8;
    private LinearLayout EditProfileClick;
private EditText edtName,edtUserName,edtWebsite,edtBio;
private CircleImageView imgProfile;
private ImageView close;
private TextView save;
private FirebaseUser fUser;
 private Uri prUri;
    private String strUriofImgProfle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activityto_edit_porfile);
        fUser=FirebaseAuth.getInstance().getCurrentUser();
        EditProfileClick=findViewById(R.id.layouChangeProfileID);
        edtName=findViewById(R.id.NameEditProfileID);
        edtUserName=findViewById(R.id.userNinEditprofile);
        edtWebsite=findViewById(R.id.websiteEditprfID);
        edtBio=findViewById(R.id.bioEditID);
        imgProfile=findViewById(R.id.profileImgEditProfile);
        close=findViewById(R.id.editCloseID);
        save=findViewById(R.id.saveafterEditprofile);

        setInfoUser();
        EditProfileClick.setOnClickListener(v ->{
            LoadImgPfofile();
        });
        save.setOnClickListener(v->{
            if(TextUtils.isEmpty(edtUserName.getText().toString())||TextUtils.isEmpty(edtName.getText().toString())){
                Toast.makeText(this, "please Enter Name and UserName", Toast.LENGTH_SHORT).show();
            }else{
                String uname=edtUserName.getText().toString();
                String name=edtName.getText().toString();
                String website=edtWebsite.getText().toString();
                String bio=edtBio.getText().toString();
               if(TextUtils.isEmpty(bio)||TextUtils.isEmpty(website)){
                  website="";
                    bio="";
                   LoadDataToserver(uname,name,website,bio);
               }
                LoadDataToserver(uname,name,website,bio);
               finish();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
    }

    private void setInfoUser() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModle user=snapshot.getValue(UserModle.class);
                        edtName.setText(user.getName());
                        edtUserName.setText(user.getUserName());
                        edtBio.setText(user.getBio());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ActivitytoEditPorfile.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getExtention(Uri uri) {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        String Extention =mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        return Extention ;
    }

    private void LoadImgPfofile() {
        Intent intent=new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,CODE_RES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CODE_RES&&resultCode==RESULT_OK){
            prUri=data.getData();
            imgProfile.setImageURI(prUri);

        }
    }

    private void LoadDataToserver(String uname, String name, String website, String bio) {
        if(prUri!=null){
        StorageReference storageRef=FirebaseStorage.getInstance().getReference().child("ProfilesPhoto")
                .child(System.currentTimeMillis()+"."+getExtention(prUri));
            StorageTask task=storageRef.putFile(prUri);
            task.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful())
                        throw task.getException();
                    return storageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri=task.getResult();
                    strUriofImgProfle = downloadUri.toString();

                    DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(fUser.getUid());
                    String id= dbRef.getKey();
                    HashMap<String,Object> map=new HashMap<>();
                    map.put("userName",uname);
                    map.put("name",name);
                    map.put("bio",bio);
                    map.put("id",id);
                    map.put("imageUrl",strUriofImgProfle);
                    dbRef.setValue(map);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
    }}
}