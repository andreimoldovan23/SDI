package domain;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Coffee.class)
public abstract class Coffee_ extends domain.BaseEntity_ {

	public static volatile SingularAttribute<Coffee, Integer> quantity;
	public static volatile SingularAttribute<Coffee, Integer> price;
	public static volatile SingularAttribute<Coffee, String> origin;
	public static volatile SingularAttribute<Coffee, String> name;
	public static volatile SetAttribute<Coffee, ShopOrder> orders;

	public static final String QUANTITY = "quantity";
	public static final String PRICE = "price";
	public static final String ORIGIN = "origin";
	public static final String NAME = "name";
	public static final String ORDERS = "orders";

}

