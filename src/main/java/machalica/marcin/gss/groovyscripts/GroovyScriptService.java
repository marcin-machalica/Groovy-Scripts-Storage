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

	public GroovyScript createGroovyScript(GroovyScript groovyScript) {
		return groovyScriptRepository.save(groovyScript);
	}

	public GroovyScript deleteGroovyScriptById(int id) {
		GroovyScript groovyScript = getGroovyScriptById(id);
		groovyScriptRepository.delete(groovyScript);
		return groovyScript;
	}

	public GroovyScript updateGroovyScript(GroovyScript groovyScript, int id) {
		try {
			GroovyScript foundGroovyScript = getGroovyScriptById(id);
			foundGroovyScript.setName(groovyScript.getName());
			foundGroovyScript.setBody(groovyScript.getBody());
			return groovyScriptRepository.save(foundGroovyScript);
		} catch (ResourceNotFoundException ex) {
			throw ex;
		}
	}

}
