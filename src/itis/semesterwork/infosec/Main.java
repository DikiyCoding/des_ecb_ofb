package itis.semesterwork.infosec;

import static itis.semesterwork.infosec.Constants.Test.*;
import static itis.semesterwork.infosec.Constants.Mode.*;

public class Main {

    public static void main(String[] args) {
        new Des()
                .encrypt(
                        INITIAL_PERMUTATION_AND_EXPANSION.getTextPath(),
                        INITIAL_PERMUTATION_AND_EXPANSION.getCipherPath(),
                        ECB)
                .encrypt(
                        INVERSE_PERMUTATION_AND_EXPANSION.getTextPath(),
                        INVERSE_PERMUTATION_AND_EXPANSION.getCipherPath(),
                        ECB)
                .decrypt(
                        RIGHT_SHIFTS_IN_DECRYPTION.getTextPath(),
                        RIGHT_SHIFTS_IN_DECRYPTION.getCipherPath(),
                        ECB)
                .encrypt(
                        KEY_PERMUTATION.getTextPath(),
                        KEY_PERMUTATION.getCipherPath(),
                        ECB)
                .encrypt(
                        DATA_PERMUTATION.getTextPath(),
                        DATA_PERMUTATION.getCipherPath(),
                        ECB)
                .encrypt(
                        S_BOX.getTextPath(),
                        S_BOX.getCipherPath(),
                        ECB)
                .encrypt(VECTOR.getTextPath(),
                        VECTOR.getCipherPath(),
                        OFB);
    }
}
