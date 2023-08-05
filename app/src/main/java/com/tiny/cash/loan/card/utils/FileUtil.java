package com.tiny.cash.loan.card.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author lv
 * @time 2020/8/6 0015  14:31
 * @describe 文件操作公共类
 */
public class FileUtil {
    private static List<File> mFileList;

    public interface FileHandler {
        boolean processFile(String path);
    }

    static public int enumFiles(FileHandler handler, String dir, boolean recursion) {
        File fdir = new File(dir);
        if (!fdir.exists() || !fdir.isDirectory()) {
            return 1;
        }
        File[] flist = fdir.listFiles();
        if (flist == null) {
            return 1;
        }
        for (File f : flist) {
            if (f.getName().equals(".") || f.getName().equals("..")) {
                continue;
            }
            if (f.isDirectory()) {
                if (recursion && 2 == enumFiles(handler, f.getAbsolutePath(), recursion)) {
                    return 2;
                }
            } else if (f.isFile()) {
                if (!handler.processFile(f.getAbsolutePath())) {
                    return 2;
                }
            }
        }
        return 0;
    }

    /**
     * 创建文件夹
     *
     * @param strPath
     */
    public static boolean creatFileDir(String strPath) {
        boolean isExists;
        File file = new File(strPath);
        // video文件夹不存在
        if (!file.exists()) {
            // 创建文件夹
            file.mkdirs();
            isExists =  false;
        }else {
            isExists =  true;
        }
        return isExists;
    }


    static public List<File> getSortedFiles(String path) {
        List<File> files = new ArrayList<File>();
        for (String fileName : getSortedChilds(path)) {
            LogUtil.i("the my files is= " + fileName);
            files.add(new File(fileName));
        }
        return files;
    }

    public static List<File> getFile(String path) {
        mFileList = new ArrayList<File>();
        File file = new File(path);
        return getFile(file);
    }

    public static List<File> getFile(File file) {
        File[] fileArray = file.listFiles();
        for (File f : fileArray) {
            if (f.isFile()) {
                LogUtil.i("f1.getPath()=" + f.getPath() + "f1.getName=" + f.getName());
                if (compareOpenFile(f.getName())) {
                    mFileList.add(f);
                }
            } else {
                LogUtil.i("f2.getPath()=" + f.getPath());
                getFile(f);
            }
        }
        return mFileList;
    }

    /**
     * 比较是否是可打开类型文件
     *
     * @param fileName
     * @return
     */
    public static boolean compareOpenFile(String fileName) {
        LogUtil.i("the compareOpenFile is= " + fileName);
        String[] openFile = {"doc", "docx", "xls", "xlsx"};
        for (String file : openFile) {
            if (fileName.endsWith(file)) {
                LogUtil.i("");
                return true;
            }
        }
        return false;
    }

    /**
     * 获取排序的一个目录下的所有文件，目录在前，文件在后，排序是不区分大小写的
     *
     * @param dir
     * @return
     */
    static public List<String> getSortedChilds(String dir) {
        File fdir = new File(dir);
        if (!fdir.exists() || !fdir.isDirectory()) {
            return null;
        }
        File[] flist = fdir.listFiles();
        if (flist == null) {
            return null;
        }
        ArrayList<String> dirs = new ArrayList<String>();
        ArrayList<String> files = new ArrayList<String>();
        for (File f : flist) {
            if (f.isDirectory()) {
                dirs.add(f.getAbsolutePath());
            } else if (f.isFile()) {
                files.add(f.getAbsolutePath());
            }
        }
        if (dirs.isEmpty() && files.isEmpty()) {
            return null;
        }
        Collections.sort(dirs, new Comparator<String>() {
            @Override
            public int compare(String str1, String str2) {
                return str1.compareToIgnoreCase(str2);
            }
        });
        Collections.sort(files, new Comparator<String>() {
            @Override
            public int compare(String str1, String str2) {
                return str1.compareToIgnoreCase(str2);
            }
        });
        ArrayList<String> ret = new ArrayList<String>();
        ret.addAll(dirs);
        ret.addAll(files);
        return ret;
    }

    @SuppressWarnings("resource")
    static public String getFileContent(String filePath) {
        StringBuffer sb = new StringBuffer();
        try {
            if (filePath == null) {
                return "";
            }
            File f = new File(filePath);
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    static public boolean mkdir(String dirPath) {
        File f = new File(dirPath);
        if (f.isFile()) {
            return false;
        } else if (f.isDirectory()) {
            return true;
        }
        String parent = dirPath.replace('\\', '/');
        int lastSlash = parent.lastIndexOf('/');
        if (-1 == lastSlash) {
            return false;
        }
        parent = parent.substring(0, lastSlash);
        if (!mkdir(parent)) {
            return false;
        }
        return f.mkdir();
    }

    static public boolean isFileExist(String filePath) {
        File f = new File(filePath);
        return f.isFile();
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param path
     */
    static public boolean deleteFile(String path) {
//        if (file.exists()) {
//            if (file.isFile()) {
//                file.delete();
//                LogUtil.i("the file name is= " + file.getName());
//            } else if (file.isDirectory()) {
//                File files[] = file.listFiles();
//                for (int i = 0; i < files.length; i++) {
//                    deleteFile(files[i]);
//                }
//            }
//            file.delete();
//        }

        File file = new File(path);
        if (!file.exists()) return false;
        if (file.isFile()) {
            file.delete();
            return true;
        }
        File[] files = file.listFiles();
        Log.d("MainActivity", "fileList：" + (files == null ? "files = null" : files.length));
        if (files == null || files.length == 0) return false;
        for (File f : files) {
            if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            } else {
                f.delete();
            }
        }
        return true;
    }

    public static long getFileLeng(File file) throws IOException {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
        }
        return size;
    }

    /**
     * 计算目录大小
     *
     * @param dir
     * @return
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        // 不是目录
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                // 递归调用
                dirSize += getDirSize(file);
            }
        }
        return dirSize;
    }


    /**
     * 转换文件大小
     *
     * @param fileS
     * @return B/KB/MB/GB
     */
    public static String formatFileSize(long fileS) {
        if (fileS == 0) {
            return "0B";
        }
        DecimalFormat dFormat = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = new DecimalFormat("#").format((int) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = dFormat.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = dFormat.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = dFormat.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 获得文件下的所有文件（按时间顺序排列）
     *
     * @param path 文件夹路径
     * @return 所有文件
     */
    public static ArrayList<File> getFiles(String path) {
        ArrayList<File> fileList = new ArrayList<File>();
        File[] allFiles = new File(path).listFiles();
        for (int i = 0; i < allFiles.length; i++) {
            File file = allFiles[i];
            if (file.isFile()) {
                fileList.add(file);
            }
            // 自动生成的文件夹(图片缩略图)
            // else if (!file.getAbsolutePath().contains(".thumnail")) {
            // getFiles(file.getAbsolutePath());
            // }
        }
        //按文件创建时间排序。
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File file1, File file2) {
                if (file1.lastModified() < file2.lastModified()) {
                    return -1;
                }
                return 1;
            }
        });
        return fileList;
    }

    public static boolean fileCopy(Context context, String fileFrom, String fileTo) {
        try {
            InputStream in = context.getAssets().open(fileFrom);
            FileOutputStream os = new FileOutputStream(fileTo);
            byte[] bt = new byte[1024];
            int count;
            while ((count = in.read(bt)) > 0) {
                os.write(bt, 0, count);
            }
            in.close();
            os.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void writeStr2File(String fileName, String content) {
        try {
            File file = new File(fileName);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            if (!file.exists()) {
                writer.write(LogUtil.collectDeviceInfo());
            }
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> listPicFiles(File file) {
        List<String> fis = new ArrayList<>();
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length != 0) {
                for (File file2 : files) {
                    if (file2.isDirectory() && !file2.getAbsolutePath().contains(".thumbnails")) {
                        fis.addAll(listPicFiles(file2));
                    } else {
                        if (file2.getAbsolutePath().endsWith(".jpg") || file2.getAbsolutePath().endsWith(".png")) {
                            fis.add(file2.getAbsolutePath());
                        }
                    }
                }
            }
        }
        return fis;
    }

    public static List<String> listFiles(File file) {
        List<String> fis = new ArrayList<>();
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length != 0) {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        fis.addAll(listPicFiles(file2));
                    } else {
                        fis.add(file2.getAbsolutePath());
                    }
                }
            }
        }
        return fis;
    }

    /**
     * 判断文件夹大小，并删除最早的几个文件
     *
     * @param filePath        文件夹路径
     * @param maxSize         文件夹最大值，如果传值 null，则选用默认值
     * @param delEarliestSize 删除最早的几个文件
     */
    public static void delFileByPathSize(String filePath, Integer maxSize, Integer delEarliestSize) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        long mFoldSize = getDirSize(file);
        if (mFoldSize > maxSize) {
            //获取文件列表
            ArrayList<File> files = getFiles(filePath);
            if (files == null) {
                return;
            }
            int size = files.size();
            if (size > 0) {
                if (size < delEarliestSize) {
                    //文件大小 小于 删除数量
                } else {
                    //文件大小 大于 删除数量
                    size = delEarliestSize;
                }
                for (int i = 0; i < size; i++) {
                    files.get(i).delete();
                }
            }
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean fileIsExists(String filePath) {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String getFileNameNoExists(String fileName, String parentPath) {
        final int lastDot = fileName.lastIndexOf('.');
        String name;
        String suffix;
        if (lastDot >= 0) {
            name = fileName.substring(0, lastDot);
            suffix = fileName.substring(lastDot);
        } else {
            name = fileName;
            suffix = "";

        }
        int count = 0;
        File renamedFile = new File(parentPath, name + suffix);
        while (renamedFile.exists()) {
            count++;
            renamedFile = new File(parentPath, name + "(" + count + ")" + suffix);
        }
        return renamedFile.getName();
    }

}
