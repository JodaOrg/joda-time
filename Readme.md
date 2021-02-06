Joda-Time
Joda-Time proporciona un reemplazo de calidad para las clases de fecha y hora de Java. El diseño permite múltiples sistemas de calendario, al mismo tiempo que proporciona una API simple. El calendario "predeterminado" es el estándar ISO8601 que utiliza XML. También se incluyen los sistemas gregoriano, juliano, budista, copto, etíope e islámico. Las clases de apoyo incluyen zona horaria, duración, formato y análisis.

Joda-time ya no está en desarrollo activo excepto para mantener actualizados los datos de la zona horaria. Desde Java SE 8 en adelante, se solicita a los usuarios que migren a java.time(JSR-310), una parte central del JDK que reemplaza este proyecto. Para los usuarios de Android, java.timese agrega en API 26+ . Los proyectos que necesitan admitir niveles de API más bajos pueden usar la biblioteca ThreeTenABP .

Como muestra de Joda-Time, aquí hay un código de ejemplo:

public  boolean isAfterPayDay ( DateTime datetime) {
   if (datetime . getMonthOfYear () ==  2 ) {    // ¡¡ Febrero es el mes 2 !! 
    return datetime . getDayOfMonth () >  26 ;
  }
  return datetime . getDayOfMonth () >  28 ;
}

public  Days daysToNewYear ( LocalDate fromDate) {
   LocalDate newYear = fromDate . másAños ( 1 ) . withDayOfYear ( 1 );
  Días de regreso  . daysBetween (fromDate, newYear);
}

public  boolean isRentalOverdue ( DateTime datetimeRented) {
   Periodo de alquilerPeriodo =  nuevo  Periodo () . withDays ( 2 ) . withHours ( 12 );
  return datetimeRented . más (período de alquiler) . isBeforeNow ();
}

public  String getBirthMonthText ( LocalDate dateOfBirth) {
   return dateOfBirth . monthOfYear () . getAsText ( Locale . INGLÉS );
}
Joda-Time tiene la licencia Apache 2.0 para empresas .

Documentación
Hay varios documentos disponibles:

La pagina de inicio
Dos guías de usuario: rápida y completa
El Javadoc
La lista de preguntas frecuentes
Información sobre la descarga e instalación de Joda-Time, incluidas las notas de la versión
Lanzamientos
La versión 2.10.3 es la última versión actual. Esta versión se considera estable y digna de la etiqueta 2.x. Depende de JDK 1.5 o posterior.

Disponible en el repositorio de Maven Central

Configuración de Maven:

< dependencia >
  < groupId > joda-time </ groupId >
  < artifactId > joda-time </ artifactId >
  < versión > 2.10.3 </ versión >
</ dependencia >
Configuración de Gradle:

compilar ' joda-time: joda-time: 2.10.3 '
Verificación de dependencia de Tidelift

Proyectos relacionados
Proyectos relacionados en GitHub:

https://github.com/JodaOrg/joda-time-hibernate
https://github.com/JodaOrg/joda-time-jsptags
https://github.com/JodaOrg/joda-time-i18n
Otros proyectos relacionados:

https://www.joda.org/joda-time/related.html
Apoyo
Utilice Stack Overflow para preguntas de uso general. Los problemas de GitHub y las solicitudes de extracción deben usarse cuando desee ayudar a avanzar en el proyecto. El soporte comercial está disponible a través de la suscripción a Tidelift .

Para informar una vulnerabilidad de seguridad, utilice el contacto de seguridad de Tidelift . Tidelift coordinará la corrección y la divulgación.

Proceso de liberación
Actualización de la versión (pom.xml, README.md, index.md, MANIFEST.MF, changes.xml)
Comprometerse y empujar
Asegúrese en Java SE 8
mvn clean deploy -Doss.repo -Dgpg.passphrase=""
Lanzar proyecto en Nexus
El sitio web será construido y publicado por Travis
