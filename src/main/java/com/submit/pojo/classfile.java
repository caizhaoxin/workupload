package com.submit.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class classfile {
    private Integer id;

    private Integer teachclassid;

    private String filename;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadtime;

    private Integer downloadtime;

    private Long filesize;

    private String filesizeReadable;

    public String getFilesizeReadable() {
        return filesizeReadable;
    }

    public void setFilesizeReadable(String filesizeReadable) {
        this.filesizeReadable = filesizeReadable;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTeachclassid() {
        return teachclassid;
    }

    public void setTeachclassid(Integer teachclassid) {
        this.teachclassid = teachclassid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Date getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(Date uploadtime) {
        this.uploadtime = uploadtime;
    }

    public Integer getDownloadtime() {
        return downloadtime;
    }

    public void setDownloadtime(Integer downloadtime) {
        this.downloadtime = downloadtime;
    }

    public Long getFilesize() {
        return filesize;
    }

    public void setFilesize(Long filesize) {
        this.filesize = filesize;
    }

    @Override
    public String toString() {
        return "classfile{" +
                "id=" + id +
                ", teachclassid=" + teachclassid +
                ", filename='" + filename + '\'' +
                ", uploadtime=" + uploadtime +
                ", downloadtime=" + downloadtime +
                ", filesize=" + filesize +
                '}';
    }
}
