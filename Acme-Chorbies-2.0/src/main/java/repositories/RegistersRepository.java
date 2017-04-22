
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Registers;

@Repository
public interface RegistersRepository extends JpaRepository<Registers, Integer> {

}
