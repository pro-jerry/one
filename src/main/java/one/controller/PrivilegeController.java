package one.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import one.pagemodel.Eprivilege;
import one.pagemodel.Json;
import one.pagemodel.SessionInfo;
import one.pagemodel.Tree;
import one.service.PrivilegeService;
import one.service.ResourceTypeService;
import one.util.ConfigUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/privilegeController")
public class PrivilegeController extends BaseController {

	@Autowired
	private PrivilegeService privilegeService;
	
	@Autowired
	private ResourceTypeService resourceTypeService;
	
	/**
	 * 获得资源树(资源类型为菜单类型)
	 * 通过用户ID判断，他能看到的资源
	 * @param session
	 * @return
	 */
	@RequestMapping("/tree")
	@ResponseBody
	public List<Tree> tree(HttpSession session){
		
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		return privilegeService.tree(sessionInfo);
	}
	
	/**
	 * 获得资源树(包括所有的类型资源)
	 * 通过用户ID判断，他能看到的资源
	 * @param httpSession
	 * @return
	 */
	@RequestMapping("/allTree")
	@ResponseBody
	public List<Tree> allTree(HttpSession httpSession){

		SessionInfo sessionInfo = (SessionInfo) httpSession.getAttribute(ConfigUtil.getSessionInfoName());
		return privilegeService.allTree(sessionInfo);
	}
	
	/**
	 * 跳转到资源管理界面
	 * @return
	 */
	@RequestMapping("/manager")
	public String manager(){
		return "/admin/resource";
	}
	
	/**
	 * 获得资源列表
	 * 通过用户ID判断，他能看到的资源
	 * @param session
	 * @return
	 */
	@RequestMapping("/treeGrid")
	@ResponseBody
	public List<Eprivilege> treeGrid(HttpSession session){
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		return privilegeService.treeGrid(sessionInfo);
	}
	
	/**
	 * 跳转到资源添加页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request){
		request.setAttribute("resourceTypeList", resourceTypeService.getResourceTypeList());
		Eprivilege r = new Eprivilege();
		r.setId(UUID.randomUUID().toString());
		request.setAttribute("resource", r);
		return "/admin/resourceAdd";
	}
	
	/**
	 * 添加资源
	 * @param eprivilege
	 * @param session
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Json add(Eprivilege eprivilege,HttpSession session){
		
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		Json j = new Json();
		privilegeService.add(eprivilege, sessionInfo);
		j.setSuccess(true);
		j.setMsg("添加成功");
		return j;
	}
	
	/**
	 * 跳转到编辑资源页面
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request,String id){
		request.setAttribute("resourceTypeList", resourceTypeService.getResourceTypeList());
		Eprivilege r = privilegeService.get(id);
		request.setAttribute("resource", r);
		return "/admin/resourceEdit";
		
	}
	
	/**
	 * 编辑资源
	 * @param eprivilege
	 * @return
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(Eprivilege eprivilege){
		Json j = new Json();
		privilegeService.edit(eprivilege);
		j.setSuccess(true);
		j.setMsg("编辑成功");
		return j;
		
	}
	
	/**
	 * 删除资源
	 * @param id
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String id){
		Json j = new Json();
		privilegeService.delete(id);
		j.setMsg("删除成功");
		j.setSuccess(true);
		return j;
	}
	
	
}
