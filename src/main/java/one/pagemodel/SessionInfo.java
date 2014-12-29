package one.pagemodel;

import java.io.Serializable;
import java.util.List;

public class SessionInfo implements Serializable{

	private String id;//用户ID
	private String name;//用户登陆名
	private String ip;//用户IP
	private List<String> privilegeList;//用户可以访问的资源列表
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public List<String> getPrivilegeList() {
		return privilegeList;
	}
	public void setPrivilegeList(List<String> privilegeList) {
		this.privilegeList = privilegeList;
	}
	
	
	
}
