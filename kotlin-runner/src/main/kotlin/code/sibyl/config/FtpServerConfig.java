package code.sibyl.config;

import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class FtpServerConfig extends CachingConfigurerSupport {

    @Value("${ftp.port}")
    private Integer ftpPort;

    @Value("${ftp.activePort}")
    private Integer ftpActivePort;

    @Value("${ftp.passivePorts}")
    private String ftpPassivePorts;

    @Bean
    public FtpServer createFtpServer() {
        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory factory = new ListenerFactory();
        //这个只是控制端口，还有主动模式端口和被动模式下的某个范围端口
        factory.setPort(ftpPort);
        //factory.setIdleTimeout(60);
        //被动模式
        DataConnectionConfigurationFactory dataConnectionConfigurationFactory = new DataConnectionConfigurationFactory();
        //dataConnectionConfigurationFactory.setIdleTime(60);
        dataConnectionConfigurationFactory.setActiveLocalPort(ftpActivePort);//主动模式端口
        dataConnectionConfigurationFactory.setPassiveIpCheck(true);
        dataConnectionConfigurationFactory.setPassivePorts(ftpPassivePorts);//被动模式使用的端口范围

        factory.setDataConnectionConfiguration(dataConnectionConfigurationFactory.createDataConnectionConfiguration());
        // 替换默认的监听器
        serverFactory.addListener("default", factory.createListener());
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        try {
            ClassPathResource classPathResource = new ClassPathResource("ftpserver.properties");
            userManagerFactory.setUrl(classPathResource.getURL());
        } catch (Exception e) {
            throw new RuntimeException("配置文件ftpserver.properties不存在");
        }

        userManagerFactory.setPasswordEncryptor(new ClearTextPasswordEncryptor());
        serverFactory.setUserManager(userManagerFactory.createUserManager());

        Map<String, Ftplet> m = new HashMap<String, Ftplet>();
        m.put("miaFtplet", new FtpServerListener());

        serverFactory.setFtplets(m);
        // 创建ftpserver
        FtpServer server = serverFactory.createServer();
        return server;
    }
}