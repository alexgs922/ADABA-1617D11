
package funcionalTesting;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;

import services.ChirpService;
import services.ChorbiService;
import services.ConfigurationService;
import services.EventService;
import services.ManagerService;
import utilities.AbstractTest;
import domain.Chorbi;
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
	private EventService			eventService;

	//Auxiliar services ---------------------------------------------------------------------------------

	@Autowired
	private ManagerService			managerService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private ChirpService			chirpService;

	@Autowired
	private ChorbiService			chorbiService;


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

	// ---------------------------------------------------------------------------------------------------------------------------------------------
	// CASO DE USO 3: UN MANAGER PUEDE GESTIONAR SUS EVENTOS, LO QUE INCLUYE CREARLOS :
	//Para este caso de uso se comprobará que:
	//El evento se crea correctamente
	//El evento se añade a la lista de eventos del manager autenticado como principal y que el evento pertenece a él
	//Que la fee del manager que crea el evento se ve incrementado tanto como indique la fee total en la configuración del sistema
	//Que la fee que se asocia a ese evento sea la que se indica en configuration y sea la misma que se le imputa al manager
	//Que la fecha del evento sea posterior a la actual (Se hace en servicios)
	//Que si se introduce información de contacto no permitida se enmascare
	//Que sólo se permite crear eventos cuando el manager tiene una credit card válida (Se hace en servicios)

	@Test
	public void testCreateEvent() {

		this.authenticate("manager1");

		final Manager principal = this.managerService.findByPrincipal();
		final double fee = this.configurationService.findConfiguration().getManagersFee();

		final int before_save = principal.getEvents().size();
		final int before_save_all = this.eventService.findAll().size();
		final double fee_before = principal.getTotalChargedFee();

		Event new_event = this.eventService.create();
		new_event.setDescription("Descripción de evento de pruebas");
		Date moment = new Date();
		final Calendar momento = new GregorianCalendar();
		momento.set(2018, 05, 15);
		moment = momento.getTime();
		new_event.setMoment(moment);
		new_event.setNumberSeatsOffered(25);
		new_event.setPicture("https://c1.staticflickr.com/4/3637/3357716385_8030394343_m.jpg");
		new_event.setTitle("Titulo del evento mi email es belramgut@alum.us.es");

		final BindingResult b = null;
		new_event = this.eventService.reconstruct(new_event, b);
		new_event = this.eventService.save2(new_event);

		this.eventService.flush();

		final int after_save = principal.getEvents().size();
		final int after_save_all = this.eventService.findAll().size();
		final double fee_after = principal.getTotalChargedFee();

		Assert.isTrue(after_save_all == 10);
		Assert.isTrue(after_save_all > before_save_all);
		Assert.isTrue(after_save_all == (before_save_all + 1));

		Assert.isTrue(after_save == 5);
		Assert.isTrue(before_save < after_save);
		Assert.isTrue(after_save == (before_save + 1));

		Assert.isTrue(principal.getEvents().contains(new_event));
		Assert.isTrue(new_event.getManager().getId() == principal.getId());

		Assert.isTrue(new_event.getTotalChargedFee() == fee);
		Assert.isTrue(fee_before < fee_after);
		Assert.isTrue(fee_after == (fee_before + fee));

		//Comprobemos que el email del titulo se ha enmascarado
		Assert.isTrue(new_event.getTitle().contains("*"));

	}

	//No crea el evento porque el manager2 tiene una tarjeta de crédito caducada
	@Test(expected = IllegalArgumentException.class)
	public void testCreateEvent2() {

		this.authenticate("manager2");

		Event new_event = this.eventService.create();
		new_event.setDescription("Descripción de evento de pruebas");
		Date moment = new Date();
		final Calendar momento = new GregorianCalendar();
		momento.set(2018, 05, 15);
		moment = momento.getTime();
		new_event.setMoment(moment);
		new_event.setNumberSeatsOffered(25);
		new_event.setPicture("https://c1.staticflickr.com/4/3637/3357716385_8030394343_m.jpg");
		new_event.setTitle("Titulo del evento mi email es belramgut@alum.us.es");

		final BindingResult b = null;
		new_event = this.eventService.reconstruct(new_event, b);
		new_event = this.eventService.save2(new_event);

		this.eventService.flush();

	}

	//No permite crear el evento porque el manager3 no tiene tarjeta de crédito registrada en el sistema
	@Test(expected = IllegalArgumentException.class)
	public void testCreateEvent3() {

		this.authenticate("manager3");

		Event new_event = this.eventService.create();
		new_event.setDescription("Descripción de evento de pruebas");
		Date moment = new Date();
		final Calendar momento = new GregorianCalendar();
		momento.set(2018, 05, 15);
		moment = momento.getTime();
		new_event.setMoment(moment);
		new_event.setNumberSeatsOffered(25);
		new_event.setPicture("https://c1.staticflickr.com/4/3637/3357716385_8030394343_m.jpg");
		new_event.setTitle("Titulo del evento mi email es belramgut@alum.us.es");

		final BindingResult b = null;
		new_event = this.eventService.reconstruct(new_event, b);
		new_event = this.eventService.save2(new_event);

		this.eventService.flush();

	}

	//No permite crear el evento con una fecha anterior a la actual
	@Test(expected = IllegalArgumentException.class)
	public void testCreateEvent4() {

		this.authenticate("manager1");

		Event new_event = this.eventService.create();
		new_event.setDescription("Descripción de evento de pruebas");
		Date moment = new Date();
		final Calendar momento = new GregorianCalendar();
		momento.set(2015, 05, 15);
		moment = momento.getTime();
		new_event.setMoment(moment);
		new_event.setNumberSeatsOffered(25);
		new_event.setPicture("https://c1.staticflickr.com/4/3637/3357716385_8030394343_m.jpg");
		new_event.setTitle("Titulo del evento mi email es belramgut@alum.us.es");

		final BindingResult b = null;
		new_event = this.eventService.reconstruct(new_event, b);
		new_event = this.eventService.save2(new_event);

		this.eventService.flush();

	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------
	//CASO DE USO: UN MANAGER PUEDE GESTIONAR SUS EVENTOS, LO QUE INCLUYE MODIFICARLOS : 
	//Para este caso de uso debemos tener en cuenta algunas reglas de negocio que hemos acordado introducir: 
	//1. No es posible modificar un evento pasado	
	//2. No es posible modificar a fecha anterior a la presente
	//3. No es posible cambiar el número de plazas libres a un número menor del número de chorbies ya registrados en él.

	//En estos tests comprobaremos que :
	//El evento se modifique correctamente
	//La fee del evento y del manager no cambia (no se imputa de nuevo cada vez que se modifica)
	//Se envían los chirps correspondientes a los chorbies afectados
	//Sólo el manager que es autor del evento puede editarlo
	//Si se introduce información de contacto no permitida, se enmascare

	@Test
	public void testEditEvent1() {

		this.authenticate("manager1");

		final Event eventoToEdit = this.eventService.findOne(287);

		final Manager principal_before = this.managerService.findByPrincipal();

		eventoToEdit.setTitle("Tests funcionales prueba");
		eventoToEdit.setDescription("mi número de teléfono es 685487595");

		final Event reconstructed = this.eventService.reconstruct(eventoToEdit, null);
		this.eventService.saveEdit(reconstructed);
		this.eventService.flush();
		this.chirpService.flush();

		final Event editedEvent = this.eventService.findOne(287);

		final Manager principal_after = this.managerService.findByPrincipal();

		Assert.isTrue(editedEvent.getTitle().equals("Tests funcionales prueba"));
		//Contendrá el caracter * si se ha enmascarado el teléfono introducido en la edición de la descripción
		Assert.isTrue(editedEvent.getDescription().contains("*"));

		//Comprobamos que por ejemplo para el chorbi 5, registrado en el evento, su lista de mensajes recibidos ha aumentado en 1 
		final Chorbi c = this.chorbiService.findOne(263);
		//No tiene ningún mensaje recibido, ahora debería tener 1
		Assert.isTrue(c.getChirpReceives().size() == 1);

		//Comprobamos que no aumentan los cargos con cada edición
		Assert.isTrue(eventoToEdit.getTotalChargedFee() == editedEvent.getTotalChargedFee());
		Assert.isTrue(principal_before.getTotalChargedFee() == principal_after.getTotalChargedFee());

	}

	//Test negativo : un manager intenta editar un evento que no es suyo
	@Test(expected = IllegalArgumentException.class)
	public void testEditEvent2() {

		this.authenticate("manager2");

		final Event eventoToEdit = this.eventService.findOne(287);

		eventoToEdit.setTitle("Tests funcionales prueba");
		eventoToEdit.setDescription("mi número de teléfono es 685487595");

		final Event reconstructed = this.eventService.reconstruct(eventoToEdit, null);
		this.eventService.saveEdit(reconstructed);
		this.eventService.flush();
		this.chirpService.flush();

	}

	//Test negativo : un manager intenta editar un evento y dejar menos plazas libres que chorbies ya registrados
	@Test(expected = IllegalArgumentException.class)
	public void testEditEvent3() {

		this.authenticate("manager1");

		final Event eventoToEdit = this.eventService.findOne(287);

		eventoToEdit.setNumberSeatsOffered(eventoToEdit.getRegistered().size() - 1);

		final Event reconstructed = this.eventService.reconstruct(eventoToEdit, null);
		this.eventService.saveEdit(reconstructed);
		this.eventService.flush();
		this.chirpService.flush();

	}

	//Test negativo : un manager intenta editar un evento y poner una fecha anterior a la actual
	@Test(expected = IllegalArgumentException.class)
	public void testEditEvent4() {

		this.authenticate("manager1");

		final Event eventoToEdit = this.eventService.findOne(287);

		Date moment = new Date();
		final Calendar momento = new GregorianCalendar();
		momento.set(2017, 04, 04);
		moment = momento.getTime();
		eventoToEdit.setMoment(moment);

		final Event reconstructed = this.eventService.reconstruct(eventoToEdit, null);
		this.eventService.saveEdit(reconstructed);
		this.eventService.flush();
		this.chirpService.flush();

	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------
	//CASO DE USO: UN MANAGER PUEDE GESTIONAR SUS EVENTOS, LO QUE INCLUYE ELIMINARLOS : 
	//Para este caso de uso debemos tener en cuenta algunas reglas de negocio que hemos acordado introducir: 
	//1. No es posible eliminar un evento pasado	

	//En estos tests comprobaremos que :
	//El evento se elimine correctamente
	//Se envían los chirps correspondientes a los chorbies afectados

	@Test
	public void testDeleteEvent1() {

		this.authenticate("manager1");

		final Event eventoToEdit = this.eventService.findOne(287);
		final int all_before = this.eventService.findAll().size();

		this.eventService.delete(eventoToEdit);
		this.eventService.flush();

		final int all_after = this.eventService.findAll().size();

		Assert.isTrue(all_after == (all_before - 1));
		Assert.isTrue(!this.eventService.findAll().contains(eventoToEdit));

		//Comprobaremos que el chorbi 5, registrado en el evento, tiene un mensaje recibido más que antes de esta operación.
		final Chorbi c = this.chorbiService.findOne(263);
		//No tiene ningún mensaje recibido, ahora debería tener 1
		Assert.isTrue(c.getChirpReceives().size() == 1);

	}

	//Test negativo : el manager 2 intenta eliminar un evento que no es suyo
	@Test(expected = IllegalArgumentException.class)
	public void testDeleteEvent2() {

		this.authenticate("manager2");

		final Event eventoToEdit = this.eventService.findOne(287);

		this.eventService.delete(eventoToEdit);
		this.eventService.flush();

	}

	//Tests negativos : el manager 1 intenta eliminar un evento pasado
	@Test(expected = IllegalArgumentException.class)
	public void testDeleteEvent3() {

		this.authenticate("manager1");

		final Event eventoToEdit = this.eventService.findOne(283);

		this.eventService.delete(eventoToEdit);
		this.eventService.flush();

	}

}
