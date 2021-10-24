package org.l2jdd.tools.startloginserver;

import java.io.FileReader;
import java.util.Arrays;

public class StartLoginServer {
    public static void main(String[] args) throws Exception {

        FileReader fr = new FileReader("java.cfg");
        char [] data = new char[150];

        int out;
        while ((out = fr.read(data))>0) {
            if (out < 150) {
                data = Arrays.copyOf(data, out);
            }
        }
        Runtime.getRuntime().exec("java -jar ../libs/LoginServer.jar " + data);
        fr.close();
    }
}
