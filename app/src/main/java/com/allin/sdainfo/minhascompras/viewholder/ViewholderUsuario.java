package com.allin.sdainfo.minhascompras.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allin.sdainfo.minhascompras.R;

public class ViewholderUsuario extends RecyclerView.ViewHolder {
    public TextView primeiro, segundo;

    public ViewholderUsuario(@NonNull View itemView) {
        super(itemView);

        primeiro = itemView.findViewById(R.id.tv_adapter_compra_nome);
        segundo = itemView.findViewById(R.id.tv_adapter_compra_data);
    }
}
