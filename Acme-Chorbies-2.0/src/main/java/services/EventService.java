
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.EventRepository;
import domain.Event;

@Service
@Transactional
public class EventService {

	// Managed Repository -------------------------------------------

	@Autowired
	private EventRepository	eventRepository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public EventService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Event create() {
		Event result;

		result = new Event();

		return result;

	}

	public Collection<Event> findAll() {
		Collection<Event> result;
		result = this.eventRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Event findOne(final int eventId) {
		Event result;
		result = this.eventRepository.findOne(eventId);
		Assert.notNull(result);
		return result;
	}

	public Event save(final Event event) {
		Assert.notNull(event);
		return this.eventRepository.save(event);
	}

	public void delete(final Event event) {
		Assert.notNull(event);
		Assert.isTrue(event.getId() != 0);

		this.eventRepository.delete(event);
	}

	// Other business methods ----------------------------------------------

	public void flush() {
		this.eventRepository.flush();
	}

}
