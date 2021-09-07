package com.wetark.main.model.user.role;

import com.wetark.main.model.Base;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import static com.wetark.main.helper.UUIDGenerator.generatorName;

@Entity
@Table(name = "roles")
public class Role extends Base {
	@Id
	@GeneratedValue(generator = generatorName)
	@GenericGenerator(name = generatorName, strategy = "uuid")
	private String id;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private ERole name;

	public Role() {

	}

	public Role(ERole name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ERole getName() {
		return name;
	}

	public void setName(ERole name) {
		this.name = name;
	}
}