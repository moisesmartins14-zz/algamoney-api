package com.algawork.algamoneyapi.repository.lancamento;

import com.algawork.algamoneyapi.model.Lancamento;
import com.algawork.algamoneyapi.repository.filter.LancamentoFilter;

import java.util.List;

public interface LancamentoRepositoryQuery {

    public List<Lancamento> filtrar(LancamentoFilter lancamentoFilter);

}
