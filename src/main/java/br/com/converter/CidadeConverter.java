package br.com.converter;

import java.io.Serializable;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;

import br.com.entidades.Cidades;



@FacesConverter(forClass = Cidades.class, value = "cidadeConverter")
public class CidadeConverter implements Serializable, Converter {

	private static final long serialVersionUID = 1L;


	
	@Override // Retorna o objeto inteiro
	public Object getAsObject(FacesContext context, UIComponent component, String codigoCidade) {

		EntityManager entityManager = CDI.current().select(EntityManager.class).get();
		

		Cidades cidades = (Cidades) entityManager.find(Cidades.class, Long.parseLong(codigoCidade));

		return cidades;
	}

	@Override // Retorna apenas o c�digo em String
	public String getAsString(FacesContext context, UIComponent component, Object cidade) {

		if (cidade == null) {
			return null;
		}

		/*
		 * O instanceof � um operador que permite testar se um objeto � uma inst�ncia de
		 * um tipo espec�fico de uma class, subclass ou interface. O instanceof em java
		 * tamb�m � conhecida como operador de compara��o de tipos, isso porque compara
		 * a inst�ncia com o tipo.
		 */

		if (cidade instanceof Cidades) {
			return ((Cidades) cidade).getId().toString();

		} else {
			return cidade.toString();
		}

	}

}
