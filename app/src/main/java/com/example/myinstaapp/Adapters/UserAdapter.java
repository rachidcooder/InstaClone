package com.example.myinstaapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myinstaapp.Moduls.UserModle;
import com.example.myinstaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.VHolder>{
    Context mcontext;
    ArrayList<UserModle> listOfUsers;
    FirebaseUser firebaseUser;
    boolean isFragment;
    //constractor :


    public void setListOfUsers(ArrayList<UserModle> listOfUsers) {
        this.listOfUsers = listOfUsers;
    }

    public UserAdapter(Context mcontext, ArrayList<UserModle> listUsers, boolean isfragment) {
        this.mcontext = mcontext;
        this.listOfUsers = listUsers;
        this.isFragment=isfragment;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(mcontext).inflate(R.layout.useritemlookssearch,parent,false);
        return new UserAdapter.VHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        UserModle users=listOfUsers.get(position);
        holder.userName.setText(users.getUserName());
        holder.FullName.setText(users.getName());
        // get & set Image Profile :
        Picasso.get().load(users.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(holder.imgProfile);
        // check for Following :
        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.btnFollow.getText().toString().equals("follow")){
                    FirebaseDatabase.getInstance().getReference()
                            .child("Follow").child(firebaseUser.getUid()).child("following").child(users.getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").
                            child(users.getId()).child("Followers").child(firebaseUser.getUid()).setValue(true);
                }else{
                    FirebaseDatabase.getInstance().getReference()
                            .child("Follow").child(firebaseUser.getUid()).child("following").child(users.getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").
                            child(users.getId()).child("Followers").child(firebaseUser.getUid()).removeValue();
                }
            }
        });
        isUserFollow(users.getId(),holder.btnFollow);
        if(users.getId()==firebaseUser.getUid()){
            holder.btnFollow.setVisibility(View.GONE);
        }
    }

    private void isUserFollow(String id, Button btnFollow) {
        DatabaseReference refDB= FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");
        refDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(id).exists()){
                    btnFollow.setText("following".toLowerCase(Locale.ROOT));
                }else{
                   btnFollow.setText("follow".toLowerCase(Locale.ROOT));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfUsers.size();
    }

    class VHolder extends RecyclerView.ViewHolder {
        CircleImageView imgProfile;
        TextView userName,FullName;
        Button btnFollow;
        public VHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile=itemView.findViewById(R.id.profile_image);
            userName=itemView.findViewById(R.id.usernamesearchID);
            FullName=itemView.findViewById(R.id.fullnamesearID);
            btnFollow=itemView.findViewById(R.id.btn_followID);
        }
    }
}
