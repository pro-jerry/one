package one.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="role")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Role implements Serializable{

	private String id;
	private Role role;
	private String name;
	private String remark;
	private Integer seq;
	private Set<Role> roles = new HashSet<Role>();
	private Set<Privilege> privileges = new HashSet<Privilege>();
	private Set<User> users = new HashSet<User>();
	
	public Role() {

	}

	public Role(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Role(String id, Role role, String name, String remark, Integer seq,
			Set<Role> roles, Set<Privilege> privileges, Set<User> users) {
		super();
		this.id = id;
		this.role = role;
		this.name = name;
		this.remark = remark;
		this.seq = seq;
		this.roles = roles;
		this.privileges = privileges;
		this.users = users;
	}

	@Id
	@Column(name="ID",nullable=false,length=135)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PID")
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name="NAME",nullable=false,length=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="REMARK",length=200)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name="SEQ")
	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	@OneToMany(fetch=FetchType.LAZY,mappedBy="role")
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="role_privilege",joinColumns={@JoinColumn(name="ROLE_ID",nullable=false,updatable=false)},
			inverseJoinColumns={@JoinColumn(name="PRIVILEGE_ID",nullable=false,updatable=false)})
	public Set<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Set<Privilege> privileges) {
		this.privileges = privileges;
	}

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="user_role",joinColumns={@JoinColumn(name="ROLE_ID",nullable=false,updatable=false)},
	inverseJoinColumns={@JoinColumn(name="USER_ID",nullable=false,updatable=false)})
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
	
	
	
}
