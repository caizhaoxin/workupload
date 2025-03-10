package com.submit.controller;

import com.submit.pojo.job;
import com.submit.pojo.student;
import com.submit.pojo.teachclass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class studentController {

    Logger logger = LoggerFactory.getLogger(studentController.class);
    @Autowired(required = false)
    com.submit.dao.studentMapper studentMapper;
    @Autowired(required = false)
    com.submit.service.studentService studentService;

    @ResponseBody
    @GetMapping("tt")
    public Object tt() {
        return studentService.getList("162210702234");
    }

    @PostMapping("updatepassword")
    public String updatepassword(String username, String oldpassword, String newpassword, Model model) {
        try {
            student student = studentMapper.selectByPrimaryKey(username);
            logger.info(student.getName() + " " + student.getStudentno() + " " + student.getPassword());
            if (student == null) {
                model.addAttribute("msg", "账号错误,非法操作");
                model.addAttribute(new student());
            } else if (!student.getPassword().equals(oldpassword)) {
                model.addAttribute("msg", "密码错误，请重新输入");
                model.addAttribute(student);
            } else {
                studentMapper.updatepassword(username, newpassword);
                model.addAttribute("msg", "密码已成功修改，退出登陆后请用新密码登录");
                model.addAttribute(student);
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("msg", "异常错误");
        }
        return "student/changepass";
    }

    @ResponseBody
    @GetMapping("getjobbyteachclassid")
    public List<job> getjobbyteachclassid(String id) {
        return studentService.getjobbyteacherclassid(Integer.parseInt(id));
    }

    @ResponseBody
    @GetMapping("getstudentclassthisterm")
    public List<teachclass> getstudentclassthisterm(HttpServletRequest request)
    {
        try {
            String studentid = (String) request.getSession().getAttribute("studentid");
            List<teachclass> list = studentService.getstudentclassbystudentno(studentid);
            return list;
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @GetMapping("getstudentexamscore")
    public List<teachclass> getstudentexamscore(HttpServletRequest request)
    {
        try {
            String studentid = (String) request.getSession().getAttribute("studentid");
            List<teachclass> list = studentService.getstudentclassbystudentno(studentid);
            return list;
        }catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @ResponseBody
    @PostMapping("addstuclaid")
    public String addstuclaid(int stuclaid, HttpServletRequest request) {
        try {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            ;
            return "插入失败";
        }
    }

    @ResponseBody
    @GetMapping("getscoreupload")
    public Map<String, Object> getscoreupload(HttpServletRequest request) {
        String studentid = (String) request.getSession().getAttribute("studentid");
        List<Map<String, String>> list = studentService.getscoreupload(studentid);
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "success");
        map.put("data", list);
        map.put("count", list.size());
        return map;
    }

    //获取某个课程的某个作业的信息
    @ResponseBody
    @GetMapping("gettaskdetailbyclass")
    public Map<String, Object> gettaskdetail(Integer classid, HttpServletRequest request) {
        String studentid = (String) request.getSession().getAttribute("studentid");
//        System.out.println("String studentid = (String) request.getSession().getAttribute(\"studentid\");:  "+studentid);
        List<Map<String, String>> list = null;
        try {
            list = studentService.gettaskdetailbyclass(classid, studentid);
        } catch (Exception e){
            list = new ArrayList<>();
        }
        System.out.println(list.toString());
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "success");
        map.put("data", list);
        map.put("count", list.size());
        return map;
    }

    //获取实验信息
    @ResponseBody
    @GetMapping("gettaskdetail")
    public List<Map<String, Object>> gettaskdetail(HttpServletRequest request) {
        String studentid = (String) request.getSession().getAttribute("studentid");
        return studentService.gettaskdetail(studentid);
    }
}
