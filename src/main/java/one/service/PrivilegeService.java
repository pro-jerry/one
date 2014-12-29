package one.service;

import java.util.List;

import one.model.Privilege;
import one.pagemodel.Eprivilege;
import one.pagemodel.SessionInfo;
import one.pagemodel.Tree;


public interface PrivilegeService {

	/**
	 * 获得资源树(资源类型为菜单类型)
	 * 通过用户ID判断，他能看到的资源类型
	 * @param sessionInfo
	 * @return
	 */
	public List<Tree> tree(SessionInfo sessionInfo);
	
	/**
	 * 获得资源树(包括所有的资源类型)
	 * 通过用户ID判断，他能看到的资源类型
	 * @param sessionInfo
	 * @return
	 */
	public List<Tree> allTree(SessionInfo sessionInfo);
	
	/**
	 * 获得资源列表
	 * @param sessionInfo
	 * @return
	 */
	public List<Eprivilege> treeGrid(SessionInfo sessionInfo);
	
	/**
	 * 添加资源
	 * @param eprivilege
	 * @param sessionInfo
	 */
	public void add(Eprivilege eprivilege,SessionInfo sessionInfo);
	
	/**
	 * 获得一个资源
	 * @param id
	 * @return
	 */
	public Eprivilege get(String id);
	
	/**
	 * 修改资源
	 * @param eprivilege
	 */
	public void edit(Eprivilege eprivilege);
	
	/**
	 * 删除资源
	 * @param id
	 */
	public void delete(String id);
	
	
}
