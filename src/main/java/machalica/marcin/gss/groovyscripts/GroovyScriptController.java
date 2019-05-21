package machalica.marcin.gss.groovyscripts;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GroovyScriptController {
	private final GroovyScriptService groovyScriptService;

	@Autowired
	public GroovyScriptController(GroovyScriptService groovyScriptService) {
		this.groovyScriptService = groovyScriptService;
	}

	@GetMapping("/groovyscripts")
	public List<GroovyScript> getAllGroovyScripts() {
		return groovyScriptService.getAllGroovyScripts();
	}

	@GetMapping("/groovyscripts/{id}")
	public GroovyScript getGroovyScriptById(@PathVariable int id) {
		return groovyScriptService.getGroovyScriptById(id);
	}

	@PostMapping("/groovyscripts")
	GroovyScript createGroovyScript(@Valid @RequestBody GroovyScript groovyScript, HttpServletResponse response) {
		GroovyScript createdGroovyScript = groovyScriptService.createGroovyScript(groovyScript);
		response.setStatus(HttpServletResponse.SC_CREATED);
		return createdGroovyScript;
	}

	@DeleteMapping("/groovyscripts/{id}")
	public GroovyScript deleteGroovyScriptById(@PathVariable int id) {
		return groovyScriptService.deleteGroovyScriptById(id);
	}

	@PutMapping("/groovyscripts/{id}")
	public GroovyScript updateGroovyScript(@Valid @RequestBody GroovyScript groovyScript, @PathVariable int id) {
		return groovyScriptService.updateGroovyScript(groovyScript, id);
	}
}
