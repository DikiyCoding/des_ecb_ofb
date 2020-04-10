package itis.semesterwork.infosec;

import java.io.*;

public class Des {

    private final byte[] mKey;
    private final Algorithm mAlgorithm;

    public Des() {
        mAlgorithm = new Algorithm();
        mKey = ConvertUtils.hexStringToByteArray(new Keygen().readKey());
    }

    public Des encrypt(String plainTextFilePath, String cipherTextFilePath, Constants.Mode mode) {
        try {
            mAlgorithm.setMode(mode.name());
            String operation = "encrypt";
            File textFile = new File(plainTextFilePath);
            File cipherFile = new File(cipherTextFilePath);
            FileInputStream textFileInputStream = new FileInputStream(textFile);
            FileOutputStream cipherFileOutputStream = new FileOutputStream(cipherFile);
            byte[] message = new byte[(int) textFile.length()];
            textFileInputStream.read(message);
            byte[] cipher = mAlgorithm.crypt(message, mKey, operation);
            cipherFileOutputStream.write(cipher);
            cipherFileOutputStream.flush();
            cipherFileOutputStream.close();
            textFileInputStream.close();
            System.out.println("Encryption's done! Please check " + cipherFile.getName() + " for output!");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return this;
    }

    public Des decrypt(String plainTextFilePath, String cipherTextFilePath, Constants.Mode mode) {
        try {
            mAlgorithm.setMode(mode.name());
            String operation = "decrypt";
            File textFile = new File(plainTextFilePath);
            File cipherFile = new File(cipherTextFilePath);
            FileInputStream cipherFileInputStream = new FileInputStream(cipherFile);
            FileOutputStream textFileOutputStream = new FileOutputStream(textFile);
            byte[] cipher = new byte[(int) cipherFile.length()];
            cipherFileInputStream.read(cipher);
            if (mode.equals(Constants.Mode.OFB)) operation = "encrypt";
            byte[] message = mAlgorithm.crypt(cipher, mKey, operation);
            textFileOutputStream.write(message);
            textFileOutputStream.flush();
            textFileOutputStream.close();
            cipherFileInputStream.close();
            System.out.println("Decryption's done! Please check " + textFile.getName() + " for output!");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return this;
    }
}
