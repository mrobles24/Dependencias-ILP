
/*  Enum Type:
 *    Define un enum Type con todos los tipos de instrucciones y de dependencias, para 
 *    no tener que crear dos enums distintos para la misma función.
 */
package ensamblador;

/**
 * @author miquelrobles
 */
public enum Type {
    ADD,SUB,MUL,DIV,LD,ST,JMP,LABEL,
    RAW,WAR,WAW
}
