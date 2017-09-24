package glavni.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

	@RequestMapping(path="uploadFile", method = {RequestMethod.POST})
	@ResponseBody
	String uploadFileHandler(@RequestParam("file") MultipartFile file){
		
		if(!file.isEmpty()){
			try{
				byte[] bytes = file.getBytes();
				
				//pravim folder gde cu da stavim fajl
				String rootPath = System.getProperty("catalina.home");
				File dir = new File(rootPath + File.separator + "slike");
				if(!dir.exists())
					dir.mkdirs();
				
				//pravim fajl na serveru
				File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());
				
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();
				
				return "Uspesno ste uploadovali sliku";
				
			}catch(Exception e){
				
				return "Greska pri uploadovanju slike: "+e.getMessage();
				
			}
		}
		return "file nije empty";
		
	}
	
}
