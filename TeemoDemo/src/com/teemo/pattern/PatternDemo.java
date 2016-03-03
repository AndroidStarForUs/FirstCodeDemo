
package com.teemo.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternDemo {

    /**
     * @brief 
     *
     * @param args
     * void
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.out.print("-99".matches("-?\\d+") + "\n");
        System.out.println("+99".matches("-?\\d+") + "\n");
        //String regEx = "(-|\\+)?\\d+";
        String regEx = "(<a>)?\\s+(</a>)";
        String str = "<a>Pattern</a>";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String result = matcher.group();
            System.out.println(result);
        }
        System.out.println("+99".matches(regEx));

        /********************************************************************/
        String[] values = new String[] {"baishuixing@163.com", "baishuixing2@qq.com", "baishuixing3@163.com"};
        //String mailRegex = "[a-zA-Z0-9]+@+\\d{3}+[.]+com";
        String mailRegex = "[a-zA-Z0-9]+@+[163|162|qq]+[.]+com";
        pattern = Pattern.compile(mailRegex);
        for (String string : values) {
            Matcher matcher2 = pattern.matcher(string);
            if (!matcher2.matches()) {
                System.out.println("Don't matcherd. >>> " + string);
            } else {
                System.out.println(string);
            }
        }
    }

}
