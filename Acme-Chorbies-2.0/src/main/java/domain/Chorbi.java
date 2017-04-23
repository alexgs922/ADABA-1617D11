
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "ban"), @Index(columnList = "country,state,province,city"), @Index(columnList = "country,province,city"), @Index(columnList = "country,state,city"), @Index(columnList = "country,city"), @Index(columnList = "creditCard_id"),
	@Index(columnList = "relationship")
})
public class Chorbi extends Actor {

	//Constructors ---------------------------------------------------------------------------

	public Chorbi() {
		super();
	}


	//Attributes ---------------------------------------------------------------------------

	private String			picture;
	private String			description;
	private Relationship	relationship;
	private Date			birthDate;
	private Genre			genre;
	private boolean			ban;
	private Coordinate		coordinate;
	private double			totalChargedFee;


	//Getters & Setters ----------------------------------------------------------------------

	@Min(0)
	@Digits(integer = 32, fraction = 2)
	public double getTotalChargedFee() {
		return this.totalChargedFee;
	}

	public void setTotalChargedFee(final double totalChargedFee) {
		this.totalChargedFee = totalChargedFee;
	}

	@NotBlank
	@URL
	public String getPicture() {
		return this.picture;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Relationship getRelationship() {
		return this.relationship;
	}

	public void setRelationship(final Relationship relationship) {
		this.relationship = relationship;
	}

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(final Date birthDate) {
		this.birthDate = birthDate;
	}

	public Genre getGenre() {
		return this.genre;
	}

	public void setGenre(final Genre genre) {
		this.genre = genre;
	}

	public boolean isBan() {
		return this.ban;
	}

	public void setBan(final boolean ban) {
		this.ban = ban;
	}

	@Valid
	@NotNull
	public Coordinate getCoordinate() {
		return this.coordinate;
	}

	public void setCoordinate(final Coordinate coordinate) {
		this.coordinate = coordinate;
	}


	//Relationships
	private Collection<Chirp>	chirpReceives;
	private Collection<Chirp>	chirpWrites;
	private Collection<Taste>	givenTastes;
	private Template			template;
	private Collection<Event>	events;


	@Valid
	@NotNull
	@ManyToMany(mappedBy = "registered")
	public Collection<Event> getEvents() {
		return this.events;
	}

	public void setEvents(final Collection<Event> events) {
		this.events = events;
	}

	@OneToMany(mappedBy = "recipient")
	public Collection<Chirp> getChirpReceives() {
		return this.chirpReceives;
	}

	public void setChirpReceives(final Collection<Chirp> chirpReceives) {
		this.chirpReceives = chirpReceives;
	}

	@OneToMany(mappedBy = "sender")
	public Collection<Chirp> getChirpWrites() {
		return this.chirpWrites;
	}

	public void setChirpWrites(final Collection<Chirp> chirpWrites) {
		this.chirpWrites = chirpWrites;
	}

	@OneToMany
	public Collection<Taste> getGivenTastes() {
		return this.givenTastes;
	}

	public void setGivenTastes(final Collection<Taste> givenTastes) {
		this.givenTastes = givenTastes;
	}

	@OneToOne(optional = false)
	public Template getTemplate() {
		return this.template;
	}

	public void setTemplate(final Template template) {
		this.template = template;
	}

}
