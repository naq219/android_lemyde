package com.project.shop.lemy.helper;

import com.telpoo.frame.utils.Mlog;

import java.text.DecimalFormat;

/**
 * Created by naq on 11/19/18.
 */

public class MoneySupport {

    /**
     * chuyển sang tiền số cuối 1000 chuyển thành k``
     * @return
     */
    public static String moneyEndK(String money){
        //12 450 000
        //116.330.343
        //3 243 433
        if(money==null) return null;

        if (money.length()>6&& money.length()<10){
            String s1= money.substring(0,money.length()-3);
            String k= s1.substring(s1.length()-3);
            String tr= s1.substring(0,s1.length()-3);
            return tr+"tr"+k+"k";
        }

        if (money!=null&&money.length()>3){
            return money.substring(0,money.length()-3)+"k";
        }

        return money;
    }


    public static String moneyDot(String ss) {

        StringBuilder b= new StringBuilder(ss);
        boolean ok=true;
        do {
            int position=b.indexOf("."); if (position==-1)position=b.length();
            if (position-3<=0) break;
            b.insert(position-3,".");
        }
        while (ok);


           return b.toString();
    }

    public static String insertPeriodically(String text, String insert, int period)
    {
        StringBuilder builder = new StringBuilder(
                text.length() + insert.length() * (text.length()/period)+1);

        int index = 0;
        String prefix = "";
        while (index < text.length())
        {
            // Don't put the insert in the very first iteration.
            // This is easier than appending it *after* each substring
            builder.append(prefix);
            prefix = insert;
            builder.append(text.substring(index,
                    Math.min(index + period, text.length())));
            index += period;
        }
        return builder.toString();
    }
}
