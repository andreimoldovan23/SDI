package serverConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.remoting.support.RemoteExporter;
import services.IAddressService;
import services.IClientService;
import services.ICoffeeService;
import services.IOrderService;

@Configuration
@Import(JpaConfig.class)
@ComponentScan(basePackages = {"serverServices", "domain.Validators"})
public class Config {

    @Autowired
    private IAddressService addressService;

    @Autowired
    private IClientService clientService;

    @Autowired
    private ICoffeeService coffeeService;

    @Autowired
    private IOrderService orderService;

    @Bean
    public RemoteExporter registerRMIClientService() {

        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName("clientService");
        exporter.setServiceInterface(IClientService.class);
        exporter.setService(clientService);
        exporter.setRegistryPort(1099);

        return exporter;
    }

    @Bean
    public RemoteExporter registerRMICoffeeService() {

        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName("coffeeService");
        exporter.setServiceInterface(ICoffeeService.class);
        exporter.setService(coffeeService);
        exporter.setRegistryPort(1099);

        return exporter;
    }

    @Bean
    public RemoteExporter registerRMIAddressService() {

        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName("addressService");
        exporter.setServiceInterface(IAddressService.class);
        exporter.setService(addressService);
        exporter.setRegistryPort(1099);

        return exporter;
    }

    @Bean
    public RemoteExporter registerRMIOrderService() {

        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName("orderService");
        exporter.setServiceInterface(IOrderService.class);
        exporter.setService(orderService);
        exporter.setRegistryPort(1099);

        return exporter;
    }

}
