package me.sibyl.collection;

import com.google.common.collect.Sets;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        //判断两个区间是否存在重叠交叉
//        if(max(A.start,B.start)<=min(A.end,B.end)){
//            return "区间存在重叠交叉！";
//        }


//        List<String> list = Arrays.asList("1", "2", "3");
//        List<String> list2 = Arrays.asList("4", "2", "3");
//        Sets.SetView intersection = Sets.intersection(new HashSet(list), new HashSet(list2));
//        System.err.println(intersection);



        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();

        set1.add("a");
        set1.add("b");
        set1.add("c");

        set2.add("c");
        set2.add("d");
        set2.add("e");

        //交集
        set1.retainAll(set2);
        System.out.println("交集是 " + set1);  //交集是 [c]

    }
}
