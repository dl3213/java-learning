package code.sibyl.controller.rest;


public class UploadResult {
    private int filesProcessed;
    private String savedPath;
    private long totalSize;
    private long uploadTime;

    public UploadResult() {}

    public UploadResult(int filesProcessed, String savedPath, long totalSize, long uploadTime) {
        this.filesProcessed = filesProcessed;
        this.savedPath = savedPath;
        this.totalSize = totalSize;
        this.uploadTime = uploadTime;
    }

    // getter和setter
    public int getFilesProcessed() {
        return filesProcessed;
    }

    public void setFilesProcessed(int filesProcessed) {
        this.filesProcessed = filesProcessed;
    }

    public String getSavedPath() {
        return savedPath;
    }

    public void setSavedPath(String savedPath) {
        this.savedPath = savedPath;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(long uploadTime) {
        this.uploadTime = uploadTime;
    }
}
