package com.shannon;

import bitreaderwriter.BitReader;
import bitreaderwriter.BitWriter;
import bitreaderwriter.Constants;

import javax.swing.*;


public class Shannon {
    JFileChooser fileChooser = new JFileChooser();
    StringBuilder stringBuilder = new StringBuilder();
    JPanel jPanel = new JPanel();

    int[] statisticsPerCharacter;
    private String inputFile;

    public static class CharacterDetails {


        Integer asciiCode;
        Integer frequency = 0;
        Integer codeVal = 0;
        Integer codeBitsNumber = 0;

        public CharacterDetails(Integer asciiCode, Integer frequency) {
            this.asciiCode = asciiCode;
            this.frequency = frequency;
        }


    }


    public static CharacterDetails[] characterArray = new CharacterDetails[256];

    public void MainClass() {
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            //get the file
            inputFile = fileChooser.getSelectedFile().toString();

            String outputFile = getOutputFileName(inputFile);

            statisticsPerCharacter = makeStatistics(inputFile);
//CharacterDetalis[] characterArray = new CharacterDetails[256];

            int characterNumber = 0;
            for (int i = 0; i < 256; i++) {
                if (statisticsPerCharacter[i] != 0) {

                    CharacterDetails temp = new CharacterDetails(i, statisticsPerCharacter[i]);
                    characterArray[characterNumber] = temp;
                    characterNumber++;
                }
            }

//        for (int i = 0; i < characterNumber; i++) {
//            System.out.println(characterArray[i].asciiCode + " "+characterArray[i].frequency);
//        }
//        System.out.println("************************* ordonat: ");
            orderCharacterArrayByFrequency(characterNumber);
//
//        for (int i = 0; i < characterNumber; i++) {
//            System.out.println(characterArray[i].asciiCode + " "+characterArray[i].frequency);
//
//        }

            modelConstruct(0, characterNumber);
            for (int i = 0; i < characterNumber; i++) {

                System.out.println(characterArray[i].asciiCode + " " + characterArray[i].frequency +
                        " " + Integer.toBinaryString(characterArray[i].codeVal) + " " + characterArray[i].codeBitsNumber);

            }
            //writeHeader(statisticsPerCharacter);

            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //DE ORDONAT "ALFABETIC" INAINTE DE SCRIEREA IN FISIER
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            writeFile(outputFile, characterNumber);
            System.out.println("file finished");
        }
    }


    private void writeHeader(BitWriter bitWriterInstance) {
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

        writeHeader(bitWriterInstance);

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
        CharacterDetails temp = new CharacterDetails(0, 0);
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
        CharacterDetails temp = new CharacterDetails(0, 0);
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


    /* real stop !!!!!! */
    static void modelConstruct(int startIndex, int stopIndex) {
        //boolean cutPositionFound = false;
        //int cutPosition = 1;
        int bestCut = 0;
        int minDiference = Integer.MAX_VALUE;

        if (startIndex >= stopIndex) {
            return;
        } else if (stopIndex - startIndex == 1) {
            if(characterArray[startIndex].codeBitsNumber == 0){
                characterArray[startIndex].codeBitsNumber++;
            }
            return;
        }

        for (int cutPosition = startIndex + 1; cutPosition < stopIndex; cutPosition++) {

            int leftSum = 0;
            int rightSum = 0;


            for (int i = startIndex; i < cutPosition; i++) {
                leftSum += characterArray[i].frequency;
            }

            for (int i = cutPosition; i < stopIndex; i++) {
                rightSum += characterArray[i].frequency;
            }

//            System.out.println(cutPosition + " " + Math.abs(leftSum - rightSum));


            if (Math.abs(leftSum - rightSum) < minDiference) {
                minDiference = Math.abs(leftSum - rightSum);
                bestCut = cutPosition;
            }
        }

        for (int i = startIndex; i < stopIndex; i++) {
//            leftSum += characterArray[i].frequency;
            characterArray[i].codeBitsNumber++;
            characterArray[i].codeVal <<= 1;
            if (i >= bestCut) {
                characterArray[i].codeVal++;
            }
        }

//        for (int i = bestCut; i < stopIndex; i++) {
////            rightSum += characterArray[i].frequency;
//            characterArray[i].codeVal += 1 << characterArray[i].codeBitsNumber;
//            characterArray[i].codeBitsNumber++;
//        }

//        System.out.println("=====" + bestCut + "=====");
/*
        137 4726
        138 3068
        139 1408
        140 254
        141 1916
        142 3580
        143 5244
        => bestCut = 140
        =>  modelConstruct(startIndex,140);
            modelConstruct(140 + 1,stopIndex);
        */
//        System.out.println(startIndex + " " + stopIndex);
//        for (int i = startIndex; i < stopIndex; i++) {
//
//            System.out.println(characterArray[i].asciiCode+" "+Integer.toBinaryString(characterArray[i].codeVal));
//        }
        modelConstruct(startIndex, bestCut);
        modelConstruct(bestCut, stopIndex);


    }

}
