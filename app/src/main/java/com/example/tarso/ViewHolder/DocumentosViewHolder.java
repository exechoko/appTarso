package com.example.tarso.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tarso.Interface.ItemClickListener;
import com.example.tarso.R;

public class DocumentosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView doc_imagen, doc_download, doc_compartir;
    public TextView doc_nombre, doc_materia, doc_fecha;

    private ItemClickListener itemClickListener;

    public DocumentosViewHolder(@NonNull View itemView) {
        super(itemView);

        doc_nombre = (TextView)itemView.findViewById(R.id.doc_nombre);
        doc_materia = (TextView)itemView.findViewById(R.id.doc_materia);
        doc_fecha = (TextView)itemView.findViewById(R.id.doc_fecha);
        doc_imagen = (ImageView) itemView.findViewById(R.id.doc_imagen);
        doc_download = (ImageView) itemView.findViewById(R.id.descargar);
        doc_compartir = (ImageView) itemView.findViewById(R.id.compartir);

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
