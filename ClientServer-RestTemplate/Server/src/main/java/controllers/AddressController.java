package controllers;

import dtos.AddressDTO;
import lombok.RequiredArgsConstructor;
import mappers.AddressMapper;
import org.springframework.web.bind.annotation.*;
import services.interfaces.IAddressService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final IAddressService addressService;
    private final AddressMapper addressMapper;

    @PostMapping
    public void addAddress(@RequestBody AddressDTO addressDTO) {
        addressService.Add(addressMapper.DTOtoAddress(addressDTO));
    }

    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable String id) {
        addressService.Delete(Integer.parseInt(id));
    }

    @PutMapping("/{id}")
    public void updateAddress(@RequestBody AddressDTO addressDTO) {
        addressService.Update(addressMapper.DTOtoAddress(addressDTO));
    }

    @GetMapping
    public List<AddressDTO> printAddresses() {
        return addressService.getAll().stream().map(addressMapper::addressToDTO).collect(Collectors.toList());
    }

    @DeleteMapping("/deleteAddressesByNumber")
    public void deleteAddressesByNumber(@RequestParam(name = "number") String number) {
        addressService.deleteAddressWithNumber(Integer.parseInt(number));
    }

}
