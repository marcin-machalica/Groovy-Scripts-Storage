package machalica.marcin.gss.groovyscripts;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroovyScriptRepository extends CrudRepository<GroovyScript, Integer> {
	Optional<GroovyScript> findByScriptName(String scriptName);
}
