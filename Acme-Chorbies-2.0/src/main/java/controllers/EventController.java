
package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.EventService;
import domain.Event;

@Controller
@RequestMapping("/event")
public class EventController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public EventController() {
		super();
	}


	////Services -----------------------------------------------------------

	@Autowired
	EventService	eventService;


	//Browse a listing that includes every event that was registered in the system.
	//Past events must be greyed out; events that are going to be organised in less than one month and have seats available must also be somewhat highlighted; 
	//the rest of events must be displayed normally.

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		Collection<Event> eventsToShow;
		final Collection<Event> aux = new ArrayList<Event>();
		final Collection<Event> toGray = new ArrayList<Event>();

		eventsToShow = this.eventService.findAll();

		final Calendar current = new GregorianCalendar();
		for (final Event tmp : eventsToShow) {
			final Calendar fecha = new GregorianCalendar();
			fecha.setTime(tmp.getMoment());
			if (fecha.before(current))
				toGray.add(tmp);
			else {

				final long dif = this.eventService.difDiasEntre2fechas(current, fecha);
				if (fecha.after(current) && dif <= 30 && tmp.getNumberSeatsOffered() > 0)
					aux.add(tmp);
			}

		}

		result = new ModelAndView("event/list");
		result.addObject("events", eventsToShow);
		result.addObject("requestURI", "event/list.do");
		result.addObject("current", new Date());
		result.addObject("tohighlight", aux);
		result.addObject("togray", toGray);

		return result;

	}

}
