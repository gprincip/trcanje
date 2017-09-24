package glavni.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import glavni.db.DBConnection;



@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	DBConnection dbConnection;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dbConnection.initializeDataSource()) 
				.passwordEncoder(passwordEncoder)
				.usersByUsernameQuery("select username, password, enabled from korisnik where username=?")
				.authoritiesByUsernameQuery("select username, autorstvo from autorizacija where username=?");

	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder(){
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

	 @Override
	  public void configure(WebSecurity web) throws Exception {
	    web
	      .ignoring()
	         .antMatchers("/resources/**");
	  }

	  @Override
	  protected void configure(HttpSecurity http) throws Exception {
  
	/*http
		  .authorizeRequests()
		  .anyRequest().authenticated()
          .antMatchers("/resources/**").permitAll() 
          .and()
          .authorizeRequests().antMatchers("/services/rest/registrujKorisnika").anonymous().anyRequest().permitAll()
          .and().authorizeRequests().antMatchers("/services/rest/registrujKorisnika").hasRole("USER")
          .and()
      .formLogin()
          .loginPage("/login.html")
          .permitAll()
          .and()
      .logout()                                    
          .permitAll()
          .and()
          .csrf().disable()
          .authorizeRequests().antMatchers("/index.html").authenticated().anyRequest().permitAll()
          .and().authorizeRequests().antMatchers("/register.html").permitAll().anyRequest().anonymous()
		  .and()
		  .authorizeRequests().antMatchers("/services/rest/test").authenticated().anyRequest().permitAll();*/
	
		  
		  http.authorizeRequests()
		  .antMatchers("/services/rest/test").permitAll()
		  .antMatchers("/services/rest/registrujKorisnika").permitAll()
		  .antMatchers("/register.html").anonymous()
		  .antMatchers("/services/rest/uploadFile").anonymous()
		  .antMatchers("/css/styles.css").permitAll()
		  .antMatchers("/services/rest/emailTest").permitAll()
		  .antMatchers("/services/rest/aktivirajNalog/*").permitAll()
		  .anyRequest().authenticated()
		  .and()
		  .formLogin().loginPage("/login.html").permitAll()
		  .and()
		  .csrf().disable();
		  
	}
	  
}
