package code.sibyl.common;

import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@ToString
public class Either<L, R> {

    public static void main123(String[] args) {
        List<Either<String, Object>> collect = Stream
                .iterate(1, i -> i + 1)
                .limit(100)
                .map(Either::getOne)
                .collect(Collectors.toList());
        Either<String, List<Object>> sequence = Either.sequence(collect, (e1, e2) -> e1 + " " + e2);
        if (sequence.isLeft()) {
            System.err.println(sequence.getLeft());
        } else {
            System.err.println(sequence.getRight().size());
            for (Object o : sequence.getRight()) {
                System.err.println(o);
            }
        }
    }

    private static Either<String, Object> getOne(int i) {
        if (new Random().nextInt() % 2 == 0) {
            //if (true) {
            return Either.right(new Object());
        } else {
            return Either.left(String.valueOf(i));
        }
    }

    private L left;
    private R right;

    public boolean isLeft() {
        return this.left != null;
    }

    public boolean isRight() {
        return this.right != null;
    }

    public static <L, R> Either<L, R> left(L exception) {
        Either<L, R> either = new Either<>();
        either.setLeft(exception);
        return either;
    }

    public static <L, R> Either<L, R> right(R value) {
        Either<L, R> either = new Either<>();
        either.setRight(value);
        return either;
    }

    public <T> Either<L, T> map(Function<R, T> function) {
        if (isLeft()) return left(left);
        else return right(function.apply(right));
    }

    public static <L, R> Either<L, List<R>> sequence(List<Either<L, R>> eitherList, BinaryOperator<L> accumulator) {
        if (eitherList.stream().allMatch(Either::isRight))
            return right(eitherList.stream().map(Either::getRight).collect(Collectors.toList()));
        else
            return left(
                    accumulator == null ?
                            null :
                            eitherList
                                    .stream()
                                    .filter(Either::isLeft)
                                    .map(Either::getLeft)
                                    .reduce(accumulator)
                                    .orElse(null)
            );
    }
}
