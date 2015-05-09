package models;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Id;

import at.ac.tuwien.big.we15.lab2.api.Avatar;
import at.ac.tuwien.big.we15.lab2.api.User;

import play.data.validation.*;

@Entity
@Access(AccessType.FIELD)
public class UserModel implements User{
	@Id
	private long id;
	
	@Constraints.MinLength(4)
	@Constraints.MaxLength(8)
	@Constraints.Required
	private String name;
	
	@Constraints.MinLength(4)
	@Constraints.MaxLength(8)
	@Constraints.Required
	private String password;
	private String avatarId;
	
	private String firstName;
	private String lastName;
	private Date dateOfBirth;
	private Gender gender;
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Avatar getAvatar() {
		return Avatar.getAvatar(avatarId);
	}

	@Override
	public void setAvatar(Avatar avatar) {
		avatarId = avatar.getId();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
}
