
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
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

	public double getChargedFee() {
		return this.chargedFee;
	}

	public void setChargedFee(final double chargedFee) {
		this.chargedFee = chargedFee;
	}


	//Relationships
	private Manager				organizer;
	private Collection<Event>	events;


	@ManyToOne(optional = false)
	@Valid
	@NotNull
	public Manager getOrganizer() {
		return this.organizer;
	}
	public void setOrganizer(final Manager organizer) {
		this.organizer = organizer;
	}

	@OneToMany(mappedBy = "organises")
	@Valid
	public Collection<Event> getEvents() {
		return this.events;
	}
	public void setEvents(final Collection<Event> events) {
		this.events = events;
	}

}
