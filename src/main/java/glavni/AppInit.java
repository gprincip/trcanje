package glavni;

import java.io.File;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import glavni.config.AppConfiguration;
import glavni.config.SecurityConfiguration;

public class AppInit implements WebApplicationInitializer{

	private int maxUploadSizeInMb = 5 * 1024 * 1024; //5mb
	
	public void onStartup(ServletContext servletContext) throws ServletException {
		// TODO Auto-generated method stub
		//*****************************Dispatcher servlet 
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(SecurityConfiguration.class, AppConfiguration.class);
		
		ServletRegistration.Dynamic registration = 
				servletContext.addServlet("dispatcher", new DispatcherServlet(context));
		registration.setLoadOnStartup(1);
		registration.addMapping("/services/rest/*");

		
		
	}

	
	


	
}
