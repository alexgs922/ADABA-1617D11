
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
	private double 			totalFeeAmount;

	//Getters & Setters ----------------------------------------------------------------------

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

	@Valid
	@NotNull
	public double getTotalFeeAmount() {
		return this.totalFeeAmount;
	}

	public void setTotalFeeAmount(final double totalFeeAmount) {
		this.totalFeeAmount = totalFeeAmount;
	}
	

	//Relationships
	private Collection<Chirp>	chirpReceives;
	private Collection<Chirp>	chirpWrites;
	private CreditCard			creditCard;
	private Collection<Taste>	givenTastes;
	private Template			template;


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

	@OneToOne(optional = true)
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
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
