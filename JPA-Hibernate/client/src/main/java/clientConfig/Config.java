package clientConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import services.IAddressService;
import services.IClientService;
import services.ICoffeeService;
import services.IOrderService;

@Configuration
@ComponentScan(basePackages = {"clientServices", "ui"})
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

}
