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
@Table(name="privilege")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Privilege implements Serializable{

	private String id;
	private String url;
	private String name;
	private String discription;
	private ResourceType resourceType;
	private Integer seq;
	private Privilege privilege;
	private Set<Role> roles = new HashSet<Role>();
	private Set<Privilege> privileges = new HashSet<Privilege>();
	private String icon;
	
	public Privilege(String id, ResourceType resourceType,String url, String name, String discription,
			Integer seq, Privilege privilege,String icon) {
		this.id = id;
		this.url = url;
		this.name = name;
		this.discription = discription;
		this.seq = seq;
		this.privilege = privilege;
		this.resourceType=resourceType;
		this.icon = icon;
	}
	
	@Column(name="ICON",length=100)
	public String getIcon() {
		return icon;
	}


	public void setIcon(String icon) {
		this.icon = icon;
	}


	@ManyToMany
	@JoinTable(name="role_privilege",
	joinColumns={@JoinColumn(name="PRIVILEGE_ID",nullable=false,updatable=false)},
	inverseJoinColumns={@JoinColumn(name="ROLE_ID",nullable=false,updatable=false)})
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="RESOURCETYPE_ID")
	public ResourceType getResourceType() {
		return this.resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	@Column(name="DISCRIPTION")
	public String getDiscription() {
		return discription;
	}

	public void setDiscription(String discription) {
		this.discription = discription;
	}

	
	@Column(name="SEQ")
	public Integer getSeq() {
		return seq;
	}


	public void setSeq(Integer seq) {
		this.seq = seq;
	}


	public Privilege() {

	}

	@Id
	@Column(name="ID",nullable=false,length=36)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name="URL",length=200)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Column(name="NAME",nullable=false,length=100)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne
	@JoinColumn(name="PID")
	public Privilege getPrivilege() {
		return privilege;
	}
	public void setPrivilege(Privilege privilege) {
		this.privilege = privilege;
	}


	@OneToMany(fetch=FetchType.LAZY,mappedBy="privilege")
	public Set<Privilege> getPrivileges() {
		return privileges;
	}
	public void setPrivileges(Set<Privilege> privileges) {
		this.privileges = privileges;
	}
	
	
}
