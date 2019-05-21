package machalica.marcin.gss.groovyscripts;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
	public void testGetAllGroovyScripts() throws Exception {
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
	public void testGetAllGroovyScriptsWhileScriptTableEmpty() throws Exception {
		when(groovyScriptsService.getAllGroovyScripts()).thenReturn(new ArrayList<GroovyScript>());
		
		mockMvc.perform(get("/api/groovyscripts")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));
		
		verify(groovyScriptsService).getAllGroovyScripts();
	}
	
	@Test
	public void testGetGroovyScriptById() throws Exception {
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
	public void testGetGroovyScriptByIdException() throws Exception {
		when(groovyScriptsService.getGroovyScriptById(100)).thenThrow(new ResourceNotFoundException());

		mockMvc.perform(get("/api/groovyscripts/100")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(status().reason(containsString("Resource not found")));
		
		verify(groovyScriptsService).getGroovyScriptById(100);
	}

}
