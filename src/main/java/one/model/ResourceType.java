package one.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@SuppressWarnings("serial")
@Entity
@Table(name="resourceType")
@DynamicInsert(true)
@DynamicUpdate(true)
public class ResourceType implements Serializable{

	private String id;
	private String name;
	private Set<Privilege> privileges = new HashSet<Privilege>();
	
	public ResourceType() {

	}
	public ResourceType(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public ResourceType(String id, String name, Set<Privilege> privileges) {
		this.id = id;
		this.name = name;
		this.privileges = privileges;
	}
	
	
	@Id
	@Column(name="ID",nullable=false,length=36)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name="NAME",nullable=false,length=100)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@OneToMany(fetch=FetchType.LAZY,mappedBy="resourceType")
	public Set<Privilege> getPrivileges() {
		return privileges;
	}
	public void setPrivileges(Set<Privilege> privileges) {
		this.privileges = privileges;
	}
	
	
}
