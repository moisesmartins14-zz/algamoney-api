package com.algawork.algamoneyapi.repository.lancamento;

import com.algawork.algamoneyapi.model.Lancamento;
import com.algawork.algamoneyapi.repository.filter.LancamentoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LancamentoRepositoryQuery {

    Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);

}
