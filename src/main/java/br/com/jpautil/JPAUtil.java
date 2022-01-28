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

	@Produces // Injeta de forma autom�tica o EntityManager ,ou seja, n�o precisa ficar
				// chamando o m�todo manualmente
	@RequestScoped
	public EntityManager getEntityManager() {
		return factory.createEntityManager();

	}

	public Object getPrimaryKey(Object entity) {
		return factory.getPersistenceUnitUtil().getIdentifier(entity);

	}
}
