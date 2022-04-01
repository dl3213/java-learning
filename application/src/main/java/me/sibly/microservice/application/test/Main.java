package me.sibly.microservice.application.test;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Classname Main
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/04/01 22:22
 */

public class Main {

    public static Member getRet() {
        List<Member> memberList = (getMembers());
        System.err.println(memberList.size());
        memberList.forEach(System.err::println);
        System.err.println();
        System.err.println();
        System.err.println();

        //Member remove = members.remove(0);
        Map<Integer, List<Member>> collectMap = memberList.stream().sorted(Comparator.comparing(Member::getGeneration)).collect(Collectors.groupingBy(Member::getGeneration));
        //Map<Integer, List<Member>> collectMap = Collections.synchronizedMap(collect);

//        collectMap.forEach((k,v)->{
//            System.err.println(k);
//            System.err.println(v);
//        });
        Member ret = memberList.get(0);
        for (int i = 0; i < memberList.size(); i++) {
            Member member = memberList.get(i);

            List<Member> temp = new ArrayList<>(memberList);
            System.err.println(temp);
            System.err.println(memberList);

            if (collectMap.get(member.getGeneration()) == null) continue;
            List<Member> same = collectMap.get(member.getGeneration())
                    .stream()
                    .filter(e -> !member.equals(e))
                    .collect(Collectors.toList());

//            member.setSameGeneration(same);

            List<Member> members1 = collectMap.get(member.getGeneration() + 1);
            if(CollectionUtils.isEmpty(members1)) continue;

            List<Member> collect1 = members1
                    .stream()
                    .filter(e -> Objects.nonNull(e.getFather()) || Objects.nonNull(e.getMother()))
                    .filter(e -> member.getName().equals(e.getFather()) || member.getName().equals(e.getMother()))
                    .collect(Collectors.toList());

//            member.setChildren(collect1);

            temp.removeAll(collect1);
            temp.remove(member);
        }

        return ret;
    }

    private static ArrayList<Member> getMembers() {
        ArrayList<Member> members = new ArrayList<Member>();
        Member a = new Member().setGeneration(1).setName("a");
        members.add(a);
        Member b = new Member().setGeneration(1).setName("b");
        members.add(b);
        Member c = new Member().setGeneration(2).setName("c");
        members.add(c);
        Member d = new Member().setGeneration(2).setName("d");
        members.add(d);
        Member e = new Member().setGeneration(3).setName("e");
        members.add(e);
        Member f = new Member().setGeneration(3).setName("f");
        members.add(f);
//        Member g = new Member().setGeneration(3).setName("g");
//        members.add(g);
//        Member h = new Member().setGeneration(3).setName("h");
//        members.add(h);
//        Member i = new Member().setGeneration(4).setName("i");
//        members.add(i);
//        Member j = new Member().setGeneration(4).setName("j");
//        members.add(j);

        c.setFather(a.getName());
        c.setMother(b.getName());

        e.setFather(c.getName());
        e.setMother(d.getName());

        f.setFather(c.getName());
        f.setMother(d.getName());


        return members;
    }
}

