
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class Registers extends Actor {

	//Constructors ---------------------------------------------------------------------------

	public Registers() {
		super();
	}


	//Attributes ---------------------------------------------------------------------------

	private double	chargedFee;


	//Getters & Setters ----------------------------------------------------------------------

	public double getChargedFee() {
		return this.chargedFee;
	}

	public void setChargedFee(final double chargedFee) {
		this.chargedFee = chargedFee;
	}


	//Relationships

	private Collection<Chorbi>	chorbi;


	@OneToMany
	@Valid
	public Collection<Chorbi> getChorbi() {
		return this.chorbi;
	}

	public void setChorbi(final Collection<Chorbi> chorbi) {
		this.chorbi = chorbi;
	}

}
