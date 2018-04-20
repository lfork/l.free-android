package com.lfork.a98620.lfree.util.file;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

/*
 * Created by 98620 on 2017/11/3.
 */

public class FileHelper {

    private static final String TAG = "PersonsFileHelper";
    public static String ExternalCacheDirStringURL;
    public static String SDRootPath;

    public static String KeyPath, ValuePath;
    private static boolean IsDirInitialed = false;

    static {
        SDRootPath =  Environment.getExternalStorageDirectory().toString();
        ExternalCacheDirStringURL = Environment.getDownloadCacheDirectory().toString();
    }


    /**
     * @param data     传入需要写入文件的数据
     * @param filePath 传入文件路径
     * @return 文件操作成功返回true 否则 返回false
     */
    private static boolean write(String data, String filePath) {
        if (data == null) {
            return false;
        }
        //资源申请
        File file = new File(filePath);
        Writer writer = null;
        try {
            file.createNewFile();
            // 这样写的话可以指定文本的编码格式 ***** 默认为utf-8
            writer = new OutputStreamWriter(new FileOutputStream(file));
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(data);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            //资源释放
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public static String load(String filePath) {


        if (filePath == null) {
            return null;
        }
        File file = new File(filePath);


        if (!file.exists()) {
            return null;
        }
        StringBuilder result = new StringBuilder();

//        byte[] buffer = new byte[3000000];


        BufferedReader in = null;
        try {
            // 当该文件不存在时再创建一个新的文件
            file.createNewFile();

            FileInputStream fis = new FileInputStream(file);
//            int i = 0, temp = 0;
//            temp = fis.read();
//            while (temp != -1) {
//                buffer[i] = (byte) temp;
//                temp = fis.read();
//            }

            // 这样写的话可以指定文本的编码格式 *****
            InputStreamReader isr = new InputStreamReader(fis);

            in = new BufferedReader(isr);

            // 这里是按照字符流进行的读取

            String str = null;
            while ((str = in.readLine()) != null) {
                result.append(str);
//            result.append(Arrays.toString(buffer));
            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result.toString();

    }

    private static boolean delete(String filePath) {
        return new File(filePath).delete();
        //Java无法直接删除一个非空目录
    }

    /**
     * 把一个文件转化为字节
     *
     * @param file
     * @return byte[]
     * @throws Exception
     */
    public static byte[] load(File file) throws Exception {
        byte[] bytes = null;
        if (file != null) {
            InputStream is = new FileInputStream(file);
            int length = (int) file.length();
            if (length > Integer.MAX_VALUE)   //当文件的长度超过了int的最大值
            {
                System.out.println("this file is max ");
                return null;
            }
            bytes = new byte[length];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            //如果得到的字节长度和file实际的长度不一致就可能出错了
            if (offset < bytes.length) {
                System.out.println("file length is error");
                return null;
            }
            is.close();
        }
        return bytes;
    }

    public static boolean write(byte[] data, String filePath) {
        if (data == null) {
            return false;
        }
        FileOutputStream out = null;
        //资源申请
        File file = new File(filePath);
        try {
            file.createNewFile();
            // 这样写的话可以指定文本的编码格式 ***** 默认为utf-8
            out = new FileOutputStream(file);
            out.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //资源释放


        }
        return true;

    }


}
