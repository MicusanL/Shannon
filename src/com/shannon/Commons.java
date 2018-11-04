package com.shannon;

import bitreaderwriter.BitReader;
import bitreaderwriter.BitWriter;
import bitreaderwriter.Constants;

public class Commons {

    private String inputFile;
    public CharacterDetails[] characterArray;
    int[] statisticsPerCharacter;
    int characterNumber;


    public Commons(String inputFile) {

        this.inputFile = inputFile;
        statisticsPerCharacter = new int[256];
        characterArray = new CharacterDetails[256];
    }

    public void constructCharacterArray() {

        for (int i = 0; i < 256; i++) {
            if (statisticsPerCharacter[i] != 0) {

                CharacterDetails temp = new CharacterDetails(i, statisticsPerCharacter[i]);
                characterArray[characterNumber] = temp;
                characterNumber++;
            }
        }
    }

    void modelConstruct(int startIndex, int stopIndex) {

        int bestCut = 0;
        int minDiference = Integer.MAX_VALUE;

        if (startIndex >= stopIndex) {
            return;
        } else if (stopIndex - startIndex == 1) {
            if (characterArray[startIndex].codeBitsNumber == 0) {
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

            if (Math.abs(leftSum - rightSum) < minDiference) {
                minDiference = Math.abs(leftSum - rightSum);
                bestCut = cutPosition;
            }
        }

        for (int i = startIndex; i < stopIndex; i++) {

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

    private void writeHeader(BitWriter bitWriterInstance, int characterNumber) {

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

        orderCharacterArrayByAscii();
        for (int i = 0; i < characterNumber; i++) {
            if (characterArray[i].frequency < 256) {
                bitWriterInstance.WriteNBits(characterArray[i].frequency, Constants.WORD_BITS_NUMBER);

            } else if (characterArray[i].frequency < 65536) {
                bitWriterInstance.WriteNBits(characterArray[i].frequency, 2 * Constants.WORD_BITS_NUMBER);

            } else {
                bitWriterInstance.WriteNBits(characterArray[i].frequency, 4 * Constants.WORD_BITS_NUMBER);
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

    public void writeCodedFile(String outputFile) {
        BitWriter bitWriterInstance = new BitWriter(outputFile);

        writeHeader(bitWriterInstance, characterNumber);

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

    void orderCharacterArrayByFrequency() {

        CharacterDetails temp;
        for (int i = 0; i < characterNumber - 1; i++) {
            for (int j = i + 1; j < characterNumber; j++) {

                if (characterArray[i].frequency > characterArray[j].frequency) {
                    temp = characterArray[i];
                    characterArray[i] = characterArray[j];
                    characterArray[j] = temp;
                }
            }
        }
    }

    void orderCharacterArrayByAscii() {
        CharacterDetails temp;
        for (int i = 0; i < characterNumber - 1; i++) {
            for (int j = i + 1; j < characterNumber; j++) {
                if (characterArray[i].asciiCode > characterArray[j].asciiCode) {
                    temp = characterArray[i];
                    characterArray[i] = characterArray[j];
                    characterArray[j] = temp;
                }
            }
        }
    }

    public void printCharacterArray() {
        for (int i = 0; i < characterNumber; i++) {

            System.out.println(characterArray[i].asciiCode + " " + characterArray[i].frequency +
                    " " + Integer.toBinaryString(characterArray[i].codeVal) + " " + characterArray[i].codeBitsNumber);

        }
    }

    void makeStatistics(String inputFile) {

        BitReader bitReaderInstance = new BitReader(inputFile);
        int bitsRemainToRead = bitReaderInstance.fileLength;
        System.out.println(inputFile);

        if (bitsRemainToRead == 0) {
            System.err.print("file is empty");
        }
        for (int i = 0; i < bitsRemainToRead; i++) {
            statisticsPerCharacter[bitReaderInstance.ReadNBits(Constants.WORD_BITS_NUMBER)]++;
        }

    }

/*    public void readCodedFile(BitReader bitReaderInstance, int charactersNumber) {




        while (charactersNumber!=0){
            int codeReaded = 0;
            while()

                charactersNumber--;
        }


    }*/

/*
    private boolean compareCode(int code){
        for(int i=0;i<characterNumber;i++){

        }
    }
*/

    public int readHeader(BitReader bitReaderInstance) {

        int[] bitMap = new int[256];
        int charactersNumber = 0;
        for (int i = 0; i < 256; i++) {
            bitMap[i] = bitReaderInstance.ReadNBits(2);
        }
        for (int i = 0; i < 256; i++) {
            if (bitMap[i] == 3) {
                statisticsPerCharacter[i] = bitReaderInstance.ReadNBits(4 * Constants.WORD_BITS_NUMBER);
            } else if (bitMap[i] == 0) {
                statisticsPerCharacter[i] = 0;
            } else {
                statisticsPerCharacter[i] = bitReaderInstance.ReadNBits(bitMap[i] * Constants.WORD_BITS_NUMBER);

            }
            charactersNumber += statisticsPerCharacter[i];
          //  System.out.println(i + " " + bitMap[i] + " " + statisticsPerCharacter[i]);
        }

//System.out.println(charactersNumber);
        return charactersNumber;
    }

}
