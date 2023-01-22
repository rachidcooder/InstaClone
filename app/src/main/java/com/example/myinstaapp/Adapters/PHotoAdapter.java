package com.example.myinstaapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myinstaapp.Moduls.PostModule;
import com.example.myinstaapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PHotoAdapter extends RecyclerView.Adapter<PHotoAdapter.vHolder> {
    Context Cxt;
    ArrayList<PostModule> listOfPHoto=new ArrayList<>();

    public PHotoAdapter(Context cxt, ArrayList<PostModule> listOfPHoto) {
        Cxt = cxt;
        this.listOfPHoto = listOfPHoto;
    }

    public void setListOfPHoto(ArrayList<PostModule> listOfPHoto) {
        this.listOfPHoto = listOfPHoto;
    }

    @NonNull
    @Override
    public vHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(Cxt).inflate(R.layout.itemphoto,parent,false);
        return new vHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull vHolder holder, int position) {
        PostModule photos= listOfPHoto.get(position);
           Picasso.get().load(photos.getImagUrl()).placeholder(R.mipmap.ic_launcher).into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return listOfPHoto.size();
    }

    class vHolder extends RecyclerView.ViewHolder{
   private ImageView photo;
        public vHolder(@NonNull View itemView) {
            super(itemView);
            photo=itemView.findViewById(R.id.photoID);
        }
    }
}
