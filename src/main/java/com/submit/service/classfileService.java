package com.submit.service;

import com.submit.dao.*;
import com.submit.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;

@Service
public class classfileService {
    private static final Logger logger = LoggerFactory.getLogger(classfileService.class);
    @Autowired(required = false)
    com.submit.dao.classfileMapper classfileMapper;

    public List<classfile> getAllClassFileByClassId(Integer teachclassid) {
        return classfileMapper.selectByTeachclassId(teachclassid);
    }

    public int insertOneFile(classfile classfile) {
        return classfileMapper.insertOneFile(classfile);
    }

    public List<classfile> getclassfile(Integer teachclassid) {
        List<classfile> classfileList = classfileMapper.selectByTeachclassId(teachclassid);
        for (classfile classfile : classfileList) {
            String fileNameArr[] = classfile.getFilename().split("_");
            StringBuilder fileNameSB = new StringBuilder();
            if (fileNameArr == null || fileNameArr.length == 1) fileNameSB = new StringBuilder(classfile.getFilename());
            for (int i = 1; i < fileNameArr.length; i++) fileNameSB.append(fileNameArr[i]);
            classfile.setFilename(fileNameSB.toString());
            classfile.setFilesizeReadable(getSize(classfile.getFilesize()));
        }
        return classfileList;
    }

    public classfile selectOneByClassfileId(Integer classid) {
        return classfileMapper.selectOneByClissfileId(classid);
    }


    public int deleteByPrimaryKey(Integer id) {
        return classfileMapper.deleteByPrimaryKey(id);
    }

    public int increDownloadTime(Integer classFileID){
        return classfileMapper.increDownloadTime(classFileID);
    }

    public static String getSize(Long size) {
        String result = "";
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        /*实现保留小数点两位*/
        DecimalFormat df = new DecimalFormat("#.00");

        if (size >= gb) {
            result = df.format((float) size / gb) + "GB";
        } else if (size >= mb) {
            result = df.format((float) size / mb) + "MB";
        } else if (size >= kb) {
            result = String.format("%.2f", (float) size / kb) + "KB";
        } else {
            result = size + "B";
        }
        return result;
    }

}
