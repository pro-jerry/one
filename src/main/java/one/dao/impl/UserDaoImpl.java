package one.dao.impl;

import one.dao.UserDao;
import one.model.User;

import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao{

}
