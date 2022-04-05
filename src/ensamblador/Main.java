
/*****************************************************************************************
 * BUSCADOR DE DEPENDENCIAS:
 * ------------------------
 * 
 * Este programa lee desde un fichero un código ensamblador proporcionado por el usuario,
 * analizando léxica y sintácticamente si es correcto o no. Posteriormente, analiza todas
 * las dependencias entre instrucciones de tipo RAW, WAR i WAW i las anuncia al usuario.
 * 
 * Notas:
 * - Instrucciones Válidas para el ensamblador:
 *      · ADD RX RX RX
 *      · SUB RX RX RX
 *      · MUL RX RX RX
 *      · DIV RX RX RX
 *      · LD RX (RX) -
 *      · ST (RX) RX -
 *      · JMP X - -
 *      · LABEL X - -
 * 
 *      Donde X es un número entre [0..9].
 * 
 * - También se pueden usar comas para separar registros.
 * - Para más información, consúltese la documentación del código.
 * 
 */

package ensamblador;

import java.io.*;
import java.util.*;

/**
 * @author Miquel Robles Mclean
 * @date 14-03-2022
 * @version 1.0
 */
public class Main {

    // Variables Globales de tipo ArrayList
    private static ArrayList<Instruction> codigo = new ArrayList<>();
    private static ArrayList<Dependencia> RAW = new ArrayList<>();
    private static ArrayList<Dependencia> WAR = new ArrayList<>();
    private static ArrayList<Dependencia> WAW = new ArrayList<>();
    
    /* main:
        Leemos el fichero con las instrucciones, imprimimos por pantalla el código 
        obtenido y buscamos las dependencias que hay en dicho código. También puede
        enseñar las dependencias de una instrucción dada.
     */
    public static void main(String[] args) {
        leerInstrucciones("codi1.txt");
        imprimirCodigo();
        buscarDependencias();
        //hayDependencias(codigo.get(0));
    }

    /* leerInstrucciones:
        Dado un fichero de texto, lee linia por linia obteniendo los parámetros de cada
        instrucción (tipo de instrucción, reg. destino, reg. fuente1, reg. fuente2) y crea
        un objeto Instruction con ellos. Finalmente, mete este objeto en una lista con
        todas las demas instrucciones del código si la instrucción es valida.
     */
    private static void leerInstrucciones(String filepath) {
        try {
            // Creamos un nuevo Scanner de ficheros, con delimitadores
            Scanner input = new Scanner(new File(filepath));
            input.useDelimiter(",| |\n");
            String t, d, f1, f2;
            int cont = -1;

            // Iteramos sobre el fichero
            while (input.hasNext()) {
                Instruction ins = new Instruction();

                // Leemos una instrucción, párametro por parámetro
                Type tip = ins.toType(input.next());    // Opcode
                d = input.next();                       // Reg. Destino
                f1 = input.next();                      // Reg. Fuente 1
                f2 = input.next();

                // Creamos la instrucción con los parámetros leídos
                ins = new Instruction(tip, d, f1, f2);
                cont++;

                // Miramos si la instrucción es válida
                if (ins.validInstruction()) {
                    codigo.add(ins); // Si lo es, la metemos en una lista de codigo
                    ins.setIndice((codigo.size() - 1));
                } else {
                    // Si no lo es, no la metemos en la lista y lo anunciamos
                    System.out.println("Error: La instrucción " + cont + " "
                            + "no es válida");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: No se encontró el fichero");
        } catch (Exception e) {
            System.out.println("Error: Mala Sintaxis");
        }
    }

    /* imprimirCodigo:
        Imprime la lista de instrucciones de un código dado, con todos sus parámetros.
     */
    private static void imprimirCodigo() {
        System.out.println("\n***** CÓDIGO VÁLIDO *****\n");

        for (int i = 0; i < codigo.size(); i++) {
            System.out.println(codigo.get(i));
        }
    }

    /* buscarDependenicas:
        Mediante el código, busca dependencias instrucción por instrucción y si las
        encuentra, crea un objeto dependencia y la mete en su correspondiente lista. Si 
        hay bucles, sigue sus respectivas trazas
     */
    private static void buscarDependencias() {

        System.out.println("\n* Saltos detectados *");

        // Iteramos sobre las instrucciones
        for (int i = 0; i < codigo.size(); i++) {

            // Si la instrucción es de salto, busca su correspondiente label
            if (codigo.get(i).getTipo() == Type.JMP) {
                for (int k = 0; k < codigo.size(); k++) {
                    // Si ha encontrado un label igual al salto, actualiza el 
                    // puntero de instrucción y anuncia el salto
                    if ((codigo.get(k).isJMP(codigo.get(i))) && (i != k)) {
                        System.out.println("de I" + i + " a I" + k);
                        i = k;
                    }
                }
            }

            // Una vez obtenido el puntero de instrucción, iteramos sobre las siguientes
            // para encontrar posibles dependencias
            for (int j = i + 1; j < codigo.size(); j++) {

                // Al igual que antes, si alguna instrucción es de tipo salto, 
                // actualizamos el nuevo puntero
                if (codigo.get(j).getTipo() == Type.JMP) {
                    for (int k = 0; k < codigo.size(); k++) {
                        if ((codigo.get(k).isJMP(codigo.get(j))) && (j != k)) {
                            j = k;
                        }
                    }
                }

                // Una vez analizado los saltos y obtenido los dos punteros, miramos si 
                // hay dependencias
                // Si hay RAW, creamos una dependencia y la metemos en su lista
                if (codigo.get(i).isRAW(codigo.get(j))) {
                    Dependencia dep = new Dependencia(Type.RAW, codigo.get(j),
                            codigo.get(i), codigo.get(i).getDestino());
                    RAW.add(dep);

                }
                // Si hay WAR, creamos una dependencia y la metemos en su lista
                if (codigo.get(i).isWAR(codigo.get(j))) {
                    Dependencia dep = new Dependencia(Type.WAR, codigo.get(j),
                            codigo.get(i), codigo.get(j).getDestino());
                    WAR.add(dep);

                }
                // Si hay WAW, creamos una dependencia y la metemos en su lista
                if (codigo.get(i).isWAW(codigo.get(j))) {
                    Dependencia dep = new Dependencia(Type.WAW, codigo.get(j),
                            codigo.get(i), codigo.get(i).getDestino());
                    WAW.add(dep);

                }
            }
        }

        // Finalmente iteramos sobre cada lista para anunciar las dependencias
        System.out.println("\n***** DEPENDENCIAS *****");
        System.out.println("\n* Dependencias RAW *");
        for (int x = 0; x < RAW.size(); x++) {
            System.out.println(RAW.get(x));
        }

        System.out.println("\n* Dependencias WAR *");
        for (int y = 0; y < WAR.size(); y++) {
            System.out.println(WAR.get(y));
        }

        System.out.println("\n* Dependencias WAW *");
        for (int z = 0; z < WAW.size(); z++) {
            System.out.println(WAW.get(z));
        }
        System.out.println("\n");
    }

    /* hayDependencias:
        Esta función adicional, nos indica, dada una instrucción, si tiene dependencias
        con otras instrucciones o no. Si las tiene, nos las imprime.
     */
    private static void hayDependencias(Instruction ins) {
        System.out.println("\nLa instrucción " + ins.getIndice()
                + " tiene las siguientes dependencias: \n");

        // Dado el índice de la instrucción, lo buscamos en cada lista de dependencias
        // para ver si se encuentra en alguna de ellas.
        for (int x = 0; x < RAW.size(); x++) {
            if (RAW.get(x).existeDependencia(ins)) {
                System.out.println("RAW: " + RAW.get(x));
            }
        }

        for (int x = 0; x < WAR.size(); x++) {
            if (WAR.get(x).existeDependencia(ins)) {
                System.out.println("WAR: " + WAR.get(x));
            }
        }

        for (int x = 0; x < WAW.size(); x++) {
            if (WAW.get(x).existeDependencia(ins)) {
                System.out.println("WAW: " + WAW.get(x));
            }
        }
    }

}
