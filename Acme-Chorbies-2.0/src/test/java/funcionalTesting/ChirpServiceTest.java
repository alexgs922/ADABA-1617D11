
package funcionalTesting;

import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ChirpService;
import services.ChorbiService;
import services.EventService;
import services.ManagerService;
import utilities.AbstractTest;
import domain.Chirp;
import domain.Chorbi;
import domain.Event;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ChirpServiceTest extends AbstractTest {

	// Service to test --------------------------------------------------------

	@Autowired
	private ChirpService	chirpService;

	@Autowired
	private ChorbiService	chorbiService;

	@Autowired
	private ManagerService	managerService;

	@Autowired
	private EventService	eventService;


	//Chorbi = 259,260,261,...,264

	//Crear chirp sin errores de validacion y otros casos comunes
	protected void template1(final String username, final int enviarId, final int recibirId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final Chorbi envia = this.chorbiService.findOneToSent(enviarId);
			final Chorbi recibe = this.chorbiService.findOneToSent(recibirId);

			final Collection<Chirp> enviadosInicial = envia.getChirpWrites();
			final int numeroDeMensajesEnviados = enviadosInicial.size();

			final Collection<Chirp> recibidosInicial = recibe.getChirpReceives();
			final int numeroDeMensajesRecibidos = recibidosInicial.size();

			final Chirp pm = this.chirpService.create();

			pm.setAttachments("");
			pm.setCopy(true);
			pm.setRecipient(recibe);
			pm.setSender(envia);
			pm.setText("Me gustaria asistir mañana dia 15 a las 9:00");
			pm.setSubject("Tutoria");

			this.chirpService.save(pm);

			Assert.isTrue(envia.getChirpWrites().size() == numeroDeMensajesEnviados + 1);
			Assert.isTrue(recibe.getChirpReceives().size() == numeroDeMensajesRecibidos + 1);

			this.unauthenticate();
			this.chirpService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	//Chorbi = 259,260,261,...,264
	@Test
	public void driver1() {

		final Object testingData[][] = {
			{   //chorbi 1 envia chirp correcto a chorbi 2
				"chorbi1", 259, 260, null
			}, {
				//chorbi 2 enviar chirp correcto a chorbi 1
				"chorbi2", 260, 259, null
			}, {
				//chorbi 1 envia chirp correcto a chorbi 3
				"chorbi1", 259, 261, null
			}, {
				//Simular post hacking. Logeado como chorbi 3, pero en realidad soy chorbi 2 intentando enviar chirp a chorbi 1
				"chorbi3", 260, 259, IllegalArgumentException.class
			}, {
				//Usuario no autenticado intenta enviar chirp a otro actor.
				null, 259, 260, IllegalArgumentException.class
			}, {
				//Chorbi 1 intenta enviar a un chorbi que no existe un chirp
				"chorbi1", 259, 5000, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template1((String) testingData[i][0], (int) testingData[i][1], (int) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	//Crear mensajes con errores de validacion
	protected void template2(final String username, final int enviarId, final int recibirId, final int opcionId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final Chorbi envia = this.chorbiService.findOneToSent(enviarId);
			final Chorbi recibe = this.chorbiService.findOneToSent(recibirId);

			final Collection<Chirp> enviadosInicial = envia.getChirpWrites();
			final int numeroDeMensajesEnviados = enviadosInicial.size();

			final Collection<Chirp> recibidosInicial = recibe.getChirpReceives();
			final int numeroDeMensajesRecibidos = recibidosInicial.size();

			final Chirp pm = this.chirpService.create();

			if (opcionId == 1) {
				pm.setAttachments("");
				pm.setCopy(true);
				pm.setRecipient(recibe);
				pm.setSender(envia);
				pm.setSubject("Reunion de mañana");

			}
			if (opcionId == 2) {
				pm.setAttachments("");
				pm.setCopy(true);
				pm.setRecipient(recibe);
				pm.setSender(envia);
				pm.setText("Me gustaria asistir mañana dia 15 a las 9:00");

			}
			if (opcionId == 3) {
				pm.setAttachments("");
				pm.setCopy(true);
				pm.setSender(envia);
				pm.setSubject("Reunion de mañana");
				pm.setText("Me gustaria asistir mañana dia 15 a las 9:00");

			}
			if (opcionId == 4) {
				pm.setAttachments("");
				pm.setCopy(true);
				pm.setRecipient(recibe);
				pm.setSubject("Reunion de mañana");
				pm.setText("Me gustaria asistir mañana dia 15 a las 9:00");

			}
			if (opcionId == 5) {

			}

			this.chirpService.save(pm);

			this.unauthenticate();
			this.chirpService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}
	//Chorbi = 259,260,261,...,264
	@Test
	public void driver2() {

		final Object testingData[][] = {
			{
				//chorbi 2 intenta enviar chirp sin texto a chirp 1
				"chorbi2", 260, 259, 1, ConstraintViolationException.class
			}, {
				//chorbi 2 intenta enviar chirp sin titulo a chorbi 3
				"chorbi2", 260, 261, 2, ConstraintViolationException.class
			}, {
				//chorbi 1 intenta enviar chirp sin recipient
				"chorbi1", 259, 260, 3, NullPointerException.class
			}, {
				//No existe sender
				"chorbi1", 259, 260, 4, NullPointerException.class
			}, {
				//Mensaje vacio
				"chorbi1", 259, 260, 5, NullPointerException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.template2((String) testingData[i][0], (int) testingData[i][1], (int) testingData[i][2], (int) testingData[i][3], (Class<?>) testingData[i][4]);

	}

	//Delete a received chirp
	protected void template3(final String username, final int chirpId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final Chirp c = this.chirpService.findOne(chirpId);

			this.chirpService.deleteReceived(c);

			final Collection<Chirp> cc = this.chirpService.findAll();

			Assert.isTrue(!cc.contains(c));

			this.unauthenticate();
			this.chirpService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}
	//Chorbi = 259,260,261,...,264
	//chorbi 2 tiene el chirp 266
	@Test
	public void driver3() {

		final Object testingData[][] = {
			{
				//chorbi 2 elimina un chirp suyo
				"chorbi2", 266, null
			}, {
				//chorbi 1 intenta eliminar un chirp que no es suyo
				"chorbi1", 266, IllegalArgumentException.class
			}

			, {
				//chorbi 2 intenta eliminar un chirp que no existe
				"chorbi2", 2000, IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.template3((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	//Delete a received chirp
	protected void template4(final String username, final int chirpId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final Chirp c = this.chirpService.findOne(chirpId);

			this.chirpService.deleteSent(c);

			final Collection<Chirp> cc = this.chirpService.findAll();

			Assert.isTrue(!cc.contains(c));

			this.unauthenticate();
			this.chirpService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}
	//Chorbi = 259,260,261,...,264
	//chorbi 1 tiene el chirp 265
	@Test
	public void driver4() {

		final Object testingData[][] = {
			{
				//chorbi 1 elimina un chirp suyo
				"chorbi1", 265, null
			}, {
				//chorbi 2 intenta eliminar un chirp que no es suyo
				"chorbi2", 265, IllegalArgumentException.class
			}

			, {
				//chorbi 1 intenta eliminar un chirp que no existe
				"chorbi1", 1000, IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.template4((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	//Test destinado a probar el bulk chirp enviado por los managers
	protected void template5(final String username, final int eventId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final Event event = this.eventService.findOne(eventId);

			final Collection<Chorbi> chorbiesEvento = event.getRegistered();
			final Integer numeroDeChorbies = chorbiesEvento.size();

			final Chirp chirp = this.chirpService.createBulk(event);
			chirp.setText("Estad atentos a la novedades");
			chirp.setSubject("Evento 1");

			final Collection<Chirp> todosLosChirps = this.chirpService.findAll();

			this.chirpService.saveBulk(chirp, event);

			final Collection<Chirp> todosLosChirps2 = this.chirpService.findAll();

			Assert.isTrue(todosLosChirps2.size() == todosLosChirps.size() + numeroDeChorbies);

			this.unauthenticate();
			this.chirpService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}
	//Event 283,284,285,286,287,288,289,290,291 9 eventos en total
	//manager1 = 250,..,manager 3=252
	//manager 1 tiene los eventos 1,5,6,7
	//manager 2 tiene los eventos 2,9
	@Test
	public void driver5() {

		final Object testingData[][] = {
			{
				//Manager 1 enviar un bulk chirp a todos los registrados en su evento
				"manager1", 283, null
			}, {
				//Manager 2 enviar un bulk chirp a todos los registrados en su evento
				"manager2", 284, null
			}, {
				//Manager 1 intenta enviar un bulk chirp sobre un evento que no es suyo
				"manager1", 284, IllegalArgumentException.class
			}, {
				//Usuario no autenticado intenta enviar un bulk chirp
				null, 283, IllegalArgumentException.class
			}, {
				//Manager 1 intenta enviar un bulk chirp sobre un evento que no existe
				"manager1", 5000, IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.template5((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);

	}

}
