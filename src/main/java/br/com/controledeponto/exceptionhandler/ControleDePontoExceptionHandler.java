package br.com.controledeponto.exceptionhandler;

import java.time.format.DateTimeParseException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.controledeponto.model.Mensagem;
import br.com.controledeponto.service.exception.DataExistenteException;
import br.com.controledeponto.service.exception.DiaDaSemanaInvalidoException;
import br.com.controledeponto.service.exception.EstouroNoLimiteDeHorariosException;
import br.com.controledeponto.service.exception.HorarioAcimaDoTrabalhadoException;
import br.com.controledeponto.service.exception.HorarioDeAlmocoNaoAtingidoException;
import br.com.controledeponto.service.exception.OrdemDeInsercaoInvalidaException;
import br.com.controledeponto.service.exception.RelatorioInexistenteException;

@ControllerAdvice
public class ControleDePontoExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return handleExceptionInternal(ex, new Mensagem("Campo obrigatório não informado"), new HttpHeaders(),
				HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler({ DateTimeParseException.class })
	public ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException ex, WebRequest request) {
		return handleExceptionInternal(ex, new Mensagem("Data e hora em formato inválido"), new HttpHeaders(),
				HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler({ DataExistenteException.class })
	public ResponseEntity<Object> handleDataExistenteException(DataExistenteException ex, WebRequest request) {
		return handleExceptionInternal(ex, new Mensagem("Horários já registrado"), new HttpHeaders(),
				HttpStatus.CONFLICT, request);
	}

	@ExceptionHandler({ EstouroNoLimiteDeHorariosException.class })
	public ResponseEntity<Object> handleEstouroNoLimiteDeHorariosException(EstouroNoLimiteDeHorariosException ex,
			WebRequest request) {
		return handleExceptionInternal(ex, new Mensagem("Apenas 4 horários podem ser registrados por dia"),
				new HttpHeaders(), HttpStatus.FORBIDDEN, request);
	}

	@ExceptionHandler({ DiaDaSemanaInvalidoException.class })
	public ResponseEntity<Object> handleDiaDaSemanaIncorretoException(DiaDaSemanaInvalidoException ex,
			WebRequest request) {
		return handleExceptionInternal(ex, new Mensagem("Sábado e domingo não são permitidos como dia de trabalho"),
				new HttpHeaders(), HttpStatus.FORBIDDEN, request);
	}

	@ExceptionHandler({ HorarioDeAlmocoNaoAtingidoException.class })
	public ResponseEntity<Object> handleHorarioDeAlmocoNaoAtingidoException(HorarioDeAlmocoNaoAtingidoException ex,
			WebRequest request) {
		return handleExceptionInternal(ex, new Mensagem("Deve haver no mínimo 1 hora de almoço"), new HttpHeaders(),
				HttpStatus.FORBIDDEN, request);
	}

	@ExceptionHandler({ OrdemDeInsercaoInvalidaException.class })
	public ResponseEntity<Object> handleOrdemDeInsercaoInvalidaException(OrdemDeInsercaoInvalidaException ex,
			WebRequest request) {
		return handleExceptionInternal(ex, new Mensagem("Não pode haver horários anteriores aos já registrados no dia"),
				new HttpHeaders(), HttpStatus.FORBIDDEN, request);
	}

	@ExceptionHandler({ HorarioAcimaDoTrabalhadoException.class })
	public ResponseEntity<Object> handleHorarioAcimaDoTrabalhadoException(HorarioAcimaDoTrabalhadoException ex,
			WebRequest request) {
		return handleExceptionInternal(ex, new Mensagem("Não pode alocar tempo maior que o tempo trabalhado no dia"),
				new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler({ RelatorioInexistenteException.class })
	public ResponseEntity<Object> handleRegistroInexistenteException(RelatorioInexistenteException ex,
			WebRequest request) {
		return handleExceptionInternal(ex, new Mensagem("Relatório não encontrado"), new HttpHeaders(),
				HttpStatus.NOT_FOUND, request);
	}

}
