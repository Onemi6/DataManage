package com.hzlf.sampletest.poiword;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.widget.Toast;

public class Tools {
    /**
     * @param fileName
     * @return byte[]
     */
    public static byte[] readFile(String fileName) {
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        byte[] data = null;
        try {
            fis = new FileInputStream(fileName);
            byte[] buffer = new byte[8 * 1024];
            int readSize = -1;
            baos = new ByteArrayOutputStream();
            while ((readSize = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, readSize);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return data;

    }

    // 分析 /mnt/sdcard/tarenaEnglish/book/1/1.txt
    // mnt/sdcard/tarenaEnglish/db/word.db
    // mnt/sdcard/tarenaEnglish/sound/

    /**
     * @param data     数据
     * @param path     路径
     * @param fileName 文件名
     * @return true成功 false失败
     */
    public static boolean writeToSdcard(byte[] data, String path,
                                        String fileName) {
        boolean isSuccess = false;
        FileOutputStream fos = null;
        try {
            // 判断有没有sdCard
            // String state=android.os.Environment.getExternalStorageState();
            // true,表示有sdcard
            // if (android.os.Environment.MEDIA_MOUNTED.equals(state))
            // {
            // 判断有没有文件夹
            // path=/mnt/sdcard/tarenaEnglish/book/1
            File filePath = new File(path);
            if (!filePath.exists()) {
                // 创建文件夹
                filePath.mkdirs();
            }

            // 判断有没有同名的文件
            File file = new File(path + fileName);
            // 有的话，删除
            if (file.exists()) {
                file.delete();
            }
            // 写文件
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            return true;
            // }

        } catch (Exception e) {
            return false;
            // TODO: handle exception
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // return false;
    }

    /**
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        if (null == str || "".equals(str) || "null".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * @param context
     * @param str
     */
    public static void showInfo(Context context, String str) {
        // 一行
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
        // 以后要改成 toast.xml.
        // Toast toast=new Toast(context);
        // toast.setView(view);
    }
}