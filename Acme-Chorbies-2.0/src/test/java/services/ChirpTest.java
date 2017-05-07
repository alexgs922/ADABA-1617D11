
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Chirp;
import domain.Chorbi;
import domain.Event;
import domain.Manager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ChirpTest extends AbstractTest {

	@Autowired
	private ChirpService	chirpService;

	@Autowired
	private ChorbiService	chorbiService;

	@Autowired
	private ManagerService	managerService;

	@Autowired
	private EventService	eventService;


	//Find one positivo: con usuario logueado
	@Test
	public void findOneChirp() {

		this.authenticate("chorbi1");

		final Chirp c = this.chirpService.findOne(265);

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("--------------------------------FIND ONE CHIRP------------------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener el Chirp 1 con id 265: ");
		System.out.println("Subject: " + c.getSubject());
		System.out.println("Id: " + c.getId());

		this.unauthenticate();

	}

	//Find one negativo: no existe dicho chirp
	@Test(expected = IllegalArgumentException.class)
	public void findOneChirpNegative() {

		final Chirp c = this.chirpService.findOne(1000);

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("--------------------------------FIND ONE CHIRP------------------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

	}

	//FindALL
	@Test
	public void findAll() {

		System.out.println("-------------------------------------------------------------");
		System.out.println("               TEST CHIRP .FINDALL()");
		System.out.println("-------------------------------------------------------------");

		final Collection<Chirp> result = this.chirpService.findAll();

		for (final Chirp a : result) {
			System.out.println("Id: " + a.getId());
			System.out.println("Subject: " + a.getSubject());
		}
	}

	//create
	@Test
	public void testCreate() {
		System.out.println("-------------------------------------------------------------");
		System.out.println("               TEST CHIRP.CREATE()");
		System.out.println("-------------------------------------------------------------");
		System.out.println("Se esperan los atributos ID = 0 y subject a null");

		final Chirp result = this.chirpService.create();
		System.out.println("ID: " + result.getId());
		System.out.println("Subject: " + result.getSubject());
	}

	//create 2
	@Test
	public void testCreate2() {
		System.out.println("-------------------------------------------------------------");
		System.out.println("               TEST CHIRP.CREATE2()");
		System.out.println("-------------------------------------------------------------");
		System.out.println("Se esperan los atributos ID = 0 y recipient con id=249");

		this.authenticate("chorbi1");
		final Chorbi c = this.chorbiService.findOne(259);

		final Chirp result = this.chirpService.create(c);
		System.out.println("ID: " + result.getId());
		System.out.println("Recipient id: " + result.getRecipient().getId());
	}

	//SendedChirp
	@Test
	public void testSendedChirp() {
		System.out.println("-------------------------------------------------------------");
		System.out.println("               TEST CHIRP.SENDEDCHIRP()");
		System.out.println("-------------------------------------------------------------");

		this.authenticate("chorbi1");
		final Collection<Chirp> cs = this.chirpService.mySendedMessages(63);

		for (final Chirp c : cs) {
			System.out.println("ID: " + c.getId());
			System.out.println("Recipient id: " + c.getRecipient().getId());
			System.out.println("Sender id: " + c.getSender().getId());
			System.out.println("Subject: " + c.getSubject());
		}

	}

	//ReceivedChirp
	@Test
	public void testReceivedChirp() {
		System.out.println("-------------------------------------------------------------");
		System.out.println("               TEST CHIRP.SENDEDCHIRP()");
		System.out.println("-------------------------------------------------------------");

		this.authenticate("chorbi1");
		final Collection<Chirp> cs = this.chirpService.myRecivedMessages(63);

		for (final Chirp c : cs) {
			System.out.println("ID: " + c.getId());
			System.out.println("Recipient id: " + c.getRecipient().getId());
			System.out.println("Sender id: " + c.getSender().getId());
			System.out.println("Subject: " + c.getSubject());
		}

	}

	//Test positivo : funcionamiento del envío de mensaje a todos los chorbies registrados en un evento cuando éste se modifica.
	@Test
	public void testEditEventChirp() {

		System.out.println("-------------------------------------------------------------");
		System.out.println("               TEST EDIT EVENT CHIRP");
		System.out.println("-------------------------------------------------------------");

		this.authenticate("manager1");

		final Manager principal = this.managerService.findByPrincipal();

		final Event evento = this.eventService.findOne(287);

		final Collection<Chorbi> ch1 = evento.getRegistered();
		final List<Integer> lista = new ArrayList<Integer>();
		for (final Chorbi c : ch1)
			lista.add(c.getChirpReceives().size());

		this.chirpService.editEventChirp(evento, principal);

		final Collection<Chorbi> ch2 = evento.getRegistered();

		System.out.println("Comprobaremos que los chorbies afectados por el evento ahora tienen un chirp más:");

		int count = 0;
		for (final Chorbi c : ch2) {
			System.out.println("El chorbi " + c.getName() + " tenía " + lista.get(count) + " chirps recibidos, y ahora tiene " + c.getChirpReceives().size());
			Assert.isTrue(lista.get(count) == c.getChirpReceives().size() - 1);
			count = count + 1;
		}
	}

	//Test negativo : funcionamiento del envío de mensaje a todos los chorbies registrados en un evento cuando éste se modifica.
	//El manager 2 intenta enviar un chirp para anunciar un cambio en  un evento que no es suyo
	@Test(expected = IllegalArgumentException.class)
	public void testEditEventChirp2() {

		this.authenticate("manager2");

		final Manager principal = this.managerService.findByPrincipal();

		final Event evento = this.eventService.findOne(287);

		final Collection<Chorbi> ch1 = evento.getRegistered();
		final List<Integer> lista = new ArrayList<Integer>();
		for (final Chorbi c : ch1)
			lista.add(c.getChirpReceives().size());

		this.chirpService.editEventChirp(evento, principal);

	}

	//Test positivo : funcionamiento del envío de mensaje a todos los chorbies registrados en un evento cuando éste se elimina.
	@Test
	public void testDeleteEventChirp() {

		System.out.println("-------------------------------------------------------------");
		System.out.println("               TEST DELETE EVENT CHIRP");
		System.out.println("-------------------------------------------------------------");

		this.authenticate("manager1");

		final Manager principal = this.managerService.findByPrincipal();

		final Event evento = this.eventService.findOne(287);

		final Collection<Chorbi> ch1 = evento.getRegistered();
		final List<Integer> lista = new ArrayList<Integer>();
		for (final Chorbi c : ch1)
			lista.add(c.getChirpReceives().size());

		this.chirpService.deleteEventChirp(evento, principal);

		final Collection<Chorbi> ch2 = evento.getRegistered();

		System.out.println("Comprobaremos que los chorbies afectados por el evento ahora tienen un chirp más:");

		int count = 0;
		for (final Chorbi c : ch2) {
			System.out.println("El chorbi " + c.getName() + " tenía " + lista.get(count) + " chirps recibidos, y ahora tiene " + c.getChirpReceives().size());
			Assert.isTrue(lista.get(count) == c.getChirpReceives().size() - 1);
			count = count + 1;
		}
	}

	//Test negativo : funcionamiento del envío de mensaje a todos los chorbies registrados en un evento cuando éste se elimina.
	//El manager 2 intenta informar de la cancelación de un evento del manager1
	@Test(expected = IllegalArgumentException.class)
	public void testDeleteEventChirp2() {

		this.authenticate("manager2");

		final Manager principal = this.managerService.findByPrincipal();

		final Event evento = this.eventService.findOne(287);

		final Collection<Chorbi> ch1 = evento.getRegistered();
		final List<Integer> lista = new ArrayList<Integer>();
		for (final Chorbi c : ch1)
			lista.add(c.getChirpReceives().size());

		this.chirpService.deleteEventChirp(evento, principal);

		final Collection<Chorbi> ch2 = evento.getRegistered();

	}
}
