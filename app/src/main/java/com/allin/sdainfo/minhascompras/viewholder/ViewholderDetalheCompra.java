package com.allin.sdainfo.minhascompras.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allin.sdainfo.minhascompras.R;

public class ViewholderDetalheCompra extends RecyclerView.ViewHolder {
    public TextView primeiro, segundo, terceiro, quarto;

    public ViewholderDetalheCompra(@NonNull View itemView) {
        super(itemView);

        primeiro = itemView.findViewById(R.id.tv_adapter_detalhe_compras_nome);
        segundo = itemView.findViewById(R.id.tv_adapter_detalhe_compras_preco);
        terceiro = itemView.findViewById(R.id.tv_adapter_detalhe_compras_qte);
        quarto = itemView.findViewById(R.id.tv_adapter_detalhe_compras_valor);
    }
}
