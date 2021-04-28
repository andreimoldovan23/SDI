package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import services.*;
import ui.Console;

@Configuration
public class Config {

    @Bean
    public RmiProxyFactoryBean rmiProxyFactoryBeanAddressService() {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceUrl("rmi://localhost:1099/addressService");
        rmiProxyFactoryBean.setServiceInterface(IAddressService.class);
        return rmiProxyFactoryBean;
    }

    @Bean
    public RmiProxyFactoryBean rmiProxyFactoryBeanClientService() {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceUrl("rmi://localhost:1099/clientService");
        rmiProxyFactoryBean.setServiceInterface(IClientService.class);
        return rmiProxyFactoryBean;
    }

    @Bean
    public RmiProxyFactoryBean rmiProxyFactoryBeanCoffeeService() {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceUrl("rmi://localhost:1099/coffeeService");
        rmiProxyFactoryBean.setServiceInterface(ICoffeeService.class);
        return rmiProxyFactoryBean;
    }

    @Bean
    public RmiProxyFactoryBean rmiProxyFactoryBeanOrderService() {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceUrl("rmi://localhost:1099/orderService");
        rmiProxyFactoryBean.setServiceInterface(IOrderService.class);
        return rmiProxyFactoryBean;
    }

    @Bean
    public IAddressService addressService(IAddressService addressService) {
        return new AddressService(addressService);
    }

    @Bean
    public IClientService clientService(IClientService clientService) {
        return new ClientService(clientService);
    }

    @Bean
    public ICoffeeService coffeeService(ICoffeeService coffeeService) {
        return new CoffeeService(coffeeService);
    }

    @Bean
    public IOrderService orderService(IOrderService orderService) {
        return new OrderService(orderService);
    }

    @Bean
    public Console appConsole(IAddressService addressService, IClientService clientService, ICoffeeService coffeeService,
                       IOrderService orderService) {
        return new Console(addressService, clientService, coffeeService, orderService);
    }

}
