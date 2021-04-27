package com.meli.reto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.meli.dto.SateliteDTO;
import com.meli.modelo.SateliteFB;

@Service
public class SatelteServiceImpl extends GenericServiceImpl<SateliteFB, SateliteDTO> implements SateliteServiceAPI{

	@Autowired
	private Firestore firestore;
	
	@Override
	public CollectionReference getCollection() {
		return firestore.collection("satellite");
	}

}
