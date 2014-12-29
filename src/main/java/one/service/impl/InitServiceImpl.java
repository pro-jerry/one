package one.service.impl;

import java.util.Date;

import one.dao.PrivilegeDao;
import one.dao.ResourceTypeDao;
import one.dao.RoleDao;
import one.dao.UserDao;
import one.model.Privilege;
import one.model.ResourceType;
import one.model.Role;
import one.model.User;
import one.service.InitService;
import one.util.MD5Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InitServiceImpl implements InitService{

	
	@Autowired
	private PrivilegeDao privilegeDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ResourceTypeDao resourceTypeDao;
	
	@Transactional
	 public void init(){
		
		initPrivilege();//初始化资源类型
		initRole();//初始化角色
		initUser();//初始化用户
		initResourceType();//初始化资源类型
	}
	
	
	private void initResourceType() {

		ResourceType resourceType1 = new ResourceType();
		resourceType1.setId("0");
		resourceType1.setName("菜单");
		resourceTypeDao.saveOrUpdate(resourceType1);
		
		ResourceType resourceType2 = new ResourceType();
		resourceType2.setId("1");
		resourceType2.setName("功能");
		resourceTypeDao.saveOrUpdate(resourceType2);
	}


	private void initUser() {

		User admin = new User();
		admin.setId("0");
		admin.setName("admin");
		admin.setPwd(MD5Util.md5("123456"));
		admin.setCreatedatetime(new Date());
		admin.getRoles().addAll(roleDao.get("FROM Role"));//给用户赋予角色
		userDao.saveOrUpdate(admin);
		
		User admin1 = new User();
		admin1.setId("1");
		admin1.setName("admin1");
		admin1.setPwd(MD5Util.md5("123456"));
		admin1.setCreatedatetime(new Date());
		//给用户赋予资源管理的角色
		admin1.getRoles().addAll(roleDao.get(" FROM Role r where r.id = 'zyAdmin' "));
		userDao.saveOrUpdate(admin1);
		
		User guest = new User();
		guest.setId("guest");
		guest.setName("guest");
		guest.setPwd(MD5Util.md5("123456"));
		guest.setCreatedatetime(new Date());
		//给用户赋予Guest角色
		guest.getRoles().addAll(roleDao.get(" FROM Role r where r.id = 'guest' "));
		userDao.saveOrUpdate(guest);
	}


	private void initRole() {

		Role superAdmin=new Role();
		superAdmin.setId("0");
		superAdmin.setName("超级管理员");
		superAdmin.getPrivileges().addAll(privilegeDao.get("FROM Privilege p"));
		//超级管理员可以访问所有资源
		superAdmin.setSeq(0);
		superAdmin.setRemark("超级管理员角色，拥有系统中所有的资源访问权限");
		roleDao.saveOrUpdate(superAdmin);
		
		Role zyAdmin = new Role();
		zyAdmin.setId("zyAdmin");
		zyAdmin.setName("资源管理员");
		zyAdmin.setSeq(1);
		zyAdmin.setRole(superAdmin);
		zyAdmin.getPrivileges().addAll(privilegeDao.get("FROM Privilege p where p.privilege.id in('zygl') or p.id in('zygl')"));
		roleDao.saveOrUpdate(zyAdmin);
		
		Role guest = new Role();
		guest.setId("guest");
		guest.setName("Guest");
		guest.getPrivileges().addAll(privilegeDao.get("FROM Privilege p where p.id in('xtgl','zygl','zyglTreeGrid','jsgl','jsglTreeGrid','yhgl','yhglDateGrid')"));
		guest.setSeq(1);
		guest.setRemark("只拥有查看的权限");
		roleDao.saveOrUpdate(guest);
	}


	private void initPrivilege() {

		ResourceType menuType = resourceTypeDao.get(ResourceType.class, "0");//菜单类型
		ResourceType funType = resourceTypeDao.get(ResourceType.class, "1");//功能类型
		
		Privilege xtgl = new Privilege();
		xtgl.setId("xtgl");
		xtgl.setResourceType(menuType);
		xtgl.setName("系统管理");
		xtgl.setSeq(0);
		xtgl.setIcon("plugin");
		privilegeDao.saveOrUpdate(xtgl);
		
		Privilege zygl = new Privilege("zygl",menuType,"/privilegeController/manager", "资源管理", "管理系统中所有的菜单和功能", 1, xtgl,"database_gear");
		privilegeDao.saveOrUpdate(zygl);
		Privilege jsgl = new Privilege("jsgl",menuType,"/roleController/manager", "角色管理", null, 2, xtgl,"tux");
		privilegeDao.saveOrUpdate(jsgl);
		Privilege yhgl = new Privilege("yhgl",menuType,"/userController/manager", "用户管理", null, 3, xtgl,"status_online");
		privilegeDao.saveOrUpdate(yhgl);
		
		//资源表格
		Privilege zyglTreeGrid = new Privilege("zyglTreeGrid",funType ,"/privilegeController/treeGrid", "资源表格", "显示资源列表", 1, zygl,"wrench");
		privilegeDao.saveOrUpdate(zyglTreeGrid);
		
		//资源功能菜单
		Privilege zyglMenu = new Privilege("zyglMenu", funType,"/privilegeController/tree", "功能菜单", null, 2, zygl,"wrench");
		privilegeDao.saveOrUpdate(zyglMenu);
	
		Privilege zyglAddPage = new Privilege("zyglAddPage", funType,"/privilegeController/addPage", "添加资源页面", null, 3, zygl,"wrench");
		privilegeDao.saveOrUpdate(zyglAddPage);
		
		Privilege zyglAdd = new Privilege("zyglAdd",funType ,"/privilegeController/add", "添加资源", null, 4, zygl,"wrench");
		privilegeDao.saveOrUpdate(zyglAdd);
		
		Privilege zyglEditPage = new Privilege("zyglEditPage",funType,"/privilegeController/editPage", "编辑资源页面", null, 5, zygl,"wrench");
		privilegeDao.saveOrUpdate(zyglEditPage);
		
		Privilege zyglEdit = new Privilege("zyglEdit", funType,"/privilegeController/edit", "编辑资源", null, 6, zygl,"wrench");
		privilegeDao.saveOrUpdate(zyglEdit);
		
		Privilege zyglDelete = new Privilege("zyglDelete", funType,"/privilegeController/delete", "删除资源", null, 7, zygl,"wrench");
		privilegeDao.saveOrUpdate(zyglDelete);
		
		
		Privilege jsglTreeGrid = new Privilege("jsglTreeGrid",funType,"/roleController/treeGrid", "角色表格", null, 1, jsgl,"wrench");
		privilegeDao.saveOrUpdate(jsglTreeGrid);
		
		Privilege jsglAddPage = new Privilege("jsglAddPage",funType,"/roleController/addPage", "添加角色页面", null, 2, jsgl,"wrench");
		privilegeDao.saveOrUpdate(jsglAddPage);
		
		Privilege jsglAdd = new Privilege("jsglAdd",funType,"/roleController/add", "添加角色", null, 3, jsgl,"wrench");
		privilegeDao.saveOrUpdate(jsglAdd);
		
		Privilege jsglEditPage = new Privilege("jsglEditPage",funType ,"/roleController/editPage", "编辑角色页面", null, 4, jsgl,"wrench");
		privilegeDao.saveOrUpdate(jsglEditPage);
		
		Privilege jsglEdit = new Privilege("jsglEdit",funType ,"/roleController/edit", "编辑角色", null, 5, jsgl,"wrench");
		privilegeDao.saveOrUpdate(jsglEdit);
		
		Privilege jsglDelete = new Privilege("jsglDelete",funType ,"/roleController/delete", "删除角色", null, 6, jsgl,"wrench");
		privilegeDao.saveOrUpdate(jsglDelete);
		
		Privilege jsglGrantPage = new Privilege("jsglGrantPage", funType,"/roleController/grantPage", "角色授权页面", null, 7, jsgl,"wrench");
		privilegeDao.saveOrUpdate(jsglGrantPage);
		
		Privilege jsglGrant = new Privilege("jsglGrant",funType ,"/roleController/grant", "角色授权", null, 8, jsgl,"wrench");
		privilegeDao.saveOrUpdate(jsglGrant);
		
		
		//用户管理
		Privilege yhglDateGrid = new Privilege("yhglDateGrid",funType ,"/userController/dataGrid", "用户表格", null, 1, yhgl,"wrench");
		privilegeDao.saveOrUpdate(yhglDateGrid);
		
		Privilege yhglAddPage= new Privilege("yhglAddPage",funType ,"/userController/addPage", "添加用户页面", null, 2, yhgl,"wrench");
		privilegeDao.saveOrUpdate(yhglAddPage);
		
		Privilege yhglAdd = new Privilege("yhglAdd",funType ,"/userController/add", "添加用户", null, 3, yhgl,"wrench");
		privilegeDao.saveOrUpdate(yhglAdd);
		
		Privilege yhglEditPage = new Privilege("yhglEditPage",funType ,"/userController/editPage", "编辑用户页面", null, 4, yhgl,"wrench");
		privilegeDao.saveOrUpdate(yhglEditPage);
		
		Privilege yhglEdit = new Privilege("yhglEdit",funType ,"/userController/edit", "编辑用户", null, 5, yhgl,"wrench");
		privilegeDao.saveOrUpdate(yhglEdit);
		
		Privilege yhglDelete = new Privilege("yhglDelete",funType ,"/userController/delete", "删除用户", null, 6, yhgl,"wrench");
		privilegeDao.saveOrUpdate(yhglDelete);
		
		Privilege yhglBatchDelete = new Privilege("yhglBatchDelete",funType ,"/userController/batchDelete", "批量删除用户", null, 7, yhgl,"wrench");
		privilegeDao.saveOrUpdate(yhglBatchDelete);
		
		Privilege yhglGrantPage = new Privilege("yhglGrantPage",funType ,"/userController/grantPage", "用户授权页面", null, 8, yhgl,"wrench");
		privilegeDao.saveOrUpdate(yhglGrantPage);
		
		Privilege yhglGrant = new Privilege("yhglGrant",funType ,"/userController/grant", "用户授权", null, 9, yhgl,"wrench");
		privilegeDao.saveOrUpdate(yhglGrant);
		
		Privilege yhglEditPwdPage = new Privilege("yhglEditPwdPage",funType ,"/userController/editPwdPage", "用户修改密码页面", null, 10, yhgl,"wrench");
		privilegeDao.saveOrUpdate(yhglEditPwdPage);
		
		Privilege yhglEditPwd = new Privilege("yhglEditPwd",funType ,"/userController/editPwd", "用户修改密码", null, 11, yhgl,"wrench");
		privilegeDao.saveOrUpdate(yhglEditPwd);
		
		//数据源管理
		Privilege sjygl = new Privilege("sjygl",menuType ,"/druidController/druid", "数据源管理", null, 5, xtgl,"server_database");
		privilegeDao.saveOrUpdate(sjygl);
		
		//文件管理
		Privilege wjgl = new Privilege("wjgl", menuType,"", "文件管理", null, 6, xtgl,"server_database");
		privilegeDao.saveOrUpdate(wjgl);
		
	}
	

}
