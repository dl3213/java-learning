package me.sibyl.test;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @Classname EnumTest
 * @Description EnumTest
 * @Date 2023/6/16 11:24
 * @Author by Qin Yazhi
 */
public class EnumTest {

    @SneakyThrows
    public static void main1(String[] args) {
        String packageName = "me.sibyl.test";
        packageName = packageName.replaceAll("\\.", "/");
        System.err.println(packageName);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(packageName);

        Set<Class<?>> classes = new HashSet<>();
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            System.err.println(url);
            System.err.println(url.getProtocol());
            if (StringUtils.isBlank(url.getProtocol())) continue;
            switch (url.getProtocol()) {
                case "file": {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    File[] files = new File(filePath)
                            .listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
                    for (File file : files) {
                        System.err.println(file.getName());
                    }
                }
                break;
                case "jar":
                    break;
                default:
                    break;
            }
        }


    }
}
