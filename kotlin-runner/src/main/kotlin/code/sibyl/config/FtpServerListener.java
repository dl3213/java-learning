package code.sibyl.config;

import org.apache.ftpserver.ftplet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FtpServerListener extends DefaultFtplet {

    public static final Logger log = LoggerFactory.getLogger(FtpServerListener.class);

    /**
     * 开始连接
     */
    @Override
    public FtpletResult onConnect(FtpSession session) throws FtpException, IOException {
        UUID sessionId = session.getSessionId();
        if (sessionId != null) {
            log.info("{}尝试登录ftpserver", sessionId.toString());
        }
        User user = session.getUser();
        if (user != null && user.getName() != null) {
            log.info("{}尝试使用用户名:{}，密码:{}登录ftpserver.", sessionId.toString(), user.getName(), user.getPassword());
        }
        return super.onConnect(session);
    }

    /**
     * 关闭连接
     */
    @Override
    public FtpletResult onDisconnect(FtpSession session) throws FtpException,
            IOException {
        UUID sessionId = session.getSessionId();
        if (sessionId != null) {
            log.info("{}关闭ftpserver连接", sessionId.toString());
        }
        User user = session.getUser();
        if (user != null && user.getName() != null) {
            log.info("{}用户名:{}关闭ftpserver连接.", sessionId.toString(), user.getName());
        }
        return super.onDisconnect(session);
    }

    /**
     * 开始上传
     */
    @Override
    public FtpletResult onUploadStart(FtpSession session, FtpRequest request)
            throws FtpException, IOException {
        //获取上传文件的上传路径
        String path = session.getUser().getHomeDirectory();
        //自动创建上传路径
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        //获取上传用户
        String name = session.getUser().getName();
        //获取上传文件名
        String filename = request.getArgument();

        log.info("用户:'{}'，上传文件到目录：'{}'，文件名称为：'{}'，状态：开始上传~", name, path, filename);
        return super.onUploadEnd(session, request);
    }

    /**
     * 上传完成
     */
    @Override
    public FtpletResult onUploadEnd(FtpSession session, FtpRequest request)
            throws FtpException, IOException {
        //获取上传文件的上传路径
        String path = session.getUser().getHomeDirectory();
        //获取上传用户
        String name = session.getUser().getName();
        //获取上传文件名
        String filename = request.getArgument();

        File file = new File(path + "/" + filename);
        if (file.exists()) {
            System.out.println(file);
        }

        log.info("用户:'{}'，上传文件到目录：'{}'，文件名称为：'{}，状态：成功！'", name, path, filename);
        return super.onUploadStart(session, request);
    }

    @Override
    public FtpletResult onDownloadStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
        return super.onDownloadStart(session, request);
    }

    @Override
    public FtpletResult onDownloadEnd(FtpSession session, FtpRequest request) throws FtpException, IOException {
        return super.onDownloadEnd(session, request);
    }

}
