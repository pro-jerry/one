package one.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import one.pagemodel.Erole;
import one.pagemodel.Json;
import one.pagemodel.SessionInfo;
import one.pagemodel.Tree;
import one.service.RoleService;
import one.util.ConfigUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/roleController")
public class RoleController extends BaseController {

	@Autowired
	private RoleService roleService;
	
	/**
	 * 只能看到自己拥有的角色
	 * @param session
	 * @return
	 */
	@RequestMapping("/tree")
	@ResponseBody
	public List<Tree> tree(HttpSession session){
		
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		return roleService.tree(sessionInfo);
		
	}
	
	/**
	 * 角色树
	 * @return
	 */
	@RequestMapping("/allTree")
	@ResponseBody
	public List<Tree> allTree(){
		return roleService.allTree();
	}
	
	/**
	 * 跳转到角色管理页面
	 * @return
	 */
	@RequestMapping("/manager")
	public String manager(){
		return "/admin/role";
	}
	
	/**
	 * 获得角色列表
	 * @param session
	 * @return
	 */
	@RequestMapping("/treeGrid")
	@ResponseBody
	public List<Erole> treeGrid(HttpSession session){
		
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		return roleService.treeGrid(sessionInfo);
		
	}
	
	/**
	 * 跳转到用户添加页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request){
		Erole er = new Erole();
		er.setId(UUID.randomUUID().toString());
		request.setAttribute("role", er);
		return "/admin/roleAdd";
		
	}
	
	/**
	 * 添加角色
	 * @param erole
	 * @param session
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Json add(Erole erole,HttpSession session){
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		Json j = new Json();
		roleService.add(erole, sessionInfo);
		j.setSuccess(true);
		j.setMsg("添加成功！");
		return j;
		
	}
	
	/**
	 * 删除角色
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String id){
		Json j = new Json();
		roleService.delete(id);
		j.setMsg("删除成功");
		j.setSuccess(true);
		return j;
		
	}
	
	/**
	 * 跳转到角色修改页面
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request,String id){
		
		Erole e = roleService.get(id);
		request.setAttribute("role", e);
		return "/admin/roleEdit";
		
	}
	
	/**
	 * 修改角色
	 * @param erole
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(Erole erole){
		
		Json j = new Json();
		roleService.edit(erole);
		j.setMsg("编辑成功！");
		j.setSuccess(true);
		return j;
		
	}
	
	/**
	 * 跳转到角色授权页面
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/grantPage")
	public String grantPage(HttpServletRequest request,String id){
		Erole er = roleService.get(id);
		request.setAttribute("role", er);
		return "/admin/roleGrant";
		
	}
	
	/**
	 * 授权
	 * @param erole
	 * @return
	 */
	@RequestMapping("/grant")
	@ResponseBody
	public Json grant(Erole erole){
		Json j = new Json();
		roleService.grant(erole);
		j.setMsg("授权成功");
		j.setSuccess(true);
		return j;
	}
	
}
