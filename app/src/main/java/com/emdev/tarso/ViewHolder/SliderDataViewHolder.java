package com.emdev.tarso.ViewHolder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emdev.tarso.Interface.ItemClickListener;
import com.emdev.tarso.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SliderDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView slider_imagen;
    public CircleImageView slider_delete;
    public TextView slider_creador;

    private ItemClickListener itemClickListener;

    public SliderDataViewHolder(@NonNull View itemView) {
        super(itemView);

        slider_creador = (TextView)itemView.findViewById(R.id.slider_creador);
        slider_delete = (CircleImageView)itemView.findViewById(R.id.slider_delete);
        slider_imagen = (ImageView)itemView.findViewById(R.id.slider_imagen);

        //click para cada item
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
