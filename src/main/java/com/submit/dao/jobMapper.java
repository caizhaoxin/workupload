package com.submit.dao;

import com.submit.pojo.job;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface jobMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(job record);

    int insertSelective(job record);

    job selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(job record);

    int updateByPrimaryKey(job record);

    //插入job，具体的那次作业
    @Insert("insert into job (teachclassid,no,title,duedate,type,note,creteTime)" +
            "values(#{teachclassid},#{no},#{title},#{duedate},#{type},#{note},#{creteTime})")
    boolean insertjob(job job);

    @Select("select * from job where teachclassid=#{teachclassid} order by createTime desc")
    List<job> getjobbyteachclassid(int teacherclassid);

    //转义字符
    @Select("SELECT DATE_FORMAT(b.createTime,'%Y-%m-%d %h:%m:%s') as time,b.*, " +
            "c.coursename FROM job b ,teachclass c " +
            "WHERE b.teachclassid in " +
            "(SELECT a.classID FROM studentclass a " +
            "WHERE a.studentno=#{studentid}) " +
            "AND c.ID=b.teachclassid " +
            "ORDER BY b.ID DESC")
    List<Map<String, Object>> gettaskdetail(String studentid);

    //转义字符
    @Select("select job.*,\n" +
            "       (\n" +
            "           CASE\n" +
            "               WHEN score.score is null THEN\n" +
            "                   0\n" +
            "               ELSE\n" +
            "                   score.score \n" +
            "               END\n" +
            "           ) AS score,\n" +
            "       (\n" +
            "           CASE\n" +
            "               WHEN score.submitTime is null THEN\n" +
            "                   '-'\n" +
            "               ELSE\n" +
            "                   score.submitTime \n" +
            "               END\n" +
            "           ) AS submitTime,\n" +
            "       (\n" +
            "           CASE\n" +
            "               WHEN score.submitTime is null THEN\n" +
            "                   '未提交'\n" +
            "               WHEN score.submitTime<=job.duedate THEN\n" +
            "                   '按时提交'\n" +
            "               WHEN score.submitTime>job.duedate THEN\n" +
            "                   '延时提交'\n" +
            "               ELSE\n" +
            "                   '未知状态'\n" +
            "               END\n" +
            "           ) AS status\n" +
            "from (SELECT DATE_FORMAT(b.createTime, '%Y-%m-%d') as time,\n" +
            "             b.*,\n" +
            "             c.coursename\n" +
            "      FROM job b,\n" +
            "           teachclass c\n" +
            "      WHERE b.teachclassid in\n" +
            "            (SELECT a.classID\n" +
            "             FROM studentclass a\n" +
            "             WHERE a.studentno = #{studentid})\n" +
            "        AND c.ID = b.teachclassid\n" +
            "        AND c.ID = #{classid}\n" +
            "      ORDER BY b.ID DESC) as job\n" +
            "         LEFT JOIN (SELECT jobID, score, DATE_FORMAT(time, '%Y-%m-%d') as submitTime from score where studentno = #{studentid}) as score\n" +
            "                   ON score.jobID = job.ID")
    List<Map<String, String>> gettaskdetailbyclass(@Param("classid") Integer classid, @Param("studentid") String studentid);

}
