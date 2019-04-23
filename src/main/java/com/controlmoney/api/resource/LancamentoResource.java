package com.controlmoney.api.resource;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.controlmoney.api.event.RecursoCriadoEvent;
import com.controlmoney.api.exceptionhandler.AlgamoneyExceptionHandler.Erro;
import com.controlmoney.api.model.Lancamento;
import com.controlmoney.api.repository.LancamentoRepository;
import com.controlmoney.api.repository.filter.LancamentoFilter;
import com.controlmoney.api.service.LancamentoService;
import com.controlmoney.api.service.ecception.PessoaInexistenteOuInativaException;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

	@Autowired
	LancamentoRepository lancamentoRepository;

	@Autowired
	LancamentoService lancamentoService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private MessageSource messageSource;

	@GetMapping
	public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {

		return lancamentoRepository.filtrar(lancamentoFilter, pageable);
	}

	@GetMapping("/{codigo}")
	public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {
		final Lancamento lancamento = lancamentoRepository.findById(codigo).get();
		return lancamento != null ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {

		final Lancamento lancamentoSalvo = lancamentoService.save(lancamento);

		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));

		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);

	}

	@ExceptionHandler({ PessoaInexistenteOuInativaException.class })
	public ResponseEntity<Object> hadlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {

		final String mensagemUsuario = messageSource.getMessage("pessoa.inativa-ou-inexistente", null,
				LocaleContextHolder.getLocale());
		final String mensagemDev = ExceptionUtils.getRootCauseMessage(ex);

		final List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDev));

		return ResponseEntity.badRequest().body(erros);
	}

	@ExceptionHandler({ NoSuchElementException.class })
	public ResponseEntity<Object> hadleNoSuchElementException(NoSuchElementException ex) {

		final String mensagemUsuario = messageSource.getMessage("pessoa.inativa-ou-inexistente", null,
				LocaleContextHolder.getLocale());
		final String mensagemDev = ExceptionUtils.getRootCauseMessage(ex);

		final List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDev));

		return ResponseEntity.badRequest().body(erros);
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		lancamentoRepository.deleteById(codigo);

	}

}
