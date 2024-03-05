package code.sibyl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseDTO {

    private Long id;
    private String name;
    private String type;
    private Integer version;

}
