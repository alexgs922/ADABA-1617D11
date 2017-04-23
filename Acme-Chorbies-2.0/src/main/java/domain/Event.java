
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Event extends DomainEntity {

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
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
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

	public void setDescription(final String description) {
		this.description = description;
	}

	@URL
	public String getPicture() {
		return this.picture;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}

	@Min(0)
	public int getNumberSeatsOffered() {
		return this.numberSeatsOffered;
	}

	public void setNumberSeatsOffered(final int numberSeatsOffered) {
		this.numberSeatsOffered = numberSeatsOffered;
	}


	//Relationships

	private Collection<Organises>	organises;
	private Collection<Registers>	registers;


	@Valid
	@NotNull
	@OneToMany(mappedBy = "event")
	public Collection<Organises> getOrganises() {
		return this.organises;
	}

	public void setOrganises(final Collection<Organises> organises) {
		this.organises = organises;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "event")
	public Collection<Registers> getRegisters() {
		return this.registers;
	}

	public void setRegisters(final Collection<Registers> registers) {
		this.registers = registers;
	}

}
