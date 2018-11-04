package com.shannon;

import javax.swing.*;


public class Code {
    JFileChooser fileChooser = new JFileChooser();


    public void CodeFileUsingShannon() {
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String inputFile;
            inputFile = fileChooser.getSelectedFile().toString();

            String outputFile = getOutputFileName(inputFile);
            Commons shannonFunctions = new Commons(inputFile);
            shannonFunctions.makeStatistics(inputFile);

            shannonFunctions.constructCharacterArray();

            shannonFunctions.orderCharacterArrayByFrequency();

            shannonFunctions.modelConstruct(0, shannonFunctions.characterNumber);
            shannonFunctions.printCharacterArray();

            shannonFunctions.writeCodedFile(outputFile);
            System.out.println("file finished");
        }
    }

    private String getOutputFileName(String inputFile) {
        String[] parts = inputFile.split("\\\\"); // regex: need to escape dot
        String outputFile = parts[parts.length - 1]; // outputs "en"
        parts = outputFile.split("\\.");
        outputFile = parts[0] + "-encrypted." + parts[1];
        return outputFile;
    }

}
