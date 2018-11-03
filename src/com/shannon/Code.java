package com.shannon;

import bitreaderwriter.BitReader;
import bitreaderwriter.BitWriter;
import bitreaderwriter.Constants;

import javax.swing.*;


public class Code {
    JFileChooser fileChooser = new JFileChooser();
    //  StringBuilder stringBuilder = new StringBuilder();
    // JPanel jPanel = new JPanel();

    int[] statisticsPerCharacter;
    private String inputFile;


    public static CharacterDetails[] characterArray = new CharacterDetails[256];

    public void CodeFileUsingShannon() {
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

            inputFile = fileChooser.getSelectedFile().toString();
            String outputFile = getOutputFileName(inputFile);
            statisticsPerCharacter = makeStatistics(inputFile);

            int characterNumber = 0;
            for (int i = 0; i < 256; i++) {
                if (statisticsPerCharacter[i] != 0) {

                    CharacterDetails temp = new CharacterDetails(i, statisticsPerCharacter[i]);
                    characterArray[characterNumber] = temp;
                    characterNumber++;
                }
            }

            orderCharacterArrayByFrequency(characterNumber);

            Commons shanonFunctions = new Commons(characterArray);
            shanonFunctions.modelConstruct(0, characterNumber);
            for (int i = 0; i < characterNumber; i++) {

                System.out.println(characterArray[i].asciiCode + " " + characterArray[i].frequency +
                        " " + Integer.toBinaryString(characterArray[i].codeVal) + " " + characterArray[i].codeBitsNumber);

            }

            writeFile(outputFile, characterNumber);
            System.out.println("file finished");
        }
    }

    private void writeHeader(BitWriter bitWriterInstance, int characterNumber) {
        System.out.println("Header");
        for (int i = 0; i < 256; i++) {
            if (statisticsPerCharacter[i] == 0) {
                bitWriterInstance.WriteNBits(0, 2);

            } else if (statisticsPerCharacter[i] < 256) {
                bitWriterInstance.WriteNBits(1, 2);
                System.out.println(1);

            } else if (statisticsPerCharacter[i] < 65536) {
                bitWriterInstance.WriteNBits(2, 2);
                System.out.println(2);


            } else {
                bitWriterInstance.WriteNBits(3, 2);
                System.out.println(3);
            }
        }

        orderCharacterArrayByAscii(characterNumber);
        for (int i = 0; i < characterNumber; i++) {
            if (characterArray[i].frequency < 256) {
                bitWriterInstance.WriteNBits(characterArray[i].frequency,1);

            } else if (characterArray[i].frequency < 65536) {
                bitWriterInstance.WriteNBits(characterArray[i].frequency,2);

            } else {
                bitWriterInstance.WriteNBits(characterArray[i].frequency,4);
            }

        }

    }

    private int getCharacterPosition(int asciiCode) {

        for (int i = 0; i < 256; i++) {
            if (characterArray[i].asciiCode == asciiCode) {
                return i;
            }
        }
        return -1;
    }

    private void writeFile(String outputFile, int characterNumber) {
        BitWriter bitWriterInstance = new BitWriter(outputFile);

        writeHeader(bitWriterInstance, characterNumber);

//        orderCharacterArrayByAscii(characterNumber);

        BitReader bitReaderInstance = new BitReader(inputFile);
        int bitsRemainToRead = bitReaderInstance.fileLength;

        for (int i = 0; i < bitsRemainToRead; i++) {

            int byteReaded = bitReaderInstance.ReadNBits(Constants.WORD_BITS_NUMBER);
            int positionInArray = getCharacterPosition(byteReaded);

            if (positionInArray == -1) {

                System.err.println("Character not find in array!");
            } else {

                bitWriterInstance.WriteNBits(characterArray[positionInArray].codeVal,
                        characterArray[positionInArray].codeBitsNumber);
            }
        }

        bitWriterInstance.WriteNBits(0, Constants.WORD_BITS_NUMBER - 1);
    }

    private String getOutputFileName(String inputFile) {
        String[] parts = inputFile.split("\\\\"); // regex: need to escape dot
        String outputFile = parts[parts.length - 1]; // outputs "en"
        parts = outputFile.split("\\.");
        outputFile = parts[0] + "-encrypted." + parts[1];
        return outputFile;
    }

    static void orderCharacterArrayByFrequency(int n) {
        CharacterDetails temp;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (characterArray[i].frequency > characterArray[j].frequency) {
                    temp = characterArray[i];
                    characterArray[i] = characterArray[j];
                    characterArray[j] = temp;
                }
            }
        }
    }

    static void orderCharacterArrayByAscii(int n) {
        CharacterDetails temp;
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (characterArray[i].asciiCode > characterArray[j].asciiCode) {
                    temp = characterArray[i];
                    characterArray[i] = characterArray[j];
                    characterArray[j] = temp;
                }
            }
        }
    }

    static int[] makeStatistics(String inputFile) {

        int[] statisticsPerCharacter = new int[256];
        BitReader bitReaderInstance = new BitReader(inputFile);

        int bitsRemainToRead = bitReaderInstance.fileLength;
        System.out.println(inputFile);
        if (bitsRemainToRead == 0) {
            System.err.print("file is empty");
        }
        for (int i = 0; i < bitsRemainToRead; i++) {
            statisticsPerCharacter[bitReaderInstance.ReadNBits(Constants.WORD_BITS_NUMBER)]++;
        }

        return statisticsPerCharacter;
    }


}
