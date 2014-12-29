package one.service;

import java.util.List;

import one.pagemodel.Erole;
import one.pagemodel.SessionInfo;
import one.pagemodel.Tree;

/**
 * 角色业务逻辑
 * @author Administrator
 *
 */
public interface RoleService {

	/**
	 * 获得角色树(只能看到自己拥有的角色)
	 * @param sessionInfo
	 * @return
	 */
	public List<Tree> tree(SessionInfo sessionInfo);
	
	/**
	 * 获得角色树
	 * @return
	 */
	public List<Tree> allTree();
	
	/**
	 * 获得角色treeGrid
	 * @param sessionInfo
	 * @return
	 */
	public List<Erole> treeGrid(SessionInfo sessionInfo);
	
	/**
	 * 保存角色
	 * @param erole
	 * @param sessionInfo
	 */
	public void add(Erole erole,SessionInfo sessionInfo);
	
	/**
	 * 删除角色
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 获得角色
	 * @param id
	 * @return
	 */
	public Erole get(String id);
	
	/**
	 * 编辑角色
	 * @param erole
	 */
	public void edit(Erole erole);
	
	/**
	 * 为角色授权
	 */
	public void grant(Erole erole);
}
