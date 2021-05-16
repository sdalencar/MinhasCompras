package com.allin.sdainfo.minhascompras.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allin.sdainfo.minhascompras.R;

public class ViewholderListaCompra extends RecyclerView.ViewHolder {
    public TextView primeiro;

    public ViewholderListaCompra(@NonNull View itemView) {
        super(itemView);

        primeiro = itemView.findViewById(R.id.tv_adapter_lista_produto_nome);
    }
}
