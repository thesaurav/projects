package com.dropwizard.TestPackage;

import java.math.BigInteger;

/**
 * Created by SauravKumar on 30-05-2016.
 */
public class Solution1
{
    public static void main(String ...args)
    {
        Test.connection();
    }

    static int Simple_powers(int a, int b) {
        int retVal = 0;
        BigInteger mBigInteger = new BigInteger(1000000007 + "");
        BigInteger aBig = new BigInteger(a + "");
        BigInteger bBig = new BigInteger(b + "");

        BigInteger sum = (aBig.pow(b).add(bBig.pow(a))).mod(mBigInteger);
        int x = Integer.parseInt(sum.toString());
        String binary = Integer.toString(x , 2);
        retVal = Integer.valueOf(binary, 3);
        return retVal;
    }

//    String input = 17+""/* your input */;
//    int inputBase = 10/* your input base */;
//    int outputBase = 2/* wished output base */;
//
//    int inputInt = Integer.valueOf(input, inputBase);
//    String output = Integer.toString(inputInt, outputBase);
//    System.out.println(output);
//
//    int base2 = Integer.parseInt(output);
//    int decimalValue = Integer.valueOf(output, 3);
//    System.out.println(decimalValue);
//
//    System.out.println(Simple_powers(120123514, 56897252));
//    System.out.println(Simple_powers(1523, 0));
}
