package com.allin.sdainfo.minhascompras.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allin.sdainfo.minhascompras.R;
import com.allin.sdainfo.minhascompras.model.Compras;
import com.allin.sdainfo.minhascompras.viewholder.ViewholderListaCompra;

import java.util.List;

public class AdapterListaCompra extends RecyclerView.Adapter<ViewholderListaCompra> {

    private List<Compras> lista;
    private Context context;

    public AdapterListaCompra(List<Compras> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewholderListaCompra onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista_produto, parent, false);

        return new ViewholderListaCompra(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewholderListaCompra holder, int position) {

        Compras compras = lista.get(position);
        holder.primeiro.setText(compras.getId());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}

