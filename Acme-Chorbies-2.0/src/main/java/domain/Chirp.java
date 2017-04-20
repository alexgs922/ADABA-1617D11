
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
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
	@Index(columnList = "copy"), @Index(columnList = "sender_id"), @Index(columnList = "recipient_id")
})
public class Chirp extends DomainEntity {

	//Constructors ---------------------------------------------------------------------------

	public Chirp() {
		super();
	}


	//Attributes ---------------------------------------------------------------------------

	private Date	moment;
	private String	subject;
	private String	text;
	private String	attachments;
	private boolean	copy;


	//Getters & Setters ----------------------------------------------------------------------

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	@NotBlank
	public String getSubject() {
		return this.subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	@NotBlank
	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	@URL
	public String getAttachments() {
		return this.attachments;
	}

	public void setAttachments(final String attachments) {
		this.attachments = attachments;
	}

	public boolean isCopy() {
		return this.copy;
	}

	public void setCopy(final boolean copy) {
		this.copy = copy;
	}


	//Relationships
	private Chorbi	recipient;
	private Chorbi	sender;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Chorbi getRecipient() {
		return this.recipient;
	}

	public void setRecipient(final Chorbi recipient) {
		this.recipient = recipient;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Chorbi getSender() {
		return this.sender;
	}

	public void setSender(final Chorbi sender) {
		this.sender = sender;
	}

}
