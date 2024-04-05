package code.sibyl.common;

import lombok.Data;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Classname TreeUtil
 * @Description TODO
 * @Create 2022/06/04 03:10
 */

public class TreeUtil {

    public static List<TreeNode> getChildren(TreeNode target, List<TreeNode> all) {
        return all
                .stream()
                .filter(e -> Objects.equals(e.getParentId(), target.getId()))
                .peek((e) -> e.setChildList(getChildren(e, all)))
                .collect(Collectors.toList());
    }

    public static List<TreeNode> getTree(Integer parentId, List<TreeNode> list) {
        //获取父节点
        return list
                .stream()
                .filter(m -> Objects.equals(parentId, m.getParentId()))
                .peek(m -> m.setChildList(getChildren(m, list)))
                .collect(Collectors.toList());
    }


    public static <T> List<T> getChildrenWithLamda(
            T target,
            Function<T, Object> targetSupplier,
            List<T> all,
            Function<T, Object> parentSupplier,
            BiConsumer<T, List<T>> childConsumer
    ) {
        return all
                .stream()
                .filter(e -> Objects.equals(parentSupplier.apply(e), targetSupplier.apply(target)))
                .peek(e -> childConsumer.accept(e, getChildrenWithLamda(e, targetSupplier, all, parentSupplier, childConsumer)))
                .collect(Collectors.toList());
    }

    public static <T> List<T> treeBuilderWithLamda(
            Object parentId,
            List<T> list,
            Function<T, Object> targetSupplier,
            Function<T, Object> parentSupplier,
            BiConsumer<T, List<T>> childConsumer
    ) {
        //获取父节点
        return list
                .stream()
                .filter(m -> Objects.equals(parentId, parentSupplier.apply(m)))
                .peek(m -> childConsumer.accept(m, getChildrenWithLamda(m, targetSupplier, list, parentSupplier, childConsumer)))
                .collect(Collectors.toList());
    }

    public static void main123(String... args) {
        //模拟从数据库查询出来
        List<TreeNode> menus = getData();
        List<TreeNode> tree = getTree(0, menus);
        List<TreeNode> treeV2 = treeBuilderWithLamda(0, menus, TreeNode::getId, TreeNode::getParentId, TreeNode::setChildList);
        System.err.println(tree);
        System.err.println(treeV2);
    }

    private static List<TreeNode> getData() {
        List<TreeNode> menus = Arrays.asList(
                new TreeNode(1, "0-1", 0),
                new TreeNode(2, "0-1-2", 1),
                new TreeNode(3, "0-1-2-3", 2),
                new TreeNode(4, "0-1-2-4", 2),
                new TreeNode(5, "0-1-2-5", 2),
                new TreeNode(6, "0-6", 0),
                new TreeNode(7, "0-6-7", 6),
                new TreeNode(8, "0-6-8", 6),
                new TreeNode(9, "0-6-7-9", 7),
                new TreeNode(10, "0-6-7-10", 7),
                new TreeNode(11, "0-11", 0),
                new TreeNode(12, "0-11-12", 11)
        );
        return menus;
    }

    @Data
    static class TreeNode {
        public Integer id;
        public String name;
        public Integer parentId;
        public List<TreeNode> childList;

        public TreeNode(Integer id, String name, Integer parentId) {
            this.id = id;
            this.name = name;
            this.parentId = parentId;
        }
    }
}