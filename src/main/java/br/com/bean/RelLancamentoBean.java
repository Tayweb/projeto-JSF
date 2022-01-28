package br.com.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.dao.DaoGeneric;
import br.com.entidades.Lancamento;
import br.com.repository.IDaoLancamento;

@ViewScoped
@Named(value = "relLancamentoBean")
public class RelLancamentoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Lancamento> lancamentos = new ArrayList<Lancamento>();

	@Inject
	private IDaoLancamento daoLancamento;
	@Inject
	private DaoGeneric<Lancamento> daoGeneric;

	private Date dataInicial;
	private Date dataFinal;
	private String numeroNotaFiscal;

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getNumeroNotaFiscal() {
		return numeroNotaFiscal;
	}

	public void setNumeroNotaFiscal(String numeroNotaFiscal) {
		this.numeroNotaFiscal = numeroNotaFiscal;
	}

	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public void buscarLacamento() {
		
		if(dataInicial == null && dataFinal == null && (numeroNotaFiscal == null || numeroNotaFiscal.isEmpty())) {
			lancamentos = daoGeneric.listaEntity(Lancamento.class);
		}else {
			lancamentos = daoLancamento.relatorioLancamento(numeroNotaFiscal, dataInicial, dataFinal);
		}
	}

}
