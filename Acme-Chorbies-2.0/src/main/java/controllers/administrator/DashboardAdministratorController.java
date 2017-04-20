
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.ChirpService;
import services.ChorbiService;
import controllers.AbstractController;
import domain.Chorbi;

@Controller
@RequestMapping("/administrator")
public class DashboardAdministratorController extends AbstractController {

	// Services -----------------------------------------------

	@Autowired
	private ChorbiService	chorbiService;

	@Autowired
	private ChirpService	chirpService;


	// Constructors -------------------------------------------
	public DashboardAdministratorController() {
		super();
	}

	// List Category ------------------------------------------

	@RequestMapping(value = "/dashboard")
	public ModelAndView dashboard() {
		final ModelAndView result;

		final Collection<Object[]> listingWithTheNumberOfChorbiesPerCountryAndCity = this.chorbiService.listingWithTheNumberOfChorbiesPerCountryAndCity();
		final Collection<Object[]> minimumMaximumAverageAgesOfTheChorbies = this.chorbiService.minimumMaximumAverageAgesOfTheChorbies();
		final Double ratioChorbiesWhoHaveNoRegisteredACreditCardOrHaveRegisteredAnInvalidCreditCard = this.chorbiService.ratioChorbiesWhoHaveNoRegisteredACreditCardOrHaveRegisteredAnInvalidCreditCard();
		final Double ratiosOfChorbiesWhoSearchActivities = this.chorbiService.ratiosOfChorbiesWhoSearchActivities();
		final Double ratiosOfChorbiesWhoSearchFriendship = this.chorbiService.ratiosOfChorbiesWhoSearchFriendship();
		final Double ratiosOfChorbiesWhoSearchLove = this.chorbiService.ratiosOfChorbiesWhoSearchLove();
		final Collection<Object[]> listOfChorbiesCortedByTheNumberOfLikesTheyHaveGot = this.chorbiService.listOfChorbiesCortedByTheNumberOfLikesTheyHaveGot();
		final Collection<Double> minOfLikesPerChorbie = this.chorbiService.minOfLikesPerChorbie();
		final Collection<Double> maxOfLikesPerChorbie = this.chorbiService.maxOfLikesPerChorbie();
		final Double averageLikesPerChorbi = this.chorbiService.averageLikesPerChorbi();
		final Collection<Chorbi> theChorbiesWhoHaveGotMoreChirps = this.chorbiService.theChorbiesWhoHaveGotMoreChirps();
		final Collection<Chorbi> theChorbiesWhoHaveSentMoreChirps = this.chorbiService.theChorbiesWhoHaveSentMoreChirps();
		final Collection<Object[]> minAvgMaxChirpsReceived = this.chirpService.minAvgMaxChirpsReceived();
		final Collection<Object[]> minAvgMaxChirpsSent = this.chirpService.minAvgMaxChirpsSent();

		result = new ModelAndView("administrator/dashboard");
		result.addObject("listingWithTheNumberOfChorbiesPerCountryAndCity", listingWithTheNumberOfChorbiesPerCountryAndCity);
		result.addObject("minimumMaximumAverageAgesOfTheChorbies", minimumMaximumAverageAgesOfTheChorbies);
		result.addObject("ratioChorbiesWhoHaveNoRegisteredACreditCardOrHaveRegisteredAnInvalidCreditCard", ratioChorbiesWhoHaveNoRegisteredACreditCardOrHaveRegisteredAnInvalidCreditCard);
		result.addObject("ratiosOfChorbiesWhoSearchActivities", ratiosOfChorbiesWhoSearchActivities);
		result.addObject("ratiosOfChorbiesWhoSearchFriendship", ratiosOfChorbiesWhoSearchFriendship);
		result.addObject("ratiosOfChorbiesWhoSearchLove", ratiosOfChorbiesWhoSearchLove);
		result.addObject("listOfChorbiesCortedByTheNumberOfLikesTheyHaveGot", listOfChorbiesCortedByTheNumberOfLikesTheyHaveGot);
		result.addObject("minOfLikesPerChorbie", minOfLikesPerChorbie);
		result.addObject("maxOfLikesPerChorbie", maxOfLikesPerChorbie);
		result.addObject("averageLikesPerChorbi", averageLikesPerChorbi);
		result.addObject("theChorbiesWhoHaveGotMoreChirps", theChorbiesWhoHaveGotMoreChirps);
		result.addObject("theChorbiesWhoHaveSentMoreChirps", theChorbiesWhoHaveSentMoreChirps);
		result.addObject("minAvgMaxChirpsReceived", minAvgMaxChirpsReceived);
		result.addObject("minAvgMaxChirpsSent", minAvgMaxChirpsSent);

		return result;
	}

}
