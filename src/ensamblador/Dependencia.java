
/*  Clase Dependencia:
 *    Define el formato de un objeto de tipo Dependencia, así como un método para comprobar
 *    si existe una dependencia entre una instrucción dada y las de la dependencia actual.
 */
package ensamblador;

/**
 * @author miquelrobles
 */
public class Dependencia {

    // Parámetros de una Dependencia
    private Type tipo;          // Tipo
    private Instruction ins1;   // Instrucción dependiente 1
    private Instruction ins2;   // Instrucción dependiente 2
    private String reg;         // Registro

    /* Constructor Vacio:
        Asigna null a todos los parámetros.
     */
    public Dependencia() {
        tipo = null;
        ins1 = null;
        ins2 = null;
        reg = null;
    }

    /* Constructor:
        Asigna valores a los parámetros de una dependencia mediante unos argumentos
        dados.
     */
    public Dependencia(Type tip, Instruction i1, Instruction i2, String r) {
        tipo = tip;
        ins1 = i1;
        ins2 = i2;
        reg = r;
    }
    
    /************************+*** MÉTODOS FUNCIONALES ************************************/
    
    /* existeDependencia:
        Dada una instrucción, comprueba si ésta forma parte de la dependencia actual
    */
    public boolean existeDependencia(Instruction i){
        return ((i==ins1)|(i==ins2));
    }
    
    /************************+****** MÉTODOS SETTERS ************************************/
    
    public void setTipo(Type ti){
        tipo = ti;
    }
    
    public void setIns1(Instruction i){
        ins1 = i;
    }
    
    public void setIns2(Instruction i){
        ins2 = i;
    }
    
    public void setReg(String s){
        reg = s;
    }
    
   /************************+****** MÉTODOS GETTERS ************************************/
    
    public Type getTipo(){
        return tipo;
    }
    
    public Instruction getIns1(){
        return ins1;
    }
    
    public Instruction getIns2(){
        return ins2;
    }
    
    public String getReg(){
        return reg;
    }
    
    /************************+****** MÉTODO toString ************************************/
    
    @Override
    public String toString(){
        return "de I" + ins1.getIndice() + " a I" + ins2.getIndice() + " por " + reg;
    }

}
