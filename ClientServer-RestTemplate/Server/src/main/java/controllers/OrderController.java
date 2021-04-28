package controllers;

import domain.Status;
import dtos.CoffeeDTO;
import dtos.ShopOrderDTO;
import lombok.RequiredArgsConstructor;
import mappers.CoffeeMapper;
import mappers.ShopOrderMapper;
import org.springframework.web.bind.annotation.*;
import services.interfaces.IOrderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final IOrderService orderService;
    private final CoffeeMapper coffeeMapper;
    private final ShopOrderMapper shopOrderMapper;

    @GetMapping
    public List<ShopOrderDTO> printOrders()
    {
        return orderService.getAll().stream().map(shopOrderMapper::shopOrderToDTO).collect(Collectors.toList());
    }

    @GetMapping("/filterClientCoffees")
    public List<CoffeeDTO> filterClientCoffees(@RequestParam(name = "id") String id)
    {
        return orderService.filterClientCoffees(Integer.parseInt(id)).stream().map(coffeeMapper::coffeeToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/filterClientOrders")
    public List<ShopOrderDTO> filterClientOrders(@RequestParam(name = "id") String id)
    {
        return orderService.filterClientOrders(Integer.parseInt(id)).stream().map(shopOrderMapper::shopOrderToDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public void changeOrderStatus(@PathVariable String id, @RequestParam(name = "status") String status)
    {
        orderService.changeStatus(Integer.parseInt(id), Status.valueOf(status));
    }

    @PutMapping
    public void buyCoffee(@RequestParam(name = "clientId") String clientId,
                          @RequestParam(name = "coffeeId") String coffeeId,
                          @RequestParam(name = "orderId") String orderId)
    {
    	orderService.buyCoffee(Integer.parseInt(orderId), Integer.parseInt(clientId), Integer.parseInt(coffeeId));

    }

    @DeleteMapping("/deleteOrdersBetweenDates")
    public void deleteOrdersBetweenDates(@RequestParam(name = "date1") String d1, @RequestParam(name = "date2") String d2)
    {
        orderService.deleteOrderByDate(LocalDateTime.parse(d1), LocalDateTime.parse(d2));
    }
}
