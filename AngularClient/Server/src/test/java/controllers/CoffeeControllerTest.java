package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Coffee;
import dtos.CoffeeDTO;
import mappers.CoffeeMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import services.interfaces.ICoffeeService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CoffeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ICoffeeService coffeeService;

    @Mock
    private CoffeeMapper coffeeMapper;

    @InjectMocks
    private CoffeeController coffeeController;

    private Coffee coffee1, coffee2;
    private CoffeeDTO coffeeDTO1, coffeeDTO2;

    @Before
    public void setUp() {
        initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(coffeeController).build();
        initData();
    }

    @Test
    public void getAllTest() throws Exception {
        Set<Coffee> coffeeList = new HashSet<>(List.of(coffee1, coffee2));

        when(coffeeService.getAll()).thenReturn(coffeeList);
        when(coffeeMapper.coffeeToDTO(coffee1)).thenReturn(coffeeDTO1);
        when(coffeeMapper.coffeeToDTO(coffee2)).thenReturn(coffeeDTO2);

        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.get("/coffees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", anyOf(is(coffee1.getName()), is(coffee2.getName()))))
                .andExpect(jsonPath("$[1].origin", anyOf(is(coffee1.getOrigin()), is(coffee2.getOrigin()))));

        String result = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println(result);

        verify(coffeeService).getAll();
        verify(coffeeMapper).coffeeToDTO(coffee1);
        verify(coffeeMapper).coffeeToDTO(coffee2);
        verifyNoMoreInteractions(coffeeService, coffeeMapper);
    }

    @Test
    public void updateTest() throws Exception {
        when(coffeeMapper.DTOtoCoffee(coffeeDTO1)).thenReturn(coffee1);

        mockMvc
                .perform(MockMvcRequestBuilders.put("/coffees/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(toJsonString(coffeeDTO1)))
                .andExpect(status().isOk());

        verify(coffeeMapper).DTOtoCoffee(coffeeDTO1);
        verify(coffeeService).Update(coffee1);
        verifyNoMoreInteractions(coffeeService, coffeeMapper);
    }

    @Test
    public void addTest() throws Exception {
        when(coffeeMapper.DTOtoCoffee(coffeeDTO1)).thenReturn(coffee1);

        mockMvc
                .perform(MockMvcRequestBuilders.post("/coffees")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(toJsonString(coffeeDTO1)))
                .andExpect(status().isOk());

        verify(coffeeMapper).DTOtoCoffee(coffeeDTO1);
        verify(coffeeService).Add(coffee1);
        verifyNoMoreInteractions(coffeeService, coffeeMapper);
    }

    @Test
    public void deleteTest() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/coffees/{id}", 1))
                .andExpect(status().isOk());

        verify(coffeeService).Delete(1);
        verifyNoMoreInteractions(coffeeService, coffeeMapper);
    }

    @Test
    public void reportTest() throws Exception {
        when(coffeeService.byHowManyClientsWasOrdered(anyInt())).thenReturn(5);

        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.get("/coffees/report")
                        .param("id", "5"))
                .andExpect(status().isOk());

        assertEquals(resultActions.andReturn().getResponse().getContentAsString(), "5");

        verify(coffeeService).byHowManyClientsWasOrdered(5);
        verifyNoMoreInteractions(coffeeService, coffeeMapper);
    }

    @Test
    public void filterNameTest() throws Exception {
        when(coffeeService.filterCoffeesByName(anyString())).thenReturn(new HashSet<>(List.of(coffee1)));
        when(coffeeMapper.coffeeToDTO(coffee1)).thenReturn(coffeeDTO1);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/coffees/filterCoffees")
                        .param("name", "Arabica"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Arabica")))
                .andExpect(jsonPath("$[0].origin", is("Brazil")));

        verify(coffeeService).filterCoffeesByName("Arabica");
        verify(coffeeMapper).coffeeToDTO(coffee1);
        verifyNoMoreInteractions(coffeeService, coffeeMapper);
    }

    @Test
    public void filterOriginTest() throws Exception {
        when(coffeeService.filterCoffeesByOrigin(anyString())).thenReturn(new HashSet<>(List.of(coffee2)));
        when(coffeeMapper.coffeeToDTO(coffee2)).thenReturn(coffeeDTO2);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/coffees/filterCoffeesByOrigin")
                        .param("origin", "Sumatra"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Robusta")))
                .andExpect(jsonPath("$[0].origin", is("Sumatra")));

        verify(coffeeService).filterCoffeesByOrigin("Sumatra");
        verify(coffeeMapper).coffeeToDTO(coffee2);
        verifyNoMoreInteractions(coffeeService, coffeeMapper);
    }

    private String toJsonString(CoffeeDTO coffeeDTO) {
        try {
            return new ObjectMapper().writeValueAsString(coffeeDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void initData() {
        coffee1 = Coffee.builder()
                .name("Arabica")
                .origin("Brazil")
                .quantity(100).price(100).build();

        coffee2 = Coffee.builder()
                .name("Robusta")
                .origin("Sumatra")
                .quantity(100).price(100).build();

        coffeeDTO1 = new CoffeeDTO();
        coffeeDTO1.setName("Arabica");
        coffeeDTO1.setOrigin("Brazil");
        coffeeDTO1.setQuantity(100);
        coffeeDTO1.setPrice(100);

        coffeeDTO2 = new CoffeeDTO();
        coffeeDTO2.setName("Robusta");
        coffeeDTO2.setOrigin("Sumatra");
        coffeeDTO2.setQuantity(100);
        coffeeDTO2.setPrice(100);
    }
}
