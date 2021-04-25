package com.meli.reto;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meli.model.Position;
import com.meli.model.Respuesta;
import com.meli.model.Satelite;
import com.meli.model.SatelitesList;


@RestController
@RequestMapping("/topsecret")
public class TopsecretController {
	
	//metodo http post
	@PostMapping
	public Respuesta comunicacionNave(@RequestBody SatelitesList satelites){		
		
		Respuesta respuesta = new Respuesta();
		Position position = new Position();
			
		
		String[] mensajeFinal = new String[5];
		for(Satelite satelite: satelites.getSatellites()) {
			int cont = 0;
			for(String mensaje : satelite.getMessage()) {
				if(mensaje!="") {
					mensajeFinal[cont] = mensaje;
				}
				cont++;
			}
			respuesta.setMessage(mensajeFinal);
		}
		position.setX(12.9);
		position.setY(10.36);
		respuesta.setPosition(position);
		return respuesta;
	}
}
