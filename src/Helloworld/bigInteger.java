package Helloworld;

import java.math.BigDecimal;
import java.math.BigInteger;

public class bigInteger {
    public static void main(String[] args) {
        BigInteger bigInteger = new BigInteger("156164196498416516");
        BigDecimal bigDec = new BigDecimal("1234567890.1234567890");
        //大数运算
        BigInteger sum = bigInteger.add(new BigInteger("16164196498416516"));
        BigDecimal product = bigDec.multiply(new BigDecimal("2"));
        System.out.println(sum);
        System.out.println(product);
    }
}
