package com.example.studentmanagersystem;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberUtils {

    /**
     * 保留两位小数
     * @param number
     * @return
     */
    public static String keep2DecimalPlaces(double number){
      return   String.format("%.2f", number);
    }


    /**
     *去掉小数点后面的0
     * @param d
     * @return
     */
    public static String noZero(double d) {
        String s = String.valueOf(d);

        if(s.indexOf(".") > 0){

            //正则表达
            s = s.replaceAll("0+?$", "");//去掉后面无用的零

            s = s.replaceAll("[.]$", "");//如小数点后面全是零则去掉小数点

        }

       return s;
    }

}
