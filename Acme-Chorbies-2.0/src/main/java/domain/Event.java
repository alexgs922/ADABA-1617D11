
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.OptimisticLock;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Event{

	//Constructors ---------------------------------------------------------------------------

	public Event() {
		super();
	}


	//Attributes ---------------------------------------------------------------------------

	private String			title;
	private Date			moment;
	private String 			description;
	private String			picture;
	private int 			numberSeatsOffered;
	
	//Getters & Setters ----------------------------------------------------------------------
	
	@NotBlank
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	@NotNull
	public Date getMoment() {
		return moment;
	}

	public void setMoment(Date moment) {
		this.moment = moment;
	}
	@NotBlank
	public String getDescription() {
		return description;
	}
	@URL
	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public int getNumberSeatsOffered() {
		return numberSeatsOffered;
	}

	public void setNumberSeatsOffered(int numberSeatsOffered) {
		this.numberSeatsOffered = numberSeatsOffered;
	}

	
	
	//Relationships
	
	private Organises organises;
	private Registers registers;

	
	@ManyToOne(optional=true)
	@Valid
	public Organises getOrganises() {
		return organises;
	}

	public void setOrganises(Organises organises) {
		this.organises = organises;
	}

	
	@ManyToOne
	@Valid
	public Registers getRegisters() {
		return registers;
	}

	public void setRegisters(Registers registers) {
		this.registers = registers;
	}
	
	
	
	
}
