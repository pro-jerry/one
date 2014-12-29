package one.service;

import java.util.List;

import one.model.User;
import one.pagemodel.DataGrid;
import one.pagemodel.Euser;
import one.pagemodel.PageHelper;

public interface UserService {

	/**
	 * 用户登陆
	 * @param euser 包含用户名和密码
	 * @return
	 */
	public Euser login(Euser euser);
	
	/**
	 * 获得用户能访问的资源地址
	 * @param id
	 * @return
	 */
	public List<String> privilegeList(String id);
	
	/**
	 * 获得用户数据表格
	 * @param user
	 * @param ph
	 * @return
	 */
	public DataGrid dataGrid(Euser euser,PageHelper ph);
	
	/**
	 * 获得用户对象
	 * @param id
	 * @return
	 */
	public Euser get(String id);
	
	/**
	 * 编辑用户
	 * @param euser
	 * @throws Exception
	 */
	public void edit(Euser euser) throws Exception;
	
	/**
	 * 删除用户
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 添加用户
	 * @param euser
	 * @throws Exception
	 */
	public void add(Euser euser) throws Exception;
	
	/**
	 * 编辑用户密码
	 * @param euser
	 */
	public void editPwd(Euser euser);
	
	/**
	 * 用户授权
	 * @param ids
	 * @param euser
	 * 				需要euser.roleIds的属性值
	 */
	public void grant(String ids,Euser euser);
}
