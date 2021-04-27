package com.meli.reto;


import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer.Optimum;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import com.meli.dto.MessageDTO;
import com.meli.dto.SateliteDTO;
import com.meli.modelo.MessageFB;
import com.meli.modelo.Position;
import com.meli.modelo.Respuesta;
import com.meli.modelo.Satelite;
import com.meli.modelo.SateliteFB;
import com.meli.modelo.SatelitesList;

import java.util.List;


@RestController
@CrossOrigin("*")
public class TopsecretController {
	private static final String TAG = null;
	//variables que contienen las posiciones de los satelites
	private double[] posSatelite1 = {-500, -200};
	private double[] posSatelite2 = {100, -100};
	private double[] posSatelite3 = {500, 100};
	
	double[][] positions = new  double[][] { posSatelite1, posSatelite2, posSatelite3 };
	double[] distances = new double[3];
	String[] mensajeFinal = new String[5];
	
	@Autowired
	private SateliteServiceAPI sateliteServiceApi;
	@Autowired
	private MessageServiceAPI messageServiceApi;
	
	/**
	 * METODO POST
	 * @param satelites
	 * @return Respuesta
	 */
	@PostMapping
	@RequestMapping("/topsecret")
	public Respuesta comunicacionNave(@RequestBody SatelitesList satelites){		
		
		Respuesta respuesta = new Respuesta();
		Position position = new Position();

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
	 * SERVICO topsecret_split
	 * @throws Exception 
	 */

	@PostMapping
	@RequestMapping("/topsecret_split/{satellite_name}")
	public Respuesta separar(@PathVariable("satellite_name") String sateliteReportado, @RequestBody Satelite satelite) throws Exception{
		satelite.setName(sateliteReportado);
		Respuesta respuesta = new Respuesta();
		SateliteFB sateliteFB = new SateliteFB();
		MessageFB messageFB = new MessageFB();
		sateliteFB.setName(satelite.getName());
		sateliteFB.setDistance(satelite.getDistance());

		//almacenar en fire base el satelite 
		if (sateliteReportado == null || sateliteReportado.length() == 0 || sateliteReportado.equals("null")) {
			sateliteReportado = sateliteServiceApi.save(sateliteFB);
		} else {
			sateliteServiceApi.save(sateliteFB, sateliteReportado);
		}
		
		onComplete(null, true, null);
		
		//listar los mensajes almacenados para luego eliminarlos
		List<MessageDTO> messages;
		messages = messageServiceApi.getAll();
		
		for(MessageDTO m : messages) {
			if(satelite.getName().equals(m.getNameSatellite())) {
				messageServiceApi.delete(m.getId());
			}
		}
		
		//se recorre el arreglo de mensajes para eliminarlos
		int potionMessage = 0;
		for(String message : satelite.getMessage()){
			messageFB.setNameSatellite(satelite.getName());
			messageFB.setMessage(message);
			messageFB.setPosition(potionMessage);
			potionMessage++;
						
			sateliteReportado = messageServiceApi.save(messageFB);
		}
		
		//listar la coleecion de satellites para recuperar la distancia
		List<SateliteDTO> satelites;
		satelites = sateliteServiceApi.getAll();
		
		for(SateliteDTO s : satelites) {
			switch(s.getName().trim())
			{
			   case "kenobi" : 
				  distances[0] = s.getDistance();
			      break; 
			   
			   case "skywalker" : 
				   distances[1] = s.getDistance();
				   break;
				   
			   case "sato" : 
				   distances[2] = s.getDistance();
				   break;

			   default : 
			}			
		}
		
		//listar la coleecion de satellites para recuperar la distancia
		List<MessageDTO> messagesDto;
		messagesDto = messageServiceApi.getAll();
		
		String mensaje0 = "";
		String mensaje1 = "";
		String mensaje2 = "";
		String mensaje3 = "";
		String mensaje4 = "";
		String mensajeFinal = "";
		
		//SE CONSTRUYE EL MENSAJE
		for(MessageDTO m : messagesDto) {
			if(m.getPosition()==0) {
				if(!m.getMessage().equals("")){
					mensaje0=m.getMessage();
				}
			}
			if(m.getPosition()==1) {
				if(!m.getMessage().equals("")){
					mensaje1=m.getMessage();
				}
			}
			if(m.getPosition()==2) {
				if(!m.getMessage().equals("")){
					mensaje2=m.getMessage();
				}
			}
			if(m.getPosition()==3) {
				if(!m.getMessage().equals("")){
					mensaje3=m.getMessage();
				}
			}
			if(m.getPosition()==4) {
				if(!m.getMessage().equals("")){
					mensaje4=m.getMessage();
				}
			}
		}
		mensajeFinal=mensaje0+" "+mensaje1+" "+mensaje2+" "+mensaje3+" "+mensaje4; 
		respuesta.setMessage(mensajeFinal);
		
		double[] location = getLocation(positions, distances);
		Position position = new Position();
		
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

		String respuesta="";
		for(String mensaje : mensajeFinal) {
			respuesta+=mensaje+" ";
		}
		
        return  respuesta.trim();
    }
	
	public void onComplete(DatabaseError databaseError, boolean committed,
                           DataSnapshot currentData) {
    }
}
