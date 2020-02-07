package com.algawork.algamoneyapi.services;

import com.algawork.algamoneyapi.model.Lancamento;
import com.algawork.algamoneyapi.model.Pessoa;
import com.algawork.algamoneyapi.repository.LancamentoRepository;
import com.algawork.algamoneyapi.repository.PessoaRepository;
import com.algawork.algamoneyapi.services.exception.PessoaInexistenteOuInativaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LancamentoService {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    public Lancamento salvar(Lancamento lancamento){

        Pessoa pessoa = pessoaRepository.findById(lancamento.getPessoa().getCodigo()).orElse(null);
        if (pessoa == null || pessoa.isInativo()){
            throw new PessoaInexistenteOuInativaException();
        }
        return lancamentoRepository.save(lancamento);
    }



}
