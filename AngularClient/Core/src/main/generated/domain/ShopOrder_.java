package domain;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ShopOrder.class)
public abstract class ShopOrder_ extends domain.BaseEntity_ {

	public static volatile SingularAttribute<ShopOrder, Coffee> coffee;
	public static volatile SingularAttribute<ShopOrder, Client> client;
	public static volatile SingularAttribute<ShopOrder, LocalDateTime> time;
	public static volatile SingularAttribute<ShopOrder, Status> status;

	public static final String COFFEE = "coffee";
	public static final String CLIENT = "client";
	public static final String TIME = "time";
	public static final String STATUS = "status";

}

