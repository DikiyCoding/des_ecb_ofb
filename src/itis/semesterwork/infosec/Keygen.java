package itis.semesterwork.infosec;

import java.io.*;

import static itis.semesterwork.infosec.Constants.*;

public class Keygen {

    private final File keyFile;

    public Keygen() {
        keyFile = new File(KEY_PATH);
    }

    String readKey() {
        String keyString = "";
        try {
            FileReader keyFileReader = new FileReader(keyFile);
            BufferedReader bufferedReader = new BufferedReader(keyFileReader);
            keyString = bufferedReader.readLine();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (keyString == null || keyString.isEmpty()) {
            System.out.println("Key does not exist, so it will be auto-generated");
            keyString = generateKey();
        }
        return keyString;
    }

    String generateKey() {
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < KEY_LENGTH; i++) {
            double random = Math.random();
            int index = (int) (random * 16);
            key.append(HEX_ARRAY[index]);
        }
        System.out.print("Key generated");
        try {
            FileWriter fileWriter = new FileWriter(keyFile);
            fileWriter.write("");
            fileWriter.append(key.toString());
            fileWriter.flush();
            fileWriter.close();
            System.out.print(" and saved in " + keyFile.getName());
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        System.out.println();
        return key.toString();
    }
}
