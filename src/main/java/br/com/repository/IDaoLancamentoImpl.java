package br.com.repository;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Lancamento;

@Named
public class IDaoLancamentoImpl implements IDaoLancamento, Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private EntityManager entityManager;

	@Override
	public List<Lancamento> consultar(Long codUser) {
		List<Lancamento> lista = null;

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		lista = entityManager.createQuery(" from Lancamento where usuarioPessoa.id = " + codUser).getResultList();

		transaction.commit();

		return lista;

	}

	@Override
	public List<Lancamento> consultarLimit10(Long codUser) {
		List<Lancamento> lista = null;

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		lista = entityManager.createQuery(" from Lancamento where usuarioPessoa.id = " + codUser + " order by id desc")
				.setMaxResults(5).getResultList();

		transaction.commit();

		return lista;

	}

	@Override
	public List<Lancamento> relatorioLancamento(String numeroNotaFiscal, Date dataInicial, Date dataFinal) {

		List<Lancamento> lancamentos = new ArrayList<Lancamento>();

		StringBuilder sql = new StringBuilder();

		sql.append("select l from Lancamento l");

		if (dataInicial == null && dataFinal == null && numeroNotaFiscal != null && !numeroNotaFiscal.isEmpty()) {
			sql.append(" where l.numeroNotaFiscal = '").append(numeroNotaFiscal.trim()).append("'");

		} else if (numeroNotaFiscal == null || (numeroNotaFiscal != null && numeroNotaFiscal.isEmpty()) && dataInicial != null && dataFinal == null) {

			String dataIniString = new SimpleDateFormat("yyyy-MM-dd").format(dataInicial);
			sql.append(" where l.dataInicial >= '").append(dataIniString).append("'");

		} else if (numeroNotaFiscal == null
				|| (numeroNotaFiscal != null && numeroNotaFiscal.isEmpty()) && dataInicial == null && dataFinal != null) {

			String dataFinString = new SimpleDateFormat("yyyy-MM-dd").format(dataFinal);
			sql.append(" where l.dataFinal <= '").append(dataFinString).append("'");

		} else if (numeroNotaFiscal == null
				|| (numeroNotaFiscal != null && numeroNotaFiscal.isEmpty()) && dataInicial != null && dataFinal != null) {

			String dataIniString = new SimpleDateFormat("yyyy-MM-dd").format(dataInicial);
			String dataFinString = new SimpleDateFormat("yyyy-MM-dd").format(dataFinal);

			sql.append(" where l.dataInicial >= '").append(dataIniString).append("' ");
			sql.append(" and l.dataFinal <= '").append(dataFinString).append("' ");
			
		}else if (numeroNotaFiscal != null && !numeroNotaFiscal.isEmpty() && dataInicial != null && dataFinal != null) {

			String dataIniString = new SimpleDateFormat("yyyy-MM-dd").format(dataInicial);
			String dataFinString = new SimpleDateFormat("yyyy-MM-dd").format(dataFinal);

			sql.append(" where l.dataInicial >= '").append(dataIniString).append("' ");
			sql.append(" and l.dataFinal <= '").append(dataFinString).append("' ");
			sql.append(" and l.numeroNotaFiscal = '").append(numeroNotaFiscal.trim()).append("'");
		}

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		lancamentos = entityManager.createQuery(sql.toString()).getResultList();
		transaction.commit();
		return lancamentos;

	}
}
