package glavni.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class DBConnection {

	public static final String dbURL = "jdbc:mysql://localhost:3306/trcanje?user=root&password=&useSSL=false";
	private DataSource dataSource = null;
	
	public Connection getConnection() throws SQLException{
		
		if(dataSource==null){
			dataSource = initializeDataSource();
		}
		return dataSource.getConnection();
	}
	
	public DataSource initializeDataSource(){
		
		final HikariConfig config = new HikariConfig();
		
		config.setDriverClassName("com.mysql.cj.jdbc.Driver");
		config.setJdbcUrl(dbURL);
		return new HikariDataSource(config);
		
	}
	
}