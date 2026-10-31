package Helloworld;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;

public class numberFormat {
    public static void main(String[] args) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        System.out.println(nf.format(1000));
    }
}
