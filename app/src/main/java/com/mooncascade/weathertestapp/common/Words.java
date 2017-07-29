package com.mooncascade.weathertestapp.common;

/**
 * Created by Talha Mir on 20-Jul-17.
 */
public class Words {

    private static final String[] tensNames = {
            "",
            "Ten ",
            "Twenty ",
            "Thirty ",
            "Forty ",
            "Fifty ",
            "Sixty ",
            "Seventy ",
            "Eighty ",
            "Ninety "
    };

    private static final String[] numNames = {
            "",
            "One ",
            "Two ",
            "Three ",
            "Four ",
            "Five ",
            "Six ",
            "Seven ",
            "Eight ",
            "Nine ",
            "Ten ",
            "Eleven ",
            "Twelve ",
            "Thirteen ",
            "Fourteen ",
            "Fifteen ",
            "Sixteen ",
            "Seventeen ",
            "Eighteen ",
            "Nineteen "
    };

    private Words() {
    }

    // Assuming for our case that number is always less than 1000, otherwise we are doomed anyway!
    public static String EnglishNumberToWords(int number) {
        String soFar;
        String numberString;

        int positiveNumber = Math.abs(number);

        if (positiveNumber == 0)
            return "Zero";
        else if (positiveNumber % 100 < 20) {
            soFar = numNames[positiveNumber % 100];
            positiveNumber /= 100;
        } else {
            soFar = numNames[positiveNumber % 10];
            positiveNumber /= 10;

            soFar = tensNames[positiveNumber % 10] + soFar;
            positiveNumber /= 10;
        }

        if (positiveNumber == 0)
            numberString =  soFar;
        else
            numberString = numNames[positiveNumber] + "hundred " + soFar;

        if(number < 0)
            numberString = "Minus " + numberString;

        return numberString;
    }

}
