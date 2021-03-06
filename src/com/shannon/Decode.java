package com.shannon;

import javax.swing.*;

import bitreaderwriter.BitReader;
import bitreaderwriter.BitWriter;
import bitreaderwriter.Constants;

public class Decode {

    JFileChooser fileChooser = new JFileChooser();
    //private String inputFile;
    // int[] statisticsPerCharacter;
    // int characterNumber = 0;

    // public static CharacterDetails[] characterArray = new CharacterDetails[256];


    public void DecodeFileUsingShannon() {
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

            String inputFile;
            inputFile = fileChooser.getSelectedFile().toString();

            String outputFile = getOutputFileName(inputFile);
            Commons shannonFunctions = new Commons(inputFile);
//            shannonFunctions.makeStatistics(inputFile);
//            readHeader(inputFile);
            BitReader bitReaderInstance = new BitReader(inputFile);

            int charactersNumber = shannonFunctions.readHeader(bitReaderInstance);

            shannonFunctions.constructCharacterArray();

            shannonFunctions.orderCharacterArrayByFrequency();

            shannonFunctions.modelConstruct(0, shannonFunctions.characterNumber);
//            shannonFunctions.printCharacterArray();
            shannonFunctions.readCodedFile(bitReaderInstance, outputFile, charactersNumber);
        }
        System.out.println("Decode finished");
        System.exit(0);
    }

    private String getOutputFileName(String inputFile) {
        String[] parts = inputFile.split("\\\\"); // regex: need to escape dot
        String outputFile = parts[parts.length - 1]; // outputs "en"
        parts = outputFile.split("\\.");
        String[] parts2 = parts[0].split("-");
        outputFile = "C://Users//lidia//Desktop//Shannon//" + parts2[0] + "-Shanon-decrypted." + parts[1];
//        System.out.println(outputFile);
        return outputFile;
    }


}
