## Descripción

Existen cinco clases. Cada una tiene una responsabilidad, aunque algunas tienen más de la que deberían.

__Cliente__ guarda los datos de quien hace la reserva: nombre, email y si es socio. Es una clase simple y no tiene lógica. 

__Funcion__ representa una proyección concreta de una película, con título, sala, hora y precio. También lleva la cuenta de los asientos ocupados en una lista de textos como "F5". Su método ocupar(fila, numero) hace dos cosas a la vez: comprueba si el asiento está libre, lanza una excepción si no lo está, y lo marca como ocupado si sí. Además, ocupados es una lista pública, así que cualquier clase puede modificarla directamente.

__Reserva__ agrupa los datos de una reserva: id, cliente, función, asientos, precio y si está cancelada. Tiene un método setCancelada, que cambia el estado con un setter genérico en lugar de una operación con nombre claro.

__SistemaReservas__ es la clase principal y la que concentra más problemas. Contiene la lógica del negocio:
- hacer(...) crea una reserva. Recibe los datos del cliente y los asientos como parámetros sueltos, tiene un parámetro que no usa (imprimir) y devuelve números (-1, -2, -3) para indicar errores. Además, marca los asientos uno a uno, así que si uno falla, los anteriores quedan ocupados sin reserva.
- calcularPrecio(reserva, tipo) decide el precio según un texto ("general" o "socio").
- descuentoDel15 y descuentoDel25 son dos métodos casi idénticos, y el segundo no se usa.
- cancelar(id) busca la reserva, libera sus asientos y la marca como cancelada. Para buscarla hace un casteo (Reserva) cada vez.
- buscar(id) devuelve la reserva como Object, y quien la usa debe hacer el casteo.

__Main__ es el ejemplo de uso. Crea una función y dos reservas, una para Ana y otra para Luis. Luis pide F7 y F6, pero F6 ya está ocupado. Como F7 se marca antes de que falle F6, se queda bloqueado, y la salida Ocupados: lo muestra. Después se cancela la reserva de Ana y se intenta cancelar una reserva inexistente.


## Problemas detectados 

| # | Problema | Archivo | Código actual | Técnica |
|---|---|---|---|---|
| 1 | Nombre poco claro | `SistemaReservas.java` | `public int hacer(...)` | Rename method |
| 2 | Parámetro sin uso | `SistemaReservas.java` | `boolean imprimir` | Remove parameter |
| 3 | Parámetro recibido pero ignorado | `SistemaReservas.java` | `String canal` | Add parameter |
| 4 | Datos del cliente desglosados | `SistemaReservas.java` | `String nombre, String email, boolean socio` | Preserve whole object |
| 5 | Asientos como arrays paralelos | `SistemaReservas.java` | `char[] filas, int[] numeros` | Introduce parameter object |
| 6 | Precio pasado como parámetro pudiendo calcularse | `SistemaReservas.java` | `calcularPrecio(Reserva r, String tipo)` usa `r.funcion.precio` | Replace parameter with method |
| 7 | Tarifa elegida con texto | `SistemaReservas.java` | `socio ? "socio" : "general"` y `tipo.equals("socio")` | Replace parameter with explicit method |
| 8 | Descuentos duplicados | `SistemaReservas.java` | `descuentoDel15` y `descuentoDel25` | Parameterize method |
| 9 | Consulta y modificación mezcladas | `Funcion.java` | `ocupar()` valida, modifica y lanza excepción | Separate query from modifier |
| 10 | Estado interno expuesto | `Funcion.java` | `public List<String> ocupados` | Hide method |
| 11 | Setter innecesario | `Funcion.java` | `setPrecio(double)` | Remove setting method |
| 12 | Setter innecesario | `Reserva.java` | `setCancelada(boolean)` | Remove setting method |
| 13 | Downcast repetido | `SistemaReservas.java` | `(Reserva) db.get(id)` | Encapsulate downcast |
| 14 | Códigos de error numéricos | `SistemaReservas.java` | `return -1`, `-2`, `-3` | Replace error code with exception |
| 15 | Control de flujo con excepciones | `SistemaReservas.java` | `try { f.ocupar(...) } catch (IllegalStateException e)` | Replace exception with test |
