package com.allin.sdainfo.minhascompras.helper;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class Mensagens {

    private Context ctx;

    public Mensagens(Context ctx) {
        this.ctx = ctx;
    }

    public void msgCurta(String texto) {
        Toast.makeText(ctx, texto, Toast.LENGTH_SHORT).show();
    }

    public void msgLonga(String texto) {
        Toast.makeText(ctx, texto, Toast.LENGTH_SHORT).show();
    }

    public void msgSnackbar(View view, String msg){
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

}
