package com.controlmoney.api.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.controlmoney.api.model.Pessoa;
import com.controlmoney.api.repository.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	
	public Pessoa atualizar(Long codigo, Pessoa pessoa ) {
		
		Optional<Pessoa> pessoaSalva = buscarPessoaPelocodigo(codigo);
		
		BeanUtils.copyProperties(pessoa, pessoaSalva.get(), "codigo");
		
		return pessoaRepository.save(pessoaSalva.get());
		
		
	}




	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Optional<Pessoa> pessoaSalva = buscarPessoaPelocodigo(codigo);
		
		pessoaSalva.get().setAtivo(ativo);
		pessoaRepository.save(pessoaSalva.get());
		
	}
	
	
	
	public Optional<Pessoa> buscarPessoaPelocodigo(Long codigo) {
		Optional<Pessoa> pessoaSalva = pessoaRepository.findById(codigo);
		
		if(!pessoaSalva.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}
		return pessoaSalva;
	}

}
