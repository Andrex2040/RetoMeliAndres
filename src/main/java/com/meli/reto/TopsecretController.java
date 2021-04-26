package com.meli.reto;


import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer.Optimum;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import com.meli.modelo.Position;
import com.meli.modelo.Respuesta;
import com.meli.modelo.Satelite;
import com.meli.modelo.SatelitesList;


@RestController
@RequestMapping("/topsecret")
public class TopsecretController {
	private double[] posSatelite1 = {-500, -200};
	private double[] posSatelite2 = {100, -100};
	private double[] posSatelite3 = {500, 100};
	
	double[][] positions = new  double[][] { posSatelite1, posSatelite2, posSatelite3 };
	double[] distances = new double[3];
	String[] mensajeFinal = new String[5];
	
	/**
	 * METODO POST
	 * @param satelites
	 * @return Respuesta
	 */
	@PostMapping
	public Respuesta comunicacionNave(@RequestBody SatelitesList satelites){		
		
		Respuesta respuesta = new Respuesta();
		Position position = new Position();
		//int tamano = satelites.getSatellites().get(0).getMessage().length;
		//String[] mensajeFinal = new String[tamano];

		int cont2 = 0;
		for(Satelite satelite: satelites.getSatellites()) {
			
			distances[cont2] = satelite.getDistance();
			String mensajeFinal = getMessage(satelite.getMessage());
			
			respuesta.setMessage(mensajeFinal);
			cont2++;
		}
		
		double[] location = getLocation(positions, distances);
		
		
		position.setX(location[0]);
		position.setY(location[1]);
		respuesta.setPosition(position);
		return respuesta;
	}
	
	
	/**
	 	*Metodo para obtener la localizacion de la nave
	 	*@Andres Rivera
	 **/
	private double[] getLocation(double[][] positions, double [] distances) {
        TrilaterationFunction trileration = new TrilaterationFunction(positions, distances);
        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(trileration, new LevenbergMarquardtOptimizer());
        Optimum optimum = solver.solve();
        
        double[] centroid = optimum.getPoint().toArray();
        return  centroid;
    }
	
	/**
	 * RETORNA EL MENSAJE
	 * @param positions
	 * @param distances
	 * @return
	 */
	private String getMessage(String[] message) {
		int cont = 0;
	
		for(String mensaje : message) {
			if(mensaje!="") {
				mensajeFinal[cont] = mensaje;
			}
			cont++;
		}
		

		System.out.println(mensajeFinal[0]+mensajeFinal[1]+mensajeFinal[2]+mensajeFinal[3]+mensajeFinal[4]);
		String respuesta="";
		for(String mensaje : mensajeFinal) {
			respuesta+=mensaje+" ";
		}
		
        return  respuesta.trim();
    }
}
