package one.dao.impl;

import one.dao.ResourceTypeDao;
import one.model.ResourceType;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public class ResourceTypeDaoImpl extends BaseDaoImpl<ResourceType> implements ResourceTypeDao{

	@Override
	@Cacheable(value="resourceTypeDaoCache",key="#id")
	public ResourceType getById(String id) {

		return super.get(ResourceType.class, id);
	}

}
