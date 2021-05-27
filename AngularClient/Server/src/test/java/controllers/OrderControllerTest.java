package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.*;
import dtos.AddressDTO;
import dtos.ClientDTO;
import dtos.CoffeeDTO;
import dtos.ShopOrderDTO;
import mappers.CoffeeMapper;
import mappers.ShopOrderMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import services.interfaces.IOrderService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IOrderService orderService;

    @Mock
    private CoffeeMapper coffeeMapper;

    @Mock
    private ShopOrderMapper shopOrderMapper;

    @InjectMocks
    private OrderController orderController;

    private ShopOrder shopOrder1, shopOrder2;
    private ShopOrderDTO shopOrderDTO1, shopOrderDTO2;

    private Client client1, client2;
    private ClientDTO clientDTO1, clientDTO2;

    private Coffee coffee1, coffee2;
    private CoffeeDTO coffeeDTO1, coffeeDTO2;

    @Before
    public void setUp() {
        initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        initData();
    }

    @Test
    public void getAllTest() throws Exception {
        Set<ShopOrder> shopOrdersList = new HashSet<>(List.of(shopOrder1, shopOrder2));

        when(orderService.getAll()).thenReturn(shopOrdersList);
        when(shopOrderMapper.shopOrderToDTO(shopOrder1)).thenReturn(shopOrderDTO1);
        when(shopOrderMapper.shopOrderToDTO(shopOrder2)).thenReturn(shopOrderDTO2);

        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].client.firstName", anyOf(is(client1.getFirstName()), is(client2.getFirstName()))))
                .andExpect(jsonPath("$[1].coffee.origin", anyOf(is(coffee1.getOrigin()), is(coffee2.getOrigin()))))
                .andExpect(jsonPath("$[0].status", anyOf(is(Status.pending.toString()), is(Status.canceled.toString()))))
                .andExpect(jsonPath("$[1].time", anyOf(is(shopOrderDTO1.getTime()), is(shopOrder2.getTime()))));

        String result = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println(result);

        verify(orderService).getAll();
        verify(shopOrderMapper).shopOrderToDTO(shopOrder1);
        verify(shopOrderMapper).shopOrderToDTO(shopOrder2);
        verifyNoMoreInteractions(orderService, shopOrderMapper, coffeeMapper);
    }

    @Test
    public void filterClientCoffeesTest() throws Exception {
        when(orderService.filterClientCoffees(anyInt())).thenReturn(new HashSet<>(List.of(coffee1, coffee2)));
        when(coffeeMapper.coffeeToDTO(coffee1)).thenReturn(coffeeDTO1);
        when(coffeeMapper.coffeeToDTO(coffee2)).thenReturn(coffeeDTO2);

        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.get("/orders/filterClientCoffees")
                    .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", anyOf(is(coffee1.getName()), is(coffee2.getName()))))
                .andExpect(jsonPath("$[1].origin", anyOf(is(coffee1.getOrigin()), is(coffee2.getOrigin()))));

        String result = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println(result);

        verify(orderService).filterClientCoffees(1);
        verify(coffeeMapper).coffeeToDTO(coffee1);
        verify(coffeeMapper).coffeeToDTO(coffee2);
        verifyNoMoreInteractions(orderService, shopOrderMapper, coffeeMapper);
    }

    @Test
    public void filterClientOrdersTest() throws Exception {
        when(orderService.filterClientOrders(anyInt())).thenReturn(new HashSet<>(List.of(shopOrder1)));
        when(shopOrderMapper.shopOrderToDTO(shopOrder1)).thenReturn(shopOrderDTO1);

        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.get("/orders/filterClientOrders")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].client.firstName", is(client1.getFirstName())))
                .andExpect(jsonPath("$[0].coffee.name", is(coffee1.getName())));

        String result = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println(result);

        verify(orderService).filterClientOrders(1);
        verify(shopOrderMapper).shopOrderToDTO(shopOrder1);
        verifyNoMoreInteractions(orderService, shopOrderMapper, coffeeMapper);
    }

    @Test
    public void changeStatusTest() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.put("/orders/{id}", 1)
                    .param("status", "canceled"))
                .andExpect(status().isOk());

        verify(orderService).changeStatus(1, Status.canceled);
        verifyNoMoreInteractions(orderService, shopOrderMapper, coffeeMapper);
    }

    @Test
    public void buyCoffeeTest() throws Exception {
        mockMvc
                .perform(MockMvcRequestBuilders.put("/orders")
                    .param("clientId", "1")
                    .param("coffeeId", "1"))
                .andExpect(status().isOk());

        verify(orderService).buyCoffee(1, 1);
        verifyNoMoreInteractions(orderService, shopOrderMapper, coffeeMapper);
    }

    @Test
    public void deleteOrdersByDateTest() throws Exception {
        when(shopOrderMapper.DTOtoShopOrder(any())).thenReturn(shopOrder1);

        mockMvc
                .perform(MockMvcRequestBuilders.delete("/orders/deleteOrdersBetweenDates")
                    .param("date1", shopOrderDTO1.getTime())
                    .param("date2", shopOrderDTO2.getTime()))
                .andExpect(status().isOk());

        verify(orderService).deleteOrderByDate(shopOrder1.getTime(), shopOrder2.getTime());
        verify(shopOrderMapper, times(2)).DTOtoShopOrder(any());
        verifyNoMoreInteractions(orderService, shopOrderMapper, coffeeMapper);
    }

    private void initData() {
        initClients();
        initCoffees();
        initOrders();
    }

    private void initClients() {
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

    private void initCoffees() {
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

    private void initOrders() {
        shopOrder1 = ShopOrder.builder()
                .status(Status.pending)
                .time(LocalDateTime.of(2000, 10, 10, 10, 10, 10))
                .client(client1)
                .coffee(coffee1).build();

        shopOrder2 = ShopOrder.builder()
                .status(Status.canceled)
                .time(LocalDateTime.of(2000, 10, 10, 10, 10, 10))
                .client(client2)
                .coffee(coffee2).build();

        shopOrderDTO1 = new ShopOrderDTO();
        shopOrderDTO1.setStatus(Status.pending);
        shopOrderDTO1.setTime("2000-10-10//10::10::10");
        shopOrderDTO1.setClient(clientDTO1);
        shopOrderDTO1.setCoffee(coffeeDTO1);

        shopOrderDTO2 = new ShopOrderDTO();
        shopOrderDTO2.setStatus(Status.canceled);
        shopOrderDTO2.setTime("2000-10-10//10::10::10");
        shopOrderDTO2.setClient(clientDTO2);
        shopOrderDTO2.setCoffee(coffeeDTO2);
    }
}
