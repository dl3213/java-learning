package code.sibyl.service;

import code.sibyl.common.r;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;

public class FtpUtil {
    private static String server = "127.0.0.1";
    private static int port = 21;
    private static String user = "ubuntu";
    private static String password = "123456";

    public static boolean uploadFile(String remoteFilePath, InputStream inputStream) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            boolean login = ftpClient.login(user, password);
            System.err.println("login - " + login);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            boolean makeDirectory = ftpClient.makeDirectory("/home/ubuntu/" + r.yyyy_MM_dd() + "/");
            System.err.println("makeDirectory - " + makeDirectory);
            boolean done = ftpClient.storeFile(remoteFilePath, inputStream);
            System.err.println("done - " + done);
            boolean logout = ftpClient.logout();
            System.err.println("logout - " + logout);
            ftpClient.disconnect();
            return done;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}