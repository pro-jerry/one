package one.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseDao<T> {

	/**
	 * 通过HQL获取一个对象
	 * @param hql
	 * @return
	 */
	public List<T> get(String hql);
	
	/**
	 * 保存一个对象
	 * @param t
	 */
	public void save(T t);
	
	/**
	 * 保存或更新一个对象
	 * @param t
	 */
	public void saveOrUpdate(T t);
	
	/**
	 * 通过HQL语句获取一个对象
	 * @param hql
	 * @param params
	 * @return
	 */
	public T get(String hql,Map<String,Object> params);
	
	/**
	 * 通过主键获取对象
	 * @param c
	 * @param id
	 * @return
	 */
	public T get(Class<T> c,Serializable id);
	
	/**
	 * 获得对象列表
	 * @param hql HQL语句
	 * @param params 参数
	 * @return
	 */
	public List<T> find(String hql,Map<String,Object>params);
	
	/**
	 * 获得分页后的对象列表
	 * @param hql	HQL语句
	 * @param params 参数
	 * @param page 要显示第几页
	 * @param rows 每页显示多少条
	 * @return
	 */
	public List<T> find(String hql,Map<String,Object> params,int page,int rows);
	
	/**
	 * 统计数目
	 * @param hql (select count(*) from T )
	 * @return
	 */
	public Long count(String hql);
	
	/**
	 * 统计数目
	 * @param hql (select count(*) from T where xx =:xx)
	 * @param params
	 * @return
	 */
	public Long count(String hql, Map<String,Object> params);
	
	/**
	 * 删除一个对象
	 * @param o
	 */
	public void delete(T o);
	
	/**
	 * 获得对象列表
	 * @param hql
	 * @return
	 */
	public List<T> find(String hql);
}
