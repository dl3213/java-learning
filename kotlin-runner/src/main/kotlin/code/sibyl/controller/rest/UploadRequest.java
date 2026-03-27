package code.sibyl.controller.rest;


import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class UploadRequest {
    private List<MultipartFile> files;
    private List<String> paths;
    private String folderName;
    private int totalFiles;

    // 构造函数、getter和setter
    public UploadRequest() {}

    public UploadRequest(List<MultipartFile> files, List<String> paths, String folderName, int totalFiles) {
        this.files = files;
        this.paths = paths;
        this.folderName = folderName;
        this.totalFiles = totalFiles;
    }

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getTotalFiles() {
        return totalFiles;
    }

    public void setTotalFiles(int totalFiles) {
        this.totalFiles = totalFiles;
    }
}
