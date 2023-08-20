package com.seleniumsimplified.seleniumtestpages.spark.app;

public class NumTextParser {
    public String parse(final String numstxt) {

        if(numstxt==null){
            return "";
        }

        String[] words = {"zero","one","two","three","four","five","six","seven","eight","nine"};

        String[] chars = numstxt.split("");

        String retStr = "";
        String prefix = "";

        for(String x : chars){
            try{
                int number = Integer.parseInt(x);
                retStr = retStr + prefix + words[number];
                prefix = ", ";
            }
            catch (NumberFormatException ex){
                // ignore conversion
            }
        }

        return retStr;
    }
}
