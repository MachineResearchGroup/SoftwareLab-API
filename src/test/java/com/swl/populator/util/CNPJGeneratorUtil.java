package com.swl.populator.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CNPJGeneratorUtil {

    public static String generateCNPJ() {
        String number = "";
        Random random = new Random();

        //Gera string com 12 números aleatórios
        for (int i = 0; i < 12; i++)
            number = number.concat(Integer.toString(random.nextInt(10)));

        return generateCheckDigit(number);
    }

    public static boolean check(String number, String vd) {
        String helper = generateCheckDigit(number);
        helper = helper.substring(12);
        return helper.equals(vd);
    }

    public static String generateCheckDigit(String number) {
        int helper;
        helper = gerarVd(number);
        number = number.concat(Integer.toString(helper));
        helper = gerarVd(number);
        number = number.concat(Integer.toString(helper));
        return number;
    }

    public static int gerarVd(String number) {
        int weight = 2;
        int sum = 0;
        int vd = 0;

        for (int i = number.length() - 1; i >= 0; i--) {
            int n = (int) number.charAt(i) - '0';
            if (weight == 10) weight = 2;
            sum += n * weight;
            weight++;
        }

        int result = sum % 11;
        return (result < 2) ? vd : 11 - result;
    }

}
