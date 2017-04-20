/*
 * CustomerController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ChirpService;
import services.ChorbiService;
import domain.Chirp;
import domain.Chorbi;

@Controller
@RequestMapping("/chirp")
public class ChirpController extends AbstractController {

	@Autowired
	private ChirpService	chirpService;

	@Autowired
	private ChorbiService	chorbiService;


	// Constructors -----------------------------------------------------------

	public ChirpController() {
		super();
	}

	@RequestMapping(value = "/listReceivedMessages", method = RequestMethod.GET)
	public ModelAndView listReceivedMessages() {
		final ModelAndView result;

		final Chorbi c = this.chorbiService.findByPrincipal();

		final Collection<Chirp> receivedMessages = this.chirpService.myRecivedMessages(c.getId());

		result = new ModelAndView("chirp/listReceivedMessages");
		result.addObject("chirps", receivedMessages);
		result.addObject("requestURI", "chirp/listReceivedMessages.do");

		return result;
	}
	@RequestMapping(value = "/listSentMessages", method = RequestMethod.GET)
	public ModelAndView listSentMessages() {
		ModelAndView result;

		final Chorbi c = this.chorbiService.findByPrincipal();

		final Collection<Chirp> sendMessages = this.chirpService.mySendedMessages(c.getId());

		result = new ModelAndView("chirp/listSentMessages");
		result.addObject("chirps", sendMessages);
		result.addObject("requestURI", "chirp/listSentMessages.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Chirp chirp;

		chirp = this.chirpService.create();

		result = this.createEditModelAndView(chirp);

		return result;
	}

	@RequestMapping(value = "/response/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int actorId) {
		ModelAndView result;
		Chirp chirp;

		final Chorbi c = this.chorbiService.findOneToSent(actorId);
		chirp = this.chirpService.create(c);

		result = this.createEditModelAndView2(chirp);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Chirp chirpToEdit, final BindingResult binding) {

		ModelAndView result;

		chirpToEdit = this.chirpService.reconstruct(chirpToEdit, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(chirpToEdit);
		else
			try {
				this.chirpService.save(chirpToEdit);
				result = new ModelAndView("redirect:/chirp/listSentMessages.do");

			} catch (final Throwable th) {
				result = this.createEditModelAndView(chirpToEdit, "chirp.commit.error");

			}

		return result;
	}

	@RequestMapping(value = "/response/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save2(Chirp chirpToEdit, final BindingResult binding) {

		ModelAndView result;

		chirpToEdit = this.chirpService.reconstruct(chirpToEdit, binding);
		if (binding.hasErrors()) {
			result = this.createEditModelAndView2(chirpToEdit);

			result.addObject("chirp", chirpToEdit);

		} else {
			try {

				this.chirpService.save(chirpToEdit);

			} catch (final Throwable th) {
				result = this.createEditModelAndView2(chirpToEdit, "chirp.commit.error");

				result.addObject("chirp", chirpToEdit);

				return result;
			}

			result = new ModelAndView("redirect:/chirp/listReceivedMessages.do");

		}

		return result;
	}

	// REPLY ----------------------------------------------------

	@RequestMapping(value = "/reply", method = RequestMethod.GET)
	public ModelAndView reply(@RequestParam final int chirpId) {
		ModelAndView result;

		final Chirp chirp = this.chirpService.findOne(chirpId);

		result = this.createEditModelAndView(chirp);

		return result;
	}

	@RequestMapping(value = "/reply", method = RequestMethod.POST, params = "save")
	public ModelAndView reply(Chirp chirpToEdit, final BindingResult binding) {

		ModelAndView result;

		chirpToEdit = this.chirpService.reply(chirpToEdit, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(chirpToEdit);
		else
			try {

				this.chirpService.save(chirpToEdit);
				result = new ModelAndView("redirect:/chirp/listSentMessages.do");

			} catch (final Throwable th) {
				result = this.createEditModelAndView(chirpToEdit, "chirp.commit.error");

			}

		return result;
	}

	// DELETE ---------------------------------------------------

	@RequestMapping(value = "/deleteReceived", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int chirpId) {

		ModelAndView result;
		Chirp chirp;

		chirp = this.chirpService.findOne(chirpId);
		try {
			this.chirpService.deleteReceived(chirp);
		} catch (final Throwable th) {

		}

		result = new ModelAndView("redirect:/chirp/listReceivedMessages.do");

		return result;

	}

	@RequestMapping(value = "/deleteSent", method = RequestMethod.GET)
	public ModelAndView delete2(@RequestParam final int chirpId) {

		ModelAndView result;
		Chirp chirp;

		chirp = this.chirpService.findOne(chirpId);
		try {
			this.chirpService.deleteSent(chirp);
		} catch (final Throwable th) {

		}

		result = new ModelAndView("redirect:/chirp/listSentMessages.do");

		return result;

	}

	//CREATE EDIT MODEL AND VIEW

	protected ModelAndView createEditModelAndView(final Chirp chirpToEdit) {
		ModelAndView result;

		result = this.createEditModelAndView(chirpToEdit, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Chirp chirpToEdit, final String message) {
		ModelAndView result;

		Collection<Chorbi> actors;
		final Chorbi c = this.chorbiService.findByPrincipal();

		actors = this.chorbiService.findAll();
		actors.remove(c);

		result = new ModelAndView("chirp/create");
		result.addObject("chirp", chirpToEdit);
		result.addObject("actors", actors);
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView createEditModelAndView2(final Chirp chirpToEdit) {
		ModelAndView result;

		result = this.createEditModelAndView2(chirpToEdit, null);

		return result;
	}

	protected ModelAndView createEditModelAndView2(final Chirp chirpToEdit, final String message) {
		ModelAndView result;

		result = new ModelAndView("chirp/response/create");
		result.addObject("chirp", chirpToEdit);
		result.addObject("message", message);

		return result;
	}

}
