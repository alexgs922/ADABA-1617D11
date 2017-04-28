
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Taste;

@Repository
public interface TasteRepository extends JpaRepository<Taste, Integer> {

	@Query("select (select sum(t.stars) from Taste t where t.chorbi.id=c.id) as auxiliar from Chorbi c order by auxiliar ASC")
	Collection<Integer> minMaxStars();

}
