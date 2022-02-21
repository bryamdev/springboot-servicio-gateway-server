package com.micro.springboot.app.gateway.filters;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;



@Component
public class EjemploGlobalFilter implements GlobalFilter, Ordered {

	private final Logger log = LoggerFactory.getLogger(EjemploGlobalFilter.class);
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		log.info("Ejecutando filtro pre!");
		
		exchange.getRequest().mutate().headers(h -> h.add("token", "123456"));
		
		//el metodo filter del chain continuidad con la ejecucion de los demas filtros
		//el operador then se ejecuta despues de la ejecucion de todos los filtros, finalizado 
		//... su proceso y se haya obtenido la respuesta.
		//El Mono.fromRunnable() permite crear un objeto reactivo que esta disponible despues de
		//... el procesamiento del request
		return chain.filter(exchange)
				.then(Mono.fromRunnable(() -> {
					log.info("Ejecutando filtro post!");
					
					//uso de api Stream(Java funcional) con Optional para evaluar un valor (como un if)
					Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("token")).ifPresent(valor -> {
						exchange.getResponse().getHeaders().add("token", valor);
					});
					
					exchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "rojo").build());
					exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
				}));
	}
	
	//metodo implementado de la interfaz Ordered para establecer orden de ejecucion del filtro
	//El orden de ejecucion es FIFO, -1 especifica que es el primero en pre y ultimo en post
	@Override
	public int getOrder() {
		return 2;
	}

	
	
	
}
