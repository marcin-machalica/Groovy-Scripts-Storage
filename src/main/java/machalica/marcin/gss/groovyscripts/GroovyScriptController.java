package machalica.marcin.gss.groovyscripts;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

}
