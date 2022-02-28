package com.submit.controller;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.read.context.AnalysisContext;
import com.alibaba.excel.read.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.submit.pojo.*;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Controller
public class fileController {
    @Value("${file.upload.url}")
    private String uploadFilePath;

    private static final Logger logger = LoggerFactory.getLogger(fileController.class);
    @Autowired(required = false)
    com.submit.dao.jobMapper jobMapper;
    @Autowired(required = false)
    com.submit.dao.teachclassMapper teachclassMapper;
    @Autowired(required = false)
    com.submit.dao.scoreMapper scoreMapper;
    @Autowired(required = false)
    com.submit.service.teacherService teacherService;
    @Autowired(required = false)
    com.submit.service.classfileService classfileService;

    public static String OriginalPassWd = "123456";

    @ResponseBody
    @PostMapping("teacher/deleteclassfile")
    public String deletestudent(Integer classid, Integer classfileid) {
        System.out.println("classfileid: " + classfileid);
        try {
            // 文件删除
            classfile classfile = classfileService.selectOneByClassfileId(classfileid);
            if (classfile == null) return "删除文件失败";
            String fileName = classfile.getFilename();
            String path = Paths.get(uploadFilePath, "classfile", Integer.toString(classid), fileName).toString();
            File file = new File(path);
            if (file.delete()) {
                logger.info(path + " 文件已被删除！");
            } else {
                logger.info(path + " 删除文件失败！");
            }
        } catch (
                Exception e) {
            return String.format("删除文件失败, 错误：", e.toString());
        }
        try {
            // 数据库删除记录
            classfileService.deleteByPrimaryKey(classfileid);
        } catch (
                Exception e) {
            return String.format("删除文件失败, 错误：", e.toString());
        }
        return "删除文件成功";
    }

    @ResponseBody
    @GetMapping("teacher/getclassfile")
    public Map<String, Object> teachergetclassfile(Integer teachclassid) {
        return getclassfile(teachclassid);
    }

    @ResponseBody
    @GetMapping("student/getclassfile")
    public Map<String, Object> studentgetclassfile(Integer teachclassid) {
        return getclassfile(teachclassid);
    }

    public Map<String, Object> getclassfile(Integer teachclassid) {
        List<classfile> list = null;
        if (teachclassid==null || teachclassid <= 0) {
            list = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("code", "0");
            map.put("count", list.size());
            map.put("data", list);
            return map;
        }
        try {
            list = classfileService.getclassfile(teachclassid);
        } catch (Exception e) {
            list = new ArrayList<>();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("code", "0");
        map.put("count", list.size());
        map.put("data", list);
        return map;
    }

    @ResponseBody
    @GetMapping("teacher/downloadclassfile")
    public String teachergetclassfile(@RequestParam("classid") Integer classid,
                               @RequestParam("classfileid") Integer classfileid,
                               HttpServletRequest request, HttpServletResponse response) throws IOException {
        return getclassfile(classid, classfileid, request, response);
    }

    @ResponseBody
    @GetMapping("student/downloadclassfile")
    public String studentgetclassfile(@RequestParam("classid") Integer classid,
                                      @RequestParam("classfileid") Integer classfileid,
                                      HttpServletRequest request, HttpServletResponse response) throws IOException {
        return getclassfile(classid, classfileid, request, response);
    }

    public String getclassfile(@RequestParam("classid") Integer classid,
                               @RequestParam("classfileid") Integer classfileid,
                               HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取文件名
        classfile classfile = classfileService.selectOneByClassfileId(classfileid);
        String fileName = "";
        String fileNameArr[] = classfile.getFilename().split("_");
        if (fileNameArr == null || fileNameArr.length <= 1) {
            return "下载失败";
        }
        for (int i = 1; i < fileNameArr.length; i++) {
            fileName += fileNameArr[i];
        }
        // 拼接成文件路径
        String pat = Paths.get(uploadFilePath, "classfile", Integer.toString(classid), classfile.getFilename()).toString();
        response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        response.setContentType("text/html");

        File file = new File(pat);
        if (!file.exists()) {
            System.out.println(pat.toString());
            return "文件不存在";
        }

        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());

        response.setHeader("Content-Disposition", "attachment;filename=" +
                new String(fileName.getBytes("utf-8"), "ISO8859_1"));

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
            byte[] buff = new byte[1024];
            OutputStream os = response.getOutputStream();
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            return "下载失败";
        }
        try {
            classfileService.increDownloadTime(classfileid);
        } catch (Exception e) {
            //
        }
        return "下载成功";
    }

    @ResponseBody
    @PostMapping("teacher/addclassfile")
    public String addclassfile(MultipartFile file, Integer lesson) throws IOException {
        // 检查文件是否合法
        if (file.isEmpty() || file == null) {
            return "请选择正确文件";
        }
        // 数据库更新
        // 文件名前面加一个hash前缀，防止冲突
        Random r = new Random();
        String fileName = r.nextInt(999999999) + "_" + file.getOriginalFilename();
        classfile classfile = new classfile();
        classfile.setFilename(fileName);
        classfile.setTeachclassid(lesson);
        classfile.setDownloadtime(0);
        classfile.setFilesize(file.getSize());
        classfile.setUploadtime(new Date());
        classfileService.insertOneFile(classfile);

        // 把文件放到file对应的位置
        String pat = "";
        pat = "classfile/" + lesson + "/";
        String path_str = Paths.get(uploadFilePath, pat).toString();
        File dir = new File(path_str);
        if (!dir.exists())//不存在就新建文件夹
        {
            dir.mkdirs();
        }
        logger.info(path_str);

        File insertFile = new File(dir, fileName);//创建文件
        OutputStream out = new FileOutputStream(insertFile);
        BufferedOutputStream buf = new BufferedOutputStream(out);

        byte by[] = file.getBytes();
        buf.write(by);
        buf.close();
        out.close();

        return "上传成功";
    }

    @ResponseBody
    @PostMapping("teacher/addstudent")
    public String teacheraddstudent(MultipartFile file, String name, String pinyin, String password, String studentno) throws IOException {
        logger.info(studentno + " " + name + " " + password + " " + pinyin);
        //logger.info(file.getOriginalFilename());
        InputStream inputStream = null;
        int success = 0;
        int fail = 0;
        if (file == null || file.isEmpty()) {
            try {
                student student = new student();
                student.setName(name);
                student.setPassword(password);
                student.setStudentno(studentno);
                student.setPinyin(pinyin);
                teacherService.addstudentuser(student);
                success++;
            } catch (Exception e) {
                e.printStackTrace();
                fail++;
            }
        } else {
            try {
                // 解析每行结果在listener中处理
                ExcelListener listener = new ExcelListener();
                inputStream = file.getInputStream();

                ExcelTypeEnum typeEnum = null;
                if (file.getOriginalFilename().contains("xlsx"))
                    typeEnum = ExcelTypeEnum.XLSX;
                else if (file.getOriginalFilename().contains("xls"))
                    typeEnum = ExcelTypeEnum.XLS;

                ExcelReader excelReader = new ExcelReader(inputStream, typeEnum, null, listener);

                excelReader.read(new Sheet(1, 1, student.class));
                List<Object> list = listener.getDatas();
                for (Object student : list) {
                    com.submit.pojo.student stu = (com.submit.pojo.student) student;
                    stu.setPassword(OriginalPassWd);
                    try {
                        teacherService.addstudentuser(stu);
                        success++;
                    } catch (Exception e) {
                        e.printStackTrace();
                        fail++;
                    }
                    logger.info(student.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "您成功插入:" + success + "条数据,插入失败:" + fail + "条数据";
    }


    @ResponseBody
    @PostMapping("onfile")
    public String onfile(MultipartFile file, int lessonid, int jobid, HttpServletRequest request) throws IOException, ParseException {
        //lessonid :teachclass jsp,安卓等信息的 id
        //jobid: jdbc,登录实验名称的 id
        // String pat="fileget/"+lessonid+"/"+jobid+"/";
        boolean isovertime = false;

        HttpSession session = request.getSession();
        if (file.isEmpty() || file == null) {
            return "请选择正确文件";
        }
        // 检查是否为zip
        String fileName = file.getOriginalFilename();
//        System.out.println("file:" + fileName);

        if (fileName == null || !fileName.endsWith(".zip")) {
            return "文件格式错误，请上传zip文件";
        }

        job job = jobMapper.selectByPrimaryKey(jobid);
        teachclass teachclass = teachclassMapper.selectByPrimaryKey(lessonid);
        score score = scoreMapper.uniqueindex(job.getId(), (String) session.getAttribute("studentid"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //判断是否超时
        Date endday = sdf.parse(job.getDuedate());
        Date now = new Date();
        if (endday.compareTo(now) < 0)//超时
        {
            isovertime = true;
        }

        logger.info(job.getTitle() + " " + job.getNo() + " " + job.getDuedate());
        logger.info(teachclass.getCoursename() + " " + teachclass.getTeachclassno());

        String pat = "";
        pat = "fileget/" + lessonid + "/" + jobid + "/";
        String path = Paths.get(uploadFilePath, pat).toString();
        File file2 = new File(path);
        if (!file2.exists())//不存在就新建文件夹
        {
            file2.mkdirs();
        }
        logger.info(path);
        String []filenameArr = file.getOriginalFilename().split("\\.");
        String filename = session.getAttribute("studentid") + "_" + (String) session.getAttribute("name") + "_"
                + "实验" + job.getNo() + "." +
                filenameArr[filenameArr.length-1];
        File file3 = new File(file2, filename);//创建文件
        OutputStream out = new FileOutputStream(file3);
        BufferedOutputStream buf = new BufferedOutputStream(out);

        byte by[] = file.getBytes();
        buf.write(by);
        buf.close();
        out.close();

        if (score == null) {
            score score1 = new score();
            score1.setJobid(job.getId());
            score1.setStudentno((String) session.getAttribute("studentid"));
            score1.setTime(new Date());
            scoreMapper.insert(score1);
        } else {
            score.setTime(new Date());
            scoreMapper.updateByPrimaryKeySelective(score);
        }

        if (isovertime) return "作业已超时，您已补交成功";
        return "上传成功";
    }

    @PostMapping(value = "/downloadclassfile")//teachclassid jobid
    public String downloadclassfile(int teachclassid, int classFileId) throws IOException {
        return "ok";
    }

    @PostMapping(value = "/download")//teachclassid jobid
    public String download(int lesson, int job, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return downloadzip(false, lesson, job, request, response);
    }

    //用于判断超时
    @PostMapping("/downloadovertime")
    public String downloadovertme(int lesson, int job, HttpServletRequest request, HttpServletResponse response) throws IOException {
        return downloadzip(true, lesson, job, request, response);
    }

    public String downloadzip(boolean isover, int lesson, int job, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pat = "";
        if (isover) pat = "fileget/overtime/" + lesson + "/" + job;
        else pat = "fileget/" + lesson + "/" + job;
        String zipname = "";
        teachclass teachclass = teachclassMapper.selectByPrimaryKey(lesson);
        job job1 = jobMapper.selectByPrimaryKey(job);
        zipname += teachclass.getCoursename();
        zipname += "实验" + job1.getNo() + job1.getTitle();
        zipname += ".zip";
        String filename = Paths.get(uploadFilePath, pat).toString();

        response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        if (session.getAttribute("teacherid") == null) return null;
        response.setContentType("text/html");

        //设置文件MIME类型
        response.setContentType(session.getServletContext().getMimeType(zipname));
        //设置Content-Disposition
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(zipname.getBytes("utf-8"), "ISO8859_1"));
        File file = new File(filename);
        if (!file.exists()) {
            file.mkdirs();
        }

        OutputStream out = response.getOutputStream();
        ZipOutputStream zipout = new ZipOutputStream(out);

        dozip(zipout, file, "");
        zipout.close();
        out.close();
        return null;
    }

    private static void dozip(ZipOutputStream zipout, File file, String addpath) throws IOException {
        if (file.isDirectory()) {
            File f[] = file.listFiles();
            for (int i = 0; i < f.length; i++) {
                if (f[i].isDirectory()) {
                    dozip(zipout, f[i], addpath + f[i].getName() + "/");
                } else {
                    dozip(zipout, f[i], addpath + f[i].getName());
                }
            }
        } else {
            InputStream input;
            BufferedInputStream buff;
            zipout.putNextEntry(new ZipEntry(addpath));
            input = new FileInputStream(file);
            buff = new BufferedInputStream(input);
            byte b[] = new byte[1024 * 5];
            int a = 0;
            while ((a = buff.read(b)) != -1) {
                zipout.write(b);
            }
            buff.close();
            input.close();
        }

    }


    /* 解析监听器，
     * 每解析一行会回调invoke()方法。
     * 整个excel解析结束会执行doAfterAllAnalysed()方法
     *
     * 下面只是我写的一个样例而已，可以根据自己的逻辑修改该类。
     * @author jipengfei
     * @date 2017/03/14
     */
    public static class ExcelListener extends AnalysisEventListener {

        //自定义用于暂时存储data。
        //可以通过实例获取该值
        private List<Object> datas = new ArrayList<Object>();

        public void invoke(Object object, AnalysisContext context) {
            datas.add(object);//数据存储到list，供批量处理，或后续自己业务逻辑处理。
            doSomething(object);//根据自己业务做处理
        }

        private void doSomething(Object object) {
            //1、入库调用接口
        }

        public void doAfterAllAnalysed(AnalysisContext context) {
            // datas.clear();//解析结束销毁不用的资源
        }

        public List<Object> getDatas() {
            return datas;
        }

        public void setDatas(List<Object> datas) {
            this.datas = datas;
        }
    }


}
