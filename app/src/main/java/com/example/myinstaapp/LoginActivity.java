package com.example.myinstaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
private EditText email,password;
private Button logIN;
private TextView newAccount;
FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.emailloginID);
        password=findViewById(R.id.passloginID);
        logIN=findViewById(R.id.btn_loginID);
        newAccount=findViewById(R.id.newAccountID);

        mAuth=FirebaseAuth.getInstance();
        newAccount.setOnClickListener(v->{
            startActivity(new Intent(this,RegisterActivity.class));
        });
        logIN.setOnClickListener(v->{
            String txtEmail=email.getText().toString();
            String txtPassword =password.getText().toString();

            LoginM(txtEmail,txtPassword);
        });
    }

    private void LoginM(String Email, String Password) {
        if(!TextUtils.isEmpty(Email)||!TextUtils.isEmpty(Password)){
            mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "some Fields are Empty", Toast.LENGTH_SHORT).show();
        }
    }

}