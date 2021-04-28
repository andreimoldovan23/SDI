package controllers;

import dtos.CoffeeDTO;
import lombok.RequiredArgsConstructor;
import mappers.CoffeeMapper;
import org.springframework.web.bind.annotation.*;
import services.interfaces.ICoffeeService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coffees")
public class CoffeeController {

    private final ICoffeeService coffeeService;
    private final CoffeeMapper coffeeMapper;

    @PostMapping
    public void addCoffee(@RequestBody CoffeeDTO coffeeDTO)
    {
        coffeeService.Add(coffeeMapper.DTOtoCoffee(coffeeDTO));
    }

    @DeleteMapping("/{id}")
    public void deleteCoffee(@PathVariable String id)
    {
        coffeeService.Delete(Integer.parseInt(id));
    }

    @PutMapping("/{id}")
    public void updateCoffee(CoffeeDTO coffeeDTO)
    {
        coffeeService.Update(coffeeMapper.DTOtoCoffee(coffeeDTO));
    }

    @GetMapping("/filterCoffees")
    public List<CoffeeDTO> filterCoffees(@RequestParam(name = "name") String name)
    {
        return coffeeService.filterCoffeesByName(name).stream().map(coffeeMapper::coffeeToDTO).collect(Collectors.toList());
    }

    @GetMapping
    public List<CoffeeDTO> printCoffees()
    {
        return coffeeService.getAll().stream().map(coffeeMapper::coffeeToDTO).collect(Collectors.toList());
    }

    @GetMapping("/filterCoffeesByOrigin")
    public List<CoffeeDTO> filterCoffeesByOrigin(@RequestParam(name = "origin") String origin)
    {
        return coffeeService.filterCoffeesByOrigin(origin).stream().map(coffeeMapper::coffeeToDTO)
                .collect(Collectors.toList());
    }
}
