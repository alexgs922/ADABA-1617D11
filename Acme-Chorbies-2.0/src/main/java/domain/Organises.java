
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
public class Organises extends Actor {

	//Constructors ---------------------------------------------------------------------------

	public Organises() {
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
	private Manager	organizer;
	private Event	event;


	@ManyToOne(optional = false)
	@Valid
	@NotNull
	public Manager getOrganizer() {
		return this.organizer;
	}
	public void setOrganizer(final Manager organizer) {
		this.organizer = organizer;
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
