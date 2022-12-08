package org.pp.spider;

import org.pp.util.NumberUtil;
import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * ************自强不息************
 *
 * @author 鹏鹏
 * @date 2022/12/7 9:33
 * ************厚德载物************
 **/
public class PythonUtils {
    public static void main(String[] args) {
        execPython2("build_file.py");
    }

    public static void execPython(String fileName) {
        try {
            // 第一个参数为本地python路径， 第二个参数为脚本路径
            String path = "D:\\soft\\Microsoft Visual Studio\\Shared\\Python36_64";
            String file = NumberUtil.getClassPath() + fileName;
            String[] args1 = new String[]{"cmd", "/c", path, file};
            Process proc = Runtime.getRuntime().exec(args1); // 执行py文件
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void execPython2(String fileName) {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.execfile(NumberUtil.getClassPath() + fileName); //fun 脚本位置
        PyFunction pyFunction = interpreter.get("get_lottery", PyFunction.class);
        //调用函数，如果函数需要参数，在Java中必须先将参数转化为对应的“Python类型”
        PyObject pyobj = pyFunction.__call__();
        System.out.println("结果: " + pyobj);
    }
}
