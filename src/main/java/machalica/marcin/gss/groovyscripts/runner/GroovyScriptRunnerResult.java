package machalica.marcin.gss.groovyscripts.runner;

public class GroovyScriptRunnerResult {
	private Object value;
	private String errorMsg;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getError() {
		return errorMsg;
	}

	public void setError(String error) {
		this.errorMsg = error;
	}
}
