package code.sibyl.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@ToString
public class MenuDTO {

    private String id;
    private String name;
    private String code;
    private String path;
    private String title;
    private String icon;
    private String linkUrl;
    private List<MenuDTO> children;
}
