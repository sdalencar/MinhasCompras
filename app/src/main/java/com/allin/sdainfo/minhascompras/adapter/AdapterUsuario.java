package com.allin.sdainfo.minhascompras.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allin.sdainfo.minhascompras.R;
import com.allin.sdainfo.minhascompras.model.Usuario;
import com.allin.sdainfo.minhascompras.viewholder.ViewholderUsuario;

import java.util.List;

public class AdapterUsuario extends RecyclerView.Adapter<ViewholderUsuario> {

    private List<Usuario> lista;
    private Context context;

    public AdapterUsuario(List<Usuario> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewholderUsuario onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_usuario, parent, false);

        return new ViewholderUsuario(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewholderUsuario holder, int position) {

        Usuario user = lista.get(position);
        holder.primeiro.setText("nome de usuário : " + user.getNome());
        holder.segundo.setText("e-mail cadastrado : "+user.getEmail());

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}

