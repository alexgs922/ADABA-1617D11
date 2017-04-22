
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.OrganisesRepository;
import domain.Organises;

@Service
@Transactional
public class OrganisesService {

	// Managed Repository -------------------------------------------

	@Autowired
	private OrganisesRepository	organisesRepository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public OrganisesService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Organises create() {
		Organises result;

		result = new Organises();

		return result;

	}

	public Collection<Organises> findAll() {
		Collection<Organises> result;
		result = this.organisesRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Organises findOne(final int organisesId) {
		Organises result;
		result = this.organisesRepository.findOne(organisesId);
		Assert.notNull(result);
		return result;
	}

	public Organises save(final Organises orgnanises) {
		Assert.notNull(orgnanises);
		return this.organisesRepository.save(orgnanises);
	}

	public void delete(final Organises organises) {
		Assert.notNull(organises);
		Assert.isTrue(organises.getId() != 0);

		this.organisesRepository.delete(organises);
	}

	// Other business methods ----------------------------------------------

	public void flush() {
		this.organisesRepository.flush();
	}

}
