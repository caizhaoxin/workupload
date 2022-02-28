package com.submit.dao;

import com.submit.pojo.score;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface scoreMapper {

    @Select("SELECT d.studentno,d.score,d.`name` ,DATE_FORMAT(d.time,'%Y-%m-%d %h:%m:%s') as time,f.coursename,e.title FROM teachclass f, " +
            "(SELECT a.studentno,a.jobID,a.time,IFNULL(a.score,0) as score,b.`name` FROM score a " +
            "LEFT JOIN student b " +
            "on a.studentno=b.studentno " +
            "where a.studentno=#{studentid})d " +
            "LEFT JOIN job e " +
            "on d.jobID=e.ID " +
            "WHERE e.teachclassid=f.ID " +
            "ORDER BY e.ID DESC")
    List<Map<String,String>> getscoreupload(String studentid) ;

    int deleteByPrimaryKey(Long id);

    int insert(score record);

    int insertSelective(score record);

    score selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(score record);

    int updateByPrimaryKey(score record);
    @Insert("insert into score")
    boolean insertscore(score score);

    @Select("select * from score where jobID=#{jobID} and studentno=#{studentid}")
    score uniqueindex(@Param("jobID") Integer id,@Param("studentid") String studentid);

//    @Select("select e.name,d.* from(SELECT a.`no`,a.classID,a.studentno,b.ID scoreid,b.score," +
//            "DATE_FORMAT(b.time,'%Y-%m-%d %h:%m:%s') as time,b.note " +
//            "from studentclass a " +
//            "LEFT   JOIN  score b " +
//            "on a.studentno=b.studentno " +
//            "and b.jobID=#{jobid} " +
//            "where a.classID =(SELECT teachclassid FROM job  WHERE ID=#{jobid}) " +
//            "ORDER BY a.`no` asc)d,student e " +
//            "WHERE d.studentno=e.studentno " +
//            "order by d.no asc")

    @Select("select e.name,\n" +
            "       d.*,\n" +
            "       (CASE\n" +
            "            WHEN d.time is null THEN\n" +
            "                '未提交'\n" +
            "            WHEN DATE_FORMAT(d.time, '%Y-%m-%d') <= d.duedate THEN\n" +
            "                '按时提交'\n" +
            "            WHEN DATE_FORMAT(d.time, '%Y-%m-%d') > d.duedate THEN\n" +
            "                '延时提交'\n" +
            "            ELSE\n" +
            "                '未知状态'\n" +
            "           END\n" +
            "           ) AS status\n" +
            "from (SELECT a.`no`,\n" +
            "             a.classID,\n" +
            "             a.studentno,\n" +
            "             b.ID                                        scoreid,\n" +
            "             b.score,\n" +
            "             DATE_FORMAT(b.time, '%Y-%m-%d %h:%m:%s') as time,\n" +
            "             b.note,\n" +
            "             tmp_job.duedate\n" +
            "      from studentclass a\n" +
            "               LEFT JOIN score b\n" +
            "                         on a.studentno = b.studentno\n" +
            "                             and b.jobID = #{jobid}\n" +
            "               LEFT JOIN job tmp_job\n" +
            "                         on tmp_job.ID = #{jobid}\n" +
            "      where a.classID = (SELECT teachclassid FROM job WHERE ID = #{jobid})\n" +
            "      ORDER BY a.`no` asc) d,\n" +
            "     student e\n" +
            "WHERE d.studentno = e.studentno\n" +
            "order by d.no asc")
    List<Map> getscorebyjobid(int jobid);
}



















