
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

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Registers extends Actor {

	//Constructors ---------------------------------------------------------------------------

	public Registers() {
		super();
	}


	//Attributes ---------------------------------------------------------------------------

	private double			chargedFee;

	//Getters & Setters ----------------------------------------------------------------------
	
	public double getChargedFee() {
		return chargedFee;
	}


	public void setChargedFee(double chargedFee) {
		this.chargedFee = chargedFee;
	}
	
	//Relationships


	private Collection<Chorbi> chorbi;


	@OneToMany
	@Valid
	public Collection<Chorbi> getChorbi() {
		return chorbi;
	}


	public void setChorbi(Collection<Chorbi> chorbi) {
		this.chorbi = chorbi;
	}
	
	
	
}
