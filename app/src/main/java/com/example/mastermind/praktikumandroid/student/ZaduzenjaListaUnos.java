package com.example.mastermind.praktikumandroid.student;

import java.util.Date;

/**
 * Created by Acer on 28-Nov-17.
 */

public class ZaduzenjaListaUnos extends Unos_StudServ {


    public Date    datumZaduzenja;
    public String  iznos;
    public String  skolskaGodina;
    public String  osnov;
    public String  tip;




    public ZaduzenjaListaUnos( Date datumZaduzenja, String iznos,
                            String skolskaGodina, String osnov, String tip)
    {
        this.datumZaduzenja = datumZaduzenja;
        this.iznos          = iznos;
        this.skolskaGodina  = skolskaGodina;
        this.osnov          = osnov;
        this.tip            = tip;

    }


}
