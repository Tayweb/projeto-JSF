package br.com.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.dao.DaoGeneric;
import br.com.entidades.Lancamento;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoPessoa;

@ViewScoped
@Named(value = "relUsuarioBean")
public class RelUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String cpf;
	private String nome;

	private List<Pessoa> pessoa = new ArrayList<Pessoa>();

	@Inject
	private IDaoPessoa iDaoPessoa;
	
	@Inject
	private DaoGeneric<Pessoa> daoGeneric;

	public void buscarUsuario() {

		if ((cpf == null || cpf.isEmpty()) && (nome == null || nome.isEmpty())) {
			pessoa = daoGeneric.listaEntity(Pessoa.class);
		} else {
			pessoa = iDaoPessoa.relatorioPessoa(nome, cpf);
		}
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Pessoa> getPessoa() {
		return pessoa;
	}

	public void setPessoa(List<Pessoa> pessoa) {
		this.pessoa = pessoa;
	}

}
