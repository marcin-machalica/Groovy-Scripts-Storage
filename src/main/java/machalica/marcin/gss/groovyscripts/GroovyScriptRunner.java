package machalica.marcin.gss.groovyscripts;

import org.springframework.stereotype.Component;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

@Component
public class GroovyScriptRunner {
	public GroovyScriptRunnerResult runGroovyScript(GroovyScript groovyScript, Object[] args) {
		GroovyScriptRunnerResult result = new GroovyScriptRunnerResult();
		GroovyShell sh = new GroovyShell();
		String methodName = groovyScript.getName();
		String methodBody = groovyScript.getBody();
		
		try {
			Script script = sh.parse(methodBody);
			Object value = args == null ? script.run() : script.invokeMethod(methodName, args);
			result.setValue(value);			
		} catch (Exception ex) {
			result.setError(ex.getMessage());
		}
		
		return result;
	}
}
