package machalica.marcin.gss.groovyscripts.runner;

import org.springframework.stereotype.Component;

import groovy.lang.GroovyShell;
import groovy.lang.Script;
import machalica.marcin.gss.groovyscripts.GroovyScript;

@Component
public class GroovyScriptRunner {
	public GroovyScriptRunnerResult runGroovyScript(GroovyScript groovyScript, Object[] args) {
		GroovyScriptRunnerResult result = new GroovyScriptRunnerResult();
		GroovyShell sh = new GroovyShell();
		String methodName = groovyScript.getMethodName();
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
