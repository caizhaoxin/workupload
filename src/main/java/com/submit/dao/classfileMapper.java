package com.submit.dao;

import com.submit.pojo.classfile;
import com.submit.pojo.job;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface classfileMapper {

    List<classfile> selectByTeachclassId(Integer teachclassid);

    classfile selectOneByClissfileId(Integer classfileid);

    int deleteByPrimaryKey(Integer id);

    int insertOneFile(classfile classfile);

    int increDownloadTime(Integer id);
}
