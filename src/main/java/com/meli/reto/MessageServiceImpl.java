package com.meli.reto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.meli.dto.MessageDTO;
import com.meli.modelo.MessageFB;

@Service
public class MessageServiceImpl extends  GenericServiceImpl<MessageFB, MessageDTO> implements MessageServiceAPI{
	@Autowired
	private Firestore firestore;
	
	@Override
	public CollectionReference getCollection() {
		return firestore.collection("message");
	}
}
