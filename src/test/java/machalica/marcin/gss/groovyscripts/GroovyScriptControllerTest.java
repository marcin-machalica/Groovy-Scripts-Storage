package machalica.marcin.gss.groovyscripts;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import machalica.marcin.gss.exceptions.ResourceNotFoundException;
import machalica.marcin.gss.groovyscripts.runner.GroovyScriptRunner;
import machalica.marcin.gss.groovyscripts.runner.GroovyScriptRunnerResult;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GroovyScriptControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private GroovyScriptService groovyScriptsService;
	@MockBean
	private GroovyScriptRunner groovyScriptRunner;
	
	private final List<GroovyScript> allGroovyScripts = new ArrayList<GroovyScript>();

	@Before
	public void setUp() throws Exception {
		allGroovyScripts.clear();
		allGroovyScripts.add(new GroovyScript("add2", "def add2(a, b) { return a + b }", "add2"));
		allGroovyScripts.get(0).setId(1);
		allGroovyScripts.add(new GroovyScript("square", "def square(int a) { return a ** 2 }", "square"));
		allGroovyScripts.get(1).setId(2);
		allGroovyScripts.add(new GroovyScript("greet", "println \"hello\"", "greet"));
		allGroovyScripts.get(2).setId(3);
		allGroovyScripts.add(new GroovyScript("empty", "", "empty"));
		allGroovyScripts.get(3).setId(4);
	}

	@Test
	public void shouldFindScripts_whenScriptsExist() throws Exception {
		when(groovyScriptsService.getAllGroovyScripts()).thenReturn(allGroovyScripts);
		
		mockMvc.perform(get("/api/groovyscripts")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].methodName", is("add2")))
				.andExpect(jsonPath("$[0].body", is("def add2(a, b) { return a + b }")))
				.andExpect(jsonPath("$[0].scriptName", is("add2")));
		
		verify(groovyScriptsService).getAllGroovyScripts();
	}
	
	@Test
	public void shouldFindEmptyArray_whenScriptsDontExist() throws Exception {
		when(groovyScriptsService.getAllGroovyScripts()).thenReturn(new ArrayList<GroovyScript>());
		
		mockMvc.perform(get("/api/groovyscripts")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));
		
		verify(groovyScriptsService).getAllGroovyScripts();
	}
	
	@Test
	public void shouldFindScriptByScriptName_whenScriptNameExists() throws Exception {
		when(groovyScriptsService.getGroovyScriptByScriptName("square")).thenReturn(allGroovyScripts.get(1));
		
		mockMvc.perform(get("/api/groovyscripts/square")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(2)))
				.andExpect(jsonPath("$.methodName", is("square")))
				.andExpect(jsonPath("$.body", is("def square(int a) { return a ** 2 }")))
				.andExpect(jsonPath("$.scriptName", is("square")));
		
		verify(groovyScriptsService).getGroovyScriptByScriptName("square");
	}
	
	@Test
	public void shouldNotFindScriptByScriptName_whenScriptNameDoesntExist() throws Exception {
		when(groovyScriptsService.getGroovyScriptByScriptName("notExistingScript")).thenThrow(new ResourceNotFoundException());

		mockMvc.perform(get("/api/groovyscripts/notExistingScript")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(status().reason(containsString("Resource not found")));
		
		verify(groovyScriptsService).getGroovyScriptByScriptName("notExistingScript");
	}
	
	@Test
	public void shouldCreateScript_whenBodyIsValid() throws Exception {
		GroovyScript groovyScript = new GroovyScript("testScript", "return 2 + 2", "testScript");
		GroovyScript expectedGroovyScript = new GroovyScript("testScript", "return 2 + 2", "testScript");
		expectedGroovyScript.setId(5);
		when(groovyScriptsService.createGroovyScript(groovyScript)).thenReturn(expectedGroovyScript);
		
		mockMvc.perform(post("/api/groovyscripts")
		.contentType(MediaType.APPLICATION_JSON)
        .content(objectToJson(groovyScript)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id", is(expectedGroovyScript.getId())))
		.andExpect(jsonPath("$.methodName", is(expectedGroovyScript.getMethodName())))
		.andExpect(jsonPath("$.body", is(expectedGroovyScript.getBody())))
		.andExpect(jsonPath("$.scriptName", is(expectedGroovyScript.getScriptName())));
		
		verify(groovyScriptsService).createGroovyScript(groovyScript);
	}
	
	@Test
	public void shouldNotCreateScript_whenDataIsInvalid() throws Exception {
		GroovyScript groovyScript = new GroovyScript("", "return 2 + 2", "");
		
		mockMvc.perform(post("/api/groovyscripts")
		.contentType(MediaType.APPLICATION_JSON)
        .content(objectToJson(groovyScript)))
		.andExpect(status().isConflict());
		
		verify(groovyScriptsService, times(0)).createGroovyScript(groovyScript);
	}
	
	@Test
	public void shouldDeleteScriptByScriptName_whenScriptNameExists() throws Exception {
		GroovyScript expectedGroovyScript = new GroovyScript("testScript", "return 2 + 2", "testScript");
		expectedGroovyScript.setId(3);
		when(groovyScriptsService.deleteGroovyScriptByScriptName("testScript")).thenReturn(expectedGroovyScript);
		
		mockMvc.perform(delete("/api/groovyscripts/testScript")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", is(expectedGroovyScript.getId())))
		.andExpect(jsonPath("$.methodName", is(expectedGroovyScript.getMethodName())))
		.andExpect(jsonPath("$.body", is(expectedGroovyScript.getBody())))
		.andExpect(jsonPath("$.scriptName", is(expectedGroovyScript.getScriptName())));
		
		verify(groovyScriptsService).deleteGroovyScriptByScriptName("testScript");
	}
	
	@Test
	public void shouldNotDeleteScriptByScriptName_whenScriptNameDoesntExist() throws Exception {
		when(groovyScriptsService.deleteGroovyScriptByScriptName("notExistingScript")).thenThrow(new ResourceNotFoundException());
		
		mockMvc.perform(delete("/api/groovyscripts/notExistingScript")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound())
		.andExpect(status().reason(containsString("Resource not found")));
		
		verify(groovyScriptsService).deleteGroovyScriptByScriptName("notExistingScript");
		verify(groovyScriptsService, times(0)).deleteGroovyScriptByScriptName("notexistingscript");
	}
	
	@Test
	public void shouldUpdateScript_whenScriptNameExistsAndDataIsValid() throws Exception {
		GroovyScript groovyScript = new GroovyScript("testScript", "return 2 + 2", "testScript");
		groovyScript.setId(3);
		GroovyScript expectedGroovyScript = new GroovyScript("changedMethodName", "changedBody", "changedScriptName");
		expectedGroovyScript.setId(3);
		when(groovyScriptsService.updateGroovyScriptByScriptName(groovyScript, "testScript")).thenReturn(expectedGroovyScript);
		
		mockMvc.perform(put("/api/groovyscripts/testScript")
		.contentType(MediaType.APPLICATION_JSON)
        .content(objectToJson(groovyScript)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", is(expectedGroovyScript.getId())))
		.andExpect(jsonPath("$.methodName", is(expectedGroovyScript.getMethodName())))
		.andExpect(jsonPath("$.body", is(expectedGroovyScript.getBody())))
		.andExpect(jsonPath("$.scriptName", is(expectedGroovyScript.getScriptName())));
		
		verify(groovyScriptsService).updateGroovyScriptByScriptName(groovyScript, "testScript");
	}
	
	@Test
	public void shouldNotUpdateScript_whenScriptNameDoesntExist() throws Exception {
		GroovyScript groovyScript = new GroovyScript("testScript", "return 2 + 2", "testScript");
		groovyScript.setId(3);
		when(groovyScriptsService.updateGroovyScriptByScriptName(groovyScript, "notExistingScript")).thenThrow(new ResourceNotFoundException());
		
		mockMvc.perform(put("/api/groovyscripts/notExistingScript")
		.contentType(MediaType.APPLICATION_JSON)
        .content(objectToJson(groovyScript)))
		.andExpect(status().isNotFound())
		.andExpect(status().reason(containsString("Resource not found")));
		
		verify(groovyScriptsService).updateGroovyScriptByScriptName(groovyScript, "notExistingScript");
	}
	
	@Test
	public void shouldNotUpdateScript_whenDataIsInvalid() throws Exception {
		GroovyScript groovyScript = new GroovyScript("", "return 2 + 2", "");
		groovyScript.setId(3);
		
		mockMvc.perform(put("/api/groovyscripts/testScript")
		.contentType(MediaType.APPLICATION_JSON)
        .content(objectToJson(groovyScript)))
		.andExpect(status().isConflict());
		
		verify(groovyScriptsService, times(0)).updateGroovyScriptByScriptName(groovyScript, "testScript");
	}
	
	@Test
	public void shouldRunScript_whenScriptExists() throws Exception {
		GroovyScript groovyScript = allGroovyScripts.get(0);
		Object[] args = new Object[] {10,20};
		GroovyScriptRunnerResult result = new GroovyScriptRunnerResult();
		result.setValue(30);
		when(groovyScriptsService.getGroovyScriptByScriptName("add2")).thenReturn(groovyScript);
		when(groovyScriptRunner.runGroovyScript(groovyScript, args)).thenReturn(result);
		
		mockMvc.perform(post("/api/groovyscripts/add2")
		.contentType(MediaType.APPLICATION_JSON)
        .content(objectToJson(args)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.value", is(result.getValue())))
		.andExpect(jsonPath("$.error", is(IsNull.nullValue())));
		
		verify(groovyScriptsService).getGroovyScriptByScriptName("add2");
		verify(groovyScriptRunner).runGroovyScript(groovyScript, args);
	}
	
	@Test
	public void shouldRunScriptAndContainError_whenScriptThrowsError() throws Exception {
		GroovyScript groovyScript = new GroovyScript("errorScript", "5/0", "errorScript");
		Object[] args = null;
		GroovyScriptRunnerResult result = new GroovyScriptRunnerResult();
		result.setError("ERROR java.lang.ArithmeticException: Division by zero");
		when(groovyScriptsService.getGroovyScriptByScriptName("errorScript")).thenReturn(groovyScript);
		when(groovyScriptRunner.runGroovyScript(groovyScript, args)).thenReturn(result);
		
		mockMvc.perform(post("/api/groovyscripts/errorScript")
		.contentType(MediaType.APPLICATION_JSON)
        .content(objectToJson(args)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.value", is(IsNull.nullValue())))
		.andExpect(jsonPath("$.error", is(result.getError())));
		
		verify(groovyScriptsService).getGroovyScriptByScriptName("errorScript");
		verify(groovyScriptRunner).runGroovyScript(groovyScript, args);
	}
	
	@Test
	public void shouldNotRunScript_whenScriptDoesntExist() throws Exception {
		when(groovyScriptsService.getGroovyScriptByScriptName("notExistingScript")).thenThrow(new ResourceNotFoundException());
		Object[] args = null;
		
		mockMvc.perform(post("/api/groovyscripts/notExistingScript")
		.contentType(MediaType.APPLICATION_JSON)
        .content(objectToJson(args)))
		.andExpect(status().isNotFound())
		.andExpect(status().reason(containsString("Resource not found")));
		
		verify(groovyScriptsService).getGroovyScriptByScriptName("notExistingScript");
		verify(groovyScriptRunner, times(0)).runGroovyScript(Mockito.any(GroovyScript.class), Mockito.any());
	}
	
	private String objectToJson(final Object obj) throws Exception {
	    try {
	        final ObjectMapper mapper = new ObjectMapper();
	        final String jsonContent = mapper.writeValueAsString(obj);
	        return jsonContent;
	    } catch (Exception ex) { 
	    	throw ex;
	    }
	}

}
