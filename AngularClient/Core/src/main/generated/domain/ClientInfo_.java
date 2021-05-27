package domain;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ClientInfo.class)
public abstract class ClientInfo_ {

	public static volatile SingularAttribute<ClientInfo, String> phoneNumber;
	public static volatile SingularAttribute<ClientInfo, Integer> age;

	public static final String PHONE_NUMBER = "phoneNumber";
	public static final String AGE = "age";

}

