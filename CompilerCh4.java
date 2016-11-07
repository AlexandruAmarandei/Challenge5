
package compilerch4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexa
 */
public class CompilerCh4 {

    private static final int NUMBEROFINSTRUCTIONS = 9;
    private static final int NUMBEROFCONDITIONS = 4;
    /**
     * @param args the command line arguments
     */
    private  int programLength;
    private  int currentLevel, maxLevel, lastLoop, openLoops  = 0;
    private  String[] instructions = new String[NUMBEROFINSTRUCTIONS];
    private  String[] errorCase = new String[10];
    private  String[] condition = new String[NUMBEROFCONDITIONS];
    private  ArrayList<Variable> variableList = new ArrayList<Variable>();
    private  ArrayList<Integer> instructionList = new ArrayList<Integer>();
    private  ArrayList<Integer> whileArgumentList = new ArrayList<Integer>();
    private  ArrayList<Integer> whileConditionList = new ArrayList<Integer>();
    private  ArrayList<String> nameList = new ArrayList<String>();
    private  ArrayList<String> solveList = new ArrayList<String>();
    private  boolean verification = true;
    private  int structureLevel = 1, currentStructure = 0;
    private  ArrayList<Integer> structureFathers = new ArrayList<Integer>();
    private  int counterExpresions = 0, expresionPosition  = 0;
    private  String fileName = "";
    public   String returnString = "";
    public CompilerCh4(String fileNameArg){
        fileName = fileNameArg;
    }
    
    public  void main(String[] args) throws FileNotFoundException, IOException {
        init();
        
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String line = br.readLine();
            int cnt = 1;
            int v = 1;
            
            
            while (line != null) {
                v=1;
                for (int i = 0; i < line.length()-1  && v == 1; i++) {
                    if ( v == 1 && line.charAt(i) == '/' && line.charAt(i + 1) == '/' ) {
                        line = line.substring(0, i);
                        v = 0;
                    }
                    if ( v == 1 && line.charAt(i) == '/' && line.charAt(i + 1) == '*' ){
                        line = line.substring(0, i);
                        v = 0;
                        
                        if( line !=null){
                            decode(line, cnt);
                            cnt++;
                            line = br.readLine();
                        }
                        boolean v2 = true;
                        while(v2){
                            line = br.readLine();
                            for (int j = 0; j < line.length()-1  && v2 == true; j++) {
                                if (line.charAt(i) == '*' && line.charAt(i + 1) == '/') {
                                    line = line.substring(i+1, line.length()-1);
                                    v2 = false;
                                }
                            }
                        
                        }
                    }

                }
                
                decode(line, cnt);
                cnt++;
                line = br.readLine();

            }

        } catch (IOException ex) {
            Logger.getLogger(CompilerCh4.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*
        System.out.println(whileArgumentList.size());
        System.out.println(whileConditionList.size());
        for(int k = 0;k< instructionList.size();k++)
            System.out.println(instructionList.get(k));
        for(int k = 0;k< whileArgumentList.size();k++)
            System.out.println(whileArgumentList.get(k)+ " "+ k+ " ");
        
        for(int k = 0; k<structureFathers.size();k++)
            System.out.println(k+ " " +structureFathers.get(k));
        */
        if(openLoops !=0) printError(errorCase[4], -1);
        if (verification == true) {
            run();
        }
        

    }

    private  void decode(String s, int line) {
        int location = 0;
        int ln = s.length();

        // reads one instruction
        for (int i = 0; i < ln; i++) {
            int locationOnCommand = 0;
            String command, name;

            while (i < ln && s.charAt(i) == ' ') {
                i++;
            }
            char[] aux = new char[100];
            while (i < ln && s.charAt(i) != ' ' && s.charAt(i) != ';') {
                aux[locationOnCommand] = s.charAt(i);
                locationOnCommand++;
                i++;
            }
            command = String.valueOf(aux, 0, locationOnCommand);
            int commandNumber = getCommand(command);

            if (commandNumber == -1) {
                printError(errorCase[2], line);
            }
            // this is the intialization of the variables
            if (commandNumber == 0) {

                while (i < ln && s.charAt(i) == ' ') {
                    i++;
                }
                locationOnCommand = 0;
                while (i < ln && s.charAt(i) != ' ' && s.charAt(i) != ';') {
                    aux[locationOnCommand] = s.charAt(i);
                    locationOnCommand++;
                    i++;
                }
                while (i < ln && s.charAt(i) == ' ') {
                    i++;
                }
                if (i < ln && s.charAt(i) == ';') {
                    name = String.valueOf(aux, 0, locationOnCommand);
                    boolean checkIfNameIsNumber = true;
                    for(int l=0;l<name.length();l++){
                        if(name.charAt(l) == '.'||name.charAt(l) == '-'||name.charAt(l) == '{'||name.charAt(l) == '['||name.charAt(l) == '}'||name.charAt(l) == ']'||name.charAt(l) == '+' || name.charAt(l) == '-'|| name.charAt(l) == ':'||name.charAt(l) == '*'|| name.charAt(l) == '(' ||name.charAt(l) == ')'){
                            checkIfNameIsNumber = false;
                        }
                    }
                    checkIfNameIsNumber = !checkIfNumber(name);
                    if(checkIfNameIsNumber == true){
                    //add the name to the  list of names 
                        nameList.add(name);

                        int lastOccurance = getIndex(new Variable(name, commandNumber, currentStructure));

                        //System.out.println(name+ commandNumber + currentStructure);
                        if (lastOccurance != -1) {
                            //4 is the command to reset the variable to the value of 0

                            instructionList.add(4);
                        } else {
                            instructionList.add(commandNumber);
                            variableList.add(new Variable(name, commandNumber, currentStructure));
                        }
                    }
                    else{
                        printError(errorCase[5], line);
                    }
                } else {
                    printError(errorCase[0], line);
                }
            }

            //System.out.println(line - 1);
            //System.out.println(nameList.get(line- 1));
            //Variable v = variableList.get(line - 1);
            //System.out.println(v.getName() + v.value);
            if (commandNumber == 1) {
                while (i < ln && s.charAt(i) == ' ') {
                    i++;
                }
                locationOnCommand = 0;
                while (i < ln && s.charAt(i) != ' ' && s.charAt(i) != ';') {
                    aux[locationOnCommand] = s.charAt(i);
                    locationOnCommand++;
                    i++;
                }
                while (i < ln && s.charAt(i) == ' ') {
                    i++;
                }

                if (i < ln && s.charAt(i) == ';') {
                    name = String.valueOf(aux, 0, locationOnCommand);

                    nameList.add(name);
                    instructionList.add(commandNumber);
                    //Variable v = variableList.get(0);

                    int lastOccurance = getIndex(new Variable(name, 0, currentStructure));
                    if (lastOccurance == -1) {
                        printError(errorCase[1], line);
                    }

                } else {
                    printError(errorCase[0], line);
                }

            }
            if (commandNumber == 2) {
                while (i < ln && s.charAt(i) == ' ') {
                    i++;
                }
                locationOnCommand = 0;
                while (i < ln && s.charAt(i) != ' ' && s.charAt(i) != ';') {
                    aux[locationOnCommand] = s.charAt(i);
                    locationOnCommand++;
                    i++;
                }
                while (i < ln && s.charAt(i) == ' ') {
                    i++;
                }

                if (i < ln && s.charAt(i) == ';') {
                    name = String.valueOf(aux, 0, locationOnCommand);

                    nameList.add(name);
                    instructionList.add(commandNumber);
                    //Variable v = variableList.get(0);

                    int lastOccurance = getIndex(new Variable(name, 0, currentStructure));
                    if (lastOccurance == -1) {
                        printError(errorCase[1], line);
                    }

                } else {
                    printError(errorCase[0], line);
                }

            }

            if (commandNumber == 3) {
                //System.out.println(line);
                while (i < ln && s.charAt(i) == ' ') {
                    i++;
                }
                locationOnCommand = 0;
                while (i < ln && s.charAt(i) != ' ') {
                    aux[locationOnCommand] = s.charAt(i);
                    locationOnCommand++;
                    i++;
                }
                //System.out.println(aux );
                if (i < ln && s.charAt(i) != ';') {
                    name = String.valueOf(aux, 0, locationOnCommand);
                    while (i < ln && s.charAt(i) == ' ') {
                        i++;
                    }
                    //System.out.println(name );
                    locationOnCommand = 0;
                    while (i < ln && s.charAt(i) != ' ') {
                        aux[locationOnCommand] = s.charAt(i);
                        locationOnCommand++;
                        i++;
                    }
                    String check = String.valueOf(aux, 0, locationOnCommand);
                    int tempCondition = getCondition(check);
                    
                    if (tempCondition != -1) {
                        while (i < ln && s.charAt(i) == ' ') {
                            i++;
                        }
                        locationOnCommand = 0;
                        String auxstring;
                        while (i < ln && s.charAt(i) != ' ') {
                            aux[locationOnCommand] = s.charAt(i);
                            locationOnCommand++;
                            i++;
                        }
                        auxstring = String.valueOf(aux, 0, locationOnCommand);
                        int argument = Integer.valueOf(auxstring);

                        if (i < ln && s.charAt(i) != ';') {
                            while (i < ln && s.charAt(i) == ' ') {
                                i++;
                            }
                            locationOnCommand = 0;
                            while (i < ln && s.charAt(i) != ' ' && s.charAt(i) != ';') {
                                aux[locationOnCommand] = s.charAt(i);
                                locationOnCommand++;
                                i++;
                            }
                            while (i < ln && s.charAt(i) == ' ') {
                                i++;
                            }
                            if (i < ln && s.charAt(i) == ';') {
                                auxstring = String.valueOf(aux, 0, locationOnCommand);
                                if (auxstring.equalsIgnoreCase("do")) {
                                    int lastOccurance = getIndex(new Variable(name, 0, currentStructure));
                                    if (lastOccurance == -1) {
                                        printError(errorCase[1], line);
                                    } else {
                                        nameList.add(name);
                                        instructionList.add(commandNumber);
                                        whileArgumentList.add(argument);
                                        whileConditionList.add(tempCondition);
                                        lastLoop = 1;
                                        openLoops++;
                                        structureFathers.add(currentStructure);
                                        currentStructure = structureLevel;
                                        structureLevel++;

                                    }
                                } else {
                                    printError(errorCase[2], line);
                                }
                            } else {
                                printError(errorCase[2], line);
                            }
                        } else {
                            printError(errorCase[2], line);
                        }
                    }
                } else {
                    printError(errorCase[2], line);
                }

            }
            if (commandNumber == 5) {

                currentStructure = structureFathers.get(currentStructure);
                instructionList.add(5);
                nameList.add("WhileOrIfEND");
                openLoops--;
                if(openLoops<0) {
                    printError(errorCase[3], line);
                    
                }
            }
            
            if(commandNumber == 6){
                while (i < ln && s.charAt(i) == ' ') {
                    i++;
                }
                locationOnCommand = 0;
                while (i < ln && s.charAt(i) != ' ') {
                    aux[locationOnCommand] = s.charAt(i);
                    locationOnCommand++;
                    i++;
                }
                //System.out.println(aux );
                if (i < ln && s.charAt(i) != ';') {
                    name = String.valueOf(aux, 0, locationOnCommand);
                    while (i < ln && s.charAt(i) == ' ') {
                        i++;
                    }
                    //System.out.println(name );
                    locationOnCommand = 0;
                    while (i < ln && s.charAt(i) != ' ') {
                        aux[locationOnCommand] = s.charAt(i);
                        locationOnCommand++;
                        i++;
                    }
                    String check = String.valueOf(aux, 0, locationOnCommand);
                    int tempCondition = getCondition(check);
                    if (tempCondition != -1) {
                        while (i < ln && s.charAt(i) == ' ') {
                            i++;
                        }
                        locationOnCommand = 0;
                        String auxstring;
                        while (i < ln && s.charAt(i) != ' ') {
                            aux[locationOnCommand] = s.charAt(i);
                            locationOnCommand++;
                            i++;
                        }
                        auxstring = String.valueOf(aux, 0, locationOnCommand);
                        int argument = Integer.valueOf(auxstring);

                        if (i < ln && s.charAt(i) != ';') {
                            while (i < ln && s.charAt(i) == ' ') {
                                i++;
                            }
                            locationOnCommand = 0;
                            while (i < ln && s.charAt(i) != ' ' && s.charAt(i) != ';') {
                                aux[locationOnCommand] = s.charAt(i);
                                locationOnCommand++;
                                i++;
                            }
                            while (i < ln && s.charAt(i) == ' ') {
                                i++;
                            }
                            if (i < ln && s.charAt(i) == ';') {
                                auxstring = String.valueOf(aux, 0, locationOnCommand);
                                if (auxstring.equalsIgnoreCase("do")) {
                                    int lastOccurance = getIndex(new Variable(name, 0, currentStructure));
                                    if (lastOccurance == -1) {
                                        printError(errorCase[1], line);
                                    } else {
                                        nameList.add(name);
                                        instructionList.add(commandNumber);
                                        whileArgumentList.add(argument);
                                        whileConditionList.add(tempCondition);
                                        lastLoop = 2;
                                        openLoops++;
                                        structureFathers.add(currentStructure);
                                        currentStructure = structureLevel;
                                        structureLevel++;

                                    }
                                } else {
                                    printError(errorCase[2], line);
                                }
                            } else {
                                printError(errorCase[2], line);
                            }
                        } else {
                            printError(errorCase[2], line);
                        }
                    }
                } else {
                    printError(errorCase[2], line);
                }

            }
            if(commandNumber == 7){
                while (i < ln && s.charAt(i) == ' ') {
                    i++;
                }
                if(i<ln && s.charAt(i)== ';'){
                    if(instructionList.get(instructionList.size()-1)==5){
                        if(lastLoop == 2){
                            openLoops++;
                            instructionList.add(7);
                            nameList.add("ElseForIfStatement");
                        }
                    }
                }
            }
            
            if(commandNumber == 8){
                while (i < ln && s.charAt(i) == ' ') {
                    i++;
                }
                locationOnCommand = 0;
                while (i < ln && s.charAt(i) != ' ' && s.charAt(i) != '=') {
                    aux[locationOnCommand] = s.charAt(i);
                    locationOnCommand++;
                    i++;
                }
                while (i < ln && s.charAt(i) == ' ') {
                    i++;
                }

                if (i < ln && s.charAt(i) == '=') {
                    name = String.valueOf(aux, 0, locationOnCommand);

                    nameList.add(name);
                    instructionList.add(commandNumber);
                    //Variable v = variableList.get(0);

                    int lastOccurance = getIndex(new Variable(name, 0, currentStructure));
                    if (lastOccurance == -1) {
                        printError(errorCase[1], line);
                    }
                    else{
                        i++;
                        locationOnCommand = 0;
                        while (i < ln && s.charAt(i) != ';') {
                            aux[locationOnCommand] = s.charAt(i);
                            locationOnCommand++;
                            i++;
                        }
                        if(i < ln && s.charAt(i)==';'){
                            name = String.valueOf(aux, 0, locationOnCommand);
                            name = name.replaceAll("\\s+","");
                            String tempName = name;
                            
                            tempName = replace(tempName, '(');
                            tempName = replace(tempName, ')');
                            tempName = replace(tempName, '[');
                            tempName = replace(tempName, ']');
                            tempName = replace(tempName, '{');
                            tempName = replace(tempName, '}');
                            tempName = replace(tempName, '-');
                            
                            int pos = 0, lastPos = 0;
                            boolean tempVerification = true;
                            while(pos <  tempName.length()){
                                
                                while(pos <  tempName.length() && tempName.charAt(pos)!='*'&& tempName.charAt(pos)!='/'&& tempName.charAt(pos)!='-'&& tempName.charAt(pos)!='+'){
                                    pos++;
                                }
                                String tempName2 = tempName.substring(lastPos, pos);
                                lastPos = pos+1;
                                if(!checkIfNumber(tempName2)){
                                    int lastOccurance2 = getIndex(new Variable(tempName2, 0, currentStructure));
                                    if (lastOccurance2 == -1) {
                                        tempVerification = false;
                                        printError(errorCase[1], line);
                                    }
                                    
                                }
                                
                                pos++;
                                //tempName = tempName.substring(pos);
                            }
                            solveList.add(name);
                        }
                        else{
                            printError(errorCase[0], line);
                        }
                    }
                } else {
                    printError(errorCase[0], line);
                }

            }

        }
    }

    private  void run() {
        programLength = instructionList.size();
        currentLevel = 0;
        maxLevel = 1;

        for (int i = 0; i < programLength; i++) {
            int command = instructionList.get(i);
            if (command == 0) {
                clearNumber(i);
                makeVisible(i);
                printVariables();
            }
            if (command == 1) {
                increment(i);
                printVariables();
            }
            if (command == 2) {
                decrement(i);
                printVariables();
            }
            if (command == 3) {

                currentLevel = maxLevel;
                maxLevel++;

                i = whilefc(i);

            }
            if (command == 4) {
                clearNumber(i);
                printVariables();
            }
            if (command == 5) {
                currentLevel = structureFathers.get(currentLevel);
            }
            if (command == 6) {
                currentLevel = maxLevel;
                maxLevel++;

                i = iffc(i);
            }
            if (command == 8){
                solvefc(i);
                printVariables();
            }

        }

    }

    public  void increment(int position) {
        Variable tempV = new Variable(nameList.get(position), 0, currentLevel);
        int pos = getIndex(tempV);
        tempV = variableList.get(pos);
        tempV.value++;
        variableList.set(pos, tempV);

    }

    public  void decrement(int position) {
        Variable tempV = new Variable(nameList.get(position), 0, currentLevel);
        int pos = getIndex(tempV);
        tempV = variableList.get(pos);
        tempV.value--;
        variableList.set(pos, tempV);
    }

    public  int whilefc(int position) {
        int command = 0, newPosition = position, i;

        Variable tempV2 = new Variable(nameList.get(position), 0, currentLevel);
        int pos2 = getIndex(tempV2);
        tempV2 = variableList.get(pos2);
        int whileLevel = currentLevel;
        boolean conditionVerification = verifyCondition(tempV2.value);
        
        if (!conditionVerification){
            currentLevel = structureFathers.get(currentLevel);
            int numberOfWhile = 1;
            for (i = position + 1; i < programLength && numberOfWhile > 0; i++) {
                command = instructionList.get(i);
                if (command == 3 || command == 6) {
                    numberOfWhile++;
                }
                if (command == 5) {
                    numberOfWhile--;
                }
            }
            newPosition = i - 1;

        } else {

            for (i = position + 1; i < programLength && command != 5; i++) {

                command = instructionList.get(i);

                if (command == 0) {
                    clearNumber(i);
                    makeVisible(i);
                    printVariables();
                }
                if (command == 1) {
                    increment(i);
                    printVariables();
                }
                if (command == 2) {
                    decrement(i);
                    printVariables();
                }
                if (command == 3) {

                    currentLevel++;

                    if (whileLevel > maxLevel) {
                        maxLevel++;
                    }

                    i = whilefc(i);

                }
                if (command == 4) {
                    clearNumber(i);
                    printVariables();
                }
                if (command == 5) {
                    Variable tempV = new Variable(nameList.get(position), 0, currentLevel);
                    int pos = getIndex(tempV);
                    tempV = variableList.get(pos);
                    conditionVerification = verifyCondition(tempV.value);
                    if (!conditionVerification) {
                        currentLevel = structureFathers.get(currentLevel);
                        newPosition = i;

                    } else {
                        currentLevel = whileLevel;
                        command = 0;
                        i = position;
                    }
                }
                if(command == 6){
                    i = iffc(i);
                }
                if (command == 8){
                    solvefc(i);
                    printVariables();
                }
            }
        }
        return newPosition;
    }

    public  void clearNumber(int position) {
        Variable tempV = new Variable(nameList.get(position), 0, currentLevel);
        int pos = getIndex(tempV);
        tempV = variableList.get(pos);
        tempV.value = 0;
        variableList.set(pos, tempV);
    }
    
    public  int iffc(int position){
        int command = 0, newPosition = position, i;

        Variable tempV2 = new Variable(nameList.get(position), 0, currentLevel);
        int pos2 = getIndex(tempV2);
        tempV2 = variableList.get(pos2);
        int whileLevel = currentLevel;
        
        boolean conditionVerification = verifyCondition(tempV2.value);
        
        if(conditionVerification){
            
            for(i =position+ 1;  i<programLength && command!=5 ; i++){
                command = instructionList.get(i);

                if (command == 0) {
                    clearNumber(i);
                    makeVisible(i);
                    printVariables();
                }
                if (command == 1) {
                    increment(i);
                    printVariables();
                }
                if (command == 2) {
                    decrement(i);
                    printVariables();
                }
                if (command == 3) {
                    currentLevel++;
                    if (whileLevel > maxLevel) {
                        maxLevel++;
                    }
                    i = whilefc(i);

                }
                if (command == 4) {
                    clearNumber(i);
                    printVariables();
                }
                if (command == 5) {
                    //we have nothing to de here
                }
                if(command == 6){
                    i = iffc(i);
                }
                if (command == 8){
                solvefc(i);
                printVariables();
                }
            }
            if(i<programLength){
                command = instructionList.get(i);
                if(command == 7){
                    currentLevel = structureFathers.get(currentLevel);
                    int numberOfLoops = 1;
                    for (; i < programLength && numberOfLoops > 0; i++) {
                        command = instructionList.get(i);
                        if (command == 3 || command == 6) {
                            numberOfLoops++;
                        }
                        if (command == 5) {
                            numberOfLoops--;
                        }
                    }
                    newPosition = i-1;
                }
                else newPosition = i-1;
            }
        }else{
            int numberOfLoops = 1;
                for (i=position+1; i < programLength && numberOfLoops > 0; i++) {
                    command = instructionList.get(i);
                    if (command == 3 || command == 6) {
                        numberOfLoops++;
                    }
                    if (command == 5) {
                        numberOfLoops--;
                    }
                }
                
                if(i<programLength)
                    command = instructionList.get(i);
                
                if(command == 7){
                    for(i = i+1;  i<programLength && command!=5 ; i++){
                        command = instructionList.get(i);

                        if (command == 0) {
                            clearNumber(i);
                            makeVisible(i);
                            printVariables();
                        }
                        if (command == 1) {
                            increment(i);
                            printVariables();
                        }
                        if (command == 2) {
                            decrement(i);
                            printVariables();
                        }
                        if (command == 3) {
                            currentLevel++;
                            if (whileLevel > maxLevel) {
                                maxLevel++;
                            }
                            i = whilefc(i);

                        }
                        if (command == 4) {
                            clearNumber(i);
                            printVariables();
                        }
                        if (command == 5) {
                        //we have nothing to de here
                        newPosition = i;
                        }
                        if(command == 6){
                             i = iffc(i);
                        }
                        if (command == 8){
                            solvefc(i);
                            printVariables();
                        }
                    }
                    
                }
                else {
                    newPosition = i-1;
                }
        }
        
        return newPosition;
    }
    
    public  void makeVisible(int position) {
        Variable tempV = new Variable(nameList.get(position), 0, currentLevel);
        int pos = getIndex(tempV);
        tempV = variableList.get(pos);
        tempV.visible = true;
        variableList.set(pos, tempV);
    }
    
    public  void solvefc( int position){
        Variable tempV = new Variable(nameList.get(position), 0, currentLevel);
        int pos = getIndex(tempV);
        tempV = variableList.get(pos);
        
        String expresion = solveList.get(counterExpresions);
        counterExpresions++;
        expresionPosition = 0;
        tempV.value = expresionfc(expresion);
        variableList.set(pos, tempV);
    }
    
    public  int expresionfc(String expresion){
        int sum = term(expresion);
        while(expresionPosition < expresion.length() && (expresion.charAt(expresionPosition) == '+' || expresion.charAt(expresionPosition) == '-')){
            if(expresion.charAt(expresionPosition) == '+'){
                expresionPosition++;
                sum+=term(expresion);
            }
            else{
                expresionPosition++;
                sum-=term(expresion);
            }
        }
        return sum;
    }
    
    public  int term(String expresion){
        int product = factor(expresion);
        while((expresionPosition < expresion.length()) && (expresion.charAt(expresionPosition) == '*' || expresion.charAt(expresionPosition) == '/')){
            if(expresion.charAt(expresionPosition) == '*'){
                expresionPosition++;
                product*=factor(expresion);
            }
            else{
                expresionPosition++;
                product/=factor(expresion);
            }
        }
        return product;
    }
    
    public  int factor(String expresion){
        int sign= 1, factorValue = 0, cntChar = 0, initPosition;
        String tempStr;
        char[] tempChar = new char[100];
        while((expresionPosition < expresion.length()) && expresion.charAt(expresionPosition) == '-'){
            expresionPosition++;
            sign = -sign;
        }
        if(expresion.charAt(expresionPosition) == '('){
            expresionPosition++;
            factorValue = expresionfc(expresion);
            expresionPosition++;
        }
        initPosition = expresionPosition;
        
        
        
        while((expresionPosition < expresion.length()) && !(expresion.charAt(expresionPosition) == '*' || expresion.charAt(expresionPosition) == '/' || expresion.charAt(expresionPosition) == '+' || expresion.charAt(expresionPosition) == '-' || expresion.charAt(expresionPosition) == ')' || expresion.charAt(expresionPosition) == ' ')){
            expresionPosition++;

        }
        tempStr = expresion.substring(initPosition, expresionPosition);
        
        if(initPosition !=expresionPosition){
            
        if(checkIfNumber(tempStr)){
            for(int k=0; k<tempStr.length(); k++){
                factorValue = factorValue * 10 + tempStr.charAt(k) - '0';
                  
            }
        }
        else {
            Variable tempV = new Variable(tempStr, 0, currentLevel);
            int pos = getIndex(tempV);
            tempV = variableList.get(pos);
            factorValue = tempV.value;
        }
        }
        return sign * factorValue;
    }
    
    public  void printVariables() {
        for (int j = 0; j < variableList.size(); j++) {
            Variable auxV = variableList.get(j);
            if (auxV.visible) {
                System.out.print(auxV.getName() + " " + auxV.value + " ");
                returnString = returnString.concat(auxV.getName());
                returnString = returnString.concat(" ");
                returnString = returnString.concat(Integer.toString(auxV.value));
                returnString = returnString.concat(" ");
                
            }
        }
        returnString = returnString.concat("\n");
        System.out.println();
    }

    private  void init() {
        structureFathers.add(0);
        whileArgumentList.add(0);
        whileConditionList.add(0);
        instructions[0] = "clear";
        instructions[1] = "incr";
        instructions[2] = "decr";
        instructions[3] = "while";
        // inst 4 is for giving a varible the value of 0
        instructions[5] = "end";
        instructions[6] = "if";
        instructions[7] = "else";
        instructions[8] = "solve";
        
        errorCase[0] = "Expected ; at the end of instruction";
        errorCase[1] = "Variable must be declared first";
        errorCase[2] = "No instruction found that matches";
        errorCase[3] = "No loops open";
        errorCase[4] = "One of the loops doesn't end";
        errorCase[5] = "Unacceptable variable name";
        
        condition[0] = "not";
        condition[1] = "is";
        condition[2] = "more";
        condition[3] = "less";
    }

    private  void printError(String s, int line) {
        verification = false;
        System.out.println(s + " on line: " + (line+1));
    }

    private  int getCommand(String s) {

        for (int i = 0; i < NUMBEROFINSTRUCTIONS; i++) {
            if (s.equals(instructions[i])) {
                return i;
            }
        }
        if (s.isEmpty()) {
            return -2;
        }
        return -1;
    }

    public  int getIndex(Variable v) {
        for (int i = 0; i < variableList.size(); i++) {
            Variable aux = variableList.get(i);
            if (aux.getName().equals(v.getName()) && aux.getInstruction() == v.getInstruction()) {
                int auxfather = v.structure;

                if (auxfather == aux.structure) {
                    return i;
                }
                while (auxfather != 0) {
                    auxfather = structureFathers.get(auxfather);

                    if (auxfather == aux.structure) {
                        return i;
                    }
                }

            }
        }
        return -1;
    }
    
    public  int getCondition(String check){
        for(int i=0;i<NUMBEROFCONDITIONS; i++){
            if(check.equals(condition[i])) return i;
        }
        return -1;
    }
    
    public  boolean verifyCondition( int tempValue){
        if(whileConditionList.get(currentLevel) == 0){
            if(tempValue == whileArgumentList.get(currentLevel)){
                return  false;
            }
        }
        if(whileConditionList.get(currentLevel) == 1){
            if(tempValue != whileArgumentList.get(currentLevel)){
                return false;
            }
        }
        if(whileConditionList.get(currentLevel) == 2){
            if(tempValue < whileArgumentList.get(currentLevel)){
                return false;
            }
        }
        if(whileConditionList.get(currentLevel) == 3){
            if(tempValue > whileArgumentList.get(currentLevel)){
                return false;
            }
        }
        return true;
    }

    private  boolean checkIfNumber(String argument) {
       for(int i = 0; i < argument.length();i++){
           if(!(argument.charAt(i) >= '0' && argument.charAt(i) <= '9'))
               return false;
       }
       return true;
    }

    private  String replace(String tempName, char charToRemove) {
        for(int i=0;i<tempName.length();i++){
            if(tempName.charAt(i) == charToRemove){
                tempName = tempName.substring(0, i) + tempName.substring(i+1);
                i--;
            }
        }
        return tempName;
    }
    
}
