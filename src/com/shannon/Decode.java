package com.shannon;

import javax.swing.*;

import bitreaderwriter.BitReader;
import bitreaderwriter.BitWriter;
import bitreaderwriter.Constants;

public class Decode {

    JFileChooser fileChooser = new JFileChooser();
    private String inputFile;
    int[] statisticsPerCharacter;
    int characterNumber = 0;

    public static CharacterDetails[] characterArray = new CharacterDetails[256];


    public void DecodeFileUsingShannon() {
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            //get the file
            inputFile = fileChooser.getSelectedFile().toString();
            BitReader bitReaderInstance = new BitReader(inputFile);
            int bitsRemainToRead = bitReaderInstance.fileLength * Constants.WORD_BITS_NUMBER;
            bitsRemainToRead -= 512;

        }
    }



    private void readHeader(BitReader bitReaderInstance) {

        for (int i = 0; i < 256; i++) {
            statisticsPerCharacter[i] = bitReaderInstance.ReadNBits(2);
        }
    }
}
