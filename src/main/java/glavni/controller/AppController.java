package glavni.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import glavni.db.DBHelper;
import glavni.exceptions.EmailNePostojiException;
import glavni.exceptions.EmailVecPostojiException;
import glavni.exceptions.UsernameNePostojiException;
import glavni.exceptions.UsernameVecPostojiException;
import glavni.model.Aktivnost;
import glavni.model.AktivnostGpx;
import glavni.model.AktivnostKorisnik;
import glavni.model.GpxRezultat;
import glavni.model.Korisnik;
import pt.karambola.gpx.beans.Gpx;
import pt.karambola.gpx.io.GpxFileIo;

@RestController
@RequestMapping("")
public class AppController {

	@Autowired
	DBHelper dbHelper;
	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Autowired
	MailSender mailSender;
	
	@RequestMapping(path="aktivnosti", method = {RequestMethod.GET} , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public List<Aktivnost> getAktivnosti() throws SQLException{
		return dbHelper.getAktivnosti();
	}
	
	@RequestMapping(path="aktivnostiPrijatelja", method = {RequestMethod.GET} , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public List<AktivnostKorisnik> getAktivnostiPrijatelja(@RequestParam(name="username") String username) throws SQLException{
		return dbHelper.getAktivnostiPrijatelja(username);
	}
	
	@RequestMapping(path="korisnici", method = {RequestMethod.GET} , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public List<Korisnik> getKorisnici() throws SQLException{
		return dbHelper.getKorisnici();
	}
	
	@RequestMapping(path="getKorisnik/{username}", method = {RequestMethod.GET} , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public Korisnik getKorisnik(@PathVariable(name="username") String username) throws SQLException, UsernameNePostojiException{
		return dbHelper.getKorisnik(username);
	}
	
	@RequestMapping(path="getAktivnostiPoStranama", method={RequestMethod.GET} , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})//TODO: dodaj eksepsn za strana=0
	public List<Aktivnost> getAktivnostiPoStranama(@RequestParam(name="velicinaStrane") int velicinaStrane, @RequestParam(name="strana") int strana) throws SQLException{
		
		return dbHelper.getAktivnostiPoStranama(velicinaStrane, strana);
		
	}
	
	//sa usernameom
	@RequestMapping(path="getAktivnostiPoStranama2", method={RequestMethod.GET} , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})//TODO: dodaj eksepsn za strana=0
	public List<AktivnostKorisnik> getAktivnostiPoStranama(@RequestParam(name="velicinaStrane") int velicinaStrane, @RequestParam(name="strana") int strana, @RequestParam(name="username") String username) throws SQLException{
		
		return dbHelper.getAktivnostiPoStranama(velicinaStrane, strana, username);
		
	}
	
	@RequestMapping(path="dodajAktivnost", method={RequestMethod.POST}, consumes={MediaType.APPLICATION_JSON_UTF8_VALUE})
	public void dodajAktivnost(@RequestBody Aktivnost aktivnost) throws SQLException{
		
		dbHelper.dodajAktivnost(aktivnost);
		
	}
	
	@RequestMapping(path="ulogovaniKorisnik", method={RequestMethod.GET}, produces={MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseBody
    public Korisnik getUlogovaniKorisnik(Principal principal) throws SQLException, UsernameNePostojiException{
        return dbHelper.getKorisnik(principal.getName());
    }
	
	@RequestMapping(path="getPrijateljiPoStranama",method = {RequestMethod.GET} , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public List<Korisnik> getPrijateljiPoStranama(@RequestParam(name="username") String username, @RequestParam(name="velicinaStrane")int velicinaStrane, @RequestParam(name="strana")int strana) throws SQLException, UsernameNePostojiException{
		return dbHelper.getPrijateljiPoStranama(username, velicinaStrane , strana);
	}
	
	@RequestMapping(path="registrujKorisnikaStari", method = {RequestMethod.POST})
	public void registrujKorisnikaStari(@RequestBody Korisnik korisnik) throws SQLException{
		korisnik.setPassword(encoder.encode(korisnik.getPassword()));
		dbHelper.dodajKorisnikaStari(korisnik);
	}
	
	//registrovanje korisnika sa posebnim parametrima(bez slanja objekta Korisnik) i slikom
	@RequestMapping(path="registrujKorisnika", method = {RequestMethod.POST})
	public ModelAndView registrujKorisnika(
			@RequestParam(name="username") String username,
			@RequestParam(name="email") String email,
			@RequestParam(name="password") String password,
			@RequestParam(name="opetpassword") String opetpassword,
			@RequestParam(name="ime") String ime,
			@RequestParam(name="prezime") String prezime,
			@RequestParam("file") MultipartFile file
			) throws SQLException, IOException, UsernameVecPostojiException, EmailNePostojiException, EmailVecPostojiException{
		
		String imeSlike = "";
		String rootPath = System.getProperty("catalina.home");
		
		if(file.isEmpty()){
			imeSlike = "default.jpg";
			
			File src = new File(rootPath + File.separator + "webapps" + File.separator + "trcko_podaci" + File.separator + "slike" + File.separator + "default.jpg");
			File dest = new File(rootPath + File.separator + "webapps" + File.separator + "trcko_podaci" + File.separator + "slike" + File.separator + username + File.separator + "profilna slika" + File.separator + imeSlike);
			if(!dest.exists()) dest.mkdirs();
			Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
			
		}
		
		if(!file.isEmpty()){
			try{
				byte[] bytes = file.getBytes();
				
				//pravim folder gde cu da stavim fajl
				
				File dir = new File(rootPath + File.separator + "webapps" + File.separator + "trcko_podaci" + File.separator + "slike" + File.separator + username + File.separator + "profilna slika");
				if(!dir.exists())
					dir.mkdirs();
				
				imeSlike = file.getOriginalFilename();
				
				//pravim fajl na serveru
				File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
				
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				
			}catch(Exception e){
				e.printStackTrace();
			}

	}

		password = encoder.encode(password);
		int br = dbHelper.dodajKorisnika(username, email, password, opetpassword, ime, prezime, imeSlike);
		
		SimpleMailMessage smm = new SimpleMailMessage();
		
		smm.setFrom("gprincip132@gmail.com");
		smm.setTo(email);
		smm.setSubject("Dobrodošao na Trčka!");
		smm.setText("Cao "+ ime+ ", dobrodošao na Trcka. Otvori sledeci link da aktiviras nalog. localhost:8080/trcanje/services/rest/aktivirajNalog/"+br);
		
		
		mailSender.send(smm);
		
		
		
		return new ModelAndView("/posleRegistracije.jsp");
		
	}
	
	@RequestMapping(path="emailTest", method = {RequestMethod.GET})
	public void emailTest(){
		SimpleMailMessage smm = new SimpleMailMessage();
		
		smm.setFrom("gprincip132@gmail.com");
		smm.setTo("gavrilo.adamovic@pmf.edu.rs");
		smm.setSubject("naslov");
		smm.setText("text");
		
		
		mailSender.send(smm);
	}
	
	//***************************************************Funkcije za rad sa gpx************************************************************
	
	//uploadovanje gpx fajla
	
	@RequestMapping(path="dodajAktivnostGpx", method = {RequestMethod.POST})
	public ModelAndView dodajAktivnostGpx(
			@RequestParam(name="username") String username,
			@RequestParam(name="naslov") String naslov,
			@RequestParam("file") MultipartFile file
			) throws SQLException, IOException{
		
		String rootPath = System.getProperty("catalina.home");

		
		if(!file.isEmpty()){
			try{
				byte[] bytes = file.getBytes();
				
				//pravim folder gde cu da stavim fajl
				
				File dir = new File(rootPath + File.separator + "webapps" + File.separator + "trcko_podaci" + File.separator + "aktivnosti" + File.separator + username);
				if(!dir.exists())
					dir.mkdirs();
				
				//pravim fajl na serveru
				File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
				
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				
				dbHelper.dodajAktivnostGpx(new AktivnostGpx(0,username,file.getOriginalFilename(), naslov));//ako stavim null ne prepoznaje ga kao int, pa sam stavio 0, a u dbhelperu je insert(null, ?, ?)
				
				Gpx gpx = GpxFileIo.parseIn(serverFile.getAbsolutePath());
				GpxRezultat gpxRez = new GpxRezultat(0,username, gpx.getTracks().get(0).getTrackSegments());
				
				String prosecanTempo = "";
				if(gpxRez.getProsecanTempo().getMinut() < 10)
					prosecanTempo += "0" + gpxRez.getProsecanTempo().getMinut() + ":";
				else prosecanTempo += "" + gpxRez.getProsecanTempo().getMinut();
				prosecanTempo+=" ";
				if(gpxRez.getProsecanTempo().getSekunda() < 10)
					prosecanTempo += "0" + gpxRez.getProsecanTempo().getSekunda();
				else prosecanTempo += "" + gpxRez.getProsecanTempo().getSekunda();
				
				Date datum =  gpxRez.getTrackPoints().get(0).getTrackPoints().get(0).getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				
				double trajanjeDbl = gpxRez.getUkupnoVreme();
				String trajanje = "";
				
				int sati = 0;
				
				int minuti = (int)Math.floor(trajanjeDbl/60);
				int sekunde = (int)Math.round(((trajanjeDbl/60) - Math.floor(trajanjeDbl/60)) * 60);
				
				if(minuti > 60){
					sati = (int)Math.floor(minuti/60);
					minuti -= 60 * sati;
				}
				
				if(sati < 10 && sati > 0)
					trajanje += "0"+sati+":";
				else if(sati > 0) trajanje += sati+":";
				
				if(minuti < 10)
					trajanje += "0"+minuti+":";
				else trajanje += minuti+":";
				
				if(sekunde < 10)
					trajanje += "0"+sekunde;
				else trajanje += sekunde;
				
				Aktivnost aktivnost = new Aktivnost(0, username, sdf.format(datum), sdf2.format(datum), prosecanTempo, gpxRez.getUkupnaRazdaljina()/1000, naslov, trajanje);
				
				dbHelper.dodajAktivnost(aktivnost);
				
				return new ModelAndView("/posleUploadaAktivnosti.jsp");
				
			}catch(Exception e){
				e.printStackTrace();
			}

	}
		return null;
		
	}
	
	//******************************************************************************************************************
	
	@RequestMapping(path="test", method = {RequestMethod.GET} , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public int test() throws SQLException{
		return 2;
	}
	
	@RequestMapping(path="lajkuj", method = {RequestMethod.POST})
	public int lajkuj(@RequestParam(name = "username") String username, @RequestParam(name="aktivnost_id") int aktivnost_id) throws SQLException{
		
		return dbHelper.lajkuj(username, aktivnost_id);
		
	}
	
	@RequestMapping(path="obrisiLajk", method = {RequestMethod.DELETE})
	public void obrisiLajk(@RequestParam(name="username") String username, @RequestParam(name="aktivnost_id") int aktivnost_id) throws SQLException{
		
		dbHelper.obrisiLajk(username, aktivnost_id);
		
	}
	
	@RequestMapping(path="lajkovao", method = {RequestMethod.GET} , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public boolean lajkovao(@RequestParam(name = "username") String username, @RequestParam(name="aktivnost_id")int aktivnost_id) throws SQLException{
		
		return dbHelper.lajkovao(username, aktivnost_id);
		
	}
	
	@RequestMapping(path="brojLajkova/{aktivnost_id}", method = {RequestMethod.GET}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public int getBrojLajkova(@PathVariable(name="aktivnost_id") int aktivnost_id) throws SQLException{
		return dbHelper.brojLajkova(aktivnost_id);
	}
	
	//daje aktivnosti jednog korisnika po stranama
	@RequestMapping(path="getAktivnostiKorisnikaPoStranama", method={RequestMethod.GET} , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})//TODO: dodaj eksepsn za strana=0
	public List<AktivnostKorisnik> getAktivnostiKorisnikaPoStranama(@RequestParam(name="velicinaStrane") int velicinaStrane, @RequestParam(name="strana") int strana, @RequestParam(name="username") String username) throws SQLException{
		
		return dbHelper.getAktivnostiKorisnikaPoStranama(velicinaStrane, strana, username);
		
	}
	
	//broj aktivnosti korisnika i njegovih prijatelja
	@RequestMapping(path="getBrojAktivnostiKorisnikaIPrijatelja", method = {RequestMethod.GET} , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public int getBrojAktivnostiAktivnostiKorisnikaIPrijatelja(@RequestParam(name="username") String username) throws SQLException{
		return dbHelper.getBrojAktivnostiKorisnikaIPrijatelja(username);
	}
	
	//broj aktivnosti samo korisnika
	@RequestMapping(path="getBrojAktivnostiKorisnika", method = {RequestMethod.GET} , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public int getBrojAktivnostiAktivnostiKorisnika(@RequestParam(name="username") String username) throws SQLException{
		return dbHelper.getBrojAktivnostiKorisnika(username);
	}
	
	@RequestMapping(path="daLiSuPrijatelji", method={RequestMethod.GET} , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public boolean daLiSuPrijatelji(@RequestParam(name="username1") String username1, @RequestParam(name="username2") String username2) throws SQLException{
		
		return dbHelper.daLiSuPrijatelji(username1, username2);
		
	}
	
	@RequestMapping(path="dodajPrijatelja", method={RequestMethod.POST} , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public void dodajPrijatelja(@RequestParam(name="username1") String username1, @RequestParam(name="username2") String username2) throws SQLException{
		
		dbHelper.dodajPrijatelja(username1, username2);
	}
	
	@RequestMapping(path="obrisiPrijatelja", method={RequestMethod.POST} , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public void obrisiPrijatelja(@RequestParam(name="username1") String username1, @RequestParam(name="username2") String username2) throws SQLException{
		dbHelper.obrisiPrijatelja(username1, username2);
	}
	
	//pratioci korisnika username
	@RequestMapping(path="getPratiociPoStranama", method={RequestMethod.GET} , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public List<Korisnik> getPratioci(@RequestParam(name="username") String username, @RequestParam(name="velicinaStrane")int velicinaStrane, @RequestParam(name="strana")int strana) throws SQLException, UsernameNePostojiException{
		return dbHelper.getPratiociPoStranama(username, velicinaStrane, strana);
	}
	

	
}
