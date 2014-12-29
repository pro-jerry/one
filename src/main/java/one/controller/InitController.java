package one.controller;

import javax.servlet.http.HttpSession;

import one.service.InitService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/initController")
public class InitController {

	@Autowired
	private InitService initService;
	
	@RequestMapping("/init")
	public String init(HttpSession session){
		
		if (session != null) {
			session.invalidate();
		}
		initService.init();
		
		return "init";
	}
}
