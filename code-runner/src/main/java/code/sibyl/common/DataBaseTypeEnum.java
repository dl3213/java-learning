package code.sibyl.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String code;
}
