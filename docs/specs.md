Paradigmas de Programación

# Trabajo Práctico N° 2

El presente trabajo práctico tiene como objetivo aplicar los conceptos, principios y características del paradigma de la programación orientada a objetos (Clases, objetos, métodos, interfaz, herencia, polimorfismo, colecciones, etc).

**Fecha de entrega: 17 de Noviembre / 20 de Noviembre de 2026 (dependiendo la comisión)**

# Objetivo

Implementar los conceptos abordados del paradigma de la Programación orientada a objetos, como solución a un problema real, partiendo del análisis de la problemática, el diseño de la solución (Diagrama de Clases) y posteriormente la construcción del producto final (Software).

# Consigna

## Temática

El Torneo de los Inmortales: la elección de los Dos Santos Supremos

## Objetivos del trabajo

- Modelar un sistema de combate por parejas entre cultivadores de distintas escuelas.
- Implementar técnicas de combate y equipamiento que modifican las capacidades de los combatientes.
- Gestionar las parejas, los cruces y las rondas de un torneo eliminatorio, utilizando distintos tipos de colección y aplicando los patrones de diseño que correspondan.
- Usar herencia y polimorfismo para permitir la diversidad de combatientes (Cultivador de la Espada, Monje del Cuerpo Celestial, Hechicero del Vacío) y que cada escuela los prepare según su filosofía.

## Descripción General

Parejas de combatientes se enfrentan en rondas eliminatorias hasta que queda una sola en pie; sus integrantes son proclamados los Dos Santos Supremos. Cada combatiente pertenece a una escuela de cultivación, que define su preparación inicial. Se espera que el sistema esté diseñado de manera flexible para que se puedan agregar nuevos tipos de combatientes, técnicas, escuelas y equipamientos fácilmente. Los términos del enunciado están definidos en el glosario del anexo, al final del documento

### 1. Combatientes

Combatiente (abstracta): identificador único e inmutable, nombre, vida y energía (actuales y máximas), equipamiento y técnicas disponibles. Dos combatientes son el mismo si tienen el mismo identificador, sin importar el nombre.

Tipos iniciales:

- **Cultivador de la Espada:** potencia el daño que provoca.
- **Monje del Cuerpo Celestial:** reduce el daño que recibe.
- **Hechicero del Vacío:** gasta menos energía al ejecutar técnicas.

La diferencia entre tipos debe surgir del polimorfismo, no de preguntar el tipo con if o instanceof: una misma técnica provoca distinto daño según quién la ejecuta, sin que la técnica conozca el tipo del combatiente. Se deben poder agregar tipos nuevos sin modificar el torneo ni las técnicas existentes.

### 2. Técnicas

Una técnica es únicamente su definición: nombre, costo de energía y efecto. Los usos se registran en cada combatiente, porque una misma técnica puede estar en varios. Agregar una técnica nueva no debe modificar las existentes. Ejemplos:

- **Corte de los Mil Amaneceres:** daña al adversario.
- **Muro del Cuerpo Dorado:** reduce el daño que recibe el ejecutor durante el resto del combate.
- **Dominio de la Nada:** daña al adversario y le quita energía.
- **Rugido de la Bestia Primordial:** ataque poderoso.
- **Meditación del Inmortal:** recupera vida.

Los valores concretos (daño, costo) los define el grupo y deben quedar documentados y justificados.

### 3. Escuelas y creación de combatientes

Las escuelas preparan combatientes de cualquier tipo según su filosofía, asignándoles el equipamiento y las técnicas iniciales:

- **Escuela de la Espada Celestial:** ataques físicos y armas.
- **Palacio del Vacío:** técnicas especiales y dominio de la energía.
- **Monasterio del Cuerpo Dorado:** resistencia, defensa y recuperación.

Una escuela es un objeto con nombre, técnicas y equipamiento iniciales, como muestra el ejemplo de main. Agregar una escuela es crear otro objeto así, sin escribir código nuevo.

La creación de combatientes está centralizada: pedir un Hechicero del Vacío preparado por el Palacio del Vacío no requiere conocer su clase concreta, sus atributos iniciales ni cómo se arma su preparación.

### 4. Equipamiento

Modifica las capacidades del combatiente mientras lo lleva. Ejemplos: Espada celestial (más daño), Armadura dorada (menos daño recibido), Anillo espiritual (más energía máxima), Sello del Fénix (más vida máxima).

- Un combatiente puede llevar hasta tres equipamientos, sin dos de la misma categoría (arma, armadura, anillo, sello). Los efectos se acumulan.
- Se puede equipar y quitar antes del torneo y entre combates. Se debe manejar correctamente cuando un cambio intenta romper una regla.
- Equipar un objeto que aumenta un máximo no aumenta el valor actual. Al quitarlo, el valor actual se recorta al nuevo máximo si lo supera.
- El combatiente conoce los objetos que lleva y puede consultarlos.

### 5. Parejas y torneo

- Cada pareja tiene identificador único, asignado al crearse; el de los combatientes lo asigna el creador, en orden de creación. Un combatiente no puede inscribirse dos veces ni una pareja registrarse dos veces; el intento debe rechazarse con una excepción.
- El torneo se debe jugar únicamente con 4 u 8 parejas.
- Los cruces de la primera ronda se arman por orden de inscripción (primera contra segunda, tercera contra cuarta, etc.). En las siguientes, por el orden de los cruces de la ronda anterior.
- Una pareja pierde cuando sus dos integrantes quedan fuera de combate. El torneo termina cuando queda una sola pareja.
- Las colecciones se eligen según la necesidad (conservar el orden de rondas y cruces, evitar duplicados, registrar cuántas veces cada combatiente usó cada técnica, ordenar la tabla de posiciones).

### 6. Reglas del combate

**Asaltos y turnos**

- El combate se desarrolla en asaltos. En cada asalto, cada pareja juega su turno completo: cada integrante en pie realiza una acción. Empieza la pareja de menor identificador.
- Dentro de la pareja actúa primero el de menor identificador. Si en cualquier momento una pareja queda sin integrantes en pie, el combate termina.

**Acción**

- La acción es un ataque básico o una técnica. El ataque básico no cuesta energía, no tiene límite de usos y no cuenta para la regla del compañero. La técnica requiere energía suficiente y usos restantes.
- **Criterio por defecto, en este orden:** una técnica de recuperación si le falta vida; una defensiva si no tiene una activa; la técnica ofensiva con más usos restantes. Si ninguna es posible (por energía, usos o regla del compañero), se realiza un ataque básico. En empate, la primera en el orden de la escuela. El grupo puede definir otro criterio, siempre que sea determinista y esté documentado.

**Objetivo**

- Para ataques y técnicas ofensivas: el rival en pie con menos vida actual; en empate, el de menor identificador. Las técnicas defensivas y de recuperación se aplican al ejecutor.

**Vida y energía**

- Al llegar a cero de vida, el combatiente queda fuera de combate: no actúa ni puede ser objetivo en ese combate.
- Al comenzar cada asalto, cada combatiente en pie recupera una cantidad fija de energía, que define el grupo, sin superar su máximo.
- Al comenzar cada combate, todos los integrantes (incluidos los caídos en combates anteriores) recuperan vida y energía al máximo.
- Los valores iniciales los define el grupo; por ejemplo, 100 puntos de vida y 40 de energía.

**Técnicas**

- Regla del compañero: cuando un integrante usa una técnica en un combate, su compañero no puede usarla mientras el primero conserve usos de ella y siga en pie; si los agota o queda fuera de combate, el compañero puede usarla. La restricción termina con el combate: en el siguiente, cualquiera de los dos puede volver a usarla. La pareja puede llevar ese registro.
- Cada combatiente puede usar cada técnica cinco veces en todo el torneo; el registro no se reinicia entre combates. Sin usos no se puede ejecutar aunque haya energía.

**Duración**

- El grupo debe fijar un límite de asaltos por combate. Al alcanzarlo gana la pareja con más vida total (la suma de la vida actual de sus integrantes; un caído suma cero); en empate, la de menor identificador.

**Reproducibilidad**

- **El combate es determinista:** ante el mismo estado inicial produce el mismo resultado.

### 7. Acontecimientos y presentación

El combate y el torneo comunican acontecimientos: inicio de combate, acción realizada (ataque básico o técnica), eliminación, resultado del combate (ronda, ganadora y vida total de cada pareja al terminar) y proclamación. Cada acontecimiento guarda los datos del momento en que ocurrió. Deben poder sumarse nuevos interesados sin modificar la lógica del torneo. Dos componentes son obligatorios:

- **Heraldo Imperial:** presenta los acontecimientos por pantalla. Es la única clase del sistema que escribe en pantalla, incluido lo que muestra el main.
- **Cronista de la Secta:** registra los acontecimientos y permite consultar el historial. Con los resultados registrados arma la tabla de posiciones: las parejas ordenadas por la última ronda del torneo que jugaron, en empate por la vida total con la que terminaron su último combate y luego por identificador. Al terminar el torneo, el main le pide la tabla al Cronista y el Heraldo la presenta.

### 8. Pruebas

Crear una batería de pruebas unitarias con JUnit 5 para técnicas, combatientes, escuelas, equipamiento, parejas, combate y torneo. Cada regla de las secciones 4, 5 y 6 debe tener al menos una prueba que la demuestre; en el informe, indiquen qué prueba cubre cada regla. Como mínimo:

- Una misma técnica daña diferente según el tipo del ejecutor.
- No se ejecuta una técnica sin energía o sin usos restantes.
- Los usos se conservan entre combates; vida y energía se recuperan.
- El compañero no puede usar una técnica mientras el primero conserve usos de ella.
- Equipar un cuarto objeto o una categoría repetida se rechaza sin cambios.
- Al quitar un equipamiento que sube la vida máxima, la vida actual se recorta.
- Un caído no actúa ni es elegido como objetivo.
- Elección de objetivo y desempate al límite de asaltos.
- Inscripciones duplicadas y cantidad de parejas inválida se rechazan.
- La tabla de posiciones queda ordenada por última ronda jugada y vida total final
- Dos torneos armados igual producen el mismo campeón y el mismo historial.

Las pruebas de combate y de torneo verifican a través del estado de los objetos o del historial del Cronista, nunca leyendo la salida por pantalla.

### 9. Bonus

Las siguientes funcionalidades son adicionales y tienen una dificultad similar. Debe implementarse al menos una.

- **Efectos temporales:** un efecto que permanece activo una cantidad de turnos del combatiente afectado (veneno, regeneración). Definir cuándo se aplica, cuánto dura y qué sucede al terminar.
- **Combatiente reactivo:** un combatiente cambia su comportamiento ante un acontecimiento; por ejemplo, potencia su próximo ataque cuando cae su compañero. Debe activarse y terminar según reglas definidas.

# Interfaz

Se evaluará el correcto funcionamiento del sistema utilizando dos enfoques simultáneos:

- Pruebas unitarias con JUnit de las funcionalidades y de las reglas.
- Un main que configure escuelas, cree combatientes preparados por ellas, forme parejas y ejecute el torneo completo, mostrando los resultados a través del Heraldo.

Ejemplo de main (no exhaustivo; los nombres son orientativos):

public class TorneoDeLosInmortales {

  public static void main(String[] args) {

    // Escuela = configuración: técnicas y equipamiento iniciales.

    Escuela escuelaDeLaEspada = new Escuela(

      "Escuela de la Espada Celestial",

      List.of(new CorteDeLosMilAmaneceres()),

      List.of(new EspadaCelestial()));

    Escuela palacioDelVacio = new Escuela("Palacio del Vacío",

      List.of(new DominioDeLaNada(), new MeditacionDelInmortal()),

      List.of(new AnilloEspiritual()));

    // La creación de combatientes está centralizada.

    CreadorCombatientes creador = new CreadorCombatientes();

    Combatiente aurelio = creador.crear(

      "Cultivador de la Espada", "Aurelio", escuelaDeLaEspada);

    Combatiente nebula = creador.crear(

      "Hechicero del Vacío", "Nébula", palacioDelVacio);

    Combatiente kael = creador.crear(

      "Monje del Cuerpo Celestial", "Kael", palacioDelVacio);

    Combatiente darian = creador.crear(

      "Cultivador de la Espada", "Darian", escuelaDeLaEspada);

    Torneo torneo = new Torneo();

    // Registrar al Heraldo y al Cronista como interesados.

    Pareja primera = new Pareja(1, aurelio, nebula);

    torneo.inscribir(primera);

    // Crear los demás combatientes e inscribir las otras tres parejas.

    torneo.iniciar();

    // El Heraldo anuncia a los Dos Santos Supremos; después, el main

    // le pide la tabla al Cronista y el Heraldo la presenta.

  }

}

Sugerimos tener varios escenarios preparados, para mostrar todas las características desarrolladas.

# Entrega

El trabajo se realizará en forma grupal, con equipos de 4 a 6 personas.

Se espera que la entrega incluya: el proyecto (código y pruebas), el diagrama de clases y un informe que contenga al menos carátula, índice, introducción, desarrollo, conclusiones y referencias (APA).

Habrá una defensa y presentación oral en la fecha designada.

Se valora la claridad del diseño acorde al nivel de la materia; la sobreingeniería no suma.

# Anexo: Glosario

- **Escuela:** configuración que define las técnicas y el equipamiento iniciales de un combatiente; no determina su tipo.
- **Técnica:** acción con efecto propio y costo de energía, con usos limitados por torneo.
- **Ataque básico:** acción sin costo de energía ni límite de usos; no es una técnica.
- **Pareja:** unidad que participa en el torneo, formada por dos combatientes.
- **Ronda:** etapa del torneo; en cada ronda se juegan los cruces y las parejas ganadoras avanzan.
- **Cruce:** enfrentamiento entre dos parejas dentro de una ronda; se resuelve con un combate.
- **Combate:** desarrollo de un cruce, dividido en asaltos; termina cuando una pareja queda sin integrantes en pie o al alcanzar el límite de asaltos.
- **Asalto:** unidad del combate en la que cada pareja juega su turno completo.
- **Turno:** parte del asalto en la que los integrantes en pie de una pareja realizan una acción cada uno.
- **Acontecimiento:** hecho del combate o del torneo que se comunica a los interesados, como el Heraldo y el Cronista.
