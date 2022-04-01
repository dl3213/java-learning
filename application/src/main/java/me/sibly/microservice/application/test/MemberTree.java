package me.sibly.microservice.application.test;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Classname MemberTree
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/04/01 22:54
 */
@Data
@Accessors(chain = true)
@ToString
class MemberTree {
    private List<Member> root;
    private List<Member> children;
}