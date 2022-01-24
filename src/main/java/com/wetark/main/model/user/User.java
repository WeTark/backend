package com.wetark.main.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wetark.main.model.Base;
import com.wetark.main.model.event.Event;
import com.wetark.main.model.matchedTrade.MatchedTrade;
import com.wetark.main.model.user.balance.Balance;
import com.wetark.main.model.user.notification.Notification;
import com.wetark.main.model.user.notification.NotificationType;
import com.wetark.main.model.user.order.Order;
import com.wetark.main.model.user.role.Role;
import lombok.Builder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static com.wetark.main.helper.UUIDGenerator.generatorName;

@Entity
@Table(	name = "users", 
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "username"),
			@UniqueConstraint(columnNames = "email") 
		})
public class User extends Base {
	@Id
	@GeneratedValue(generator = generatorName)
	@GenericGenerator(name = generatorName, strategy = "uuid")
	private String id;

	@Size(max = 50)
	private String name;

	@Size(max = 50)
	private String username;

	@Size(max = 50)
	private String phoneNo;

	@Size(max = 50)
	@Email
	private String email;

	private String rocketChatToken;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Size(max = 120)
	private String password;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Balance balance = new Balance();

	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Order> order = new HashSet<>();

	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Notification> notifications = new HashSet<>();

	public User() {

	}

	public User(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getRocketChatToken() {
		return rocketChatToken;
	}

	public void setRocketChatToken(String rocketChatToken) {
		this.rocketChatToken = rocketChatToken;
	}

	public Set<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(Set<Notification> notifications) {
		this.notifications = notifications;
	}

	public Set<Order> getOrder() {
		return order;
	}

	public void setOrder(Set<Order> order) {
		this.order = order;
	}

	public Balance getBalance() {
		return balance;
	}

	public void setBalance(Balance balance) {
		this.balance = balance;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

}
