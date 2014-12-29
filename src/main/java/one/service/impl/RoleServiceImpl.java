package one.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.dao.PrivilegeDao;
import one.dao.RoleDao;
import one.dao.UserDao;
import one.model.Privilege;
import one.model.Role;
import one.model.User;
import one.pagemodel.Erole;
import one.pagemodel.SessionInfo;
import one.pagemodel.Tree;
import one.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PrivilegeDao privilegeDao;
	
	@Override
	public List<Tree> tree(SessionInfo sessionInfo) {
		List<Role> l=null;
		List<Tree> lt = new ArrayList<Tree>();
		Map<String,Object> params = new HashMap<String, Object>();
		if(sessionInfo!=null){
			params.put("userId", sessionInfo.getId());//查看自己有权限的角色
			l=roleDao.find(" select distinct r from Role r " +
					"join fetch r.users user where user.id = :userId order by r.seq ", params);
		}else{
			l=roleDao.find(" from Role r order by r.seq ");
		}
		
		if(l!=null && l.size()>0){
			for(Role r:l){
				Tree tree= new Tree();
				BeanUtils.copyProperties(r, tree);
				tree.setText(r.getName());
				tree.setIconCls("status_online");
				if(r.getRole()!=null){
					tree.setPid(r.getRole().getId());
				}
				lt.add(tree);
			}
		}
		return lt;
	}

	@Override
	public List<Tree> allTree() {

		return this.tree(null);
	}

	@Override
	public List<Erole> treeGrid(SessionInfo sessionInfo) {

		List<Erole> el = new ArrayList<Erole>();
		List<Role> rl=null;
		Map<String,Object> params = new HashMap<String, Object>();
		if(sessionInfo!=null){
			//查看自己有权限的角色
			params.put("userId", sessionInfo.getId());
			rl = roleDao.find(" select distinct r from Role r " +
					"left join fetch r.privileges privilege " +
					"join fetch r.users user where user.id = :userId order by r.seq",params);
			
		}else{
			rl = roleDao.find(" select distinct r from Role r " +
					"left join fetch r.privileges prililege order by r.seq");
		}
		
		if(rl!=null && rl.size()>0){
			for(Role r:rl){
				Erole er = new Erole();
				BeanUtils.copyProperties(r, er);
				er.setIconCls("status_online");
				if(r.getRole()!=null){
					er.setPid(r.getRole().getId());
					er.setPname(r.getRole().getName());
				}
				Set<Privilege> s = r.getPrivileges();
				if(s!=null && !s.isEmpty()){
					boolean b = false;
					String ids="";
					String names="";
					for(Privilege p:s){
						if(b){
							ids+=",";
							names+=",";
						}else{
							b=true;
						}
						ids+=p.getId();
						names += p.getName();
					}
					er.setPrivilegeIds(ids);
					er.setPrivilegeNames(names);
					
				}
				el.add(er);
			}
		}
		return el;
	}

	@Override
	public void add(Erole erole, SessionInfo sessionInfo) {

		Role r = new Role();
		BeanUtils.copyProperties(erole, r);
		if(erole.getPid()!=null && !erole.getPid().equalsIgnoreCase("")){
			r.setRole(roleDao.get(Role.class, erole.getPid()));
		}
		roleDao.save(r);
		//刚刚添加的角色，赋予给当前用户
		User user = userDao.get(User.class, sessionInfo.getId());
		user.getRoles().add(r);
		userDao.saveOrUpdate(user);
	}

	@Override
	public void delete(String id) {

		Role r = roleDao.get(Role.class, id);
		del(r);
	}

	private void del(Role r) {
		if(r.getRoles()!=null && r.getRoles().size()>0){
			for(Role o:r.getRoles()){
				del(o);
			}
		}
		roleDao.delete(r);
	}

	@Override
	public Erole get(String id) {

		Erole e = new Erole();
		Map<String, Object>params = new HashMap<String, Object>();
		params.put("id", id);
		Role r = roleDao.get("select distinct r from Role r " +
				"left join fetch r.privileges privilege where r.id = :id", params);
		if(r!=null){
			BeanUtils.copyProperties(r, e);
			if(r.getRole()!=null){
				e.setPid(r.getRole().getId());
				e.setPname(r.getRole().getName());
			}
			Set<Privilege> s = r.getPrivileges();
			if(s!=null && !s.isEmpty()){
				boolean b=false;
				String ids="";
				String names="";
				for(Privilege p:s){
					if(b){
						ids+=",";
						names +=",";
					}else{
						b=true;
					}
					ids += p.getId();
					names += p.getName();
				}
				e.setPrivilegeIds(ids);
				e.setPrivilegeNames(names);
			}
		}
		return e;
	}

	@Override
	public void edit(Erole erole) {

		Role t = roleDao.get(Role.class, erole.getId());
		if(t!=null){
			BeanUtils.copyProperties(erole, t);
			if (erole.getPid() != null && !erole.getPid().equalsIgnoreCase("")) {
				t.setRole(roleDao.get(Role.class, erole.getPid()));
			}
			if (erole.getPid() != null && !erole.getPid().equalsIgnoreCase("")){
				//说明前台选中了上级资源
				Role pt = roleDao.get(Role.class, erole.getPid());
				isChildren(t, pt);// 说明要将当前资源修改到当前资源的子/孙子资源下
				t.setRole(pt);
				roleDao.saveOrUpdate(t);
			}else{
				t.setRole(null);// 前台没有选中上级资源，所以就置空
			} 
		}
	}

	/**
	 * 判断是否是将当前节点修改到当前子节点
	 * @param t	当前节点
	 * @param pt 要修改到的节点
	 */
	private boolean isChildren(Role t, Role pt) {
		
		if (pt != null && pt.getRole() != null) {
			if (pt.getRole().getId().equalsIgnoreCase(t.getId())) {
				pt.setRole(null);
				return true;
			}else{
				
				return isChildren(t, pt.getRole());
			}
		}
		return false;
	}

	@Override
	public void grant(Erole erole) {

		Role t = roleDao.get(Role.class, erole.getId());
		if(erole.getPrivilegeIds()!=null && !erole.getPrivilegeIds().equalsIgnoreCase("")){
			String ids="";
			boolean b = false;
			for(String id:erole.getPrivilegeIds().split(",")){
				if(b){
					ids += ",";
				}else{
					b =true;
				}
				ids += "'"+id+"'";
			}
			t.setPrivileges(new HashSet<Privilege>(privilegeDao.find(" select distinct t from Privilege t " +
					"	where t.id in ("+ ids +")")));
			roleDao.saveOrUpdate(t);
		}else{
			t.setPrivileges(null);
		}
	}

}
