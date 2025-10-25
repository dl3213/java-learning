package code.sibyl;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ListProcesses {
    public static void main(String[] args) {

        //Paralogue-Win64-Shipping.exe

        try {
            String command;
            if (System.getProperty("os.name").contains("Windows")) {
                // Windows系统
                command = "tasklist";
            } else {
                // Unix/Linux系统
                command = "ps -e";
            }
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

