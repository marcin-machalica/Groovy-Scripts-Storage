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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GroovyScriptControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private GroovyScriptService groovyScriptsService;
	
	private final List<GroovyScript> allGroovyScripts = new ArrayList<GroovyScript>();

	@Before
	public void setUp() throws Exception {
		allGroovyScripts.clear();
		allGroovyScripts.add(new GroovyScript("add2", "def add2(a, b) { return a + b }"));
		allGroovyScripts.get(0).setId(1);
		allGroovyScripts.add(new GroovyScript("square", "def square(int a) { return a ** 2 }"));
		allGroovyScripts.get(1).setId(2);
		allGroovyScripts.add(new GroovyScript("greet", "println \"hello\""));
		allGroovyScripts.get(2).setId(3);
		allGroovyScripts.add(new GroovyScript("empty", null));
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
				.andExpect(jsonPath("$[0].name", is("add2")))
				.andExpect(jsonPath("$[0].body", is("def add2(a, b) { return a + b }")));
		
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
	public void shouldFindScriptById_whenIdExists() throws Exception {
		when(groovyScriptsService.getGroovyScriptById(2)).thenReturn(allGroovyScripts.get(1));
		
		mockMvc.perform(get("/api/groovyscripts/2")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(2)))
				.andExpect(jsonPath("$.name", is("square")))
				.andExpect(jsonPath("$.body", is("def square(int a) { return a ** 2 }")));
		
		verify(groovyScriptsService).getGroovyScriptById(2);
	}
	
	@Test
	public void shouldNotFindScriptById_whenIdDoesntExist() throws Exception {
		when(groovyScriptsService.getGroovyScriptById(100)).thenThrow(new ResourceNotFoundException());

		mockMvc.perform(get("/api/groovyscripts/100")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(status().reason(containsString("Resource not found")));
		
		verify(groovyScriptsService).getGroovyScriptById(100);
	}
	
	@Test
	public void shouldCreateScript_whenBodyIsValid() throws Exception {
		GroovyScript groovyScript = new GroovyScript("testScript", "return 2 + 2");
		GroovyScript expectedGroovyScript = new GroovyScript("testScript", "return 2 + 2");
		expectedGroovyScript.setId(5);
		when(groovyScriptsService.createGroovyScript(groovyScript)).thenReturn(expectedGroovyScript);
		
		mockMvc.perform(post("/api/groovyscripts")
		.contentType(MediaType.APPLICATION_JSON)
        .content(objectToJson(groovyScript)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.id", is(expectedGroovyScript.getId())))
		.andExpect(jsonPath("$.name", is(expectedGroovyScript.getName())))
		.andExpect(jsonPath("$.body", is(expectedGroovyScript.getBody())));
		
		verify(groovyScriptsService).createGroovyScript(groovyScript);
	}
	
	@Test
	public void shouldNotCreateScript_whenBodyIsInvalid() throws Exception {
		GroovyScript groovyScript = new GroovyScript("", "return 2 + 2");
		
		mockMvc.perform(post("/api/groovyscripts")
		.contentType(MediaType.APPLICATION_JSON)
        .content(objectToJson(groovyScript)))
		.andExpect(status().isBadRequest());
		
		verify(groovyScriptsService, times(0)).createGroovyScript(groovyScript);
	}
	
	@Test
	public void shouldDeleteScriptById_whenIdExists() throws Exception {
		GroovyScript expectedGroovyScript = new GroovyScript("testScript", "return 2 + 2");
		expectedGroovyScript.setId(3);
		when(groovyScriptsService.deleteGroovyScriptById(3)).thenReturn(expectedGroovyScript);
		
		mockMvc.perform(delete("/api/groovyscripts/3")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", is(expectedGroovyScript.getId())))
		.andExpect(jsonPath("$.name", is(expectedGroovyScript.getName())))
		.andExpect(jsonPath("$.body", is(expectedGroovyScript.getBody())));
		
		verify(groovyScriptsService).deleteGroovyScriptById(3);
	}
	
	@Test
	public void shouldNotDeleteScriptById_whenIdDoesntExist() throws Exception {
		when(groovyScriptsService.deleteGroovyScriptById(100)).thenThrow(new ResourceNotFoundException());
		
		mockMvc.perform(delete("/api/groovyscripts/100")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound())
		.andExpect(status().reason(containsString("Resource not found")));
		
		verify(groovyScriptsService).deleteGroovyScriptById(100);
	}
	
	@Test
	public void shouldUpdateScript_whenIdExistsAndBodyIsValid() throws Exception {
		GroovyScript groovyScript = new GroovyScript("testScript", "return 2 + 2");
		groovyScript.setId(3);
		GroovyScript expectedGroovyScript = new GroovyScript("changedName", "changedBody");
		expectedGroovyScript.setId(3);
		when(groovyScriptsService.updateGroovyScript(groovyScript, 3)).thenReturn(expectedGroovyScript);
		
		mockMvc.perform(put("/api/groovyscripts/3")
		.contentType(MediaType.APPLICATION_JSON)
        .content(objectToJson(groovyScript)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id", is(expectedGroovyScript.getId())))
		.andExpect(jsonPath("$.name", is(expectedGroovyScript.getName())))
		.andExpect(jsonPath("$.body", is(expectedGroovyScript.getBody())));
		
		verify(groovyScriptsService).updateGroovyScript(groovyScript, 3);
	}
	
	@Test
	public void shouldNotUpdateScript_whenIdDoesntExist() throws Exception {
		GroovyScript groovyScript = new GroovyScript("testScript", "return 2 + 2");
		groovyScript.setId(3);
		when(groovyScriptsService.updateGroovyScript(groovyScript, 100)).thenThrow(new ResourceNotFoundException());
		
		mockMvc.perform(put("/api/groovyscripts/100")
		.contentType(MediaType.APPLICATION_JSON)
        .content(objectToJson(groovyScript)))
		.andExpect(status().isNotFound())
		.andExpect(status().reason(containsString("Resource not found")));
		
		verify(groovyScriptsService).updateGroovyScript(groovyScript, 100);
	}
	
	@Test
	public void shouldNotUpdateScript_whenBodyIsInvalid() throws Exception {
		GroovyScript groovyScript = new GroovyScript("", "return 2 + 2");
		groovyScript.setId(3);
		
		mockMvc.perform(put("/api/groovyscripts/3")
		.contentType(MediaType.APPLICATION_JSON)
        .content(objectToJson(groovyScript)))
		.andExpect(status().isBadRequest());
		
		verify(groovyScriptsService, times(0)).updateGroovyScript(groovyScript, 3);
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
