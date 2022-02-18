package com.micro.springboot.app.gateway.filters.factory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class EjemploGatewayFilterFactory extends AbstractGatewayFilterFactory<EjemploGatewayFilterFactory.Configuration>{

	private Logger log = LoggerFactory.getLogger(EjemploGatewayFilterFactory.class);	

	public EjemploGatewayFilterFactory() {
		super(Configuration.class);
	}

	@Override
	public GatewayFilter apply(Configuration config) {
		
		//Expresion lambda que implementa implicitamente el metodo 'filer' de interfaz GatewayFilter
		return new OrderedGatewayFilter((exchange, chain) -> {
			
			log.info("Ejecutando pre gateway filter factory: " + config.mensaje);
			
			return chain.filter(exchange)
					.then(Mono.fromRunnable(()->{
						
						Optional.ofNullable(config.cookieValor).ifPresent( valorCookie -> {
							exchange.getResponse().addCookie(ResponseCookie.from(config.cookieNombre, valorCookie).build());
						});
						
						log.info("Ejecutando post gateway filter factory: " + config.mensaje);						
					}));
		}, 2);
	}	
	
	//Metodo de la interfaz GateWayFilterFactory que permite establecer el nombre del filtro
	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "EjemploFiltroProductos";
	}
	

	//Metodo de interfaz ShortcutConfigurable para definir el orden de argumentos
	//de una linea configurados en el yml, especificando el nombre de los campos/argumentos
	@Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList("mensaje","cookieNombre","cookieNombre");
	}


	public static class Configuration {
		
		private String mensaje;
		private String cookieNombre;
		private String cookieValor;
		
		public String getMensaje() {
			return mensaje;
		}
		
		public void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}
		
		public String getCookieValor() {
			return cookieValor;
		}
		
		public void setCookieValor(String cookieValor) {
			this.cookieValor = cookieValor;
		}
		
		public String getCookieNombre() {
			return cookieNombre;
		}
		
		public void setCookieNombre(String cookieNombre) {
			this.cookieNombre = cookieNombre;
		}
	}

}
