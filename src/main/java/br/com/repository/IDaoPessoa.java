package br.com.repository;

import java.util.List;

import javax.faces.model.SelectItem;

import br.com.entidades.Pessoa;

public interface IDaoPessoa {

	Pessoa consutarUsurio(String login, String senha);
	
	List<SelectItem> listaEstados();
	
	List<Pessoa> relatorioPessoa(String nome, String cpf);
}
