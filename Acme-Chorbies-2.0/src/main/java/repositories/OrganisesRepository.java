
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Organises;

@Repository
public interface OrganisesRepository extends JpaRepository<Organises, Integer> {

}
