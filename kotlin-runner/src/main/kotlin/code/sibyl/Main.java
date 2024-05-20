package code.sibyl;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

public class Main {


    public static void main(String[] args) {
        File dir = new File("D:\\4game\\steam\\steamapps\\workshop\\content\\431960");
        Date beginDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beginDateStr = simpleDateFormat.format(beginDate);
        System.out.println("开始时间：" + beginDateStr);
        printDirByRecursive(dir, 0, file -> {
            System.err.println(file + " = > " + file.getName());
            try {
                FileUtils.moveFile(file, new File("D:\\4pc\\dl3213\\" + file.getName()));
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
                    printDirByRecursive(f, level + 1, consumer);
                }
            }
        }
    }

}
