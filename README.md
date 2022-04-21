
------ ESP ------
# Dependencias-ILP
Detector de Dependencias en un código ensamblador

Este programa lee desde un fichero un código ensamblador proporcionado por el usuario,
analizando léxica y sintácticamente si es correcto o no. Posteriormente, analiza todas
las dependencias entre instrucciones de tipo RAW, WAR i WAW i las anuncia al usuario.

Notas:
- Instrucciones Válidas para el ensamblador:
     · ADD RX RX RX
     · SUB RX RX RX
     · MUL RX RX RX
     · DIV RX RX RX
     · LD RX (RX) -
     · ST (RX) RX -
     · JMP X - -
     · LABEL X - -
 
     Donde X es un número entre [0..9].
 
- También se pueden usar comas para separar registros.
- Se pueden cargar en el programa los archivos de prueba "codiX.txt"
- Para más información, consúltese la documentación del código con el autor.

<br>
<br>
<br>

------ ENG ------
# ILP-Dependencies
Dependency detector in an assembly languaje code

This program reads from a file wich contains an assembly code provided by the user, analizing its lexic and sintax to find any possible errors. Once that is completed, it analizes all RAW, WAR and WAW dependencies between the code's instructions.


