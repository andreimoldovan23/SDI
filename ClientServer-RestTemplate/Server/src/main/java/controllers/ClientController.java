package controllers;

import dtos.ClientDTO;
import lombok.RequiredArgsConstructor;
import mappers.ClientMapper;
import org.springframework.web.bind.annotation.*;
import services.interfaces.IClientService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientController {

    private final ClientMapper clientMapper;
    private final IClientService clientService;

    @PostMapping
    public void addClient(@RequestBody ClientDTO clientDTO)
    {
        clientService.Add(clientMapper.DTOtoClient(clientDTO));
    }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable String id)
    {
        clientService.Delete(Integer.parseInt(id));
    }

    @PutMapping("/{id}")
    public void updateClient(@RequestBody ClientDTO clientDTO)
    {
        clientService.Update(clientMapper.DTOtoClient(clientDTO));
    }

    @GetMapping("/filterClients")
    public List<ClientDTO> filterClients(@RequestParam(name = "name") String name)
    {
        return clientService.filterClientsByName(name).stream().map(clientMapper::clientToDTO).collect(Collectors.toList());
    }

    @GetMapping
    public List<ClientDTO> printClients()
    {
        return clientService.getAll().stream().map(clientMapper::clientToDTO).collect(Collectors.toList());
    }

    @GetMapping("/filterClientsByAge")
    public List<ClientDTO> filterClientsByAge(@RequestParam(name = "age1") String age1, @RequestParam(name = "age2") String age2)
    {
        return clientService.filterClientsInAgeInterval(Integer.parseInt(age1), Integer.parseInt(age2)).stream()
                .map(clientMapper::clientToDTO)
                .collect(Collectors.toList());
    }
}
