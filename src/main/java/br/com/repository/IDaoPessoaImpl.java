package br.com.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Estados;
import br.com.entidades.Pessoa;

@Named
public class IDaoPessoaImpl implements IDaoPessoa, Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private EntityManager entityManager;

	@Override
	public Pessoa consutarUsurio(String login, String senha) {

		Pessoa pessoa = null;

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		try {
			pessoa = (Pessoa) entityManager
				.createQuery("select p from Pessoa p where p.login = '" + login + "' and p.senha = '" + senha + "'")
				.getSingleResult();
		} catch (javax.persistence.NoResultException e) {
			
		}
		

		transaction.commit();

		return pessoa;
	}

	@Override
	public List<SelectItem> listaEstados() {

		List<SelectItem> selectItems = new ArrayList<SelectItem>();

		List<Estados> estados = entityManager.createQuery("from Estados").getResultList();

		for (Estados estado : estados) { // converte a lista de estado para um selectItems
			selectItems.add(new SelectItem(estado, estado.getNome()));
		}

		return selectItems;
	}

	@Override
	public List<Pessoa> relatorioPessoa(String nome, String cpf) {
		List<Pessoa> pessoa = new ArrayList<Pessoa>();

		StringBuilder sql = new StringBuilder();
		
		sql.append("select p from Pessoa p");

		if ((cpf == null || cpf.isEmpty()) && nome != null && !nome.isEmpty()) {
			sql.append(" where upper (p.nome) like '").append(nome.trim().toUpperCase()).append("%'");
		
		}else if ((nome == null || nome.isEmpty()) && cpf !=null &&  !cpf.isEmpty() ) {
			sql.append(" where p.cpf = '").append(cpf.trim()).append("'");
			
		}else if (nome != null && !nome.isEmpty() && cpf !=null && !cpf.isEmpty() ) {
			sql.append(" where upper (p.nome) like '").append(nome.trim()).append("%' ");
			sql.append(" and p.cpf = '").append(cpf.trim()).append("' ");
		}
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		pessoa = entityManager.createQuery(sql.toString()).getResultList();
		transaction.commit();
		
		return pessoa;
	}

}
