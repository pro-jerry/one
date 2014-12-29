package one.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import one.pagemodel.DataGrid;
import one.pagemodel.Euser;
import one.pagemodel.Json;
import one.pagemodel.PageHelper;
import one.pagemodel.SessionInfo;
import one.service.UserService;
import one.util.ConfigUtil;
import one.util.IpUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/userController")
public class UserController extends BaseController{

	@Autowired
	private UserService userService;
	
	@ResponseBody
	@RequestMapping("/login")
	public Json login(Euser euser,HttpSession session,HttpServletRequest request){
		
		Json j = new Json();
		Euser eu = userService.login(euser);
		if(eu!=null){
			j.setSuccess(true);
			j.setMsg("登陆成功！");
			
			SessionInfo sessionInfo = new SessionInfo();
			BeanUtils.copyProperties(eu, sessionInfo);
			sessionInfo.setIp(IpUtil.getIpAddr(request));
			sessionInfo.setPrivilegeList(userService.privilegeList(eu.getId()));
			session.setAttribute(ConfigUtil.getSessionInfoName(), sessionInfo);
			
			j.setObj(sessionInfo);
		}else{
			j.setMsg("用户名或密码错误！");
		}
		return j;
	}
	
	@ResponseBody
	@RequestMapping("/logout")
	public Json logout(HttpSession session){
		
		Json j = new Json();
		if(session!=null){
			session.invalidate();
		}
		j.setSuccess(true);
		j.setMsg("注销成功");
		return j;
	}
	
	@RequestMapping("/manager")
	public String manager(){
		
		return "/admin/user";
	}
	
	/**
	 * 获取用户表格
	 * @param euser
	 * @param ph
	 * @return
	 */
	@RequestMapping("/dataGrid")
	@ResponseBody
	public DataGrid dataGrid(Euser euser,PageHelper ph ){
		return userService.dataGrid(euser, ph);
	}
	
	/**
	 * 跳转到用户修改页面
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request,String id){
		
		Euser eu = userService.get(id);
		request.setAttribute("user", eu);
		return "admin/userEdit";
	}
	
	/**
	 * 跳转到添加用户界面
	 * @param request
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request){
		Euser eu = new Euser();
		eu.setId(UUID.randomUUID().toString());
		request.setAttribute("user", eu);
		return "/admin/userAdd";
	}
	
	/**
	 * 添加用户
	 * @param euser
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Json add(Euser euser){
		
		Json j = new Json();
		try {
			userService.add(euser);
			j.setSuccess(true);
			j.setMsg("添加成功!");
			j.setObj(euser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	
	/**
	 * 修改用户
	 * @param euser
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(Euser euser){
		Json j = new Json();
		try {
			userService.edit(euser);
			j.setSuccess(true);
			j.setMsg("编辑成功");
			j.setObj(euser);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
		
	}
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String id,HttpSession session){
		
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		Json j = new Json();
		if(id!=null && !id.equalsIgnoreCase(sessionInfo.getId())){//不能删除自己
			userService.delete(id);
		}
		j.setMsg("删除成功");
		j.setSuccess(true);
		return j;
		
	}
	
	/**
	 * 编辑用户密码
	 * @param euser
	 * @return
	 */
	@RequestMapping("/editPwd")
	@ResponseBody
	public Json editPwd(Euser euser){
		Json j = new Json();
		userService.editPwd(euser);
		j.setSuccess(true);
		j.setMsg("编辑成功！");
		return j;
	}
	
	/**
	 * 跳转到编辑用户密码页面
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping("/editPwdPage")
	public String editPwdPage(String id,HttpServletRequest request){
		
		Euser eu = userService.get(id);
		request.setAttribute("user", eu);
		
		return "/admin/userEditPwd";
	}
	
	/**
	 * 跳转到用户授权页面
	 * @param ids
	 * @param request
	 * @return
	 */
	@RequestMapping("/grantPage")
	public String grantPage(String ids,HttpServletRequest request){
		
		request.setAttribute("ids", ids);
		if(ids!=null && !ids.equalsIgnoreCase("") && ids.indexOf(",")==-1){
			Euser eu = userService.get(ids);
			request.setAttribute("user", eu);
		}
		return "/admin/userGrant";
	}
	
	/**
	 * 用户授权
	 * @param ids
	 * @param euser
	 * @return
	 */
	@RequestMapping("/grant")
	@ResponseBody
	public Json grant(String ids,Euser euser){
		
		Json j = new Json();
		userService.grant(ids, euser);
		j.setSuccess(true);
		j.setMsg("授权成功");
		return j;
		
	}
	
	@RequestMapping("/batchDelete")
	@ResponseBody
	public Json batchDelete(String ids,HttpSession session){
		Json j = new Json();
		if(ids!=null && ids.length()>0){
			for(String id:ids.split(",")){
				if(id!=null){
					this.delete(id, session);
				}
			}
		}
		j.setMsg("批量删除成功");
		j.setSuccess(true);
		return j;
	}
	
}
