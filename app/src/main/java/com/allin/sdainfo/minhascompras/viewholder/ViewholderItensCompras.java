package com.allin.sdainfo.minhascompras.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allin.sdainfo.minhascompras.R;

public class ViewholderItensCompras extends RecyclerView.ViewHolder {
    public TextView primeiro, segundo, terceiro;

    public ViewholderItensCompras(@NonNull View itemView) {
        super(itemView);

        primeiro = itemView.findViewById(R.id.tv_adapter_compras_prod);
        segundo = itemView.findViewById(R.id.tv_adapter_compras_qte);
        terceiro = itemView.findViewById(R.id.tv_adapter_compras_valor);
    }
}
