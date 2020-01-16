package com.algawork.algamoneyapi.exceptionhandler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

@ControllerAdvice
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    // 400 = bad request
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex,
                                                                  final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        String mensagemUsuario = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
        String mensagemDesenvolverdor = ex.getCause().toString();
        List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolverdor));

        return handleExceptionInternal(ex, erros, headers,
                HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<Erro> erros = criarListaDeErros(ex.getBindingResult());
        return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
    }

    private List<Erro> criarListaDeErros(BindingResult bindingResult) {
        List<Erro> erros = new ArrayList<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()){
        String mensagemUsuario = messageSource.getMessage(fieldError,LocaleContextHolder.getLocale());
        String mensagemDesenvolvendor = fieldError.toString();
        erros.add(new Erro(mensagemUsuario, mensagemDesenvolvendor));
    }
            return erros;
    }

    public static class Erro {
        private String mensagemUsuario;
        private String mensagemDesenvolverdor;

        public Erro(String mensagemUsuario, String mensagemDesenvolverdor) {
            this.mensagemUsuario = mensagemUsuario;
            this.mensagemDesenvolverdor = mensagemDesenvolverdor;
        }

        public String getMensagemUsuario() {
            return mensagemUsuario;
        }

        public String getMensagemDesenvolverdor() {
            return mensagemDesenvolverdor;
        }
    }
}

/*
  Nível 100 (Informativo) - O servidor reconhece uma solicitação
  Nível 200 (Sucesso) - O servidor concluiu a solicitação conforme o esperado
  Nível 300 (Redirecionamento) - O cliente precisa executar ações adicionais para concluir a solicitação
  Nível 400 (erro do cliente) - o cliente enviou uma solicitação inválida
  Nível 500 (erro do servidor) - o servidor não conseguiu atender a uma solicitação válida devido a um erro no servidor
Mais Informações http://bit.ly/HTTPStatusCodes
 */