package br.com.jpautil;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped // Somente Uma instancia para o projeto inteiro
public class JPAUtil {

	private EntityManagerFactory factory = null;

	public JPAUtil() {
		if (factory == null) {
			factory = Persistence.createEntityManagerFactory("cursojsf");

		}
	}

	@Produces // Injeta de forma automática o EntityManager ,ou seja, não precisa ficar
				// chamando o método manualmente
	@RequestScoped
	public EntityManager getEntityManager() {
		return factory.createEntityManager();

	}

	public Object getPrimaryKey(Object entity) {
		return factory.getPersistenceUnitUtil().getIdentifier(entity);

	}
}
