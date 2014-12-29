package one.service.impl;

import java.util.ArrayList;
import java.util.List;

import one.dao.ResourceTypeDao;
import one.model.ResourceType;
import one.pagemodel.EresourceType;
import one.service.ResourceTypeService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ResourceTypeServiceImpl implements ResourceTypeService{

	@Autowired
	private ResourceTypeDao resourceTypeDao;
	
	@Cacheable(value="resourceTypeServiceCache",key="'resourceTypeList'")
	@Override
	public List<EresourceType> getResourceTypeList() {

		List<ResourceType> l = resourceTypeDao.find("from ResourceType t");
		List<EresourceType> rl = new ArrayList<EresourceType>();
		if(l!=null && l.size()>0){
			for(ResourceType t : l){
				EresourceType rt= new EresourceType();
				BeanUtils.copyProperties(t, rt);
				rl.add(rt);
			}
		}
		return rl;
	}

}
