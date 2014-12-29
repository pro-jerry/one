package one.pagemodel;

import java.io.Serializable;

public class Erole implements Serializable{

	private String id;
	private String pid;
	private String pname;
	private String name;
	private String remark;
	private Integer seq;
	private String iconCls;
	
	private String privilegeIds;
	private String privilegeNames;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getPrivilegeIds() {
		return privilegeIds;
	}
	public void setPrivilegeIds(String privilegeIds) {
		this.privilegeIds = privilegeIds;
	}
	public String getPrivilegeNames() {
		return privilegeNames;
	}
	public void setPrivilegeNames(String privilegeNames) {
		this.privilegeNames = privilegeNames;
	}
	
	
}
