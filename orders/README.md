# micro servicio orders
	- Contiene los siguientes endPoints:
		- POST /orders (Crear Ordenes)
		- GET /orders/{id} (Devuelve una Orden por su ID)
	- Contiene la entidad de Orders
	- Contiene los Dtos:
		- OrderRequest
		- PaymentRequest
	- Las ordenes se crean simulando el pago, por lo que se establece una conexion hacia el micro servicio Payments
	- Se validan que los productos existan via micro servicio products