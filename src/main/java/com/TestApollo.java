package com;

public class TestApollo {

    public static String transformEnv(String envName) {

        switch (envName.trim().toUpperCase()) {
            case "LPT":
                return "LPT";
            case "FAT":

            case "FWS":
                return "FAT";
            case "UAT":
                return "UAT";
            case "PRO":
            case "PROD": //just in case
                return "PRO";
            case "DEV":
                return "DEV";
            case "LOCAL":
                return "LOCAL";
            case "TOOLS":
                return "TOOLS";
            default:
                return null;
        }
    }

    public static void  main (String[] args){
        String aa = transformEnv("fat");
        System.out.println(aa);
    }
}
