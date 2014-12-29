package one.dao;

import one.model.ResourceType;

/**
 * 资源类型操作库
 * @author Administrator
 *
 */
public interface ResourceTypeDao extends BaseDao<ResourceType>{

	/**
	 * 通过ID获取资源类型
	 * @param id
	 * @return
	 */
	public ResourceType getById(String id);
}
