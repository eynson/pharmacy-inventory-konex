INTRODUCCION
Prueba técnica para desarrolladores full stack en Java con Spring Boot y Angular, con el fin de evaluar conocimientos y metodologías aplicadas para el desarrollo.
REQUISITOS FUNCIONALES
La empresa Konex actualmente cuenta con un proceso de inventario para sus droguerías, este inventario se lleva manual en los libros de la empresa, se quiere poder automatizar el proceso de inventario y venta de los medicamentos.
• Se deben poder crear, consultar, actualizar y eliminar los medicamentos con la siguiente información:
o Nombre
o Laboratorio de fabrica
o Fecha de fabricación
o Fecha de vencimiento
o Cantidad en stock
o Valor unitario
• Se desea poder filtrar los medicamentos y que se muestre una tabla paginada con los resultados.
• Dado el resultado cada fila de medicamento debe tener una columna con la opción para vender, editar y eliminar.
• Si se desea vender se debe mostrar una ventana modal donde solicite la cantidad y posteriormente nos indique el valor a pagar de acuerdo con esa cantidad.
• En el momento que se confirme la venta se debe actualizar el inventario y registrar la venta con la siguiente información:
o Fecha y hora
o Medicamento
o Cantidad
o Valor unitario y valor total
• Las ventas se pueden consultar y filtrar de acuerdo con un rango de fechas.
REQUISITOS NO FUNCIONALES
• El backend de la aplicación debe estar construido en java 17 con spring boot, basado en arquitectura de microservicios.
• El front de la aplicación debe estar construido en angular con uso de prime faces.
• Se valora adicionales pruebas unitarias de front y back, cobertura de código, el uso de la herramienta sonarlink para el análisis de código estático y que la base de datos se encuentre en Oracle.

se debe realizar bajo una arquitectura hexagonal
tener en cuenta buenas practicas de desarrollo de software teniendo en cuenta que el rol que vas a desempeñar es el de coordinador de desarrollo
que los contratos de los servicios sean con muy buenas practicas, devolviendo los estados correctos a cada peticion y que se tenga un buen manejo de errores
validar si usas docker para una mejor ejecucion
ENTREGABLES
• Código almacenado y versionado en repositorio github
• Adicional se valora la presentación del aplicativo funcional