
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Manager extends Actor {

	//Constructors ---------------------------------------------------------------------------

	public Manager() {
		super();
	}


	//Attributes ---------------------------------------------------------------------------

	private String			company;
	private String			vatNumber;


	
	//Getters & Setters ----------------------------------------------------------------------
	
	@NotBlank
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@NotBlank
	@Pattern(regexp="^ES[ABCDEFGHJNPQRSUVW]{1}[1-9]{8}$")
	public String getVatNumber() {
		return vatNumber;
	}

	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}


	//Relationships

	Collection<Organises> organises;
	
	@Valid
	@NotNull
	@OneToMany(mappedBy = "organizer")
	public Collection<Organises> getOrganises() {
		return this.organises;
	}

	public void setOrganises(final Collection<Organises> organises) {
		this.organises = organises;
	}

	
}
