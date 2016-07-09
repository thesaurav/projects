package com.dropwizard.TestPackage;

/**
 * Created by SauravKumar on 25-06-2016.
 */
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int testCases = in.nextInt();

        for(int count = 0; count < testCases; count++)
        {
            int k = in.nextInt();
            int a = in.nextInt(16);
            int b = in.nextInt(16);
            int c = in.nextInt(16);

            String aString = Integer.toBinaryString(a);
            String bString = Integer.toBinaryString(b);
            String cString = Integer.toBinaryString(c);

           int arrSize =  getMax(aString.length(), bString.length(), cString.length());

            char aArray[] = new char[arrSize];
            char bArray[] = new char[arrSize];
            char cArray[] = new char[arrSize];

            aString.getChars(0, aString.length(), aArray, arrSize - aString.length());
            bString.getChars(0, bString.length(), bArray, arrSize - bString.length());
            cString.getChars(0, cString.length(), cArray, arrSize - cString.length());

            boolean []arrExtra = new boolean[arrSize];

            for(int index = 0; index < arrSize; index++)
            {
                if(aArray[index] == 0)
                {
                    aArray[index] = 48;
                }
                if(bArray[index] == 0)
                {
                    bArray[index] = 48;
                }
                if(cArray[index] == 0)
                {
                    cArray[index] = 48;
                }
            }

            int change = 0;
            boolean isPossible = true;
            for(int index = 0; index < arrSize; index++)
            {
                if(change == k)
                {
                    isPossible = false;
                    break;
                }
                if(cArray[index] == 48)
                {
                    if(aArray[index] == 49)
                    {
                        change++;
                        aArray[index] = 48;
                    }

                    if(bArray[index] == 49)
                    {
                        change++;
                        bArray[index] = 48;
                    }
                }
                else
                {
                    if(bArray[index] == 48 && aArray[index] == 48)
                    {
                        change++;
                        bArray[index] = 49;
                    }

                    if(bArray[index] == 49 && aArray[index] == 49)
                    {
                        arrExtra[index] = true;
                    }
                }
            }

            for(int index = 0; index < arrExtra.length && change < k; index++)
            {
                if(arrExtra[index])
                {
                    change++;
                    aArray[index] = 48;
                }
            }

            if(isPossible)
            {
                int aa = 0;
                int bb = 0;
                StringBuffer aStr = new StringBuffer("");
                StringBuffer bStr = new StringBuffer("");
                for(int index = 0; index < arrSize; index++)
                {
                    aStr.append(aArray[index]);
                    bStr.append(bArray[index]);
                }
                System.out.println(Integer.toHexString(Integer.parseInt(aStr.toString(), 2))+ "\n"+ Integer.toHexString(Integer.parseInt(bStr.toString(), 2)));
            }
            else
            {
                System.out.println(-1);
            }

        }

    }

    public static int getMax(int a, int b, int c)
    {
        int returnVal = 0;
        if(a > b)
        {
            if(a > c)
            {
                returnVal = a;
            }
            else
            {
                returnVal = c;
            }
        }
        else
        {
            if(b > c)
            {
                returnVal = b;
            }
            else
            {
                returnVal = c;
            }
        }

        return returnVal;
    }
}
