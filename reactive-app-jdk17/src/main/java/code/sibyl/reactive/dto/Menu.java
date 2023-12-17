package code.sibyl.reactive.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Menu {
    private String id;
    private String name;
    private String code;
    private String path;
    private String title;
    private String icon;
    private String linkUrl;
    private List<Menu> children;
    public String getId() {
        return id;
    }
    public Menu setId(String id) {
        this.id = id;
        return this;
    }
    public String getName() {
        return name;
    }
    public Menu setName(String name) {
        this.name = name;
        return this;
    }
    public String getCode() {
        return code;
    }
    public Menu setCode(String code) {
        this.code = code;
        return this;
    }
    public String getPath() {
        return path;
    }
    public Menu setPath(String path) {
        this.path = path;
        return this;
    }
    public String getTitle() {
        return title;
    }
    public Menu setTitle(String title) {
        this.title = title;
        return this;
    }
    public String getIcon() {
        return icon;
    }
    public Menu setIcon(String icon) {
        this.icon = icon;
        return this;
    }
    public String getLinkUrl() {
        return linkUrl;
    }
    public Menu setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
        return this;
    }
    public List<Menu> getChildren() {
        return children;
    }
    public Menu setChildren(List<Menu> children) {
        this.children = children;
        return this;
    }

    
}
