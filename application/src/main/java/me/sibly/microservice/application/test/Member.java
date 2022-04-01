package me.sibly.microservice.application.test;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Objects;

/**
 * @Classname Member
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/04/01 22:53
 */

@Data
@Accessors(chain = true)
@ToString(of = {"name","generation","father","mother"})
public class Member {
    private String name;
    private Integer generation;
    private String father;
    private String mother;
    private List<Member> sameGeneration;
    private List<Member> children;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(name, member.name) && Objects.equals(generation, member.generation) && Objects.equals(father, member.father) && Objects.equals(mother, member.mother);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, generation, father, mother);
    }
}