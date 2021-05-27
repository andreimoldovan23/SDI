package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Address;
import dtos.AddressDTO;
import mappers.AddressMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import services.interfaces.IAddressService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.hamcrest.core.Is.is;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AddressControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IAddressService addressService;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressController addressController;

    private Address address1, address2;
    private AddressDTO addressDTO1, addressDTO2;

    @Before
    public void setUp() {
        initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(addressController).build();
        initData();
    }

    @Test
    public void getAllTest() throws Exception {
        Set<Address> addressList = new HashSet<>(List.of(address1, address2));

        when(addressService.getAll()).thenReturn(addressList);
        when(addressMapper.addressToDTO(address1)).thenReturn(addressDTO1);
        when(addressMapper.addressToDTO(address2)).thenReturn(addressDTO2);

        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.get("/addresses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].city", anyOf(is(address1.getCity()), is(address2.getCity()))))
                .andExpect(jsonPath("$[1].street", anyOf(is(address1.getStreet()), is(address2.getStreet()))));

        String result = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println(result);

        verify(addressService).getAll();
        verify(addressMapper).addressToDTO(address1);
        verify(addressMapper).addressToDTO(address2);
        verifyNoMoreInteractions(addressService, addressMapper);
    }

    @Test
    public void updateTest() throws Exception {
        when(addressMapper.DTOtoAddress(addressDTO1)).thenReturn(address1);

        mockMvc
                .perform(MockMvcRequestBuilders.put("/addresses/{id}", 1)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(toJsonString(addressDTO1)))
                .andExpect(status().isOk());

        verify(addressMapper).DTOtoAddress(addressDTO1);
        verify(addressService).Update(address1);
        verifyNoMoreInteractions(addressService, addressMapper);
    }

    @Test
    public void addTest() throws Exception {
        when(addressMapper.DTOtoAddress(addressDTO1)).thenReturn(address1);

        mockMvc
                .perform(MockMvcRequestBuilders.post("/addresses")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(toJsonString(addressDTO1)))
                .andExpect(status().isOk());

        verify(addressMapper).DTOtoAddress(addressDTO1);
        verify(addressService).Add(address1);
        verifyNoMoreInteractions(addressService, addressMapper);
    }

    @Test
    public void deleteTest() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/addresses/{id}", 1))
                .andExpect(status().isOk());

        verify(addressService).Delete(1);
        verifyNoMoreInteractions(addressService, addressMapper);
    }

    @Test
    public void reportTest() throws Exception {
        when(addressService.howManyClientsLiveHere(anyInt())).thenReturn(5);

        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.get("/addresses/report")
                    .param("id", "5"))
                .andExpect(status().isOk());

        assertEquals(resultActions.andReturn().getResponse().getContentAsString(), "5");

        verify(addressService).howManyClientsLiveHere(5);
        verifyNoMoreInteractions(addressService, addressMapper);
    }

    @Test
    public void deleteByNumberTest() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/addresses/deleteAddressesByNumber")
                        .param("number", "5"))
                .andExpect(status().isOk());

        verify(addressService).deleteAddressWithNumber(5);
        verifyNoMoreInteractions(addressService, addressMapper);
    }

    private String toJsonString(AddressDTO addressDTO) {
        try {
            return new ObjectMapper().writeValueAsString(addressDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void initData() {
        address1 = Address.builder()
                .city("Los Angeles").street("Beverly Hills").number(100).build();
        address2 = Address.builder()
                .city("Moscow").street("Kremlin").number(120).build();

        addressDTO1 = new AddressDTO();
        addressDTO1.setCity("Los Angeles");
        addressDTO1.setStreet("Beverly Hills");
        addressDTO1.setNumber(100);

        addressDTO2 = new AddressDTO();
        addressDTO2.setCity("Moscow");
        addressDTO2.setStreet("Kremlin");
        addressDTO2.setNumber(120);
    }
}
