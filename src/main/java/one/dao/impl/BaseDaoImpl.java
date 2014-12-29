package one.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import one.dao.BaseDao;

import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unchecked")
@Repository
public class BaseDaoImpl<T> implements BaseDao<T>{

	@Autowired
	private SessionFactory sessionFactory;
	
	public Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}


	@Override
	public List<T> get(String hql) {
		Query q =  this.getCurrentSession().createQuery(hql);
		return q.list();
	}


	@Override
	public void save(T t) {

		if(t!=null){
		this.getCurrentSession().save(t);
		this.getCurrentSession().flush();
		}
	}


	@Override
	public void saveOrUpdate(T t) {

		this.getCurrentSession().saveOrUpdate(t);
		this.getCurrentSession().flush();
	}


	@Override
	public T get(String hql, Map<String, Object> params) {
		Query q = this.getCurrentSession().createQuery(hql);
		if(params!=null&&!params.isEmpty()){
			for(String key:params.keySet()){
				q.setParameter(key, params.get(key));
			}
		}
		List<T> l =q.list();
		if(l!=null && l.size()>0){
			return l.get(0);
		}
		return null;
	}


	@Override
	public T get(Class<T> c, Serializable id) {

		return (T) this.getCurrentSession().get(c, id);
	}


	@Override
	public List<T> find(String hql, Map<String, Object> params) {
		Query q= this.getCurrentSession().createQuery(hql);
		if(params!=null && !params.isEmpty()){
			for(String key:params.keySet()){
				q.setParameter(key, params.get(key));
			}
		}
		return  q.list();
		
	}


	@Override
	public List<T> find(String hql, Map<String, Object> params, int page,
			int rows) {
		Query q = this.getCurrentSession().createQuery(hql);
		if(params!=null && !params.isEmpty()){
			for(String key:params.keySet()){
				q.setParameter(key, params.get(key));
			}
		}
		return q.setFirstResult((page-1)*rows).setMaxResults(rows).list();
	}


	@Override
	public Long count(String hql) {

		Query q= this.getCurrentSession().createQuery(hql);
		return (Long) q.uniqueResult();
	}


	@Override
	public Long count(String hql, Map<String, Object> params) {
		Query q= this.getCurrentSession().createQuery(hql);
		if(params!=null && !params.isEmpty()){
			for(String key:params.keySet()){
				q.setParameter(key, params.get(key));
			}
		}
		return (Long) q.uniqueResult();
	}


	@Override
	public void delete(T o) {

		if(o!=null){
			this.getCurrentSession().delete(o);
			this.getCurrentSession().flush();
		}
	}


	@Override
	public List<T> find(String hql) {

		Query q = this.getCurrentSession().createQuery(hql);
		return q.list();
	}

	
}
