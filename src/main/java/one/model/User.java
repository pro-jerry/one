package one.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="user")
@DynamicInsert(true)
@DynamicUpdate(true)
public class User implements Serializable{

	private String id;
	private Date createdatetime;
	private Date modifydatetime;
	
	private String name;
	private String pwd;
	private Set<Role> roles = new HashSet<Role>();
	
	public User() {

	}

	public User(String id, String name, String pwd) {
		super();
		this.id = id;
		this.name = name;
		this.pwd = pwd;
	}

	public User(String id, Date createdatetime, Date modifydatetime,
			String name, String pwd, Set<Role> roles) {
		super();
		this.id = id;
		this.createdatetime = createdatetime;
		this.modifydatetime = modifydatetime;
		this.name = name;
		this.pwd = pwd;
		this.roles = roles;
	}

	@Id
	@Column(name="ID",nullable=false,length=36)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATEDDATETIME",length=19)
	public Date getCreatedatetime() {
		return createdatetime;
	}

	public void setCreatedatetime(Date createdatetime) {
		this.createdatetime = createdatetime;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFYDATETIME",length=19)
	public Date getModifydatetime() {
		return modifydatetime;
	}

	public void setModifydatetime(Date modifydatetime) {
		this.modifydatetime = modifydatetime;
	}
	@Column(name="NAME",unique=true,nullable=false,length=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(name="PWD",nullable=false,length=100)
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="user_role",joinColumns={@JoinColumn(name="USER_ID",nullable=false,updatable=false)},
		inverseJoinColumns={@JoinColumn(name="ROLE_ID",nullable=false,updatable=false)})
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	
	
}
