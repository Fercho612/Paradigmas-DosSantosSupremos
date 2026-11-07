# El Torneo de los Inmortales: la elección de los Dos Santos Supremos

Trabajo Práctico N° 2 - Paradigmas de Programación (2026)  
**Desarrollado por:** Equipo Kappa  
**Documentación:** [Leer Consigna Completa del TP](./docs/consigna.md)  
**Fecha de Entrega:** 20 de Noviembre de 2026

---

## 📌 Objetivos del Sistema
* Modelar un sistema de combate por parejas entre cultivadores de distintas escuelas, aplicando herencia y polimorfismo puro (prohibiendo el uso de `if` o `instanceof` para discernir tipos) y gestionando un torneo eliminatorio determinista.

## 🛠️ Reglas de Negocio a Implementar y Testear (Punto 8)
Las pruebas unitarias en **JUnit 5** deben cubrir las siguientes reglas:
* **Combatientes & Técnicas:** Polimorfismo en el efecto/daño según el ejecutante, validaciones de energía y usos restantes por torneo (máximo 5), y restauración de vida/energía entre combates.
* **Equipamiento:** Límite máximo de 3 ítems por combatiente, rechazo de categorías duplicadas, y manejo de valores máximos actuales al equipar o desequipar.
* **Parejas & Estructura del Torneo:** Excepciones ante inscripciones duplicadas, validación de 4 u 8 parejas, regla del compañero, gestión de combatientes caídos (0 de vida), orden de la tabla de posiciones e idoneidad determinista.

## 📂 Estructura de Paquetes del Proyecto
* `src/combatientes/`: Clase abstracta, tipos de cultivadores y factoría centralizada.
* `src/equipamiento/`: Sistema de ítems, categorías (Enum) y excepciones de negocio.
* `src/tecnicas/`: Definición y lógica polimórfica de efectos de habilidades.
* `src/torneo/`: Motores de combate (asaltos/turnos), parejas y sistema de eventos (Observer).
