package one.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import one.util.StringEscapeEditor;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/baseController")
public class BaseController {

	@InitBinder
	public void initBinder(ServletRequestDataBinder binder){
		
		/**
		 * 自动转换日期类型格式
		 */
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
		
		/**
		 * 防止XSS攻击
		 */
		binder.registerCustomEditor(String.class, new StringEscapeEditor(true,false));
		
	}
	
	/**
	 * 用户跳转到JSP页面
	 * 此方法不考虑权限控制
	 * @param folder
	 * @param jspName
	 * @return
	 */
	@RequestMapping("/{folder}/{jspName}")
	public String redirectJsp(@PathVariable String folder,@PathVariable String jspName){
		
		return "/"+folder+"/"+jspName;
	}
}
