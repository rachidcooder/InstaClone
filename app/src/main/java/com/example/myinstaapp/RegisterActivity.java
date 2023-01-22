package com.example.myinstaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
private EditText name,username,email,password;
private Button Register;
private TextView logiN;
//private FirebaseDatabase firebaseDatabase;
private DatabaseReference db_ref;
private FirebaseAuth mAuth;
ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=findViewById(R.id.nameID);
        username=findViewById(R.id.usernameID);
        email=findViewById(R.id.emailID);
        password=findViewById(R.id.passID);
        Register=findViewById(R.id.btn_regesterID);
        logiN=findViewById(R.id.alreadyloginID);
        pd=new ProgressDialog(this);
        //
        db_ref=FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        logiN.setOnClickListener(view -> {
            startActivity(new Intent(this,LoginActivity.class));
        });
        Register.setOnClickListener(v ->{
            String txtUsername=username.getText().toString();
            String txtName=name.getText().toString();
            String txtEmail=email.getText().toString();
            String txtPasswod=password.getText().toString();
            if(TextUtils.isEmpty(txtUsername)||TextUtils.isEmpty(txtName)||
                    TextUtils.isEmpty(txtEmail)||TextUtils.isEmpty(txtPasswod)){
                Toast.makeText(this, "some fields are Empty !", Toast.LENGTH_LONG).show();
            }else if(txtPasswod.length()<6){
                Toast.makeText(this, "Password too short", Toast.LENGTH_SHORT).show();
            }else{
                //code to  add information in firebase :
                RegisterUser(txtUsername,txtName,txtEmail,txtPasswod);
            }
        });
    }

    private void RegisterUser(String username, String name, String email, String password) {
        pd.setMessage("please wait!");
        pd.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String,Object> map=new HashMap<>();
                map.put("name",name);
                map.put("userName",username);
                map.put("id",mAuth.getCurrentUser().getUid());
                map.put("bio","");
                map.put("imageUrl","https://firebasestorage.googleapis.com/v0/b/myinstaapp-b3a7a.appspot.com/o/Posts%2F1663059589347.jpg?alt=media&token=ad4233bf-da3e-4536-9870-17949675ed57");

                db_ref.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "updat the profile for better Expereince", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
                            pd.dismiss();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, ""+e.toString(), Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        });
    }
}