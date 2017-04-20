/*
 * ProfileController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ChorbiService;
import services.CreditCardService;
import services.TasteService;
import services.TemplateService;
import domain.Chorbi;
import domain.Coordinate;
import domain.CreditCard;

@Controller
@RequestMapping("/profile")
public class ProfileController extends AbstractController {

	@Autowired
	private ChorbiService chorbiService;

	@Autowired
	private TasteService tasteService;

	@Autowired
	private TemplateService templateService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private CreditCardService creditCardService;
	
	
	//Edit profile
	
	@RequestMapping(value = "/editProfile", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int chorbiId) {
		ModelAndView res;
		Chorbi chorbi;
		int  principal;
		chorbi = chorbiService.findOne(chorbiId);
		principal = this.actorService.findByPrincipal().getId();
		Assert.isTrue(principal==chorbiId);

		try {
			Assert.isTrue(principal == chorbiId);
		} catch (Throwable th) {
			res = createEditModelAndViewError(chorbi);
			return res;
		}
		Assert.notNull(chorbi);
		res = createEditModelAndView(chorbi);
		return res;
	}

	@RequestMapping(value = "/editProfile", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Chorbi chorbi, BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors()) {
			if (binding.getGlobalError() != null)
				result = createEditModelAndView(chorbi, binding
						.getGlobalError().getCode());
			else
				result = createEditModelAndView(chorbi);
		} else {
			try {
				Chorbi chorbi1 = this.chorbiService.reconstruct(chorbi, binding);
				this.chorbiService.save(chorbi1);
				result = new ModelAndView("redirect:../chorbi/profile.do?chorbiId="+chorbi.getId());
			} catch (Throwable oops) {
				result = createEditModelAndView(chorbi, "chorbi.commit.error");
			}
		}
		return result;
	}
	
	@RequestMapping(value = "/createCreditCard", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		CreditCard creditCard;

		creditCard = this.creditCardService.create();

		result = this.editCreditCardModelAndView(creditCard);

		return result;
	}
	
	
	@RequestMapping(value = "/createCreditCard", method = RequestMethod.POST, params = "save")
	public ModelAndView save(CreditCard creditCard, final BindingResult binding) {

		ModelAndView result;
		CreditCard res;
		Chorbi chorbi = this.chorbiService.findByPrincipal();
		res = this.creditCardService.reconstruct(creditCard, binding);
		if (binding.hasErrors())
			result = this.editCreditCardModelAndView(res);
		else
			try {
				this.chorbiService.saveAndFlush2(chorbi, creditCard);
				result = new ModelAndView("redirect:../chorbi/profile.do?chorbiId="+chorbi.getId());

			} catch (final Throwable th) {
				result = this.editCreditCardModelAndView(res, "creditCard.commit.error");

			}

		return result;
	}

	
	// Edit CreditCard ----------------------------------

	@RequestMapping(value = "/editCreditCard", method = RequestMethod.GET)
	public ModelAndView editCreditCard(@RequestParam int chorbiId) {
		ModelAndView res;
		CreditCard creditCard;
		Chorbi principal;
		principal = chorbiService.findByPrincipal();
		creditCard = chorbiService.findOne(chorbiId).getCreditCard();
		try {
			Assert.isTrue(principal.getId() == chorbiId);
		} catch (Throwable th) {
			res = createEditCreditCardModelAndViewError(creditCard);
			return res;
		}
		Assert.notNull(creditCard);
		res = createEditCreditCardModelAndView(creditCard);
		return res;
	}

	@RequestMapping(value = "/editCreditCard", method = RequestMethod.POST, params = "save")
	public ModelAndView saveCreditCard(@Valid CreditCard creditCard,
			BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors()) {
			result = new ModelAndView("profile/editCreditCard");
			result.addObject("creditCard", creditCard);
			result.addObject("forbiddenOperation", false);
		} else {
			try {
				Chorbi t = this.chorbiService.findByPrincipal();
				t.setCreditCard(creditCard);
				this.chorbiService.save(t);
				this.creditCardService.save(creditCard);
				result = new ModelAndView("redirect:../chorbi/profile.do?chorbiId="+t.getId());
			} catch (Throwable oops) {
				result = new ModelAndView("profile/editCreditCard");
				result.addObject("creditCard", creditCard);
				result.addObject("message", "chorbi.commit.error");
			}
		}
		return result;

	}
	

	
	
	// Edit LocationInformation ----------------------------------

	@RequestMapping(value = "/editLocationInformation", method = RequestMethod.GET)
	public ModelAndView editLocationInformation(@RequestParam int chorbiId) {
		ModelAndView res;
		Chorbi principal;
		principal = chorbiService.findByPrincipal();
		Coordinate c = principal.getCoordinate();
		try {
			Assert.isTrue(principal.getId() == chorbiId);
		} catch (Throwable th) {
			res = createEditModelAndView(c);
			return res;
		}
		Assert.notNull(c);
		res = createEditModelAndView(c);
		return res;
	}

	@RequestMapping(value = "/editLocationInformation", method = RequestMethod.POST, params = "save")
	public ModelAndView saveCreditCard(@Valid Coordinate coordinate,
			BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors()) {
			result = new ModelAndView("profile/editLocationInformation");
			result.addObject("coordinate", coordinate);
			result.addObject("forbiddenOperation", false);
		} else {
			try {
				Chorbi t = this.chorbiService.findByPrincipal();
				t.setCoordinate(coordinate);
				this.chorbiService.save(t);
				result = new ModelAndView("redirect:../chorbi/profile.do?chorbiId="+t.getId());
			} catch (Throwable oops) {
				result = new ModelAndView("profile/editLocationInformation");
				result.addObject("coordinate", coordinate);
				result.addObject("message", "chorbi.commit.error");
			}
		}
		return result;

	}

	
	
	
	
	
	
	//Other methods 
	protected ModelAndView createEditModelAndView(Coordinate c) {
		ModelAndView result;
		result = createEditModelAndView(c, null);
		return result;
	}

	protected ModelAndView createEditModelAndViewError(Coordinate c) {
		ModelAndView res;
		res = createEditModelAndViewError(c, null);
		return res;

	}

	protected ModelAndView createEditModelAndView(Coordinate c, String message) {
		ModelAndView result;
		result = new ModelAndView("profile/editLocationInformation");
		result.addObject("coordinate", c);
		result.addObject("message", message);
		result.addObject("forbiddenOperation", false);
		return result;
	}

	protected ModelAndView createEditModelAndViewError(Coordinate c,
			String message) {
		ModelAndView res;
		res = new ModelAndView("profile/editProfile");
		res.addObject("coordinate", c);
		res.addObject("message", message);
		res.addObject("forbiddenOperation", true);
		return res;

	}


	
	
	
	
	
	protected ModelAndView createEditModelAndView(Chorbi chorbi) {
		ModelAndView result;
		result = createEditModelAndView(chorbi, null);
		return result;
	}

	protected ModelAndView createEditModelAndViewError(Chorbi chorbi) {
		ModelAndView res;
		res = createEditModelAndViewError(chorbi, null);
		return res;

	}

	protected ModelAndView createEditModelAndView(Chorbi chorbi, String message) {
		ModelAndView result;
		result = new ModelAndView("profile/editProfile");
		result.addObject("chorbi", chorbi);
		result.addObject("message", message);
		result.addObject("forbiddenOperation", false);
		return result;
	}

	protected ModelAndView createEditModelAndViewError(Chorbi chorbi,
			String message) {
		ModelAndView res;
		res = new ModelAndView("profile/editProfile");
		res.addObject("chorbi", chorbi);
		res.addObject("message", message);
		res.addObject("forbiddenOperation", true);
		return res;

	}

	protected ModelAndView createEditCreditCardModelAndView(
			CreditCard creditCard) {
		ModelAndView result;
		result = createEditCreditCardModelAndView(creditCard, null);
		return result;

	}

	protected ModelAndView createEditCreditCardModelAndViewError(
			CreditCard creditCard) {
		ModelAndView res;
		res = createEditCreditCardModelAndViewError(creditCard, null);
		return res;
	}

	protected ModelAndView createEditCreditCardModelAndView(
			CreditCard creditCard, String message) {
		ModelAndView result;

		result = new ModelAndView("profile/editCreditCard");
		result.addObject("creditCard", creditCard);
		result.addObject("message", message);
		result.addObject("forbiddenOperation", false);

		return result;
	}

	protected ModelAndView createEditCreditCardModelAndViewError(
			CreditCard creditCard, String message) {
		ModelAndView res;
		res = new ModelAndView("profile/editCreditCard");
		res.addObject("creditCard", creditCard);
		res.addObject("message", message);
		res.addObject("forbiddenOperation", true);
		return res;

	}
	
	
	//----------------------
	
	protected ModelAndView editCreditCardModelAndView(
			CreditCard creditCard) {
		ModelAndView res;
		res = editCreditCardModelAndView(creditCard, null);
		return res;
	}

	protected ModelAndView editCreditCardModelAndView(
			CreditCard creditCard, String message) {
		ModelAndView result;

		result = new ModelAndView("profile/createCreditCard");
		result.addObject("creditCard", creditCard);
		result.addObject("message", message);
		result.addObject("forbiddenOperation", false);

		return result;
	}

	
	
}
