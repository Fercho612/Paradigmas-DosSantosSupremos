# ⚔️ El Torneo de los Inmortales: La Elección de los Dos Santos Supremos

[![Java](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5.10-blue?style=for-the-badge&logo=junit5&logoColor=white)](https://junit.org/junit5/)
[![Paradigma POO](https://img.shields.io/badge/Paradigma-Orientado%20a%20Objetos-purple?style=for-the-badge)]()
[![Estado](https://img.shields.io/badge/Estado-En%20Desarrollo-green?style=for-the-badge)]()


---

## 📜 Descripción del Proyecto
Este software simula **El Torneo de los Inmortales**, una competencia eliminatoria donde parejas de cultivadores pertenecientes a distintas escuelas se enfrentan en duelos tácticos por turnos. El objetivo principal es consagrar a la única pareja victoriosa como los **Dos Santos Supremos**, aplicando de forma rigurosa los conceptos del paradigma de POO: polimorfismo puro, clases abstractas, encapsulamiento, gestión avanzada de colecciones y patrones de diseño (como *Observer* para los acontecimientos).

---

## 👥 Grupo
**Comisión**: 5900 - Viernes Noche. 

**Grupo**: Kappa

**Integrantes**:
- Pereyra Fernando *(Fercho612)*
- Biagio Facundo *(FacundoBiagio)*
- Toloza Facundo *(FacuToloza)*
- Patri Tiago *(jtiagopatri)*
- Lautaro Alejandro Vega *(LauVega17)*
- Omar Vladimir Quispe Rea *(omarqr95)*

---

## 🏛️ Arquitectura y Decisiones de Diseño

- **Polimorfismo sin `instanceof`:** Las diferencias entre tipos de combatientes (*Cultivador de la Espada*, *Monje del Cuerpo Celestial*, *Hechicero del Vacío*) y el impacto de las técnicas se resuelven mediante polimorfismo dinámico. Las técnicas ignoran el subtipo del combatiente, delegando en este último el cálculo del efecto.
- **Patrón Observer:** El combate y el torneo emiten acontecimientos en tiempo real que son capturados de forma desacoplada por suscriptores clave:
  - **Heraldo Imperial:** Único componente autorizado para mostrar información por pantalla (interfaz de consola).
  - **Cronista de la Secta:** Registra el historial detallado de eventos, gestiona las estadísticas y computa la tabla de posiciones final.
- **Patrón Factory Simple:** `CreadorCombatientes` centraliza la creación de combatientes. Dado el tipo, el nombre y la escuela, devuelve un `Combatiente` ya preparado (equipamiento y técnicas iniciales), sin que el cliente conozca la clase concreta ni cómo se arma su preparación. Agregar un tipo nuevo no modifica al torneo.
- **Patrón State:** El estado del combatiente (*en pie* / *fuera de combate*) encapsula qué puede hacer: un combatiente caído no actúa ni puede ser elegido como objetivo.
---
## 📄Diagrama UML
![Diagrama de Clases del Torneo](./docs/UMLDosSantosSupremos.drawio.svg)
---
## ⚖️ Reglas del Juego
### Combates

### Combatientes 
- **Cultivador de la Espada**: Guerrero de disciplina férrea que forja su cuerpo y su voluntad a través del filo. Su maestría con las armas potencia cada golpe: **inflige más daño** del que corresponde a la técnica base. **Estadísticas Base**: 1000 HP, 150 EP.
- **Monje del Cuerpo Celestial**: Asceta que ha templado su carne hasta volverla casi indestructible. Su resistencia lo vuelve el pilar de la pareja: **reduce el daño que recibe**. **Estadísticas Base**: 1300 HP, 120 EP.
- **Hechicero del Vacío**: Estudioso de la energía primordial que domina el flujo del qi con una eficiencia sobrenatural. Su control le permite **gastar menos energía** al ejecutar técnicas. **Estadísticas Base**: 800 HP, 230 EP.
### Equipamiento
- **Danza de la Muerte**: Incrementa el daño realizado.
- **Velo de Banshee**: Reduce el daño recibido.
- **Anillo de Doran**: Aumenta la energía máxima.
- **Corazón de Acero**: Aumenta la vida máxima.
### Tecnicas

