package com.controlmoney.api.resource;

import com.controlmoney.api.event.RecursoCriadoEvent;
import com.controlmoney.api.exceptionhandler.AlgamoneyExceptionHandler.Erro;
import com.controlmoney.api.model.Lancamento;
import com.controlmoney.api.repository.LancamentoRepository;
import com.controlmoney.api.repository.filter.LancamentoFilter;
import com.controlmoney.api.repository.projection.ResumoLancamento;
import com.controlmoney.api.service.LancamentoService;
import com.controlmoney.api.service.ecception.PessoaInexistenteOuInativaException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

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
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {

        return lancamentoRepository.filtrar(lancamentoFilter, pageable);
    }

    @GetMapping(params = "resumo")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {

        return lancamentoRepository.resumir(lancamentoFilter, pageable);
    }

    @GetMapping("/{codigo}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {
        final Lancamento lancamento = lancamentoRepository.findById(codigo).get();
        return lancamento != null ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
    public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {

        final Lancamento lancamentoSalvo = lancamentoService.save(lancamento);

        publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));

        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
    }

    @ExceptionHandler({PessoaInexistenteOuInativaException.class})
    public ResponseEntity<Object> hadlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {

        final String mensagemUsuario = messageSource.getMessage("pessoa.inativa-ou-inexistente", null,
                LocaleContextHolder.getLocale());
        final String mensagemDev = ExceptionUtils.getRootCauseMessage(ex);

        final List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDev));

        return ResponseEntity.badRequest().body(erros);
    }

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<Object> hadleNoSuchElementException(NoSuchElementException ex) {

        final String mensagemUsuario = messageSource.getMessage("pessoa.inativa-ou-inexistente", null,
                LocaleContextHolder.getLocale());
        final String mensagemDev = ExceptionUtils.getRootCauseMessage(ex);

        final List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDev));

        return ResponseEntity.badRequest().body(erros);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('write')")
    public void remover(@PathVariable Long codigo) {
        lancamentoRepository.deleteById(codigo);

    }

}
