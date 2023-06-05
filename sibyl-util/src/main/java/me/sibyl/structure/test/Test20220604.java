package me.sibyl.structure.test;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author dyingleaf3213
 * @Classname Test20220604
 * @Description TODO
 * @Create 2022/06/04 20:23
 */

public class Test20220604 {


    private static int[] getData() {
        String pathname = "D:\\4code\\4java\\workspace\\sibyl-microserivce\\sibyl-microservice\\sibyl-util\\src\\main\\java\\me\\sibyl\\structure\\test\\data.txt";

        try (Stream<String> lines = Files.lines(Paths.get(pathname));) {
            return lines.map(Integer::valueOf).mapToInt(Integer::intValue).toArray();
        } catch (Exception e) {
        }
        return new int[0];
    }

    public static void main_data_builder(String[] args) {
        File file = new File("D:\\4code\\4java\\workspace\\sibyl-microserivce\\sibyl-microservice\\sibyl-util\\src\\main\\java\\me\\sibyl\\structure\\test\\data.txt"); //文件对象
        if (file.exists()) file.delete();
        try (PrintWriter output = new PrintWriter(new FileOutputStream(file, true))) {
            for (int i = 1000; i >= 0; i--)
                output.print(i + "\n");
        } catch (Exception e) {

        }
        System.err.println("end");
    }

    public static void main2(String[] args) {
        float f = 3.4f;
        double d = 3.4f;
        System.err.println(((Object) f).getClass());
    }

    public static void main1(String[] args) {
        System.err.println("start");
        temp:
        for (int i = 0; i < 5; i++) {
            System.err.println(i);
            for (int j = 0; j < 5; j++) {
                if (j == 3) break temp;
            }
        }
        System.err.println("end");
    }
}
