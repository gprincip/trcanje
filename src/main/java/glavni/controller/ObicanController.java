package glavni.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import glavni.db.DBHelper;
import glavni.exceptions.UsernameNePostojiException;

@Controller
public class ObicanController {

	@Autowired
	DBHelper dbHelper;
	
	@RequestMapping("korisnik/{username}")
	public String showUserDetails(@PathVariable String username, Model model) throws SQLException, UsernameNePostojiException {
	    model.addAttribute("userDetails",dbHelper.getKorisnik(username));
	    return "/profil.jsp";
	}
	
	 @RequestMapping(value = "/profilna/{username}", method = {RequestMethod.GET})
	    @ResponseBody
	    public byte[] getImage(@PathVariable(value = "username") String username, @RequestParam(value = "imeSlike") String imeSlike) throws IOException {
		 	
		    String rootPath = System.getProperty("catalina.home");

	        File serverFile = new File(rootPath + File.separator + "webapps" + File.separator + "trcko_podaci" + File.separator + "slike" + File.separator + username + File.separator + "profilna slika" + File.separator + imeSlike);
	        
	        return Files.readAllBytes(serverFile.toPath());
	    }

	@RequestMapping(path="aktivirajNalog/{id}", method={RequestMethod.GET})
	public String aktivirajNalog(@PathVariable(name="id") int id) throws SQLException{
		dbHelper.aktivirajKorisnika(id);
		return "/posleAktivacije.html";
	}
}
