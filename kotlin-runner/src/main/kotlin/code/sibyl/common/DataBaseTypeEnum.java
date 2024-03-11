package code.sibyl.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum DataBaseTypeEnum {

    h2("h2"),
    mysql("mysql"),
    postgresql("postgresql"),
    oracle("oracle"),
    sqlserver("sqlserver"),

    other("other"),
    ;

    public static DataBaseTypeEnum get(String code) {
        return Arrays.stream(DataBaseTypeEnum.values()).filter(e -> e.getCode().equals(code)).findFirst().orElse(null);
    }

    private String code;
}
