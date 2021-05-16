package com.allin.sdainfo.minhascompras.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allin.sdainfo.minhascompras.R;
import com.allin.sdainfo.minhascompras.model.ItensListas;
import com.allin.sdainfo.minhascompras.viewholder.ViewholderCriarLista;

import java.util.List;

public class AdapterCriarLista extends RecyclerView.Adapter<ViewholderCriarLista> {

    private List<ItensListas> lista;
    private Context context;

    public AdapterCriarLista(List<ItensListas> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewholderCriarLista onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista_produto, parent, false);

        return new ViewholderCriarLista(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewholderCriarLista holder, int position) {

        ItensListas item = lista.get(position);
        holder.primeiro.setText(item.getProduto());

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}

