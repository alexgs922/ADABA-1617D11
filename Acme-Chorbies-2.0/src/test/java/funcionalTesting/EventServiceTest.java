
package funcionalTesting;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.EventService;
import services.ManagerService;
import utilities.AbstractTest;
import domain.Event;
import domain.Manager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class EventServiceTest extends AbstractTest {

	//The SUT ---------------------------------------------------------------------------------

	@Autowired
	private EventService	eventService;

	//Auxiliar services ---------------------------------------------------------------------------------

	@Autowired
	private ManagerService	managerService;


	//CASO DE USO 1 : Browse a listing that includes every event that was registered in the system. 
	//Past events must be greyed out; 
	//events that are going to be organised in less than one month and have seats available must also be somewhat highlighted; 
	//the rest of events must be displayed normally.

	//Para comprobar el correcto funcionamiento de este caso de uso haremos varias comprobaciones:
	//1. Se recoja de la base de datos el número correcto de eventos que debería, tanto total, como en cada listado independiente
	//2.Es indiferente si el usuario está logueado o no
	//3.Se recojan tres listados diferentes para hacer evidente cada forma de mostrar los eventos
	//4.Que los eventos en cada lista sean los correctos

	protected void templateListUseCase1(final String username, final Collection<Event> allEvents, final Collection<Event> pasados, final Collection<Event> envigor, final Collection<Event> futuros, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {

			this.authenticate(username);

			Assert.isTrue(allEvents.size() == 9);

			Assert.isTrue(pasados.size() == 3);

			Assert.isTrue(envigor.size() == 3);

			Assert.isTrue(futuros.size() == 3);

			for (final Event e : allEvents)
				Assert.isTrue(e.getId() == 283 || e.getId() == 284 || e.getId() == 285 || e.getId() == 286 || e.getId() == 287 || e.getId() == 288 || e.getId() == 289 || e.getId() == 290 || e.getId() == 291);

			for (final Event e : pasados)
				Assert.isTrue(e.getId() == 283 || e.getId() == 284 || e.getId() == 285);

			for (final Event e : envigor)
				Assert.isTrue(e.getId() == 286 || e.getId() == 287 || e.getId() == 288);

			for (final Event e : futuros)
				Assert.isTrue(e.getId() == 289 || e.getId() == 290 || e.getId() == 291);

			this.unauthenticate();
			this.eventService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverListUseCase1() {

		final Object testingData[][] = {
			//Este test comprueba todo lo descrito con anterioridad, para cualquiera tipo de usuario, registrado, o no y con cualquier tipo de rol para los 4 primeros tests (POSITIVOS)
			{
				null, this.eventService.findAll(), this.eventService.listPastEvents(), this.eventService.listEnVigorEvents(), this.eventService.listEventosFuturos(), null
			}, {
				"chorbi1", this.eventService.findAll(), this.eventService.listPastEvents(), this.eventService.listEnVigorEvents(), this.eventService.listEventosFuturos(), null
			}, {
				"manager1", this.eventService.findAll(), this.eventService.listPastEvents(), this.eventService.listEnVigorEvents(), this.eventService.listEventosFuturos(), null
			}, {
				"admin", this.eventService.findAll(), this.eventService.listPastEvents(), this.eventService.listEnVigorEvents(), this.eventService.listEventosFuturos(), null
			}, {
				//Se procede a tomar como la lista total de eventos en todos los casos (NEGATIVO)
				null, this.eventService.findAll(), this.eventService.findAll(), this.eventService.findAll(), this.eventService.findAll(), IllegalArgumentException.class
			}, {
				//Se toman las istas especificas para todos los casos (NEGATIVO)
				null, this.eventService.listPastEvents(), this.eventService.listPastEvents(), this.eventService.listPastEvents(), this.eventService.listPastEvents(), IllegalArgumentException.class
			}, {
				null, this.eventService.listEnVigorEvents(), this.eventService.listEnVigorEvents(), this.eventService.listEnVigorEvents(), this.eventService.listEnVigorEvents(), IllegalArgumentException.class
			}, {
				null, this.eventService.listEventosFuturos(), this.eventService.listEventosFuturos(), this.eventService.listEventosFuturos(), this.eventService.listEventosFuturos(), IllegalArgumentException.class
			}, {
				//Intercambio entre eventos futuros y pasados
				null, this.eventService.findAll(), this.eventService.listEventosFuturos(), this.eventService.listEnVigorEvents(), this.eventService.listPastEvents(), IllegalArgumentException.class
			}, {
				//Intercambion entre eventos futuros y en vigor
				null, this.eventService.findAll(), this.eventService.listPastEvents(), this.eventService.listEventosFuturos(), this.eventService.listEnVigorEvents(), IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListUseCase1((String) testingData[i][0], (Collection<Event>) testingData[i][1], (Collection<Event>) testingData[i][2], (Collection<Event>) testingData[i][3], (Collection<Event>) testingData[i][4], (Class<?>) testingData[i][5]);

	}
	// ---------------------------------------------------------------------------------------------------------------------------------------------
	//CASO DE USO 2: UN MANAGER PUEDE GESTIONAR SUS EVENTOS, LO QUE INCLUYE LISTARLOS :
	//Para este caso de uso comprobaremos que :
	//1. Cada manager recibe un listado de eventos con el número correcto de eventos que le pertenecen
	//2. Se comprueba para cada manager que el listado de eventos recibido son los suyos
	//3. Se comprueba que un manager no puede ver eventos que no son suyos

	protected void templateListUseCaseManager(final String username, final int option, final Collection<Event> events, final Class<?> expected) {
		Class<?> caught;

		caught = null;

		try {

			this.authenticate(username);

			final Manager principal = this.managerService.findByPrincipal();

			if (option == 1)
				Assert.isTrue(events.size() == 4);
			else if (option == 2)
				Assert.isTrue(events.size() == 2);
			else
				Assert.isTrue(events.size() == 3);

			for (final Event e : events)
				Assert.isTrue(principal.getEvents().contains(e));

			this.unauthenticate();
			this.eventService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverListUseCaseManager() {

		final Manager m1 = this.managerService.findOne2Tests(250);
		final Manager m2 = this.managerService.findOne2Tests(251);
		final Manager m3 = this.managerService.findOne2Tests(252);

		final Object testingData[][] = {

			{
				"manager1", 1, m1.getEvents(), null
			}, {
				"manager2", 2, m2.getEvents(), null
			}, {
				"manager3", 3, m3.getEvents(), null
			}, {
				//El manager1 intenta acceder a los eventos del manager2
				"manager1", 2, m2.getEvents(), IllegalArgumentException.class
			}, {
				//El manager 2 intenta acceder a los eventos del manager 3
				"manager2", 3, m3.getEvents(), IllegalArgumentException.class
			}, {
				//El manager 3 intenta acceder a los eventos del manager1
				"manager3", 1, m1.getEvents(), IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListUseCaseManager((String) testingData[i][0], (int) testingData[i][1], (Collection<Event>) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	// CASO DE USO 3: UN MANAGER PUEDE GESTIONAR SUS EVENTOS, LO QUE INCLUYE CREARLOS :

	@Test
	public void testCreateEvent() {

	}
}
