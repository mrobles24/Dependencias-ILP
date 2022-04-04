
/*  Clase Instruction:
 *    Contiene información sobre como es una instrucción en el programa mediante objetos
 *    de tipo Instruction. También contiene métodos para comparar y evaluar dos 
 *    instrucciones.
 */
package ensamblador;

/**
 * @author Miquel Robles Mclean
 */
public class Instruction {

    // Parámetros de una Instrucción
    private Type tipo;      // Opcode
    private String destino; // Reg. Destino (para saltos será la etiqueta)
    private String fuente1; // Reg. Fuente 1
    private String fuente2; // Ref. Fuente 2
    private int indice;     // Posicion en el código

    /* Constructor Vacio:
        Asigna null a todos los parámetros, excepto al índice, asignandóle el num. máximo
        de linias de código.
     */
    public Instruction() {
        tipo = null;
        destino = null;
        fuente1 = null;
        fuente2 = null;
        indice = 99999;
    }

    /* Constructor:
        Asigna valores a los parámetros de una instrucción mediante unos argumentos
        dados.
     */
    public Instruction(Type tip, String dest, String fnt1, String fnt2) {
        tipo = tip;
        destino = dest;
        fuente1 = fnt1;
        fuente2 = fnt2;
        indice = 99999;
    }

    /**
     * **********************+*** MÉTODOS FUNCIONALES
     * ************************************
     *
     * /* toType: Dado un String, retorna un Type de instrucción acorde con él.
     * Si el String no es ningún tipo, retorna null.
     */
    public Type toType(String t) {
        Type tip;
        switch (t) {
            case "ADD":
                tip = Type.ADD;
                break;
            case "SUB":
                tip = Type.SUB;
                break;
            case "MUL":
                tip = Type.MUL;
                break;
            case "DIV":
                tip = Type.DIV;
                break;
            case "LD":
                tip = Type.LD;
                break;
            case "ST":
                tip = Type.ST;
                break;
            case "JMP":
                tip = Type.JMP;
                break;
            case "LABEL":
                tip = Type.LABEL;
                break;
            default:
                tip = null;
                break;
        }
        return tip;
    }

    /* validInstruction:
        Nos devuelve true si una instrucción es válida para nuestro código. Para ver que 
        es una instrucción válida, mirar la documentación del código.
     */
    public boolean validInstruction() {
        char[] dest = destino.toCharArray();
        char[] fnt1 = fuente1.toCharArray();
        char[] fnt2 = fuente2.toCharArray();

        // Instrucciones tipo ALU
        if ((tipo != Type.ST) && (tipo != Type.LD) && (tipo != Type.JMP)
                && (tipo != Type.LABEL) && (tipo != null)) {
            // Miramos que haya máximo 2 campos en el registro
            if ((dest.length > 2) || (fnt1.length > 2) || (fnt2.length > 2)) {
                return false;
            } else {
                // Miramos que los registros sean correctos
                return (goodRegister(dest)) && (goodRegister(fnt1))
                        && (goodRegister(fnt2));
            }

            // Instrucciones LOAD
        } else if (tipo == Type.LD) {
            // Miramos que haya 2 campos en el registro destino y 4 en el fuente
            if ((dest.length > 2) || (fnt1.length > 4)) {
                return false;
            } else {
                fuente2 = " ";
                // Miramos que los registros sean correctos
                return (goodRegister(dest)) && (goodIndirectRegister(fnt1));
            }

            // Instrucciones STORE
        } else if (tipo == Type.ST) {
            // Miramos que haya 4 campos en el registro destino y 2 en el fuente
            if ((dest.length > 4) || (fnt1.length > 2)) {
                return false;
            } else {
                fuente2 = " ";
                // Miramos que los registros sean correctos
                return (goodIndirectRegister(dest)) && (goodRegister(fnt1));
            }

            // Instrucciones JUMP
        } else if ((tipo == Type.JMP) || (tipo == Type.LABEL)) {
            // Miramos que haya 1 campo en el destino y en las fuentes
            if ((dest.length > 1) || (fnt1.length > 1) || (fnt2.length > 1)) {
                return false;
            } else {
                fuente1 = " ";
                fuente2 = " ";
                // Miramos que la etiqueta sea correcta
                return (goodLabel(dest));
            }

            // Si la instrucción es de tipo null, no es válida
        } else if (tipo == null) {
            return false;
        }

        return false;
    }

    /* goodRegister:
        Comprueba que el registro empiece por R y que contenga un digito, 
        p.e. R5: valido
             xT: no valido
     */
    private boolean goodRegister(char[] reg) {
        return (reg[0] == 'R') && (Character.isDigit(reg[1]));
    }

    private boolean goodIndirectRegister(char[] reg) {
        return (reg[0] == '(') && (reg[1] == 'R') && (reg[3] == ')')
                && (Character.isDigit(reg[2]));
    }


    /* goodLabel:
        Comprueba que la etiqueta tenga un dígito y no un carácter.
     */
    private boolean goodLabel(char[] label) {
        return ((Character.isDigit(label[0])));
    }

    /* isRAW:
        Dada una instrucción, comprueba si hay una dependencia RAW con la instrucción actual
     */
    public boolean isRAW(Instruction i1) {
        // Si es un RAW de dos registros normales o dos indirectos
        if ((this.getDestino().equals(i1.getFuente1()))
                || (this.getDestino().equals(i1.getFuente2()))) {
            return true;

            // Si es un RAW de un normal a un indirecto en fuente (LD)
        } else if ((i1.getTipo() == Type.LD)&&(this.getTipo() != Type.LABEL)&&
                (this.getTipo() != Type.JMP)) {
            char[] esta = this.getDestino().toCharArray();
            char[] ins1 = i1.getFuente1().toCharArray();
            if (esta[1] == ins1[2]) {
                return true;
            }

            // Si es un RAW de un normal a un indirecto en destino (ST)
        } else if ((i1.getTipo() == Type.ST)&&(this.getTipo() != Type.LABEL)&&
                (this.getTipo() != Type.JMP)) {
            char[] esta = this.getDestino().toCharArray();
            char[] ins1 = i1.getDestino().toCharArray();
            if (esta[1] == ins1[2]) {
                return true;
            }
        }
        return false;
    }

    /* isWAR:
        Dada una instrucción, comprueba si hay una dependencia WAR con la instrucción actual
     */
    public boolean isWAR(Instruction i1) {
        return (i1.getDestino().equals(this.getFuente1()))
                || (i1.getDestino().equals(this.getFuente2()));
    }

    /* isWAW:
        Dada una instrucción, comprueba si hay una dependencia WAW con la instrucción actual,
        solo si las instrucciones no son de tipo JMP o LABEL, para que no salten 
        dependencias entre etiquetas
     */
    public boolean isWAW(Instruction i1) {
        if ((this.tipo != Type.JMP) && (i1.tipo != Type.JMP)
                && (this.tipo != Type.LABEL) && (i1.tipo != Type.LABEL)) {
            return (this.getDestino().equals(i1.getDestino()));
        } else {
            return false;
        }
    }

    /* isJMP:
        Dada una instrucción, comprueba si hay un salto entre esa instrucción y la actual,
        mirando si esa es de tipo JMP y si la nuestra de tipo LABEL, ademas de comprobar
        que las etiquetas (destino) sean iguales
     */
    public boolean isJMP(Instruction i1) {
        return ((this.getDestino().equals(i1.getDestino()))
                && (this.tipo == Type.LABEL));
    }

    /**
     * **********************+****** MÉTODOS SETTERS
     * ***********************************
     */
    public void setType(Type nuevoTipo) {
        tipo = nuevoTipo;
    }

    public void setDestino(String nuevoDestino) {
        destino = nuevoDestino;
    }

    public void setFuente1(String nuevaFuente1) {
        fuente1 = nuevaFuente1;
    }

    public void setFuente2(String nuevaFuente2) {
        fuente2 = nuevaFuente2;
    }

    public void setIndice(int i) {
        indice = i;
    }

    /**
     * **********************+****** MÉTODOS GETTERS
     * ***********************************
     */
    public Type getTipo() {
        return tipo;
    }

    public String getDestino() {
        return destino;
    }

    public String getFuente1() {
        return fuente1;
    }

    public String getFuente2() {
        return fuente2;
    }

    public int getIndice() {
        return indice;
    }

    /**
     * **********************+****** MÉTODO toString
     * ***********************************
     */
    @Override
    public String toString() {
        return "I" + indice + ": " + tipo.toString() + "  " + destino + "  "
                + fuente1 + "  " + fuente2;
    }

}
