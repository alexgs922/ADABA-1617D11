
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.RegistersRepository;
import domain.Registers;

@Service
@Transactional
public class RegistersService {

	// Managed Repository -------------------------------------------

	@Autowired
	private RegistersRepository	registersRepository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public RegistersService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Registers create() {
		Registers result;

		result = new Registers();

		return result;

	}

	public Collection<Registers> findAll() {
		Collection<Registers> result;
		result = this.registersRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Registers findOne(final int registersId) {
		Registers result;
		result = this.registersRepository.findOne(registersId);
		Assert.notNull(result);
		return result;
	}

	public Registers save(final Registers registers) {
		Assert.notNull(registers);
		return this.registersRepository.save(registers);
	}

	public void delete(final Registers registers) {
		Assert.notNull(registers);
		Assert.isTrue(registers.getId() != 0);

		this.registersRepository.delete(registers);
	}

	// Other business methods ----------------------------------------------

	public void flush() {
		this.registersRepository.flush();
	}

}
