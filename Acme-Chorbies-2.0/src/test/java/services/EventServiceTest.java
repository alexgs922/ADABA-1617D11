
package services;

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

	@Autowired
	private EventService			eventService;

	@Autowired
	private ManagerService			managerService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private ChorbiService			chorbiService;

	@Autowired
	private ChirpService			chirpService;


	//LOS TESTS DE GESTIÓN DE EVENTOS (CREACIÓN, EDICIÓN Y BORRADO) REQUIEREN DE VARIOS MÉTODOS DE SERVICIOS PARA LLEVARSE A CABO, ESTOS TESTS SE REALIZAN 
	//CON MAYOR DETALLE EN LOS TESTS FUNCIONALES, YA QUE NO ES POSIBLE COMPROBAR EL CORRECTO FUNCIONAMIENTO DE ALGUNOS MÉTODOS AISLADOS (COMO RECONSTRUCT)
	//SIN TENER EN CUENTA LOS OTROS.
	//POR ELLO AQUÍ SE INTRODUCEN TRES TESTS POSITIVOS A MODO DE MUESTRA DEL FUNCIONAMIENTO DE LA GESTIÓN DE EVENTOS, EL RESTO DE TESTS, INCLUIDOS LOS TESTS
	//NEGATIVOS, SE ENCUENTRAN EN LOS TESTS FUNCIONALES DE EVENTO.

	//Creación de un evento
	//Test positivo
	//Para crear un evento, se deben hacer uso de varios métodos de servicio: create, reconstruct y save2
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

	//Test findAll events (tienen acceso todos los actores del sistema, incluso los no logeados)
	@Test
	public void testFindAllEvents() {

		final Collection<Event> events = this.eventService.findAll();
		Assert.isTrue(events.size() == 9);
	}

	//Test findOne tienen acceso todos los actores del sistema, incluso los no logeados)
	@Test
	public void testFindOneEvent() {

		final Event e = this.eventService.findOne(283);

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("--------------------------------FIND ONE EVENT--------------------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("El titulo del evento es: " + e.getTitle());
		System.out.println("La descripción del evento es: " + e.getDescription());
		System.out.println("El momento del evento es: " + e.getMoment());

	}

	//Editar un evento :
	//El evento se modifique correctamente
	//La fee del evento y del manager no cambia (no se imputa de nuevo cada vez que se modifica)
	//Se envían los chirps correspondientes a los chorbies afectados
	//Sólo el manager que es autor del evento puede editarlo
	//Si se introduce información de contacto no permitida, se enmascare
	//Para editar el evento son necesarios varios métodos de servicio: reconstruct y saveEdit
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

	//Eliminar un evento :
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

	@Test
	public void testListPastEvents() {

		final Collection<Event> lista = this.eventService.listPastEvents();

		final Date current = new Date();
		for (final Event e : lista) {
			Assert.isTrue(e.getMoment().before(current));

			//Este assert
			Assert.isTrue(e.getId() == 283 || e.getId() == 284 || e.getId() == 285);
		}

		//Este assert
		Assert.isTrue(lista.size() == 3);

		//Estos dos assert son válidos a día de hoy en que se realizan los tests, en función del día en que se ejecuten
		//Habrá más o menos eventos pasados, con lo que tanto la cantidad de eventos pasados como sus ids pueden verse modificados (Ya que no sólo serán estos tres que se indican).
		//Comentando estas dos comprobaciones y dejando el primer Assert, es posible ver como todos los eventos en esta lista pertenecen al pasado.
	}

	@Test
	public void testListEnVigorEvents() {

		final Collection<Event> lista = this.eventService.listEnVigorEvents();

		//Este assert
		Assert.isTrue(lista.size() == 3);

		final Calendar current = new GregorianCalendar();
		for (final Event e : lista) {
			final Calendar fecha = new GregorianCalendar();
			fecha.setTime(e.getMoment());
			final long dif = this.eventService.difDiasEntre2fechas(current, fecha);
			Assert.isTrue(fecha.after(current) && dif <= 30 && e.getNumberSeatsOffered() > 0);

			//Este Assert
			Assert.isTrue(e.getId() == 286 || e.getId() == 287 || e.getId() == 288);
		}

		//Estos dos assert son válidos a día de hoy en que se realizan los tests, en función del día en que se ejecuten
		//Habrá más o menos eventos pasados, con lo que tanto la cantidad de eventos pasados como sus ids pueden verse modificados (Ya que no sólo serán estos tres que se indican).
		//Comentando estas dos comprobaciones y dejando el primer Assert, es posible ver como todos los eventos en esta lista ocurrirán en menos de 30 días y tienen plazas libres.

	}

	@Test
	public void testListEventosFuturos() {

		final Collection<Event> lista = this.eventService.listEventosFuturos();

		//Este Assert
		Assert.isTrue(lista.size() == 3);

		final Calendar current = new GregorianCalendar();
		for (final Event e : lista) {
			final Calendar fecha = new GregorianCalendar();
			fecha.setTime(e.getMoment());
			final long dif = this.eventService.difDiasEntre2fechas(current, fecha);
			Assert.isTrue(fecha.after(current) && dif > 30);

			//Este Assert
			Assert.isTrue(e.getId() == 289 || e.getId() == 290 || e.getId() == 291);
		}

		//Esos dos Asserts presentan la misma problemática que en los tests anteriores, en funcion del día en que se ejecuten estos tests, habrá más o menos, eventos en cada franja temporal.

	}
}
