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

import machalica.marcin.gss.groovyscripts.runner.GroovyScriptRunner;
import machalica.marcin.gss.groovyscripts.runner.GroovyScriptRunnerResult;

@RestController
@RequestMapping("/api")
public class GroovyScriptController {
	private final GroovyScriptService groovyScriptService;
	private final GroovyScriptRunner groovyScriptRunner;

	@Autowired
	public GroovyScriptController(GroovyScriptService groovyScriptService, GroovyScriptRunner groovyScriptRunner) {
		this.groovyScriptService = groovyScriptService;
		this.groovyScriptRunner = groovyScriptRunner;
	}

	@GetMapping("/groovyscripts")
	public List<GroovyScript> getAllGroovyScripts() {
		return groovyScriptService.getAllGroovyScripts();
	}

	@GetMapping("/groovyscripts/{scriptName}")
	public GroovyScript getGroovyScriptById(@PathVariable String scriptName) {
		return groovyScriptService.getGroovyScriptByScriptName(scriptName);
	}

	@PostMapping("/groovyscripts")
	GroovyScript createGroovyScript(@Valid @RequestBody GroovyScript groovyScript, HttpServletResponse response) {
		GroovyScript createdGroovyScript = groovyScriptService.createGroovyScript(groovyScript);
		response.setStatus(HttpServletResponse.SC_CREATED);
		return createdGroovyScript;
	}

	@DeleteMapping("/groovyscripts/{scriptName}")
	public GroovyScript deleteGroovyScriptById(@PathVariable String scriptName) {
		return groovyScriptService.deleteGroovyScriptByScriptName(scriptName);
	}

	@PutMapping("/groovyscripts/{scriptName}")
	public GroovyScript updateGroovyScript(@Valid @RequestBody GroovyScript groovyScript, @PathVariable String scriptName) {
		return groovyScriptService.updateGroovyScriptByScriptName(groovyScript, scriptName);
	}
	
	@PostMapping("/groovyscripts/{scriptName}")
	public GroovyScriptRunnerResult runGroovyScript(@RequestBody (required = false) Object[] args, @PathVariable String scriptName) {
		GroovyScript groovyScript = groovyScriptService.getGroovyScriptByScriptName(scriptName);
		return groovyScriptRunner.runGroovyScript(groovyScript, args);
	}

}
