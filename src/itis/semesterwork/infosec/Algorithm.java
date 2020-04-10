package itis.semesterwork.infosec;

import java.util.Arrays;

import static itis.semesterwork.infosec.ConvertUtils.*;
import static itis.semesterwork.infosec.Transforms.*;

public class Algorithm {

    private String mode;

    public void setMode(String mode) {
        this.mode = mode.toUpperCase();
    }

    public byte[] crypt(byte[] message, byte[] key, String operation) {
        if (message.length < 8) {
            System.out.println("Message should be atleast 64 bits");
            System.exit(1);
        }
        if (key.length != 8) {
            System.out.println("Key should be 64 bits");
            System.exit(1);
        }

        int length = message.length;
        int n = (length + 7) / 8 * 8;
        byte[] cipher = new byte[n];

        if (length == 8) {
            if (mode.equals("ECB")) {
                return cryptText(message, key, operation);
            } else if (mode.equals("OFB")) {
                byte[] nounce = getNounce();
                byte[] temp = cryptText(nounce, key, operation);
                // byte[] temp = cryptText(nounce, key, "encrypt"); for decrypt
                return XORBytes(temp, message);
            } else {
                System.out.println("Unsupported mode of operation!");
                return null;
            }
        }

        byte[] feedback = new byte[8];
        if (mode.equals("OFB")) feedback = getNounce();
        int i = 0, k = 0;

        while (i < length) {

            int j = 0;
            byte[] result, block;
            block = new byte[8];

            for (; j < 8 && i < length; j++, i++)
                block[j] = message[i];

            while (j < 8)
                /* pad with white spaces */
                block[j++] = 0x20;

            //System.out.println("BLOCK: ");
            //printBytes(block);

            if (mode.equals("ECB")) {
                result = cryptText(block, key, operation);
            } else if (mode.equals("OFB")) {
                result = cryptText(feedback, key, operation);
                // result = cryptText(feedback, key, "encrypt"); for decrypt
                feedback = Arrays.copyOfRange(result, 0, 8);
                result = XORBytes(result, block);
            } else {
                System.out.println("Unsupported mode of operation!");
                return null;
            }

            //System.out.println("RESULT: ");
            //printBytes(result);

            for (j = 0; j < 8 && k < cipher.length; j++, k++) cipher[k] = result[j];
        }
        return cipher;
    }

    private byte[][] getSubKeys(byte[] masterKey) {
        int noOfSubKeys = SHIFTS.length;
        int keySize = PC1.length;
        byte[] key = permute(masterKey, PC1);
        byte[][] subKeys = new byte[noOfSubKeys][keySize];
        byte[] leftHalf = getBits(key, 0, keySize / 2);
        byte[] rightHalf = getBits(key, keySize / 2, keySize / 2);
        for (int i = 0; i < noOfSubKeys; i++) {
            leftHalf = rotateLeft(leftHalf, SHIFTS[i], keySize / 2);
            rightHalf = rotateLeft(rightHalf, SHIFTS[i], keySize / 2);
            byte[] subKey = concatBits(leftHalf, keySize / 2, rightHalf, keySize / 2);
            subKeys[i] = permute(subKey, PC2);
        }
        return subKeys;
    }

    private byte[] getNounce() {
        return hexStringToByteArray("DCBE6AE7EA5D5C61");
    }

    public byte[] cryptText(byte[] message, byte[] key, String operation) {
        if (message.length != 8) {
            System.out.println("Message should be 64 bits");
            System.exit(1);
        }
        if (key.length != 8) {
            System.out.println("Key should be 64 bits");
            System.exit(1);
        }
        byte[] result = null;
        int blockSize = IP.length;
        byte[][] subKeys = getSubKeys(key);
        int noOfRounds = subKeys.length;
        /**
         * Initial Permutation
         */
        message = permute(message, IP);
        /**
         * Split message into two halves
         */
        byte[] leftHalf = getBits(message, 0, blockSize / 2);
        byte[] rightHalf = getBits(message, blockSize / 2, blockSize / 2);
        for (int i = 0; i < noOfRounds; i++) {
            byte[] temp = rightHalf;
            /**
             * Expansion
             */
            rightHalf = permute(rightHalf, E);
            /**
             * XOR rightHalf with roundKey
             */
            byte[] roundKey = null;
            if (operation.equalsIgnoreCase("encrypt")) {
                roundKey = subKeys[i];
            } else if (operation.equalsIgnoreCase("decrypt")) {
                roundKey = subKeys[noOfRounds - i - 1];
            } else {
                System.out.println("Unsupported operation");
                System.exit(0);
            }
            rightHalf = performXOR(rightHalf, roundKey);
            /**
             * S-Box
             */
            rightHalf = sBox(rightHalf);
            /**
             * Permutation
             */
            rightHalf = permute(rightHalf, P);
            /**
             * XOR rightHalf with leftHalf
             */
            rightHalf = performXOR(rightHalf, leftHalf);
            /**
             * L(i) = R(i-1)
             */
            leftHalf = temp;
        }
        /**
         * 32 bit swap
         */
        byte[] concatHalves = concatBits(rightHalf, blockSize / 2, leftHalf, blockSize / 2);
        /**
         * Inverse Initial Permutation
         */
        result = permute(concatHalves, IIP);
        return result;
    }

    private byte[] sBox(byte[] input) {
        /**
         * Split input to 6-bit blocks
         */
        input = split(input, 6);
        byte[] output = new byte[input.length / 2];
        int leftHalf = 0;
        for (int i = 0; i < input.length; i++) {
            byte block = input[i];
            /**
             * row - first and last bits
             * column - 4 bits in the middle
             */
            int row = 2 * (block >> 7 & 0x0001) + (block >> 2 & 0x0001);
            int col = block >> 3 & 0x000F;
            int[] selectedSBox = getSBox(i);
            int rightHalf = 0;
            if(selectedSBox != null)
                rightHalf = selectedSBox[16 * row + col];
            if (i % 2 == 0) {
                leftHalf = rightHalf;
            } else {
                output[i / 2] = (byte) (16 * leftHalf + rightHalf);
                leftHalf = 0;
            }
        }
        return output;
    }

    private int[] getSBox(int i) {
        switch (i) {
            case 0:
                return S1;
            case 1:
                return S2;
            case 2:
                return S3;
            case 3:
                return S4;
            case 4:
                return S5;
            case 5:
                return S6;
            case 6:
                return S7;
            case 7:
                return S8;
            default:
                return null;
        }
    }
}
