package edu.pdx.cs410J.leolu;

import java.io.File;

public class Converter {

    public static void main(String[] args){
        if(args.length != 2){
            System.err.println("usage: java edu.pdx.cs410J.leolu.Converter textFile xmlFile.");
            System.err.println("Txt file and xml file arguments are needed.");
            return;
        }
        String textFilePath = args[0];
        String xmlFilePath = args[1];
        if(!textFilePath.endsWith(".txt")){
            System.err.println("The first argument must be a .txt file!");
            return;
        }
        if(!isValidFilePath(textFilePath)){
            System.err.println("Invalid txt file path: " + textFilePath);
            return;
        }
        if(!xmlFilePath.endsWith(".xml")){
            System.err.println("The second argument must be a .xml file!");
            return;
        }
        if(!isValidFilePath(xmlFilePath)){
            System.err.println("Invalid xml file path: " + xmlFilePath);
            return;
        }



    }

    public static boolean isValidFilePath(String filePath) {
        File file = new File(filePath);
        return file.exists() && !file.isDirectory();
    }
}
