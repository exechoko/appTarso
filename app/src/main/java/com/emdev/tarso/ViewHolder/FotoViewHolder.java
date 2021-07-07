package com.emdev.tarso.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emdev.tarso.Interface.ItemClickListener;
import com.emdev.tarso.R;

public class FotoViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    public ImageView foto;
    public TextView nombreFoto;
    private ItemClickListener itemClickListener;

    public FotoViewHolder(@NonNull View itemView) {
        super(itemView);

        foto = (ImageView) itemView.findViewById(R.id.foto);
        nombreFoto = (TextView) itemView.findViewById(R.id.nombreFoto);

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
