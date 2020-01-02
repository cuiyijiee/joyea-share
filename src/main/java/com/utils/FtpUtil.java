package com.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FTP 处理类
 *
 * @author Tom
 */
public class FtpUtil {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private FTPClient ftpClient;
    private static final String ENCODING = "UTF-8";
    private static final int BUFFER_SIZE = 1024 * 10;
    private static final int TIMEOUT = 60 * 1000;

    // --------------------------------------------------------------------------------
    //  require to inject attributes
    // --------------------------------------------------------------------------------
    private String username;
    private String password;
    private String hostname;
    private int port = 21;

    public FtpUtil(String hostname, String username, String password) {
        this.hostname = hostname;
        this.username = username;
        this.password = password;
        this.ftpClient = new FTPClient();
    }

    public FtpUtil(String hostname, int port, String username, String password) {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;
        this.ftpClient = new FTPClient();
    }

    /**
     * Login to the FTP Server
     */
    public boolean login() {
        FTPClientConfig fcc = new FTPClientConfig();
        fcc.setServerTimeZoneId(TimeZone.getDefault().getID());
        ftpClient.setControlEncoding(ENCODING);
        ftpClient.configure(fcc);
        try {
            if (port > 0) {
                ftpClient.connect(hostname, port);
            } else {
                ftpClient.connect(hostname);
            }
            // check reply code
            int code = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(code)) {
                ftpClient.disconnect();
                logger.error("Login FTP Server is failure!");
                return false;
            }

            if (ftpClient.login(username, password)) {
                // setting
                this.ftpClient.enterLocalPassiveMode();
                this.ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                this.ftpClient.setBufferSize(BUFFER_SIZE);
                this.ftpClient.setDataTimeout(TIMEOUT);
//				logger.info(username + " successfully logined to the FTP server.");
                return true;
            } else {
                throw new Exception("Please check your username and password.");
            }
        } catch (Exception e) {
            logger.error(username + " failed to login to the FTP server", e);
        }
        return false;
    }

    /**
     * 上传文件至FTP服务器
     *
     * @param file          需要上传的文件, 包括文件目录及名称的完整目录
     * @param remoteDirPath FTP服务器的路径
     */
    public boolean uploadFileToRemoteDir(String file, String remoteDirPath) {
        return uploadFileToRemoteDir(new File(file), remoteDirPath);
    }

    /**
     * 上传文件至FTP服务器
     *
     * @param srcFile       需要上传的文件, 包括文件目录及名称的完整目录
     * @param remoteDirPath FTP服务器的路径
     */
    public boolean uploadFileToRemoteDir(File srcFile, String remoteDirPath) {
        BufferedInputStream bis = null;
        String filePath = srcFile.getPath();
        try {
            String fileName = srcFile.getName();
            boolean result = createMultiDirectory(remoteDirPath);
            logger.info("创建目录:" + remoteDirPath + " ：" + result);
            ftpClient.changeWorkingDirectory(remoteDirPath);
            bis = new BufferedInputStream(new FileInputStream(srcFile));

            // store file
            if (this.ftpClient.storeFile(fileName, bis)) {
                logger.info("文件: " + fileName + " 成功上传到目录: " + remoteDirPath);
                return true;
            }
        } catch (FileNotFoundException e) {
            logger.error(filePath + " 未找到", e);
        } catch (IOException e) {
            logger.error(filePath + " 上传出错", e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                }
            }
        }
        return false;
    }

    /**
     * 将文件夹上传至FTP服务器
     *
     * @param localPath  本地需要上传的文件夹路径
     * @param remotePath FTP服务器路径
     */
    public boolean uploadDirectory(String localPath, String remotePath) {
        return uploadDirectory(new File(localPath), remotePath);
    }

    /**
     * 将文件夹上传至FTP服务器
     *
     * @param dir        本地需要上传的文件夹路径
     * @param remotePath FTP服务器路径
     */
    public boolean uploadDirectory(File dir, String remotePath) {
        boolean result = createMultiDirectory(remotePath);
        logger.info("创建目录:" + remotePath + " ：" + result);
        File[] files = dir.listFiles();
        // 上传文件
        for (File file : files) {
            if (!file.isDirectory()) {
                if (!file.getName().equals(".DS_Store")) {
                    uploadFileToRemoteDir(file, remotePath);
                }
            } else {
                String remoteSubDir = remotePath + File.separator + file.getName();
                uploadDirectory(file, remoteSubDir);
            }
        }
        return true;
    }

    //创建多重目录
    public boolean createMultiDirectory(String remote) {
        boolean result = true;
        List<File> filePathList = new ArrayList<>();
        File file = new File(remote);
        while (file != null) {
            filePathList.add(file);
            file = file.getParentFile();
        }
        for (int index = filePathList.size() - 1; index >= 0; index--) {
            try {
                if (ftpClient.makeDirectory(filePathList.get(index).getAbsolutePath())) ;
            } catch (IOException e) {
                result = false;
                e.printStackTrace();
                logger.error("创建文件夹失败:" + e.getLocalizedMessage());
            }
        }
        return result;
    }

    /**
     * download single file
     */
    public boolean downloadFile(String remoteFilePath, String localFilePath) {
        BufferedOutputStream bos = null;
        try {
            File localFile = new File(localFilePath);
            File remoteFile = new File(remoteFilePath);
            ftpClient.changeWorkingDirectory(remoteFile.getParent());
            bos = new BufferedOutputStream(new FileOutputStream(localFile));

            if (ftpClient.retrieveFile(remoteFile.getName(), bos)) {
                logger.info(remoteFile.getName() + " successfully downloaded to the " + localFilePath);
                System.out.println(localFile.lastModified());
                return true;
            }
        } catch (Exception e) {
            logger.error(remoteFilePath + " download failure", e);
        } finally {
            if (null != bos) {
                try {
                    bos.close();
                } catch (IOException e) {
                }
            }
        }
        return false;
    }

//    /**
//     * download directory
//     *
//     * @param localPath  本地路径
//     * @param remotePath 远程文件夹
//     * @param delete     下载完毕是否删除文件
//     */
//    public boolean downLoadDirectory(String localPath, String remotePath, boolean delete) {
//        try {
//            File localFile = new File(localPath);
//            localFile.mkdirs();
//
//            FTPFile[] files = this.ftpClient.listFiles(remotePath);
//
//            // download single file
//            for (FTPFile file : files) {
//                if (file.isFile()) {
//                    downloadFile(remotePath + File.separator + file.getName(),localPath);
//                }
//            }
//
//            // download directory
//            for (FTPFile file : files) {
//                String subDir = file.getName();
//                if (file.isDirectory() && !".".equals(subDir) && !"..".equals(subDir)) {
//                    downLoadDirectory(localPath + File.separator + subDir,
//                            remotePath + File.separator + subDir, delete);
//                }
//            }
//        } catch (IOException e) {
//            logger.error("download directory failure", e);
//            return false;
//        }
//        return true;
//    }

    /**
     * 从FTP服务器删除文件
     *
     * @param remoteFile
     * @return
     * @throws Exception
     */
    public boolean delete(String remoteFile) throws Exception {
        return ftpClient.deleteFile(remoteFile);
    }

    /**
     * logout from the FTP Server and release connect
     */
    public void logout() {
        if (null == ftpClient || !ftpClient.isConnected()) {
            return;
        }
        try {
            // logout from the FTP Server
            if (ftpClient.logout()) {
//				logger.info("logout from the FTP Server.");
            }
        } catch (IOException e) {
            logger.error("failed to logout to the FTP server!", e);
        } finally {
            try {
                // release connect
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<FTPFile> listFile(String remotePath) {
        List<FTPFile> fileList = new ArrayList<>();
        try {
            if (ftpClient.changeWorkingDirectory(remotePath)) {
                FTPFile[] files = ftpClient.listFiles();
                fileList.addAll(Arrays.asList(files));
            }
        } catch (IOException e) {
            logger.error("列出远程目录失败：", e);
        }
        return fileList;
    }

    public static void main(String[] args) throws IOException {
        FtpUtil ftp = new FtpUtil(
                "10.211.55.3",
                "cuje",
                "cc112211"
        );
        ftp.login();
        File[] files = new File("/Users/cuje/Desktop/test").listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        for (File file : files) {
            ftp.uploadFileToRemoteDir(file.getAbsolutePath(), "/test");
            file.delete();
            ftp.downloadFile("/test/" + file.getName(),"/Users/cuje/Desktop/test/" + file.getName());
        }
        ftp.logout();
    }
}