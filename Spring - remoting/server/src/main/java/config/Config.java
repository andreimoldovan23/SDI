package config;

import domain.Address;
import domain.Client;
import domain.Coffee;
import domain.Order;
import domain.Validators.AddressValidator;
import domain.Validators.ClientValidator;
import domain.Validators.CoffeeValidator;
import domain.Validators.OrderValidator;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.remoting.support.RemoteExporter;
import repository.*;
import service.AddressService;
import service.ClientService;
import service.CoffeeService;
import service.OrderService;
import services.IAddressService;
import services.IClientService;
import services.ICoffeeService;
import services.IOrderService;

import javax.sql.DataSource;

@Configuration
@PropertySources(
        @PropertySource("classpath:application.properties")
)
public class Config {

    @Value("${database}")
    String database;

    @Bean
    public JdbcOperations jdbcOperations() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }

    private DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:" + database);
        dataSource.setInitialSize(2);
        return dataSource;
    }

    @Bean
    public DBRepo<Integer, Address> addressRepo(JdbcOperations jdbcOperations) {
        return new AddressDbRepository(new AddressValidator(), jdbcOperations);
    }

    @Bean
    public DBRepo<Integer, Client> clientRepo(JdbcOperations jdbcOperations) {
        return new ClientDbRepository(new ClientValidator(), jdbcOperations);
    }

    @Bean
    public DBRepo<Integer, Coffee> coffeeRepo(JdbcOperations jdbcOperations) {
        return new CoffeeDbRepository(new CoffeeValidator(), jdbcOperations);
    }

    @Bean
    public DBRepo<Pair<Integer, Integer>, Order> orderRepo(JdbcOperations jdbcOperations) {
        return new OrderDbRepository(new OrderValidator(), jdbcOperations);
    }

    @Bean
    public AddressService addressService(DBRepo<Integer, Address> addressRepo) {
        return new AddressService(addressRepo);
    }

    @Bean
    public ClientService clientService(DBRepo<Integer, Client> clientRepo) {
        return new ClientService(clientRepo);
    }

    @Bean
    public CoffeeService coffeeService(DBRepo<Integer, Coffee> coffeeRepo) {
        return new CoffeeService(coffeeRepo);
    }

    @Bean
    public OrderService orderService(DBRepo<Pair<Integer, Integer>, Order> orderRepo,
                                      DBRepo<Integer, Coffee> coffeeRepo,
                                      DBRepo<Integer, Client> clientRepo) {
        return new OrderService(orderRepo, coffeeRepo, clientRepo);
    }

    @Bean
    public RemoteExporter registerRMIClientService(IClientService clientService) {

        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName("clientService");
        exporter.setServiceInterface(IClientService.class);
        exporter.setService(clientService);

        return exporter;
    }

    @Bean
    public RemoteExporter registerRMICoffeeService(ICoffeeService coffeeService) {

        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName("coffeeService");
        exporter.setServiceInterface(ICoffeeService.class);
        exporter.setService(coffeeService);

        return exporter;
    }

    @Bean
    public RemoteExporter registerRMIAddressService(IAddressService addressService) {

        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName("addressService");
        exporter.setServiceInterface(IAddressService.class);
        exporter.setService(addressService);

        return exporter;
    }

    @Bean
    public RemoteExporter registerRMIOrderService(IOrderService orderService) {

        RmiServiceExporter exporter = new RmiServiceExporter();
        exporter.setServiceName("orderService");
        exporter.setServiceInterface(IOrderService.class);
        exporter.setService(orderService);

        return exporter;
    }

}
