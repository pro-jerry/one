package one.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import one.dao.RoleDao;
import one.dao.UserDao;
import one.model.Privilege;
import one.model.Role;
import one.model.User;
import one.pagemodel.DataGrid;
import one.pagemodel.Euser;
import one.pagemodel.PageHelper;
import one.service.UserService;
import one.util.MD5Util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Override
	public Euser login(Euser euser) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("name", euser.getName());
		params.put("pwd", MD5Util.md5(euser.getPwd()));
		User u = userDao.get(" from User u where u.name = :name and u.pwd = :pwd ", params);
		if(u!=null){
			BeanUtils.copyProperties(u, euser);
			return euser;
		}
		return null;
	}
	
	public List<String> privilegeList(String id){
		
		List<String> privilegeList = new ArrayList<String>();
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("id", id);
		User u = userDao.get("from User u join fetch u.roles role join fetch role.privileges privilege where u.id = :id ", params);
		if(u!=null){
			Set<Role> roles = u.getRoles();
			if(roles!=null && !roles.isEmpty()){
				for(Role role:roles){
					Set<Privilege> privileges = role.getPrivileges();
					if(privileges!=null && !privileges.isEmpty()){
						for(Privilege privilege:privileges){
							if(privilege!=null && privilege.getUrl()!=null){
								privilegeList.add(privilege.getUrl());
							}
						}
					}
				}
			}
		}
		return privilegeList;
		
	}

	@Override
	public DataGrid dataGrid(Euser euser, PageHelper ph) {

		DataGrid dataGrid = new DataGrid();
		List<Euser> el = new ArrayList<Euser>();
		Map<String,Object> params = new HashMap<String, Object>();
		String hql=" from User u ";
		List<User> l=userDao.find(hql+whereHql(euser,params)+orderHql(ph), params,ph.getPage(),ph.getRows());
		if(l!=null && l.size()>0){
			for(User u:l){
				Euser eu = new Euser();
				BeanUtils.copyProperties(u, eu);
				Set<Role> roles = u.getRoles();
				if(roles!=null && !roles.isEmpty()){
					String roleIds = "";
					String roleNames="";
					boolean b = false;
					for(Role r:roles){
						if(b){
							roleIds += ",";
							roleNames += ",";
						}else{
							b = true;
						}
						roleIds += r.getId();
						roleNames += r.getName();
					}
					eu.setRoleIds(roleIds);
					eu.setRoleNames(roleNames);
				}
				el.add(eu);
			}
		}
		dataGrid.setRows(el);
		dataGrid.setTotal(userDao.count("select count(*) "+hql+ whereHql(euser, params),params));
		return dataGrid;
	}

	private String orderHql(PageHelper ph) {
		String orderString="";
		if(ph.getSort()!=null && ph.getOrder()!=null){
			orderString=" order by u."+ph.getSort()+" "+ph.getOrder();
		}
		return orderString;
	}

	private String whereHql(Euser euser, Map<String, Object> params) {
		String hql="";
		if(euser!=null){
			hql+=" where 1=1 ";
			if(euser.getName()!=null){
				hql += " and u.name like :name ";
				params.put("name", "%%"+euser.getName()+"%%");
			}
			if(euser.getCreatedatetimeStart()!=null){
				hql += " and u.createdatetime >= :createdatetimeStart";
				params.put("createdatetimeStart", euser.getCreatedatetimeStart());
			}
			if(euser.getCreatedatetimeEnd()!=null){
				hql += " and u.createdatetime <= :createdatetimeEnd ";
				params.put("createdatetimeEnd", euser.getCreatedatetimeEnd());
			}
			if(euser.getModifydatetimeStart()!=null){
				hql += " and u.modifydatetime >= :modifydatetimeStart ";
				params.put("modifydatetimeStart", euser.getModifydatetimeStart());
			}
			if(euser.getModifydatetimeEnd()!=null){
				hql += " and u.modifydatetime <= :modifydatetimeEnd ";
				params.put("modifydatetimeEnd", euser.getModifydatetimeEnd());
			}
			
		}
		return hql;
	}

	@Override
	public Euser get(String id) {

		Map<String,Object> params = new HashMap<String, Object>();
		params.put("id", id);
		User u = userDao.get("select distinct u from User u " +
				"left join fetch u.roles role where u.id = :id", params);
		Euser eu = new Euser();
		BeanUtils.copyProperties(u, eu);
		if(u.getRoles()!=null && !u.getRoles().isEmpty()){
			String roleIds = "";
			String roleNames="";
			boolean b = false;
			for(Role role:u.getRoles()){
				if(b){
					roleIds +=",";
					roleNames +=",";
				}else{
					b= true;
				}
				roleIds += role.getId();
				roleNames += role.getName();
			}
			eu.setRoleIds(roleIds);
			eu.setRoleNames(roleNames);
		}
		return eu;
	}

	@Override
	synchronized public void edit(Euser euser) throws Exception {

		Map<String,Object> params = new HashMap<String, Object>();
		params.put("id", euser.getId());
		params.put("name", euser.getName());
		if(userDao.count("select count(*) from User u where u.name = :name and u.id != :id ", params)>0){
			throw new Exception("登录名已存在！");
		}else{
			User u = userDao.get(User.class, euser.getId());
			BeanUtils.copyProperties(euser, u,new String[]{"pwd","createdatetime"});
			u.setModifydatetime(new Date());
			userDao.save(u);
		}
		
	}

	@Override
	public void delete(String id) {
		userDao.delete(userDao.get(User.class, id));
	}

	@Override
	public void add(Euser euser) throws Exception {

		Map<String,Object> params = new HashMap<String, Object>();
		params.put("name", euser.getName());
		if(userDao.count("select count(*) from User u where u.name = :name ", params)>0){
			throw new Exception("登录名已存在");
		}else{
			User u = new User();
			BeanUtils.copyProperties(euser, u);
			u.setCreatedatetime(new Date());
			u.setPwd(MD5Util.md5(euser.getPwd()));
			userDao.saveOrUpdate(u);
		}
	}

	@Override
	public void editPwd(Euser euser) {
		if(euser!=null && euser.getPwd() !=null && !euser.getPwd().trim().equalsIgnoreCase("")){
			User u = userDao.get(User.class, euser.getId());
			u.setPwd(MD5Util.md5(euser.getPwd()));
			u.setModifydatetime(new Date());
			userDao.save(u);
		}
		
	}

	@Override
	public void grant(String ids, Euser euser) {

		if(ids !=null && ids.length()>0){
			List<Role> roles = new ArrayList<Role>();
			if(euser.getRoleIds()!=null){
				for(String roleId:euser.getRoleIds().split(",")){
					roles.add(roleDao.get(Role.class, roleId));
				}
			}
			for(String id:ids.split(",")){
				if(id!=null && !id.equalsIgnoreCase("")){
					User u = userDao.get(User.class, id);
					u.setRoles(new HashSet<Role>(roles));
					userDao.saveOrUpdate(u);
				}
			}
		}
	}

}
