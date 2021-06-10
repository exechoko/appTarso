package com.emdev.tarso.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emdev.tarso.Interface.ItemClickListener;
import com.emdev.tarso.R;

public class DocumentosViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    public ImageView doc_imagen, doc_download, doc_compartir;
    public TextView doc_nombre, doc_materia, doc_fecha, doc_nota, doc_concepto, doc_creador;

    private ItemClickListener itemClickListener;

    public DocumentosViewHolder(@NonNull View itemView) {
        super(itemView);

        doc_nombre = (TextView)itemView.findViewById(R.id.doc_nombre);
        doc_materia = (TextView)itemView.findViewById(R.id.doc_materia);
        doc_fecha = (TextView)itemView.findViewById(R.id.doc_fecha);
        doc_nota = (TextView)itemView.findViewById(R.id.doc_nota);
        doc_concepto = (TextView)itemView.findViewById(R.id.doc_concepto);
        doc_creador = (TextView)itemView.findViewById(R.id.doc_creador);
        doc_imagen = (ImageView) itemView.findViewById(R.id.doc_imagen);
        doc_download = (ImageView) itemView.findViewById(R.id.descargar);
        doc_compartir = (ImageView) itemView.findViewById(R.id.compartir);

        //click para cada item
        itemView.setOnClickListener(this);

        //para el menu contextual de las categorias (cuando mantenes apretado)
        //itemView.setOnCreateContextMenuListener((View.OnCreateContextMenuListener) this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Selecciona una acción");

        menu.add(0,0,getAdapterPosition(), "AGREGAR NOTA");
        menu.add(0,1,getAdapterPosition(), "AGREGAR CONCEPTO");

    }*/

}
