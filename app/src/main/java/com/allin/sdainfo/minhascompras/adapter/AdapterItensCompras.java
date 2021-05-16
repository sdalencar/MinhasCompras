package com.allin.sdainfo.minhascompras.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allin.sdainfo.minhascompras.R;
import com.allin.sdainfo.minhascompras.model.ItensCompras;
import com.allin.sdainfo.minhascompras.viewholder.ViewholderItensCompras;

import java.util.List;

public class AdapterItensCompras extends RecyclerView.Adapter<ViewholderItensCompras> {

    private List<ItensCompras> lista;
    private Context context;

    public AdapterItensCompras(List<ItensCompras> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewholderItensCompras onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_compras, parent, false);

        return new ViewholderItensCompras(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewholderItensCompras holder, int position) {

        ItensCompras item = lista.get(position);
        holder.primeiro.setText(String.valueOf(item.getProduto()));
        holder.segundo.setText(String.valueOf(item.getQtde()));
        holder.terceiro.setText(String.valueOf(item.getTotal()));

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}

