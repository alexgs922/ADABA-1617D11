
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.EventRepository;
import domain.Chorbi;
import domain.Event;

@Service
@Transactional
public class EventService {

	// Managed Repository -------------------------------------------

	@Autowired
	private EventRepository	eventRepository;


	// Supporting services ----------------------------------------------------
	
	@Autowired
	private ChorbiService chorbiService;

	
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

	public Event registerEvent(Chorbi chorbi, Event event){
		Assert.notNull(chorbi);
		Assert.notNull(event);
		Collection<Chorbi>coll = event.getRegistered();
		
		int seats = event.getNumberSeatsOffered();
		int actualSeats = coll.size();
		Assert.isTrue(seats>actualSeats);
		
		//Añadir chorbi al listado del evento
			List<Chorbi> list = new ArrayList<Chorbi>(coll);
			list.add(chorbi);
			event.setRegistered(list);
		
			//Añadir el evento al chorbi
			Collection<Event>events = chorbi.getEvents();
			List<Event>listEvents = new ArrayList<Event>(events);
			listEvents.add(event);
			chorbi.setEvents(listEvents);			
			
			//Nada de fee...
			
		
		this.save(event);
		this.chorbiService.save(chorbi);
		return event;
		
	}

	
	public Event unregisterEvent(Chorbi chorbi, Event event){
		Assert.notNull(chorbi);
		Assert.notNull(event);
		Collection<Chorbi>coll = event.getRegistered();
		List<Chorbi>list = new ArrayList<Chorbi>(coll);
		
		Assert.isTrue(list.contains(chorbi));
		int index = list.indexOf(chorbi);
		list.remove(index);
		event.setRegistered(list);
		
		
		Collection<Event>collEvent = chorbi.getEvents();
		List<Event>listEvent = new ArrayList<Event>(collEvent);
		Assert.isTrue(listEvent.contains(event));
		int index2 = listEvent.indexOf(event);
		listEvent.remove(index2);
		chorbi.setEvents(listEvent);
		
		this.save(event);
		this.chorbiService.save(chorbi);
		return event;
		
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
	public long difDiasEntre2fechas(final Calendar current, final Calendar fecha) {
		final long difms = fecha.getTimeInMillis() - current.getTimeInMillis();
		final long difd = difms / (1000 * 60 * 60 * 24);
		return difd;
	}
}
