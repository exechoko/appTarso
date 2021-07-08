package com.emdev.tarso.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emdev.tarso.Interface.ItemClickListener;
import com.emdev.tarso.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminFotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView img_foto;
    public CircleImageView foto_delete;
    public TextView foto_creador;

    private ItemClickListener itemClickListener;

    public AdminFotoViewHolder(@NonNull View itemView) {
        super(itemView);

        foto_creador = (TextView)itemView.findViewById(R.id.foto_creador);
        foto_delete = (CircleImageView)itemView.findViewById(R.id.foto_delete);
        img_foto = (ImageView)itemView.findViewById(R.id.img_foto);

        //click para cada item
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) { itemClickListener.onClick(v,getAdapterPosition(),false); }
}
