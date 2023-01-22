package com.example.myinstaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
private ImageView iconInsta,backInsta;
private LinearLayout linLayout;
private Button logIN,Register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iconInsta=findViewById(R.id.istaIconID);
        backInsta=findViewById(R.id.imagbackID);
        linLayout=findViewById(R.id.lnyerL_ID);
        logIN=findViewById(R.id.loginID);
        Register=findViewById(R.id.registerID);
        //
        linLayout.animate().alpha(0f).setDuration(10);

        TranslateAnimation animation=new TranslateAnimation(0,0,0,-1000);
        animation.setDuration(2000);
        animation.setFillAfter(true);
        animation.setAnimationListener(new myAnimation());
        iconInsta.setAnimation(animation);
        // listeners :
        logIN.setOnClickListener(v->{
            startActivity(new Intent(this,LoginActivity.class));
        });
        Register.setOnClickListener(v->{
            startActivity(new Intent(this,RegisterActivity.class));
        });

    }
    class myAnimation implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            iconInsta.clearAnimation();
            iconInsta.setVisibility(View.INVISIBLE);
            linLayout.animate().alpha(1f).setDuration(3000);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(this,HomeActivity.class));
        }
    }
}