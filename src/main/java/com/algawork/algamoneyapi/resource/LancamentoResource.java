package com.algawork.algamoneyapi.resource;

import com.algawork.algamoneyapi.event.RecursoCriadoEvent;
import com.algawork.algamoneyapi.exceptionhandler.AlgamoneyExceptionHandler;
import com.algawork.algamoneyapi.model.Lancamento;
import com.algawork.algamoneyapi.repository.LancamentoRepository;
import com.algawork.algamoneyapi.repository.filter.LancamentoFilter;
import com.algawork.algamoneyapi.services.LancamentoService;
import com.algawork.algamoneyapi.services.exception.PessoaInexistenteOuInativaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/lancamento")
public class LancamentoResource {

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @GetMapping
    public ResponseEntity<?> pesquisar(LancamentoFilter lancamentoFilter){

        List<Lancamento> lancamentos = lancamentoRepository.findAll();


        return  !lancamentos.isEmpty() ? ResponseEntity.ok(lancamentos) : ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Lancamento> criarLancamento(@Valid @RequestBody Lancamento lancamento,  HttpServletResponse response){
        Lancamento lancamentoSalva = lancamentoService.salvar(lancamento);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalva.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalva);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Lancamento> buscarPeloCodigoLancamento(@PathVariable Long codigo){
        Lancamento lancamento = this.lancamentoRepository.findById(codigo).orElse(null);

        return lancamento !=null ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();
    }

    @ExceptionHandler({PessoaInexistenteOuInativaException.class})
    public ResponseEntity<Object> handlerPessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex){
        String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
        String mensagemDesenvolverdor = ex.toString();
        List<AlgamoneyExceptionHandler.Erro> erros = Arrays.asList(new AlgamoneyExceptionHandler.Erro(mensagemUsuario, mensagemDesenvolverdor));
        return ResponseEntity.badRequest().body(erros);
    }
}
