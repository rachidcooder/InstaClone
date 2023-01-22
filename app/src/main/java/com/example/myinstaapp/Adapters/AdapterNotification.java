package com.example.myinstaapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myinstaapp.Moduls.notificationModle;
import com.example.myinstaapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.vHolder>{

    Context mCxt;
    ArrayList<notificationModle> notifications=new ArrayList<>();

    public AdapterNotification(Context cxt, ArrayList<notificationModle> listOfNotis) {
        mCxt = cxt;
        this.notifications = listOfNotis;
    }

    @NonNull
    @Override
    public vHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vItem = LayoutInflater.from(mCxt).inflate(R.layout.itemnotification,parent,false);
        return new vHolder(vItem);
    }

    @Override
    public void onBindViewHolder(@NonNull vHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class vHolder extends RecyclerView.ViewHolder{

        TextView userN,textnoti;
        CircleImageView imgProNoti;
        ImageView imgPostNoti;
        public vHolder(@NonNull View itemView) {
            super(itemView);

            userN=itemView.findViewById(R.id.txtUsernameNotiID);
            textnoti=itemView.findViewById(R.id.txtNotiID);
            imgProNoti=itemView.findViewById(R.id.imgProfileNotiID);
            imgPostNoti=itemView.findViewById(R.id.imgPostInNotiID);
        }
    }
}
