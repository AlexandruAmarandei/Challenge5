/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilerch4;

/**
 *
 * @author alexa
 */
public class Variable {
    private String name = null;
    private int instruction = -1;
    public int value=0;
    public int structure;
    public boolean visible;
    public Variable( String s, int i, int lvl){
        name = s;
        instruction = i;
        value = 0;
        structure = lvl;
        visible = false;
    }
    public String getName (){
        return name;
    }
    public int getInstruction(){
        return instruction;
    }
}
