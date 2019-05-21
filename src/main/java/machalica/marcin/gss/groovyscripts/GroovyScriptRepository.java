package machalica.marcin.gss.groovyscripts;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroovyScriptRepository extends CrudRepository<GroovyScript, Integer> {
	
}
