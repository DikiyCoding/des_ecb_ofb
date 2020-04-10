package itis.semesterwork.infosec;

import static itis.semesterwork.infosec.Constants.*;

public class ConvertUtils {

    static byte[] hexStringToByteArray(String string) {
        int length = string.length();
        int n = (int) Math.ceil((length + 1) >> 1);
        byte[] result = new byte[n];
        for (int i = length - 1; i >= 0 ; i -= 2) {
            if (i == 0) {
                result[i / 2] = (byte) ((Character.digit('0', 16) << 4)
                        + Character.digit(string.charAt(i), 16));
            } else {
                result[i / 2] = (byte) ((Character.digit(string.charAt(i - 1), 16) << 4)
                        + Character.digit(string.charAt(i), 16));
            }
        }
        return result;
    }

    static byte[] performXOR(byte[] one, byte[] two) {
        byte[] result = new byte[one.length];
        for (int i = 0 ; i < one.length ; i++)
            result[i] = (byte) (one[i] ^ two[i]);
        return result;
    }

    public static byte[] XORBytes(byte[] in1, byte[] in2) {
        byte[] out = new byte[in1.length];
        for (int i = 0 ; i < in1.length ; i++) {
            out[i] = (byte)((in1[i] ^ in2[i]) & 0xff);
        }
        return out;
    }

    static byte[] permute(byte[] input, int[] mapping) {
        int byteCount = 1 + (mapping.length - 1) / 8;
        byte[] output = new byte[byteCount];
        int pos;

        for (int i = 0 ; i < mapping.length ; i++) {
            pos = mapping[i] - 1;
            int value = getBitFromArray(input, pos);
            setBitInArray(output, i, value);
        }
        return output;
    }

    static int getBitFromArray(byte[] array, int pos) {
        int value;
        int bytePos = pos / 8;
        int bitPos = pos % 8;
        value = (array[bytePos] >> (8 - (bitPos + 1))) & 0x0001;
        /* eg: right shift selected byte 5 times to get 3rd bit
         * (bitPos = 2) at rightmost position and
         * then AND with 0x0001*/
        return value;
    }

    static void setBitInArray(byte[] input, int pos, int value) {
        int bytePos = pos / 8;
        int bitPos = pos % 8;
        byte old = input[bytePos];
        old = (byte) (((0xFF7F >> bitPos) & old) & 0x00FF);
        byte newByte = (byte) ((value << (8 - (bitPos + 1))) | old);
        input[bytePos] = newByte;
    }

    static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    static void printBytes(byte[] input) {
        for (byte b : input) System.out.print(byteToBits(b) + " ");
        System.out.println();
    }

    static String byteToBits(byte b) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0 ; i < 8 ; i++)
            stringBuilder.append(b >> (8-(i+1)) & 0x0001);
        return stringBuilder.toString();
    }

    static byte[] getBits(byte[] input, int startPos, int length) {
        int noOfBytes = (length-1) / 8 + 1;
        byte[] output = new byte[noOfBytes];
        for (int i = 0 ; i < length ; i++) {
            int value = getBitFromArray(input, startPos + i);
            setBitInArray(output, i, value);
        }
        return output;
    }

    static byte[] rotateLeft(byte[] input, int step, int length) {
        int noOfBytes = (length - 1) / 8 + 1;
        byte[] output = new byte[noOfBytes];
        for (int i = 0 ; i < length ; i++) {
            int value = getBitFromArray(input, (i + step) % length);
            setBitInArray(output, i, value);
        }
        return output;
    }

    static byte[] concatBits(byte[] one, int oneLength,
                              byte[] two, int twoLength) {
        int noOfBytes = (oneLength + twoLength - 1) / 8 + 1;
        byte[] output = new byte[noOfBytes];
        int i = 0, j = 0;
        for (; i < oneLength ; i++) {
            int value = getBitFromArray(one, i);
            setBitInArray(output, j, value);
            j++;
        }
        for (i = 0 ; i < twoLength ; i++) {
            int value = getBitFromArray(two, i);
            setBitInArray(output, j, value);
            j++;
        }
        return output;
    }

    static byte[] mergeBytes(byte[] in1, byte[] in2) {
        byte[] out = new byte[in1.length + in2.length];
        int i = 0;
        for (byte b : in1) out[i++] = b;
        for (byte b : in2) out[i++] = b;
        return out;
    }

    static byte[] split(byte[] input, int length) {
        int noOfBytes = (8 * input.length - 1) / length + 1;
        byte[] output = new byte[noOfBytes];
        for (int i = 0 ; i < noOfBytes ; i++) {
            for (int j = 0; j < length ; j++) {
                int value = getBitFromArray(input, length * i + j);
                setBitInArray(output, 8 * i + j, value);
            }
        }
        return output;
    }
}
