package machalica.marcin.gss.groovyscripts;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class GroovyScriptService {
	private final GroovyScriptRepository groovyScriptRepository;
	
	@Autowired
	public GroovyScriptService(GroovyScriptRepository groovyScriptRepository) {
		this.groovyScriptRepository = groovyScriptRepository;
	}
	
	public List<GroovyScript> getAllGroovyScripts() {
		List<GroovyScript> groovyScripts = new ArrayList<GroovyScript>();
		groovyScriptRepository.findAll().forEach(groovyScripts::add);
		return groovyScripts;
	}
	
	public GroovyScript getGroovyScriptById(int id) {
		return groovyScriptRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException());
	}
}
