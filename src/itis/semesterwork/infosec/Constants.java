package itis.semesterwork.infosec;

public class Constants {

    final static int KEY_LENGTH = 16;
    final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    final static String ROOT_PATH = "files/";
    final static String ECB_MODE_PATH = "ecb/";
    final static String OFB_MODE_PATH = "ofb/";
    final static String KEY_PATH = ROOT_PATH + "key/key.txt";
    final static String FIRST_TEST_PATH = "encrypt_initial_permutation_and_expansion_test/";
    final static String SECOND_TEST_PATH = "encrypt_inverse_permutation_and_expansion_test/";
    final static String THIRD_TEST_PATH = "decrypt_right_shifts_in_decryption_test/";
    final static String FOURTH_TEST_PATH = "encrypt_key_permutation_test/";
    final static String FIFTH_TEST_PATH = "encrypt_data_permutation_test/";
    final static String SIXTH_TEST_PATH = "encrypt_s_box_test/";
    final static String SEVENTH_TEST_PATH = "encrypt_vector_test/";
    final static String PLAIN_TEXT_FILE_NAME = "plain_text.txt";
    final static String CIPHER_TEXT_FILE_NAME = "cipher_text.txt";

    enum Mode {
        ECB, OFB
    }

    enum Test {

        //ECB
        INITIAL_PERMUTATION_AND_EXPANSION(ECB_MODE_PATH, FIRST_TEST_PATH),
        INVERSE_PERMUTATION_AND_EXPANSION(ECB_MODE_PATH, SECOND_TEST_PATH),
        RIGHT_SHIFTS_IN_DECRYPTION(ECB_MODE_PATH, THIRD_TEST_PATH),
        KEY_PERMUTATION(ECB_MODE_PATH, FOURTH_TEST_PATH),
        DATA_PERMUTATION(ECB_MODE_PATH, FIFTH_TEST_PATH),
        S_BOX(ECB_MODE_PATH, SIXTH_TEST_PATH),

        //OFB
        VECTOR(OFB_MODE_PATH, SEVENTH_TEST_PATH);

        private final String textPath, cipherPath;

        Test(String modePath, String filePath) {
            textPath = ROOT_PATH + modePath + filePath + PLAIN_TEXT_FILE_NAME;
            cipherPath = ROOT_PATH + modePath + filePath + CIPHER_TEXT_FILE_NAME;
        }

        public String getTextPath() {
            return textPath;
        }

        public String getCipherPath() {
            return cipherPath;
        }
    }
}
