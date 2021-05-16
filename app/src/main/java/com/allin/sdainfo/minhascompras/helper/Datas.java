package com.allin.sdainfo.minhascompras.helper;

import java.text.SimpleDateFormat;

public class Datas {
    public static String hoje(){
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String data = simpleDateFormat.format(date);
        return data;
    }

    public static String hojeHora(){
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String dataHora = simpleDateFormat.format(date);
        return dataHora;
    }
}
