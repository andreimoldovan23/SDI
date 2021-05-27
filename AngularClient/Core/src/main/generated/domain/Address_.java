package domain;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Address.class)
public abstract class Address_ extends domain.BaseEntity_ {

	public static volatile SingularAttribute<Address, Integer> number;
	public static volatile SetAttribute<Address, Client> clients;
	public static volatile SingularAttribute<Address, String> city;
	public static volatile SingularAttribute<Address, String> street;

	public static final String NUMBER = "number";
	public static final String CLIENTS = "clients";
	public static final String CITY = "city";
	public static final String STREET = "street";

}

