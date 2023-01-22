package com.example.myinstaapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myinstaapp.HomeActivity;
import com.example.myinstaapp.MainActivity;
import com.example.myinstaapp.Moduls.CommentModule;
import com.example.myinstaapp.Moduls.UserModle;
import com.example.myinstaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.holderV> {
Context mCxt;
ArrayList<CommentModule> arrComments;
String postID;
FirebaseUser fUser;
    public CommentAdapter(Context mCxt, ArrayList<CommentModule> arrComments,String postID) {
        this.mCxt = mCxt;
        this.arrComments = arrComments;
        this.postID=postID;
        fUser= FirebaseAuth.getInstance().getCurrentUser();
    }

    public void setArrComments(ArrayList<CommentModule> arrComments) {
        this.arrComments = arrComments;
    }

    @NonNull
    @Override
    public holderV onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v= LayoutInflater.from(mCxt) .inflate(R.layout.itemcomment,parent,false) ;
        return new holderV(v);
    }

    @Override
    public void onBindViewHolder(@NonNull holderV holder, int position) {
        CommentModule comments= arrComments.get(position);
        holder.txtComment.setText(comments.getComment());

        FirebaseDatabase.getInstance().getReference().child("Users").child(comments.getPublisher()).
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModle user=snapshot.getValue(UserModle.class) ;
                holder.txtuser.setText(user.getUserName());
                if(user.getImageUrl().equals("default")){
                }else{
                    Picasso.get().load(user.getImageUrl()).into(holder.imgV);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.txtComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mCxt, HomeActivity.class);
                intent.putExtra("publisherID",comments.getPublisher());
                mCxt.startActivity(intent);
            }
        });
        holder.imgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mCxt, HomeActivity.class);
                intent.putExtra("publisherID",comments.getPublisher());
                mCxt.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(comments.getPublisher().endsWith(fUser.getUid())){
                    AlertDialog dialog=new AlertDialog.Builder(mCxt).create();
                    dialog.setTitle("Do you want to delet comment?");
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // code to delet comment here :
                            FirebaseDatabase.getInstance().getReference().child("Comments").child(postID).
                                    child(comments.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(mCxt, "comment deleted successfuly!", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }
                                    });


                        }
                    });
                    dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrComments.size();
    }

    class holderV extends RecyclerView.ViewHolder {
        CircleImageView imgV;
        TextView txtuser,txtComment;
        public holderV(@NonNull View itemView) {
            super(itemView);
            imgV=itemView.findViewById(R.id.profileCommenter);
            txtuser=itemView.findViewById(R.id.usernameCommenterID);
            txtComment=itemView.findViewById(R.id.txtCommentID);
        }
    }
}
