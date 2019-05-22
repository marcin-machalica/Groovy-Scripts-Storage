package machalica.marcin.gss.groovyscripts;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity(name = "groovy_script")
public class GroovyScript {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "method_name")
	private String methodName;

	@NotNull
	@Size(max = 300)
	private String body;
	
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "script_name", unique = true)
	private String scriptName;

	protected GroovyScript() { }

	public GroovyScript(@Valid String methodName, @Valid String body, @Valid String scriptName) {
		this.methodName = methodName;
		this.body = body;
		this.scriptName = scriptName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String name) {
		this.methodName = name;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(@Valid String scriptName) {
		this.scriptName = scriptName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + id;
		result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
		result = prime * result + ((scriptName == null) ? 0 : scriptName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GroovyScript other = (GroovyScript) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (id != other.id)
			return false;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		if (scriptName == null) {
			if (other.scriptName != null)
				return false;
		} else if (!scriptName.equals(other.scriptName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GroovyScript [id=" + id + ", methodName=" + methodName + ", body=" + body + ", scriptName=" + scriptName
				+ "]";
	}

}
