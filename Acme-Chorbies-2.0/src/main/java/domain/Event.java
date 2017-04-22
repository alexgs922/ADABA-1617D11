
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Event {

	//Constructors ---------------------------------------------------------------------------

	public Event() {
		super();
	}


	//Attributes ---------------------------------------------------------------------------

	private String	title;
	private Date	moment;
	private String	description;
	private String	picture;
	private int		numberSeatsOffered;


	//Getters & Setters ----------------------------------------------------------------------

	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}
	@NotNull
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(final Date moment) {
		this.moment = moment;
	}
	@NotBlank
	public String getDescription() {
		return this.description;
	}
	@URL
	public void setDescription(final String description) {
		this.description = description;
	}

	public String getPicture() {
		return this.picture;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}

	public int getNumberSeatsOffered() {
		return this.numberSeatsOffered;
	}

	public void setNumberSeatsOffered(final int numberSeatsOffered) {
		this.numberSeatsOffered = numberSeatsOffered;
	}


	//Relationships

	private Organises	organises;
	private Registers	registers;


	@ManyToOne(optional = true)
	@Valid
	public Organises getOrganises() {
		return this.organises;
	}

	public void setOrganises(final Organises organises) {
		this.organises = organises;
	}

	@ManyToOne
	@Valid
	public Registers getRegisters() {
		return this.registers;
	}

	public void setRegisters(final Registers registers) {
		this.registers = registers;
	}

}
