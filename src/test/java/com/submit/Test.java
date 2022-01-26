package com.submit;

import java.io.File;
import java.nio.file.Paths;

public class Test {
    public static void main(String[] args) {
        String path = "E:\\workip\\2\\411064248_研究生个人自传.docx";
        File file = new File(path);
        if(file.delete()){
            System.out.println(path.toString() + " 文件已被删除！");
        }else{
            System.out.println(path.toString() + " 删除文件失败！");
        }
    }
}
