package com.controlmoney.api.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.controlmoney.api.model.Lancamento;
import com.controlmoney.api.model.Pessoa;
import com.controlmoney.api.repository.LancamentoRepository;
import com.controlmoney.api.repository.PessoaRepository;
import com.controlmoney.api.service.ecception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {
	
	@Autowired
	PessoaRepository pessoarepository;
	
	@Autowired
	LancamentoRepository lancamentoRepository;

	public Lancamento save(@Valid Lancamento lancamento) {
		
		Pessoa pessoa = pessoarepository.findById(lancamento.getPessoa().getCodigo()).get();
		
		if(pessoa == null || !pessoa.isAtivo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		
		
		
		return lancamentoRepository.save(lancamento);
	}

}
