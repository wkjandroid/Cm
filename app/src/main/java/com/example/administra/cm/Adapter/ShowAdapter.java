package com.example.administra.cm.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administra.cm.R;
import com.example.administra.cm.po.ShowOwn;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administra on 2017/3/28.
 */

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ViewHolder> {
    private List<ShowOwn> SList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
       ImageView imageView;
        TextView textView;
        LinearLayout linearLayout;
        public ViewHolder(View view){
            super(view);
            circleImageView=(CircleImageView)view.findViewById(R.id.touxiang);
            imageView=(ImageView)view.findViewById(R.id.image);
            textView=(TextView)view.findViewById(R.id.name);
            linearLayout=(LinearLayout)view.findViewById(R.id.ownshow);

        }
    }
    public ShowAdapter(List<ShowOwn> mlist){
        SList=mlist;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_use,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ShowAdapter.ViewHolder holder, int position) {
        ShowOwn showOwn=SList.get(position);
        holder.textView.setText(showOwn.getName());
        holder.linearLayout.setBackgroundResource(showOwn.getLayoutID());
        holder.circleImageView.setImageResource(showOwn.getTouID());
        holder.imageView.setImageBitmap(showOwn.getImageID());

    }

    @Override
    public int getItemCount() {
        return SList.size();
    }
}
