package com.utils;

import java.io.*;

public class CommonUtil {
    public static void copy(String src, String dest, boolean deleteSrc) throws IOException {
        copy(new File(src), new File(dest), deleteSrc);
    }

    public static void copy(File src, File dest, boolean deleteSrc) throws IOException {
        copy(src, dest, deleteSrc, null);
    }

    public static void copy(File src, File dest, boolean deleteSrc, String[] ignoreEmptyFolders) throws IOException {
        _copy(src, dest, deleteSrc, ignoreEmptyFolders);
        if (deleteSrc)
            delete(src);
    }

    private static void _copy(File src, File dest, boolean deleteSrc, String[] ignoreEmptyFolders) throws IOException {
        if (!src.exists())
            return;
        if (src.isDirectory()) {
            if (!dest.exists()) {
                dest.mkdirs();
            }
            if (ignoreEmptyFolders != null) {
                for (String folder : ignoreEmptyFolders) {
                    if (dest.getAbsolutePath().endsWith(folder) && dest.list().length < 1) {
                        dest.delete();
                        return;
                    }
                }
            }
            // if directory not exists, create it
            // list all the directory contents
            String files[] = src.list();
            for (String file : files) {
                // construct the src and dest file structure
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                // recursive copy
                _copy(srcFile, destFile, deleteSrc, ignoreEmptyFolders);
            }
        } else if (src.isFile()) {
            // if file, then copy it
            // Use bytes stream to support all file types
            InputStream in = new FileInputStream(src);
            if (!dest.getParentFile().exists())
                dest.getParentFile().mkdirs();
            OutputStream out = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            // copy the file content in bytes
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.flush();
            out.close();
            if (deleteSrc) {
                src.delete();
            }
        }
    }

    public static boolean delete(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            // 递归删除目录中的子目录下
            for (File child : children) {
                boolean success = delete(child);
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static boolean delete(String path) {
        return delete(new File(path));
    }

    public static String readFile(String path, String defaultValue) throws Exception {
        return readFile(new File(path), defaultValue);
    }

    public static String readFile(File f, String defaultValue) throws Exception {
        if (f.isFile()) {
            FileInputStream fis = new FileInputStream(f);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            return new String(data, "utf-8").trim();
        }
        return defaultValue;
    }

    public static void writeFile(String path, String data) throws Exception {
        writeFile(new File(path), data);
    }

    public static void writeFile(File f, String data) throws Exception {
        FileOutputStream o = new FileOutputStream(f);
        OutputStreamWriter ow = new OutputStreamWriter(o, "utf-8");
        BufferedWriter fout = new BufferedWriter(ow);
        fout.write(data);
        fout.flush();
        fout.close();
        ow.close();
        o.close();
    }

    public static void appendFile(String path, String data) throws Exception {
        appendFile(new File(path), data);
    }

    public static void appendFile(File f, String data) throws Exception {
        FileWriter fw = new FileWriter(f, true);
        fw.write(data + "\n");
        fw.close();
    }
}

