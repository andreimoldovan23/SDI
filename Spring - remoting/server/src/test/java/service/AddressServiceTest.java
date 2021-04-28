package service;

import config.Config;
import domain.Address;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import repository.DBRepo;
import services.IAddressService;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ContextConfiguration(classes = Config.class)
public class AddressServiceTest {

    Address a1, a2, a3;

    @Autowired
    DBRepo<Integer, Address> addressDBRepo;

    @Autowired
    IAddressService service;

    @BeforeEach
    public void setUp() {
        a1 = Address.Builder()
                .id(14)
                .city("aaaa")
                .street("bbbb")
                .number(1)
                .build();
        a2 = Address.Builder()
                .id(15)
                .city("cccc")
                .street("dddd")
                .number(2)
                .build();
        a3 = Address.Builder()
                .id(16)
                .city("eeee")
                .street("ffff")
                .number(1)
                .build();

        service.Add(a1);
        service.Add(a2);
        service.Add(a3);
    }

    @AfterEach
    public void tearDown() {
        a1 = null;
        a2 = null;
        a3 = null;

        addressDBRepo.deleteTable();
    }


    @Test
    public void deleteAddressByNumber() {
        service.deleteAddressWithNumber(1);
        Set<Address> addresses = service.getAll();
        assertFalse(addresses.contains(a1));
    }

}
