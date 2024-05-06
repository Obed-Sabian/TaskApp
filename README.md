# TaskApp

Aplicación móvil de recordatorios para la plataforma Android. Ayuda a la organización de tareas y poder cumplirlas a tiempo. 

## Descripción 

La aplicación cuenta con dos pantallas.
En la primera muestra una lista de las tareas que se han agregado (completas y pendientes), la cual se puede filtrar por el tipo de actividad de la tarea. Además muestra un botón para ir a la segunda pantalla, la cual permite agregar o editar una actividad junto con sus campos:

- Título
- Descripción
- Fecha límite
- Tipo de actividad

Una vez creada la tarea, se programa una notificación que se envía una vez se cumple el plazo de la fecha dada. Las tareas atrasadas que aún no han sido atendidas se muestran en color rojo, y las tareas pendientes muestran el tiempo que falta para la fecha límite. Clickeando una tarea podemos ver información más detallada de esta. 

## Tecnologías utilizadas 
- Kotlin 
- Room
- Recycler View
- Alarm Manager
