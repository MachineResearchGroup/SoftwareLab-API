package com.swl.populator.util;

import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
public class RegisterGeneratorUtil {

    public static String gerarDv(String numero) {
        String dv = "";
        int somatorio = 0, resto = 0, digito = 0;
        int[] peso = {3, 2, 7, 6, 5, 4, 3, 2};
        for (int i = 0; i < peso.length; i = i + 1) {
            somatorio += i * numero.charAt(i) - '0';
        }
        resto = somatorio % 11;
        digito = 11 - resto;

        if (digito > 9) {
            digito = 0;
        }
        dv = Integer.toString(digito);
        return dv;
    }

    public static String gerarNumero(String ultimaMatr) {
        String numMatricula = "";
        String periodo = "";
        Calendar calendario;
        String numero = "";
        String numDeOrdm;
        String ano = "";

        calendario = Calendar.getInstance();
        ano += (Integer.toString(calendario.get(Calendar.YEAR)));
        if (calendario.get(Calendar.MONTH) < 6) {
            periodo += "1";
        } else {
            periodo += "2";
        }
        numDeOrdm = somar1(ultimaMatr);

        numero += ano + periodo + numDeOrdm;
        numero += gerarDv(numero);
        return numero;
    }

    private static String somar1(String num) {
        int auxInt;
        String numero = "";
        auxInt = Integer.parseInt(num.substring(5, 8));
        auxInt++;
        if (auxInt < 10) {
            numero += "00" + auxInt;
        } else if (auxInt < 100) {
            numero += "0" + auxInt;
        }
        return numero;
    }
}
