package com.controlmoney.api.repository.lancamento;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.controlmoney.api.model.Lancamento;
import com.controlmoney.api.model.Lancamento_;
import com.controlmoney.api.repository.filter.LancamentoFilter;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Lancamento> criteria = criteriaBuilder.createQuery(Lancamento.class);

		final Root<Lancamento> root = criteria.from(Lancamento.class);

		// criar restricoes
		final Predicate[] predicate = criarRestricoes(lancamentoFilter, criteriaBuilder, root);
		criteria.where(predicate);

		final TypedQuery<Lancamento> query = entityManager.createQuery(criteria);
		adicionarRestricoesDePagiancao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));

	}

	private Long total(LancamentoFilter lancamentoFilter) {

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		final Root<Lancamento> root = criteria.from(Lancamento.class);

		final Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);

		criteria.select(builder.count(root));

		return entityManager.createQuery(criteria).getSingleResult();
	}

	private void adicionarRestricoesDePagiancao(TypedQuery<Lancamento> query, Pageable pageable) {
		final int paginaAtual = pageable.getPageNumber();
		final int totalRegistrosPorPagina = pageable.getPageSize();
		final int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;

		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);

	}

	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder criteriaBuilder,
			Root<Lancamento> root) {

		final List<Predicate> predicates = new ArrayList<>();

		if (!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
			predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Lancamento_.descricao)),
					"%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));

			System.out.println("passei no descricao  ");
		}

		if (lancamentoFilter.getDataVencimentoDe() != null) {
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento),
					lancamentoFilter.getDataVencimentoDe()));
			System.out.println("passei no de ");

		}

		if (lancamentoFilter.getDataVencimentoAte() != null) {

			predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento),
					lancamentoFilter.getDataVencimentoAte()));

			System.out.println("passei no ate ");
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
