package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Client;
import dtos.ClientDTO;
import mappers.ClientMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import services.interfaces.IClientService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IClientService clientService;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientController clientController;

    private Client client1, client2;
    private ClientDTO clientDTO1, clientDTO2;

    @Before
    public void setUp() {
        initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
        initData();
    }

    @Test
    public void getAllTest() throws Exception {
        Set<Client> clientList = new HashSet<>(List.of(client1, client2));

        when(clientService.getAll()).thenReturn(clientList);
        when(clientMapper.clientToDTO(client1)).thenReturn(clientDTO1);
        when(clientMapper.clientToDTO(client2)).thenReturn(clientDTO2);

        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.get("/clients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", anyOf(is(client1.getFirstName()), is(client2.getFirstName()))))
                .andExpect(jsonPath("$[1].lastName", anyOf(is(client1.getLastName()), is(client2.getLastName()))));

        String result = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println(result);

        verify(clientService).getAll();
        verify(clientMapper).clientToDTO(client1);
        verify(clientMapper).clientToDTO(client2);
        verifyNoMoreInteractions(clientService, clientMapper);
    }

    @Test
    public void updateTest() throws Exception {
        when(clientMapper.DTOtoClient(clientDTO1)).thenReturn(client1);

        mockMvc
                .perform(MockMvcRequestBuilders.put("/clients/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(toJsonString(clientDTO1)))
                .andExpect(status().isOk());

        verify(clientMapper).DTOtoClient(clientDTO1);
        verify(clientService).Update(client1);
        verifyNoMoreInteractions(clientService, clientMapper);
    }

    @Test
    public void addTest() throws Exception {
        when(clientMapper.DTOtoClient(clientDTO1)).thenReturn(client1);

        mockMvc
                .perform(MockMvcRequestBuilders.post("/clients")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(toJsonString(clientDTO1)))
                .andExpect(status().isOk());

        verify(clientMapper).DTOtoClient(clientDTO1);
        verify(clientService).Add(client1);
        verifyNoMoreInteractions(clientService, clientMapper);
    }

    @Test
    public void deleteTest() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/clients/{id}", 1))
                .andExpect(status().isOk());

        verify(clientService).Delete(1);
        verifyNoMoreInteractions(clientService, clientMapper);
    }

    @Test
    public void reportTest() throws Exception {
        when(clientService.howManyCoffeesClientOrdered(anyInt())).thenReturn(5);

        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.get("/clients/report")
                        .param("id", "5"))
                .andExpect(status().isOk());

        assertEquals(resultActions.andReturn().getResponse().getContentAsString(), "5");

        verify(clientService).howManyCoffeesClientOrdered(5);
        verifyNoMoreInteractions(clientService, clientMapper);
    }

    @Test
    public void filterNameTest() throws Exception {
        when(clientService.filterClientsByName(anyString())).thenReturn(new HashSet<>(List.of(client1)));
        when(clientMapper.clientToDTO(client1)).thenReturn(clientDTO1);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/clients/filterClients")
                    .param("name", "Mike"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("Mike")))
                .andExpect(jsonPath("$[0].lastName", is("Tyson")));

        verify(clientService).filterClientsByName("Mike");
        verify(clientMapper).clientToDTO(client1);
        verifyNoMoreInteractions(clientService, clientMapper);
    }

    @Test
    public void filterAgeTest() throws Exception {
        when(clientService.filterClientsInAgeInterval(anyInt(), anyInt())).thenReturn(new HashSet<>(List.of(client2)));
        when(clientMapper.clientToDTO(client2)).thenReturn(clientDTO2);

        mockMvc
                .perform(MockMvcRequestBuilders.get("/clients/filterClientsByAge")
                        .param("age1", "20")
                        .param("age2", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("Stephen")))
                .andExpect(jsonPath("$[0].lastName", is("King")));

        verify(clientService).filterClientsInAgeInterval(20, 50);
        verify(clientMapper).clientToDTO(client2);
        verifyNoMoreInteractions(clientService, clientMapper);
    }

    private String toJsonString(ClientDTO clientDTO) {
        try {
            return new ObjectMapper().writeValueAsString(clientDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void initData() {
        client1 = Client.builder()
                .firstName("Mike")
                .lastName("Tyson").build();
        client1.setAge(15);
        client1.setPhoneNumber("+40748234342");

        client2 = Client.builder()
                .firstName("Stephen")
                .lastName("King").build();
        client2.setAge(45);
        client2.setPhoneNumber("+40748234342");

        clientDTO1 = new ClientDTO();
        clientDTO1.setFirstName("Mike");
        clientDTO1.setLastName("Tyson");
        clientDTO1.setAge(15);
        clientDTO1.setPhoneNumber("+40748234342");

        clientDTO2 = new ClientDTO();
        clientDTO2.setFirstName("Stephen");
        clientDTO2.setLastName("King");
        clientDTO2.setAge(45);
        clientDTO2.setPhoneNumber("+40748234342");
    }
}
