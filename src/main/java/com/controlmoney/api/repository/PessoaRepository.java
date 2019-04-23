package com.controlmoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.controlmoney.api.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{

}
