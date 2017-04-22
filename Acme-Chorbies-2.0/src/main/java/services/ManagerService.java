
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.Validator;

import repositories.ManagerRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Manager;

@Service
@Transactional
public class ManagerService {

	// ---------- Repositories----------------------

	@Autowired
	private ManagerRepository	managerRepository;

	// Supporting services ------------------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private Md5PasswordEncoder	encoder;

	@Autowired
	private Validator			validator;


	// Simple CRUD methods ----------------------------------------------------

	public Manager create() {
		Manager result;
		result = new Manager();
		return result;
	}

	//TODO reconstruct de manager

	public Manager findOne(final int managerId) {
		Manager res;
		final Actor principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		res = this.managerRepository.findOne(managerId);
		Assert.notNull(res);

		return res;
	}

	public Collection<Manager> findAll() {
		Collection<Manager> res;
		res = this.managerRepository.findAll();
		return res;
	}

	public Manager save(final Manager manager) {
		Assert.notNull(manager);
		return this.managerRepository.save(manager);

	}

	public Manager findOneToSent(final int managerId) {

		Manager result;

		result = this.managerRepository.findOne(managerId);
		Assert.notNull(result);

		return result;

	}

	// Other business methods ----------------------------------------------------

	public Manager findByPrincipal() {
		Manager result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		result = this.findByUserAccount(userAccount);

		return result;
	}

	public Manager findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		Manager result;

		result = this.managerRepository.findByUserAccountId(userAccount.getId());

		return result;
	}

	public void flush() {
		this.managerRepository.flush();

	}

}
