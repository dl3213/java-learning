package me.sibyl.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

/**
 * Druid数据源配置
 * 
 * @author administrator
 *
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "datasource", name = "dynamic", havingValue = "false")
public class DruidConfig {

	@Bean
	public ServletRegistrationBean druidServlet() {
		log.info("init Druid Servlet Configuration ");
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),
				"/druid/*");
		// IP白名单
		servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
		servletRegistrationBean.addInitParameter("allow", "192.168.1.120");
		// IP黑名单(共同存在时，deny优先于allow)
//		servletRegistrationBean.addInitParameter("deny", "192.168.1.108");
//		// 控制台管理用户
		servletRegistrationBean.addInitParameter("loginUsername", "admin");
		servletRegistrationBean.addInitParameter("loginPassword", "123456");
//		// 是否能够重置数据 禁用HTML页面上的“Reset All”功能
//		servletRegistrationBean.addInitParameter("resetEnable", "false");
		return servletRegistrationBean;
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		return filterRegistrationBean;
	}

	/**
	 * 数据源配置
	 * 
	 * @author administrator
	 *
	 */
	@ConfigurationProperties(prefix = "spring.datasource")
	@Data
	class DataSourceProperties {
		private String url;
		private String username;
		private String password;
		private String driverClassName;

		private Map<String, Object> druid;

//		private int initialSize;
//		private int minIdle;
//		private int maxActive;
//		private int maxWait;
//		private int timeBetweenEvictionRunsMillis;
//		private int minEvictableIdleTimeMillis;
//		private String validationQuery;
//		private boolean testWhileIdle;
//		private boolean testOnBorrow;
//		private boolean testOnReturn;
//		private boolean poolPreparedStatements;
//		private int maxPoolPreparedStatementPerConnectionSize;
//		private String filters;
//		private String connectionProperties;

		@Bean
		@Primary
		public DataSource dataSource() {
			DruidDataSource datasource = new DruidDataSource();
			datasource.setUrl(url);
			datasource.setUsername(username);
			datasource.setPassword(password);
			datasource.setDriverClassName(driverClassName);

			System.err.println(druid);

//			datasource.setInitialSize(initialSize);
//			datasource.setMinIdle(minIdle);
//			datasource.setMaxActive(maxActive);
//			datasource.setMaxWait(maxWait);
//			datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
//			datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
//			datasource.setValidationQuery(validationQuery);
//			datasource.setTestWhileIdle(testWhileIdle);
//			datasource.setTestOnBorrow(testOnBorrow);
//			datasource.setTestOnReturn(testOnReturn);
//			datasource.setPoolPreparedStatements(poolPreparedStatements);
//			datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
//			try {
//				datasource.setFilters(filters);
//			} catch (SQLException e) {
//				log.error("异常", e);
//			}
//			datasource.setConnectionProperties(connectionProperties);
			return datasource;
		}
	}
}
