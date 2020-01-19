package com.algawork.algamoneyapi.resource;

import com.algawork.algamoneyapi.event.RecursoCriadoEvent;
import com.algawork.algamoneyapi.model.Pessoa;
import com.algawork.algamoneyapi.repository.PessoaRepository;
import com.algawork.algamoneyapi.services.PessoaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private PessoaRepository pessoaRepository;

    @GetMapping
    public ResponseEntity<?> listarPessoa() {
        List<Pessoa> pessoas = pessoaRepository.findAll();
        return !pessoas.isEmpty() ? ResponseEntity.ok(pessoas) : ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Pessoa> criarPessoa(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));

        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Pessoa> buscarPeloCodigoPessoa(@PathVariable Long codigo) {

        Pessoa pessoa = this.pessoaRepository.findById(codigo).orElse(null);
        return pessoa != null ? ResponseEntity.ok(pessoa) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long codigo) {

        pessoaRepository.deleteById(codigo);

    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Pessoa> atualizar(@PathVariable Long codigo, @Valid @RequestBody Pessoa pessoa) {
        Pessoa pessoaSalva = pessoaRepository.findById(codigo).orElse(null);

        if (pessoaSalva == null) {
            throw new EmptyResultDataAccessException(1);
        }
        BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");
        pessoaRepository.save(pessoaSalva);

        return ResponseEntity.ok(pessoaSalva);

    }

    @PutMapping("/{codigo}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarPropriedadeAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo){
        pessoaService.atualizarPropriedadeAtivo(codigo,ativo);
    }

}
