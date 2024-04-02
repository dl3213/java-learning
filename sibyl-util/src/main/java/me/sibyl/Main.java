package me.sibyl;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

public class Main {

    /**
     *  1.递归
     *  2.枚举
     *  3.贪心
     *  4.回溯
     *  5.分治
     *  6.动态规划
     */

    public static void main(String[] args) {
        File dir = new File("D:\\4game\\steam\\steamapps\\workshop\\content\\431960");
        Date beginDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginDateStr = simpleDateFormat.format(beginDate);
        System.out.println("开始时间：" + beginDateStr);
        printDirByRecursive(dir,0, file -> {
            System.err.println(file + " = > " + file.getName());
            try {
                FileUtils.moveFile(file, new File("D:\\4pc\\dl3213\\"+file.getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //searchDir(dir);
        Date endDate = new Date();
        String endDateStr = simpleDateFormat.format(endDate);
        System.out.println("结束时间：" + endDateStr);

    }

    public static void printDirByRecursive(File dir, int level, Consumer<File> consumer) {
        System.out.println(dir.getAbsolutePath());
        //输出层次数
        for (int i = 0; i < level; i++) {
            System.out.print("-");
        }
        //获取这个目录下所有的子文件和子目录的数组。
        File[] files = dir.listFiles();
        //遍历这个数组，取出每个File对象
        if (files != null) {
            for (File f : files) {
                //判断这个File是否是一个文件，是：
                if (f.isFile()) {
                    consumer.accept(f);
                } else {//否则就是一个目录，继续递归
                    //递归调用
                    printDirByRecursive(f,level+1,consumer);
                }
            }
        }
    }

    public static void main22(String[] args) {
        int a= 0;
        while (a<5){
            switch (a){
                case 0:;
                case 3:a=a+2;
                case 1:;
                case 2:a=a+3;
                default:a=a+5;
            }
        }
        System.err.println(a);

        System.err.println("3".compareTo("2"));
    }
}
