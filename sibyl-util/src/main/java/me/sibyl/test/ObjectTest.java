package me.sibyl.test;

import lombok.Builder;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author dyingleaf3213
 * @Classname ObjectTest
 * @Description TODO
 * @Create 2023/06/11 20:21
 */

public class ObjectTest {

    public static void main1(String[] args) {
        Obj obj1 = Obj.builder().id(1).name("test1").build();
        Obj obj2 = Obj.builder().id(1).name("test1").build();
        System.err.println(obj1);
        System.err.println(obj1.hashCode());//754666084
        System.err.println();
        System.err.println(obj2);
        System.err.println(obj2.hashCode());//88558700
        System.err.println();
        System.err.println(obj1 == obj2);// false
        System.err.println(Objects.equals(obj1, obj2));// false
        System.err.println();
        HashMap<Obj, String> hashMap = new HashMap<>();
        hashMap.put(obj1, "1");
        hashMap.put(obj2, "2");
        System.err.println(hashMap);
        System.err.println(hashMap.get(obj1));//1
        System.err.println(hashMap.get(obj2));//2
    }

    //    @Data
    @Builder
    @Accessors(chain = true)
    @ToString
    static class Obj {
        private Integer id;
        private String name;

//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (!(o instanceof Obj)) return false;
//            Obj obj = (Obj) o;
//            return Objects.equals(id, obj.id) && Objects.equals(name, obj.name);
//        }

//    @Override
//    public int hashCode() {
//        return Objects.hash(id, name);
//    }
    }
}
