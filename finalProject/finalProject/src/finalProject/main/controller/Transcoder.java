package finalProject.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import finalProject.main.Video;
import finalProject.main.model.FileUpload;

@Controller
public class Transcoder {

	@RequestMapping(value="/transcoder", method=RequestMethod.GET)
	public String transcoderForm(Model model) {
		model.addAttribute("fileUpload",new FileUpload());
		return "transcoder";
	}
	
	
	@RequestMapping(value="/transcoder", method=RequestMethod.POST)
	public String transcoderSubmit(@ModelAttribute FileUpload fileUpload,Model model){
		model.addAttribute("fileUpload",fileUpload);
		return "uploaded";
	}
}
