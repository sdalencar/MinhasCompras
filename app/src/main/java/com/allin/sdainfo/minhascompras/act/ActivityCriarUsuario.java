package com.allin.sdainfo.minhascompras.act;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.allin.sdainfo.minhascompras.R;
import com.allin.sdainfo.minhascompras.adapter.AdapterUsuario;
import com.allin.sdainfo.minhascompras.config.ConfigurarFirebase;
import com.allin.sdainfo.minhascompras.helper.Constantes;
import com.allin.sdainfo.minhascompras.helper.Datas;
import com.allin.sdainfo.minhascompras.helper.Mensagens;
import com.allin.sdainfo.minhascompras.helper.RecyclerItemClickListener;
import com.allin.sdainfo.minhascompras.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivityCriarUsuario extends AppCompatActivity {
    private EditText enome, eemail, esenha;
    private String snome, semail, ssenha;
    private FirebaseAuth autenticacao = ConfigurarFirebase.getAutenticacao();
    private Mensagens msg;
    private String id_user = "";
    private RecyclerView recyclerView;
    private AdapterUsuario adapterUsuario;
    private List<Usuario> listaUsuario = new ArrayList<>();
    private Usuario usuario = new Usuario();
    private DatabaseReference tb_usuario;
    private Button bt_criar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_usuario);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.voltar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_voltar);

        iniciaComponentes();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapterUsuario = new AdapterUsuario(listaUsuario, this);
        recyclerView.setAdapter(adapterUsuario);


        listarUsuarios();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                usuario = listaUsuario.get(position);
                id_user = usuario.getId();
                enome.setText(usuario.getNome());
                eemail.setText(usuario.getEmail());
                esenha.setText(usuario.getSenha());
                bt_criar.setVisibility(View.GONE);
            }
            @Override
            public void onLongItemClick(View view, int position) {
                usuario = listaUsuario.get(position);
                id_user = usuario.getId();
                alertaDialogExcluir();
            }
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        }
        ));

    }

    public void onclickSelecao(View view) {
        switch (view.getId()) {
            case R.id.bt_usuario_criar:
                msg.msgCurta("" + listaUsuario.size());
                //verificaCampos();
                break;
        }
    }

    private void cadastrarDados() {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setId(id_user);
        novoUsuario.setNome(snome);
        novoUsuario.setEmail(semail);
        novoUsuario.setSenha(ssenha);

        novoUsuario.setData(Datas.hoje());
        novoUsuario.salvarUsuario();
        limparCampos();
    }

    private void iniciaComponentes() {


        msg = new Mensagens(getApplicationContext());

        enome = findViewById(R.id.et_usuario_nome);
        eemail = findViewById(R.id.et_usuario_email);
        esenha = findViewById(R.id.et_usuario_senha);
        bt_criar = findViewById(R.id.bt_usuario_criar);

        recyclerView = findViewById(R.id.rv_criar_usuario);

    }

    private void verificaCampos() {
        snome = enome.getText().toString();
        semail = eemail.getText().toString();
        ssenha = esenha.getText().toString();

        if (!snome.isEmpty()) {
            if (!semail.isEmpty()) {
                if (!ssenha.isEmpty()) {
                    criarUsuario(semail, ssenha);
                } else {
                    msg.msgCurta("digite uma senha numérica");
                }
            } else {
                msg.msgCurta("digite um e-mail válido");
            }
        } else {
            msg.msgCurta("digite um nome de usário");
        }


    }

    private void criarUsuario(String email, String senha) {
        autenticacao.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            id_user = autenticacao.getUid();
                            cadastrarDados();
                        } else {
                            String erroExcecao;
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                erroExcecao = "Digite uma senha mais forte";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                erroExcecao = "Digite um e-mail válido";
                            } catch (FirebaseAuthUserCollisionException e) {
                                erroExcecao = "E-mail já cadastrado";
                            } catch (Exception e) {
                                erroExcecao = "Erro ao cadastrar usuário : " + e.getMessage();
                                e.printStackTrace();
                            }
                            msg.msgLonga("Erro: " + erroExcecao);
                        }
                    }
                });

    }

    private void limparCampos() {
        enome.setText("");
        eemail.setText("");
        esenha.setText("");
        id_user = "";
        bt_criar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editar, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_user_1:
                if (!id_user.isEmpty()) {
                    alertaDialogEditar(id_user);
                }
                break;
            case R.id.menu_user_2:
                if (!id_user.isEmpty()) {
                    alertaDialogExcluir();
                }
                break;
            case R.id.menu_user_3:
                msg.msgLonga("voltando...");
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    private void alertaDialogEditar(String id_selecionado) {
        new AlertDialog.Builder(this)
                .setTitle("Atenção:")
                .setMessage("Você esta preste a mudar as informções de " + usuario.getNome() + ", tem certeza?")
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.alterar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        snome = enome.getText().toString();
                        semail = eemail.getText().toString();
                        ssenha = esenha.getText().toString();

                        if(!snome.isEmpty()){
                            if(!semail.isEmpty()){
                                if(!ssenha.isEmpty()){
                                    usuario.setId(id_selecionado);
                                    usuario.setNome(snome);
                                    usuario.setEmail(semail);
                                    usuario.setSenha(ssenha);

                                    usuario.setData(Datas.hoje());

                                    usuario.atualizarUsuario();
                                    msg.msgLonga("alterando...\n"+usuario.getNome()+", na base de dados.");
                                    limparCampos();
                                }else {
                                    msg.msgCurta("digite a senha");
                                }
                            }else {
                                msg.msgCurta("digite o e-mail");
                            }
                        }else {
                            msg.msgCurta("digite o nome");
                        }
                    }
                })
                .create()
                .show();
    }

    private void alertaDialogExcluir() {
        new AlertDialog.Builder(this)
                .setTitle("Atenção:")
                .setMessage("Você esta preste a apagar, " + usuario.getNome() + ", tem certeza?")
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.apagar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        usuario.deletarUsuario(usuario.getId());
                        msg.msgLonga("apagando...\n"+usuario.getNome()+", na base de dados.");
                        limparCampos();
                    }
                })
                .create()
                .show();
    }

    private void listarUsuarios() {
        tb_usuario = ConfigurarFirebase.getBanco().child(Constantes.TB_USUARIO);
        tb_usuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaUsuario.clear();
                for (DataSnapshot users : snapshot.getChildren()) {
                    Usuario user = users.getValue(Usuario.class);
                    listaUsuario.add(user);
                    if(listaUsuario.size() > 0){
                        bt_criar.setVisibility(View.GONE);
                    }
                }

                Collections.reverse(listaUsuario);
                adapterUsuario.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}