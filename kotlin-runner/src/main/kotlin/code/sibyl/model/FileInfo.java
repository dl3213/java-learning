package code.sibyl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {
    private String name;
    private String url;
    private long length;
    private boolean isDir;
    private boolean isFile;
}