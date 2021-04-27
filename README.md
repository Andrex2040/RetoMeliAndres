OPRERACION FUEGO DE QUASAR

Este proyecto busca dar solucion a un problama de comunicacion y ubicacion de una nave que esta pidiendo  auxilio.

USO DE LA APLICACION
Existen dos servicios que permiten obtener el mensaje de la nave y su ubicacion.

PRIMER SERVICIO
Cuando los tres satelines envian informacion al mismo tiempo para determinar el mensaje y la pocision de la nave:

Ingresa a la url
url: https://workinginmeli.herokuapp.com/topsecret

Se puede usar el aplicativo Postman para el consumo del servicio


![image](https://user-images.githubusercontent.com/3359026/116325137-146c5680-a787-11eb-9cc2-1dab8391a3cb.png)

ABRE UNA NUEVA VENTANA EN POSTAMAN

1. Selecciona POST
2. ingresa la siguiente url: https://workinginmeli.herokuapp.com/topsecret
3. Selecciona la opcion Body
4. Selecciona la opcion raw y JSON
5. Ingresa el json con la informacion en la siguiente estructura
   {
    "satellites": [
        {
            "name": "kenobi",
            "distance": 100.0,
            "message": ["este", "", "", "mensaje", ""]
        },
        {
            "name": "skywalker",
            "distance": 115.5,
            "message": ["", "es", "", "", "secreto"]
        },
        {
            "name": "sato",
            "distance": 142.7,
            "message": ["este", "", "un", "", ""]
        }
    ]
}

6. Da click en el boton Send
7. Visualiza la respuesta del servicio en la estructura como la siguiente:
   
   {
    "position": {
        "x": -58.315252587138595,
        "y": -69.55141837312165
    },
    "message": "este es un mensaje secreto"
  }


SEGUNDO SERVICIO
Cuando solo un sateline envia informacion:

![image](https://user-images.githubusercontent.com/3359026/116325887-d8d28c00-a788-11eb-8942-1a2eb3e80d81.png)

ABRE UNA NUEVA VENTANA EN POSTMAN

1. Selecciona POST O GET
2. ingresa la siguiente url: https://workinginmeli.herokuapp.com/topsecret_split/{satellite_name}
3. Donde dice {satellite_name} pon el nombre del satelite que se esta reportando ejemplo sato
4. Selecciona la opcion Body
5. Selecciona la opcion raw 
6. Selecciona la opcion JSON
7. Ingresa el json con la informacion en la siguiente estructura

   Nota del desarrollaoor: Este mensaje del ejemplo es un frace que le dije a mi perrita Belly mientras hacia este documento :)
   
   {
    "distance": 142.7,
    "message": ["", "", "SEA", "", "POR FAVOR"]
   }
9. 

COMPONENTES


LINK DE GITHUB

