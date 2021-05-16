package com.allin.sdainfo.minhascompras.act;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allin.sdainfo.minhascompras.R;
import com.allin.sdainfo.minhascompras.adapter.AdapterItensCompras;
import com.allin.sdainfo.minhascompras.config.ConfigurarFirebase;
import com.allin.sdainfo.minhascompras.helper.Constantes;
import com.allin.sdainfo.minhascompras.helper.Datas;
import com.allin.sdainfo.minhascompras.helper.Mensagens;
import com.allin.sdainfo.minhascompras.helper.RecyclerItemClickListener;
import com.allin.sdainfo.minhascompras.model.Compras;
import com.allin.sdainfo.minhascompras.model.ItensCompras;
import com.allin.sdainfo.minhascompras.model.ItensListas;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ActivityComprar extends AppCompatActivity {
    private EditText eproduto, epreco, eqtde, etotal;
    private TextView tv_soma_total, tv_produto;
    private String sproduto;
    private String id_compra;
    private String id_item = "";

    private double dpreco = 0, dtotal = 0, somaTotal = 0, itotalqde, iqtde;
    private Mensagens msg;

    private RecyclerView recyclerView;
    private AdapterItensCompras adapterCompra;

    private final List<ItensCompras> listaItensDaCompra = new ArrayList<>();
    private ItensCompras itensCompras;

    private final List<ItensListas> listaDeItens = new ArrayList<>();
    private ItensListas itenslistaParaCompras;

    private Button bt_levar;

    private NumberFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar);

        id_compra = Datas.hoje();
        listarItensCompras();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.voltar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_voltar);

        iniciaComponentes();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapterCompra = new AdapterItensCompras(listaItensDaCompra, this);
        recyclerView.setAdapter(adapterCompra);


        listarProdutosCompras();


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                itensCompras = listaItensDaCompra.get(position);
                id_item = itensCompras.getId();
                eproduto.setText(itensCompras.getProduto());
                epreco.setText(String.valueOf(itensCompras.getPreco()));
                eqtde.setText(String.valueOf(itensCompras.getQtde()));
                etotal.setText(String.valueOf(itensCompras.getTotal()));
                bt_levar.setVisibility(View.GONE);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                itensCompras = listaItensDaCompra.get(position);
                id_item = itensCompras.getId();
                alertaDialogExcluir();
                bt_levar.setVisibility(View.GONE);
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        }
        ));

    }

    @SuppressLint("NonConstantResourceId")
    public void onclickSelecao(View view) {
        switch (view.getId()) {
            case R.id.bt_finalizar:
                if (listaItensDaCompra.size() == 0) {
                    msg.msgLonga("lita vazia");
                }else {
                    alertaFinalizarCompras();
                }
                break;
            case R.id.bt_levar:
                verificaCampos();
                break;
            case R.id.bt_calcular:
                calcular();
                break;
            case R.id.tv_listar_produtos:
                selecionarItem();
                break;
        }
    }

    private void alertaFinalizarCompras() {
        new AlertDialog.Builder(this)
                .setTitle("Finalizar a Compra?")
                .setMessage("Você esta preste a finalizar a compra, tem certeza?")
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.finalizar, (dialogInterface, i) -> {
                    finalizarCompra();
                })
                .create()
                .show();
    }

    private void finalizarCompra() {
        Compras compras = new Compras();
        compras.setId(Datas.hojeHora());
        compras.setItemLista(listaItensDaCompra);
        compras.salvarCompras(Datas.hojeHora());
        itensCompras.deletarCompra(id_compra);
        tv_soma_total.setText(getString(R.string.total_compras) + " 0.00 " + getString(R.string.total_itens) + "0");
        limparCampos();
    }

    private void calcular() {
        if (!epreco.getText().toString().isEmpty() || !eqtde.getText().toString().isEmpty()) {
            dpreco = Double.parseDouble(epreco.getText().toString());
            iqtde = Double.parseDouble(eqtde.getText().toString());
            etotal.setText(df.format(dpreco * iqtde));
        } else {
            msg.msgCurta("preencha os valores do produto");
        }

    }

    private void verificaCampos() {
        String prod = "Sem nome";
        sproduto = eproduto.getText().toString();
        if (!sproduto.equals("")) {
            prod = sproduto;
        }
        dpreco = Double.parseDouble(epreco.getText().toString());
        iqtde = Double.parseDouble(eqtde.getText().toString());
        dtotal = Double.parseDouble(df.format(dpreco * iqtde));


        if (dpreco != 0) {
            if (iqtde != 0) {
                if (dtotal != 0) {
                    addItem(prod, dpreco, iqtde, dtotal);
                } else {
                    msg.msgCurta("calcule a soma do produto.");
                }
            } else {
                msg.msgCurta("qual a quantidade.");
            }
        } else {
            msg.msgCurta("qual o preço.");
        }
    }

    private void addItem(String produto, double preco, double qtde, double total) {

        itensCompras = new ItensCompras();
        itensCompras.setProduto(produto);
        itensCompras.setPreco(preco);
        itensCompras.setQtde(qtde);
        itensCompras.setTotal(total);

        itensCompras.salvarItens(id_compra);

        msg.msgCurta(itensCompras.getProduto() + ", adicionado a lista com sucesso.");

        epreco.setText("");
        eqtde.setText("");
        etotal.setText("");
        eproduto.setText("");
    }

    private void iniciaComponentes() {

        Locale.setDefault(Locale.US); //forçar a usar o ponto ao inves da virgula para separar casas decimais

        msg = new Mensagens(getApplicationContext());

        eproduto = findViewById(R.id.et_lista_compra_produto_nome);
        epreco = findViewById(R.id.et_produto_preco);
        eqtde = findViewById(R.id.tv_produto_qte);
        etotal = findViewById(R.id.tv_produto_total);
        tv_produto = findViewById(R.id.tv_listar_produtos);
        tv_soma_total = findViewById(R.id.tv_soma_total);
        bt_levar = findViewById(R.id.bt_levar);

        recyclerView = findViewById(R.id.rv_produtos);

        df = NumberFormat.getInstance();
        df.setMaximumFractionDigits(9);
        df.setMinimumFractionDigits(2);
        df.setMaximumIntegerDigits(7);
        df.setRoundingMode(RoundingMode.HALF_UP);

    }

    private void limparCampos() {

        eproduto.setText("");
        epreco.setText("");
        eqtde.setText("");
        etotal.setText("");

        sproduto = "";
        dpreco = 0;
        iqtde = 0;
        dtotal = 0;

        tv_produto.setText(R.string.lista_de_compras);

        id_item = "";

        bt_levar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_user_1:
                if (!id_item.isEmpty()) {
                    alertaDialogEditar();
                }
                break;
            case R.id.menu_user_2:
                if (!id_item.isEmpty()) {
                    alertaDialogExcluir();
                }
                break;
            case R.id.menu_user_3:
                startActivity(new Intent(ActivityComprar.this, ActivityListarCompras.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    private void alertaDialogEditar() {
        new AlertDialog.Builder(this)
                .setTitle("Atenção:")
                .setMessage("Você esta preste a mudar as informções de " + itensCompras.getProduto() + ", tem certeza?")
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.alterar, (dialogInterface, i) -> {
                    sproduto = eproduto.getText().toString();
                    itensCompras.setPreco(Double.parseDouble(epreco.getText().toString()));
                    itensCompras.setProduto(eproduto.getText().toString());
                    itensCompras.setQtde(Integer.parseInt(eqtde.getText().toString()));
                    itensCompras.setTotal(Double.parseDouble(etotal.getText().toString()));
                    itensCompras.atualizarItens(id_compra, id_item);
                    msg.msgLonga("alterando...\na compra do dia : " + itensCompras.getProduto() + ", na base de dados.");
                    limparCampos();
                    listaItensDaCompra.clear();
                })
                .create()
                .show();
    }

    private void alertaDialogExcluir() {
        new AlertDialog.Builder(this)
                .setTitle("Atenção:")
                .setMessage("Você esta preste a apagar,\nA compra do dia : " + itensCompras.getProduto() + ", tem certeza?")
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.apagar, (dialogInterface, i) -> {
                    itensCompras.deletarItens(id_compra, id_item);
                    msg.msgLonga("apagando...\n" + itensCompras.getProduto() + ", na base de dados.");
                    limparCampos();
                })
                .create()
                .show();
    }

    private void listarProdutosCompras() {
        DatabaseReference tb_itens = ConfigurarFirebase.getBanco().child(Constantes.TB_COMPRA).child(id_compra);

        tb_itens.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaItensDaCompra.clear();
                somaTotal = 0;
                itotalqde = 0;
                for (DataSnapshot item : snapshot.getChildren()) {
                    itensCompras = item.getValue(ItensCompras.class);

                    somaTotal += Objects.requireNonNull(itensCompras).getTotal(); // somar valor
                    itotalqde += itensCompras.getQtde();   // somar qtde
                    tv_soma_total.setText(getString(R.string.total_compras) + df.format(somaTotal) + getString(R.string.total_itens) + itotalqde);

                    listaItensDaCompra.add(itensCompras);
                }

                Collections.reverse(listaItensDaCompra);
                adapterCompra.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void listarItensCompras() {
        Query tb_itens = ConfigurarFirebase.getBanco().child(Constantes.TB_LISTA).orderByChild(Constantes.PRODUTO);
        tb_itens.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaDeItens.clear();
                for (DataSnapshot item : snapshot.getChildren()) {
                    itenslistaParaCompras = item.getValue(ItensListas.class);
                    listaDeItens.add(itenslistaParaCompras);
                }
                adapterCompra.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void selecionarItem() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione um Produto");
        builder.setIcon(R.drawable.ic_lista);

        View viewSpinner = getLayoutInflater().inflate(R.layout.dialogo_spinner, null);

        Spinner spinner = viewSpinner.findViewById(R.id.spinner_Lista);

        ArrayAdapter<ItensListas> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaDeItens);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        builder.setView(viewSpinner);

        builder.setPositiveButton("Confirmar", (dialogInterface, i) -> eproduto.setText(spinner.getSelectedItem().toString()));

        builder.setNegativeButton("Cancelar", (dialogInterface, i) -> {

        });

        Dialog dialog = builder.create();
        dialog.show();
    }

}