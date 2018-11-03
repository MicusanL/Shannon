package com.shannon;

public class Commons {

    public static CharacterDetails[] characterArray;
    public Commons(CharacterDetails[] characterArrayReceived) {
        characterArray = characterArrayReceived;
    }

    static void modelConstruct(int startIndex, int stopIndex) {


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
