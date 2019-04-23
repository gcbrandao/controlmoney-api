package com.controlmoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.controlmoney.api.model.Lancamento;
import com.controlmoney.api.repository.lancamento.LancamentoRepositoryQuery;


public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery{

}
