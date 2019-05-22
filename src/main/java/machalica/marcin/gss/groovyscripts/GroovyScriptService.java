package machalica.marcin.gss.groovyscripts;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import machalica.marcin.gss.exceptions.ResourceNotFoundException;

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
	
	public GroovyScript getGroovyScriptByScriptName(String scriptName) {
		return groovyScriptRepository.findByScriptName(scriptName)
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
	
	public GroovyScript deleteGroovyScriptByScriptName(String scriptName) {
		GroovyScript groovyScript = getGroovyScriptByScriptName(scriptName);
		groovyScriptRepository.delete(groovyScript);
		return groovyScript;
	}
	
	public GroovyScript updateGroovyScriptById(GroovyScript groovyScript, int id) {
		GroovyScript foundGroovyScript = getGroovyScriptById(id);
		foundGroovyScript.setMethodName(groovyScript.getMethodName());
		foundGroovyScript.setBody(groovyScript.getBody());
		foundGroovyScript.setScriptName(groovyScript.getScriptName());
		return groovyScriptRepository.save(foundGroovyScript);
	}
	
	public GroovyScript updateGroovyScriptByScriptName(GroovyScript groovyScript, String scriptName) {
		GroovyScript foundGroovyScript = getGroovyScriptByScriptName(scriptName);
		foundGroovyScript.setMethodName(groovyScript.getMethodName());
		foundGroovyScript.setBody(groovyScript.getBody());
		foundGroovyScript.setScriptName(groovyScript.getScriptName());
		return groovyScriptRepository.save(foundGroovyScript);
	}

}
