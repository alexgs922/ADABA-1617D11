
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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

	@Min(0)
	@Digits(integer = 32, fraction = 2)
	public double getChargedFee() {
		return this.chargedFee;
	}

	public void setChargedFee(final double chargedFee) {
		this.chargedFee = chargedFee;
	}


	//Relationships

	private Chorbi	chorbi;
	private Event	event;


	@ManyToOne(optional = false)
	@Valid
	@NotNull
	public Chorbi getChorbi() {
		return this.chorbi;
	}
	public void setChorbi(final Chorbi chorbi) {
		this.chorbi = chorbi;
	}

	@ManyToOne(optional = false)
	@Valid
	public Event getEvent() {
		return this.event;
	}
	public void setEvent(final Event event) {
		this.event = event;
	}

}
