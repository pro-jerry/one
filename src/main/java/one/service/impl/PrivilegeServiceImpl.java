package one.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import one.dao.PrivilegeDao;
import one.dao.ResourceTypeDao;
import one.dao.UserDao;
import one.model.Privilege;
import one.model.Role;
import one.model.User;
import one.pagemodel.Eprivilege;
import one.pagemodel.SessionInfo;
import one.pagemodel.Tree;
import one.service.PrivilegeService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@SuppressWarnings("unused")
@Service
public class PrivilegeServiceImpl implements PrivilegeService{

	@Autowired
	private PrivilegeDao privilegeDao;
	
	@Autowired
	private ResourceTypeDao resourceTypeDao;
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public List<Tree> tree(SessionInfo sessionInfo) {

		List<Privilege> l=null;
		List<Tree> lt = new ArrayList<Tree>();
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("resourceTypeId", "0");//菜单类型的资源
		if(sessionInfo!=null){
			params.put("userId", sessionInfo.getId());//查看自己有权限的资源
			l=privilegeDao.find("select distinct p from Privilege p " +
					"join fetch p.resourceType type join fetch p.roles role join role.users user " +
					"where type.id = :resourceTypeId and user.id = :userId order by p.seq", params);
		}else{
			
			l=privilegeDao.find("select distinct p from Privilege p join fetch p.resourceType type " +
					" where type.id = :resourceTypeId order by p.seq ", params);
		}
		
		if(l!=null&&l.size()>0){
			for(Privilege p:l){
				Tree tree = new Tree();
				BeanUtils.copyProperties(p, tree);
				if(p.getPrivilege()!=null){
					tree.setPid(p.getPrivilege().getId());
				}
				tree.setText(p.getName());
				tree.setIconCls(p.getIcon());
				Map<String, Object> attr = new HashMap<String, Object>();
				attr.put("url", p.getUrl());
				tree.setAttributes(attr);
				lt.add(tree);
			}
		}
		return lt;
	}


	@Override
	public List<Tree> allTree(SessionInfo sessionInfo) {

		List<Privilege> l=null;
		List<Tree> lt = new ArrayList<Tree>();
		
		Map<String,Object> params = new HashMap<String, Object>();
		if(sessionInfo!=null){
			params.put("userId", sessionInfo.getId());//查看自己有权限的资源
			l=privilegeDao.find("select distinct p from Privilege p " +
					"join fetch p.resourceType type join fetch p.roles role " +
					"join role.users user where user.id = :userId order by p.seq ", params);
		}else{
			
			l = privilegeDao.find("select distinct p from Privilege p " +
					"join fetch p.resourceType type order by p.seq ", params);
		}
		
		if(l!=null && l.size()>0){
			for(Privilege p:l){
				Tree tree = new Tree();
				BeanUtils.copyProperties(p, tree);
				if(p.getPrivilege()!=null){
					tree.setPid(p.getPrivilege().getId());
				}
				tree.setText(p.getName());
				tree.setIconCls(p.getIcon());
				Map<String,Object> attr = new HashMap<String, Object>();
				attr.put("url", p.getUrl());
				tree.setAttributes(attr);
				lt.add(tree);
			}
		}
		return lt;
	}


	@Override
	public List<Eprivilege> treeGrid(SessionInfo sessionInfo) {
		List<Privilege> l = null;
		List<Eprivilege> lr = new ArrayList<Eprivilege>();
		
		Map<String,Object> params = new HashMap<String, Object>();
		if(sessionInfo!=null){
			params.put("userId", sessionInfo.getId());//自查自己有权限的资源
			l=privilegeDao.find("select distinct t from Privilege t " +
					"join fetch t.resourceType type " +
					"join fetch t.roles role join role.users user " +
					"where user.id = :userId order by t.seq", params);
		}else{
			l = privilegeDao.find("select distinct t from Privilege t " +
					"join fetch t.resourcetype type order by t.seq", params);
		}
		if(l!=null && l.size()>0){
			for(Privilege t:l){
				Eprivilege r = new Eprivilege();
				BeanUtils.copyProperties(t, r);
				if(t.getPrivilege()!=null){
					r.setPid(t.getPrivilege().getId());
					r.setPname(t.getPrivilege().getName());
				}
				r.setTypeId(t.getResourceType().getId());
				r.setTypeName(t.getResourceType().getName());
				if(t.getIcon()!=null && !t.getIcon().equalsIgnoreCase("")){
					r.setIconCls(t.getIcon());
				}
				lr.add(r);
			}
		}
		return lr;
	}


	@Override
	public void add(Eprivilege eprivilege, SessionInfo sessionInfo) {
		
		Privilege t = new Privilege();
		BeanUtils.copyProperties(eprivilege, t);
		if(eprivilege.getPid()!=null && !eprivilege.getPid().equalsIgnoreCase("")){
			t.setPrivilege(privilegeDao.get(Privilege.class, eprivilege.getPid()));
		}
		if(eprivilege.getTypeId()!=null && !eprivilege.getTypeId().equalsIgnoreCase("")){
			t.setResourceType(resourceTypeDao.getById(eprivilege.getTypeId()));
		}
		if(eprivilege.getIconCls()!=null && !eprivilege.getIconCls().equalsIgnoreCase("")){
			t.setIcon(eprivilege.getIconCls());
		}
		privilegeDao.saveOrUpdate(t);
		
		// 由于当前用户所属的角色，没有访问新添加的资源权限，所以在新添加资源的时候，将当前资源授权给当前用户的所有角色，以便添加资源后在资源列表中能够找到
		User user = userDao.get(User.class, sessionInfo.getId());
		Set<Role> roles = user.getRoles();
		for(Role r:roles){
			r.getPrivileges().add(t);
		}
		userDao.saveOrUpdate(user);
	}


	@Override
	public Eprivilege get(String id) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("id", id);
		Privilege t = privilegeDao.get("from Privilege t left join fetch t.privilege resource " +
				"left join fetch t.resourceType resourceType where t.id = :id", params);
		Eprivilege r = new Eprivilege();
		BeanUtils.copyProperties(t, r);
		if(t.getPrivilege()!=null){
			r.setPid(t.getPrivilege().getId());
			r.setName(t.getPrivilege().getName());
		}
		r.setTypeId(t.getResourceType().getId());
		r.setTypeName(t.getResourceType().getName());
		if(t.getIcon()!=null && !t.getIcon().equalsIgnoreCase("")){
			r.setIconCls(t.getIcon());
		}
		return r;
	}


	@Override
	public void edit(Eprivilege eprivilege) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", eprivilege.getId());
		Privilege t = privilegeDao.get("select distinct t from Privilege t where t.id = :id", params);
		if(t!=null){
			BeanUtils.copyProperties(eprivilege, t);
			if (eprivilege.getTypeId() != null && !eprivilege.getTypeId().equalsIgnoreCase("")) {
				t.setResourceType(resourceTypeDao.getById(eprivilege.getTypeId()));// 赋值资源类型
			}
			if (eprivilege.getIconCls() != null && !eprivilege.getIconCls().equalsIgnoreCase("")) {
				t.setIcon(eprivilege.getIconCls());
			}
			if (eprivilege.getPid() != null && !eprivilege.getPid().equalsIgnoreCase("")) {// 说明前台选中了上级资源
				Privilege pt = privilegeDao.get(Privilege.class, eprivilege.getPid());
				isChildren(t, pt);// 说明要将当前资源修改到当前资源的子/孙子资源下
				t.setPrivilege(pt);
			} else {
				t.setPrivilege(null);// 前台没有选中上级资源，所以就置空
			}
			privilegeDao.saveOrUpdate(t);
		}
	}


	/**
	 * 判断是否是将当前节点修改到当前子节点
	 * @param t 当前节点
	 * @param pt 要修改到的节点
	 * @return
	 */
	private boolean isChildren(Privilege t, Privilege pt) {

		if (pt != null && pt.getPrivilege() != null) {
			if (pt.getPrivilege().getId().equalsIgnoreCase(t.getId())) {
				pt.setPrivilege(null);
				return true;
			} else {
				return isChildren(t, pt.getPrivilege());
			}
		}
		return false;
	}


	@Override
	public void delete(String id) {

		Privilege t = privilegeDao.get(Privilege.class, id);
		del(t);
	}


	private void del(Privilege t) {

		if(t.getPrivileges()!=null && t.getPrivileges().size()>0){
			for(Privilege r:t.getPrivileges()){
				del(r);
			}
		}
		privilegeDao.delete(t);
	}

}
