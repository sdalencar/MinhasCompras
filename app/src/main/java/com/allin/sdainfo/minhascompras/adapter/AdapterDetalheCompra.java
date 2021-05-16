package com.allin.sdainfo.minhascompras.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allin.sdainfo.minhascompras.R;
import com.allin.sdainfo.minhascompras.model.Compras;
import com.allin.sdainfo.minhascompras.model.ItensListas;
import com.allin.sdainfo.minhascompras.viewholder.ViewholderCriarLista;
import com.allin.sdainfo.minhascompras.viewholder.ViewholderDetalheCompra;

import java.util.List;

public class AdapterDetalheCompra extends RecyclerView.Adapter<ViewholderDetalheCompra> {

    private List<Compras> lista;
    private Context context;

    public AdapterDetalheCompra(List<Compras> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewholderDetalheCompra onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_detale_compra, parent, false);

        return new ViewholderDetalheCompra(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewholderDetalheCompra holder, int position) {

        Compras item = lista.get(position);
        holder.primeiro.setText(item.getItemLista().indexOf(0));
        holder.segundo.setText(item.getItemLista().indexOf(1));
        holder.terceiro.setText(item.getItemLista().indexOf(2));
        holder.quarto.setText(item.getItemLista().indexOf(3));

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}

