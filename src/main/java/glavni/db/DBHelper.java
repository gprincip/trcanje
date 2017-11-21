package glavni.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import glavni.exceptions.EmailNePostojiException;
import glavni.exceptions.EmailVecPostojiException;
import glavni.exceptions.UsernameNePostojiException;
import glavni.exceptions.UsernameVecPostojiException;
import glavni.model.Aktivnost;
import glavni.model.AktivnostGpx;
import glavni.model.AktivnostKorisnik;
import glavni.model.Korisnik;

@Component
public class DBHelper {

	@Autowired
	DBConnection conn;

	public void napraviBazu() throws SQLException{
		
		Connection connection = null;
		Statement statement = null;
		
		try{
		
			connection = conn.getConnection();
			statement = connection.createStatement();
			String query = "create database trcanje; use trcanje; create table korisnik( username varchar(25) primary key, password varchar(60), ime varchar(25), prezime varchar(25), slika varchar(25), enabled int(2), email varchar(50) )engine=InnoDB default charset=utf8; create table aktivnost( id int(11) primary key auto_increment, username varchar(25), datum datetime, tempo varchar(12), distanca float(10), datumformatiran varchar(50), trajanje varchar(25), foreign key (username) references korisnik(username) on update cascade on delete restrict ) engine=InnoDB default charset=utf8; create table prijateljSa( username1 varchar(25), username2 varchar(25), primary key(username1, username2), foreign key (username1) references korisnik(username) on update cascade on delete restrict, foreign key (username2) references korisnik(username) on update cascade on delete restrict ) engine=InnoDB default charset=utf8; create table autorizacija( username varchar(25), autorstvo varchar(50), primary key(username, autorstvo), foreign key(username) references korisnik(username) on update cascade on delete restrict )engine=InnoDB default charset=utf8; create unique index ix_auth_username on autorizacija(username, autorstvo); create table lajkovao( username varchar(25), aktivnost_id int(11), foreign key(username) references korisnik(username) on update cascade on delete restrict, foreign key(aktivnost_id) references aktivnost(id) on update cascade on delete restrict )engine=InnoDB default charset=utf8; create table aktivnostgpx( id int(11) primary key auto_increment, username varchar(25), naziv_fajla varchar(50), naslov varchar(25), foreign key(username) references korisnik(username) on update cascade on delete restrict )engine=InnoDB default charset=utf8; create table aktivacija( username varchar(25) primary key, id int(11) )engine=InnoDB default charset=utf8";
			statement.executeUpdate(query);
		}finally{
		releaseResources(connection, statement, null);
	}
	}
	
public void unosPocetnihPodataka() throws SQLException{
		
		Connection connection = null;
		Statement statement = null;
		
		try{
		
			connection = conn.getConnection();
			statement = connection.createStatement();
			String query = "insert into korisnik values ('pera', '$2a$10$MrzU.AbhPw6reqnHnLoDl.FTuVyU0j3e6IIkCvz9IYaP6f01b9BG.', 'Petar' , 'Peric','pera.jpg', 1), ('mika', '$2a$10$NkaGGO0iL00eQYHyZILbIu6iM1rR5Bnf/kg7WuR6VkjcAVdWmXmn2', 'Miladin' , 'Mikic','mika.jpg' ,1), ('laza', '$2a$10$5Gtz8In9rIEFVftMN8.yLOAvBaTPwTRz2dIrgYe2Izy8inNmpqYoy' , 'Lazar','Lazarevic', 'laza.jpg' ,1); insert into aktivnost values (null, 'pera', '2017-07-15 10:52', '4:35' , 14.5), (null, 'mika', '2017-07-15 10:53', '4:33' , 14.4); insert into prijateljSa values ('pera','mika'), ('mika','pera'); insert into autorizacija values ('pera','ROLE_USER'), ('mika','ROLE_USER'), ('laza','ROLE_USER'), ('laza','ROLE_ADMIN')";
			statement.executeUpdate(query);
		}finally{
		releaseResources(connection, statement, null);
	}
	}
	
	public List<Aktivnost> getAktivnosti() throws SQLException {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		String query = "select * from aktivnost";

		List<Aktivnost> ret = new ArrayList<Aktivnost>();

		try {

			connection = conn.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {

				int id = resultSet.getInt("id");
				String username = resultSet.getString("username");
				String datum = resultSet.getString("datum");
				String datumFormatiran = resultSet.getString("datumformatiran");
				String tempo = resultSet.getString("tempo");
				double distanca = resultSet.getDouble("distanca");
				String naslov = resultSet.getString("naslov");
				String trajanje = resultSet.getString("trajanje");
				
				ret.add(new Aktivnost(id, username, datum, datumFormatiran, tempo, distanca, naslov, trajanje));

			}

			return ret;

		} finally {
			releaseResources(connection, statement, resultSet);
		}

	}

	public List<Korisnik> getKorisnici() throws SQLException {

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		String query = "select * from korisnik";

		try {

			connection = conn.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);

			List<Korisnik> ret = new ArrayList<Korisnik>();

			while (resultSet.next()) {

				String email = resultSet.getString("email");
				String username = resultSet.getString("username");
				String password = resultSet.getString("password");
				String ime = resultSet.getString("ime");
				String prezime = resultSet.getString("prezime");
				String slika = resultSet.getString("slika");
				int enabled = resultSet.getInt("enabled");
				
				ret.add(new Korisnik(email, username, password, ime, prezime,slika, enabled));
			}

			return ret;

		} finally {
			releaseResources(connection, statement, resultSet);
		}

	}

	public List<Aktivnost> getAktivnostiPoStranama(int velicinaStrane, int strana) throws SQLException{
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		String query = "select * from aktivnost order by datum desc limit ? offset ?";
		
		List<Aktivnost> ret = new ArrayList<Aktivnost>();
		
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(1, velicinaStrane);
			statement.setInt(2, velicinaStrane * (strana-1));
			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				
				int id = resultSet.getInt("id");
				String username = resultSet.getString("username");
				String datum = resultSet.getString("datum");
				String datumFormatiran = resultSet.getString("datumformatiran");
				String tempo = resultSet.getString("tempo");
				double distanca = resultSet.getDouble("distanca");
				String naslov = resultSet.getString("naslov");
				String trajanje = resultSet.getString("trajanje");
				
				ret.add(new Aktivnost(id, username, datum, datumFormatiran, tempo, distanca, naslov, trajanje));
				
			}
			
			return ret;
			
		}finally{
			releaseResources(connection, statement, resultSet);
		}
		
	}
	
	//aktivnosti po stranama od korisnika i njegovih prijatelja
	public List<AktivnostKorisnik> getAktivnostiPoStranama(int velicinaStrane, int strana, String username) throws SQLException{
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		List<AktivnostKorisnik> ret = new ArrayList<AktivnostKorisnik>();
		
		String query = "select aktivnost.*, korisnik.* from aktivnost, korisnik "
				+ " where aktivnost.username = korisnik.username and("
				+ " aktivnost.username in("
				+ " select username2 from prijateljsa"
				+ " where username1 = ?"
				+ " )or aktivnost.username = ?)"
				+ " order by datum desc limit ? offset ?";
			
				
		
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			
			statement.setString(1, username);
			statement.setString(2, username);
			statement.setInt(3, velicinaStrane);
			statement.setInt(4, velicinaStrane * (strana-1));
			
			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				
				int id = resultSet.getInt("id");
				String username2 = resultSet.getString("username");
				String datum = resultSet.getString("datum");
				String datumFormatiran = resultSet.getString("datumformatiran");
				String tempo = resultSet.getString("tempo");
				double distanca = resultSet.getDouble("distanca");
				String naslov = resultSet.getString("naslov");
				String trajanje = resultSet.getString("trajanje");
				
				Aktivnost a = new Aktivnost(id, username2, datum, datumFormatiran, tempo, distanca, naslov, trajanje);
				
				String email = resultSet.getString("email");
				String ime = resultSet.getString("ime");
				String prezime = resultSet.getString("prezime");
				String slika = resultSet.getString("slika");
				int enabled = resultSet.getInt("enabled");
				Korisnik k = new Korisnik(email, username2, null, ime, prezime, slika, enabled);
				
				ret.add(new AktivnostKorisnik(a,k,brojLajkova(id)));
				
			}
			
			return ret;
			
		}finally{
			releaseResources(connection, statement, resultSet);
		}
		
	}
	
	//isto kao prethodna samo daje ukupan broj aktivnosti korisnika i njegovih prijatelja
	public int getBrojAktivnostiKorisnikaIPrijatelja(String username) throws SQLException{
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		String query = "select count(*) as cnt from aktivnost "+
		"inner join korisnik on aktivnost.username = korisnik.username "+
		"and aktivnost.username in( "+
		"select username2 from prijateljsa "+
		"where username1 = ? "+
		"or username2 = ? "+
		") ";
		
		int ret = 0;
		
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			statement.setString(1, username);
			statement.setString(2, username);
			
			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				
				ret = resultSet.getInt("cnt");

			}
			
			return ret;
			
		}finally{
			releaseResources(connection, statement, resultSet);
		}
		
	}
	
	//daje aktivnosti jednog korisnika po stranama
		public List<AktivnostKorisnik> getAktivnostiKorisnikaPoStranama(int velicinaStrane, int strana, String username) throws SQLException{
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		List<AktivnostKorisnik> ret = new ArrayList<AktivnostKorisnik>();
		
		String query = "select aktivnost.*, korisnik.* from aktivnost "
				+"inner join korisnik on aktivnost.username = korisnik.username "
				+"and aktivnost.username = ? "
				+ "order by datum desc limit ? offset ?";
				
		
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			
			statement.setString(1, username);
			statement.setInt(2, velicinaStrane);
			statement.setInt(3, velicinaStrane * (strana-1));
			
			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				
				int id = resultSet.getInt("id");
				String username2 = resultSet.getString("username");
				String datum = resultSet.getString("datum");
				String datumFormatiran = resultSet.getString("datumformatiran");
				String tempo = resultSet.getString("tempo");
				double distanca = resultSet.getDouble("distanca");
				String naslov = resultSet.getString("naslov");
				String trajanje = resultSet.getString("trajanje");
				
				Aktivnost a = new Aktivnost(id, username2, datum, datumFormatiran, tempo, distanca, naslov, trajanje);
				
				String email = resultSet.getString("email");
				String ime = resultSet.getString("ime");
				String prezime = resultSet.getString("prezime");
				String slika = resultSet.getString("slika");
				int enabled = resultSet.getInt("enabled");
				Korisnik k = new Korisnik(email, username2, null, ime, prezime, slika, enabled);
				
				ret.add(new AktivnostKorisnik(a,k,brojLajkova(id)));
				
			}
			
			return ret;
			
		}finally{
			releaseResources(connection, statement, resultSet);
		}
		
	}
	
		//isto kao prethodna samo daje ukupan broj aktivnosti korisnika
		public int getBrojAktivnostiKorisnika(String username) throws SQLException{
			
			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet resultSet = null;
			
			String query = "select count(*) as cnt from aktivnost"
					+ " inner join korisnik on aktivnost.username = korisnik.username "
					+ "and aktivnost.username = ? ";
			
			int ret = 0;
			
			try{
				
				connection = conn.getConnection();
				statement = connection.prepareStatement(query);
				statement.setString(1, username);
				
				resultSet = statement.executeQuery();
				
				while(resultSet.next()){
					
					ret = resultSet.getInt("cnt");

				}
				
				return ret;
				
			}finally{
				releaseResources(connection, statement, resultSet);
			}
			
		}
		
	public void dodajAktivnost(Aktivnost aktivnost) throws SQLException{
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		String query = "insert into aktivnost values(null,?,?,?,?,?,?,?)"; //null je zbog auto_incrementa
		
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			statement.setString(1, aktivnost.getUsername());
			statement.setString(2, aktivnost.getDatum());
			statement.setString(3, aktivnost.getTempo());
			statement.setDouble(4, aktivnost.getDistanca());
			statement.setString(5, aktivnost.getDatumFormatiran());
			statement.setString(6, aktivnost.getNaslov());
			statement.setString(7, aktivnost.getTrajanje());
			
			statement.executeUpdate();
		
		}finally{
			releaseResources(connection, statement, null);
		}
		
	}
	
	//**********Aktivnostgpx dodavanje i citanje aktivnosti za jednog korisnika
	
	public void dodajAktivnostGpx(AktivnostGpx aktivnost) throws SQLException{
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs;
		
		String query = "insert into aktivnostgpx values(null,?,?,?)"; //null je zbog auto_incrementa
		
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			//statement.setInt(1, aktivnost.getId());
			statement.setString(1, aktivnost.getUsername());
			statement.setString(2, aktivnost.getNaziv_aktivnosti());
			statement.setString(3, aktivnost.getNaslov());
			statement.executeUpdate();
			
		}finally{
			releaseResources(connection, statement, null);
		}
		
	}
	
	public List<AktivnostGpx> getAktivnostiGpx(String username) throws SQLException{
		
		Connection connection= null;
		PreparedStatement statement= null;
		ResultSet resultSet = null;
		
		String query = "select * from aktivnostgpx where username = ?";
		
		List<AktivnostGpx> ret = new ArrayList<AktivnostGpx>();
		
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			statement.setString(1, username);
			
			resultSet = statement.executeQuery();

			
			while(resultSet.next()){
				
				int id = resultSet.getInt("id");
				String username2 = resultSet.getString("username");
				String naziv_fajla = resultSet.getString("naziv_fajla");
				String naslov = resultSet.getString("naslov");
				
				ret.add(new AktivnostGpx(id,username2, naziv_fajla, naslov));
				
			}
			
			return ret;
			
		}finally{
			releaseResources(connection, statement, resultSet);
		}
	}
	
	
	//*****************************************
	
	public Korisnik getKorisnik(String username) throws SQLException, UsernameNePostojiException{
		
		Connection connection= null;
		PreparedStatement statement= null;
		ResultSet resultSet = null;
		
		String query = "select * from korisnik where username = ?";
		
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			statement.setString(1, username);
			
			resultSet = statement.executeQuery();
			
			Korisnik ret = null;
			
			if(resultSet.next()){
				
				String email = resultSet.getString("email");
				String username2 = resultSet.getString("username");
				String password = resultSet.getString("password");
				String ime = resultSet.getString("ime");
				String prezime = resultSet.getString("prezime");
				String slika = resultSet.getString("slika");
				int enabled = resultSet.getInt("enabled");
				
				ret = new Korisnik(email, username2,password,ime,prezime,slika,enabled);
				
			}else{
				throw new UsernameNePostojiException("Korisnik sa korisnickim imenom " + username + " ne postoji");
			}
			
			return ret;
			
		}finally{
			releaseResources(connection, statement, resultSet);
		}
	}
	
	
public Korisnik getKorisnikFromEmail(String email) throws SQLException, EmailNePostojiException{
		
		Connection connection= null;
		PreparedStatement statement= null;
		ResultSet resultSet = null;
		
		String query = "select * from korisnik where email = ?";
		
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			statement.setString(1, email);
			
			resultSet = statement.executeQuery();
			
			Korisnik ret = null;
			
			if(resultSet.next()){
				
				String email2 = resultSet.getString("email");
				String username2 = resultSet.getString("username");
				String password = resultSet.getString("password");
				String ime = resultSet.getString("ime");
				String prezime = resultSet.getString("prezime");
				String slika = resultSet.getString("slika");
				int enabled = resultSet.getInt("enabled");
				
				ret = new Korisnik(email2, username2,password,ime,prezime,slika,enabled);
				
			}else{
				throw new EmailNePostojiException("Korisnik sa emailom " + email + " ne postoji");
			}
			
			return ret;
			
		}finally{
			releaseResources(connection, statement, resultSet);
		}
	}
	
	
public List<Korisnik> getPrijateljiPoStranama(String username, int velicinaStrane, int strana) throws SQLException, UsernameNePostojiException{
		
		Connection connection= null;
		PreparedStatement statement= null;
		ResultSet resultSet = null;
		
		String query = "select * from prijateljsa where username1 = ? limit ? offset ?";
		
		List<Korisnik> ret = new ArrayList<Korisnik>();
		
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			statement.setString(1, username);
			statement.setInt(2, velicinaStrane);
			statement.setInt(3, velicinaStrane * (strana-1));
			
			resultSet = statement.executeQuery();
					
			while(resultSet.next()){
				
				String username2 = resultSet.getString("username2");
				
				Korisnik k = getKorisnik(username2);
				
				ret.add(k); 
				
			}
			
			return ret;
			
		}finally{
			releaseResources(connection, statement, resultSet);
		}
	}

//registracija korisnika
	public void dodajKorisnikaStari(Korisnik korisnik) throws SQLException{
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		String query = "insert into korisnik values(? , ? , ? , ? , ? , ?)";
		
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			
		   statement.setString(1, korisnik.getUsername());
		   statement.setString(2, korisnik.getPassword());
		   statement.setString(3, korisnik.getIme());
		   statement.setString(4, korisnik.getPrezime());
		   statement.setString(5, korisnik.getSlika());
		   statement.setInt(6, korisnik.getEnabled());
		   
		   statement.executeUpdate();
		   
		}finally{
			releaseResources(connection, statement, null);
		}
		
	}
	
	//Nova registracija korisnika sa posebnim parametrima i slikom
	
public int dodajKorisnika( //vraca slucajno generisani broj koji sluzi za njegovu aktivaciju
		String username,
		String email,
		String password,
		String opetpassword,
		String ime,
		String prezime,
		String imeSlike
		) throws SQLException, UsernameVecPostojiException, EmailNePostojiException, EmailVecPostojiException{
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		String query = "insert into korisnik values(? , ? , ? , ? , ? , ? , ?)";
		
		if(imeSlike == "")imeSlike = "default.jpg";
		
		try{
			getKorisnik(username);
			throw new UsernameVecPostojiException("Korisnik sa korisnickim imenom "+username+ " vec postoji");
		}catch(UsernameNePostojiException e){
			
		}
		
		try{
			getKorisnikFromEmail(email);
			throw new EmailVecPostojiException("Korisnik sa emailom "+email+ " vec postoji");
		}catch(EmailNePostojiException e){
			
		}
		
		
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			
		   statement.setString(1, username);
		   statement.setString(2, password);
		   statement.setString(3, ime);
		   statement.setString(4, prezime);
		   statement.setString(5, imeSlike);
		   statement.setInt(6, 0);
		   statement.setString(7, email);
		   
		   statement.executeUpdate();
		   
		   dodajAutorizaciju(username, "ROLE_USER");
		   return ubaciUAktivaciju(username);
		   
		}finally{
			releaseResources(connection, statement, null);
		}
		
	}
	
	public void dodajAutorizaciju(String username, String autorstvo) throws SQLException{
	
	Connection connection = null;
	PreparedStatement statement = null;
	
	String query = "insert into autorizacija values(?,?)";
	
	try{
		
		connection = conn.getConnection();
		statement = connection.prepareStatement(query);
		statement.setString(1, username);
		statement.setString(2, autorstvo);

		
		statement.executeUpdate();
	
	}finally{
		releaseResources(connection, statement, null);
	}
	
  }


	//daje aktivnosti korisnika i aktivnosti njegovih prijatelja
	public List<AktivnostKorisnik> getAktivnostiPrijatelja(String username) throws SQLException{
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		List<AktivnostKorisnik> ret = new ArrayList<AktivnostKorisnik>();
		
		String query = "select aktivnost.*, korisnik.* from aktivnost "
				+"inner join korisnik on aktivnost.username = korisnik.username "
				+"and aktivnost.username in( "
				+"select username2 from prijateljsa "
				+"where username1 = ? "
				+"or username2 = ? "
				+")";
		
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			
			statement.setString(1, username);
			statement.setString(2, username);
			
			resultSet = statement.executeQuery();
			
			while(resultSet.next()){
				
				int id = resultSet.getInt("id");
				String username2 = resultSet.getString("username");
				String datum = resultSet.getString("datum");
				String datumFormatiran = resultSet.getString("datumformatiran");
				String tempo = resultSet.getString("tempo");
				double distanca = resultSet.getDouble("distanca");
				String naslov = resultSet.getString("naslov");
				String trajanje = resultSet.getString("trajanje");
				
				Aktivnost a = new Aktivnost(id, username2, datum, datumFormatiran, tempo, distanca, naslov, trajanje);
				
				String email = resultSet.getString("email");
				String ime = resultSet.getString("ime");
				String prezime = resultSet.getString("prezime");
				String slika = resultSet.getString("slika");
				int enabled = resultSet.getInt("enabled");
				Korisnik k = new Korisnik(email, username2, null, ime, prezime, slika, enabled);
				
				ret.add(new AktivnostKorisnik(a,k,brojLajkova(id)));
				
			}
			
			return ret;
			
		}finally{
			releaseResources(connection, statement, resultSet);
		}
		
	}
	
	public int lajkuj(String username, int aktivnost_id) throws SQLException{
		
		if(lajkovao(username, aktivnost_id)){
			obrisiLajk(username, aktivnost_id);
			return -1;
		}
		else{
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		String query = "insert into lajkovao values( ? , ? )";

		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			
			statement.setString(1, username);
			statement.setInt(2, aktivnost_id);
			
			statement.executeUpdate();
			return 1;
		}finally{
			releaseResources(connection, statement, null);
		}
		}	
	}
	
	public void obrisiLajk(String username, int aktivnost_id) throws SQLException{
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		String query = "delete from lajkovao where username = ? and aktivnost_id = ?";
		
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			
			statement.setString(1, username);
			statement.setInt(2, aktivnost_id);
			
			statement.executeUpdate();
			
		}finally{
			releaseResources(connection, statement, null);
		}
		
	}
	
	public boolean lajkovao(String username, int aktivnost_id) throws SQLException{
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		String query = "select *"
				+ "from lajkovao "
				+ "where username = ? "
				+ "and aktivnost_id = ?";
		
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			statement.setString(1, username);
			statement.setInt(2, aktivnost_id);
			resultSet = statement.executeQuery();

			return resultSet.next();
			
			
		}finally{
			releaseResources(connection, statement, resultSet);
		}

	}
	
public int brojLajkova(int aktivnost_id) throws SQLException{
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		String query = "select count(*) as br "
				+ "from lajkovao "
				+ "where aktivnost_id = ?";
		
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(1, aktivnost_id);
			resultSet = statement.executeQuery();

			if(resultSet.next()) return resultSet.getInt("br");
			else return 0;
			
		}finally{
			releaseResources(connection, statement, resultSet);
		}

	}

	public boolean daLiSuPrijatelji(String username1, String username2) throws SQLException{
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		String query = "select * "
				+ "from prijateljsa "
				+ "where username1 = ? and username2 = ?";
		
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			statement.setString(1, username1);
			statement.setString(2, username2);
			resultSet = statement.executeQuery();

			return resultSet.next();

		}finally{
			releaseResources(connection, statement, resultSet);
		}

		
	}
	
	public void dodajPrijatelja(String username1, String username2) throws SQLException{
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		String query = "insert into prijateljsa values (? , ?)";
		
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			statement.setString(1, username1);
			statement.setString(2, username2);
			statement.executeUpdate();

		}finally{
			releaseResources(connection, statement, null);
		}

		
	}
	
public void obrisiPrijatelja(String username1, String username2) throws SQLException{
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		String query = "delete from prijateljsa where username1 = ? and username2 =  ?";
		
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			statement.setString(1, username1);
			statement.setString(2, username2);
			statement.executeUpdate();

		}finally{
			releaseResources(connection, statement, null);
		}

		
	}
	
	//ljudi koji prate username
	public List<Korisnik> getPratiociPoStranama(String username, int velicinaStrane, int strana) throws SQLException, UsernameNePostojiException{
	
	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet resultSet = null;
	
	List<Korisnik> ret = new ArrayList<Korisnik>();
	
	String query = "select * "
			+ "from prijateljsa "
			+ "where username2 = ? "
			+ "limit ? offset ?";
	
	try{
		
		connection = conn.getConnection();
		statement = connection.prepareStatement(query);
		statement.setString(1, username);
		statement.setInt(2, velicinaStrane);
		statement.setInt(3, velicinaStrane * (strana-1));
		resultSet = statement.executeQuery();

		while(resultSet.next()){
			
			String username1 = resultSet.getString("username1");
						
			ret.add(getKorisnik(username1));
			
		}

		return ret;
		
	}finally{
		releaseResources(connection, statement, resultSet);
	}

	
}

	//korisnici koji su se registrovali i cekaju aktivaciju
	public int ubaciUAktivaciju(String username) throws SQLException{
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		List<Korisnik> ret = new ArrayList<Korisnik>();
		
		String query = "insert into aktivacija values(?,?)";
		Random r = new Random();
		int rnd = r.nextInt(999999999);
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			statement.setString(1, username);
			statement.setInt(2, rnd);
			statement.executeUpdate();
			return rnd;
		}finally{
			releaseResources(connection, statement, null);
		}
		
	}
	
	//oni koji aktiviraju nalog brisu se iz baze
	public void ObrisiIzAktivacije(String username) throws SQLException{
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		List<Korisnik> ret = new ArrayList<Korisnik>();
		
		String query = "delete from aktivacija where username=?";
		
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			statement.setString(1, username);
			statement.executeUpdate();
			
		}finally{
			releaseResources(connection, statement, null);
		}
		
	}
	
	//daje username korisnika po id-u iz tabele aktivacija
	public String getUsername(int id) throws SQLException{
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		List<Korisnik> ret = new ArrayList<Korisnik>();
		
		String query = "select username from aktivacija where id = ? limit 1";
		
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();

			if(resultSet.next()){
				return resultSet.getString("username");
			}
			
		}finally{
			releaseResources(connection, statement, null);
		}
		return "Greska";
		
	}
	
	//menja kolonu enabled iz 0 u 1 korisniku
	public void aktivirajKorisnika(int id) throws SQLException{
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		List<Korisnik> ret = new ArrayList<Korisnik>();
		
		String query = "update korisnik set enabled=1 where username = ?";
		
		String username = getUsername(id);
		
		try{
			
			connection = conn.getConnection();
			statement = connection.prepareStatement(query);
			statement.setString(1, username);
			statement.executeUpdate();
			ObrisiIzAktivacije(username);
		}finally{
			releaseResources(connection, statement, null);
		}
		
	}
	
	private void releaseResources(Connection connection, Statement statement, ResultSet resultSet) {
		if (resultSet != null) {

			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (statement != null) {

			try {
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
