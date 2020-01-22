package util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class main {
   private static Scanner scanner = new Scanner(System.in);

   static final String SEP = File.separator;
   static String workDir = System.getProperty("user.dir");
   static String outputDir = workDir + SEP + "output";
   
   @Test
   public void testGetInputs() throws CannotCompileException, IOException, NotFoundException {
	 
	      ClassPool pool = ClassPool.getDefault();
	      System.out.println("=============================================");
	      System.out.println("Project Java Assist");
	
	      while (true) {
	         System.out.println("=============================================");
	         System.out.print("Enter three classes (\"q\" to terminate)\n");
	         String[] inputs = getInputs();
	         if (inputs.length != 3) 
	         {
	        	 System.out.println("[WRN] Invalid Input");
	        	 continue;
	         }
	         int commonInt = 0;
	         int found = 0;
	         String longestString = "";
	         String superClass = "";
	         for (int i = 0; i < inputs.length; i++)
	         {
	        	 if (inputs[i].startsWith("Common"))
	        	 {
	        		 commonInt++;
	        		 found = i;
	        	 }
	        	 if (inputs[i].length() > longestString.length()) longestString = inputs[i];	 
	         }
	         if (commonInt == 0) superClass = inputs[0];
	         else if (commonInt > 1) superClass = longestString;
	         else
	         {
	        	 superClass = inputs[found];
	         }
	         CtClass ccSuperClass = pool.makeClass(superClass);
	         ccSuperClass.writeFile(outputDir);
	         for (int i = 0; i < inputs.length; i++)
	         {
	        	if (inputs[i] == superClass) continue; 
	        	CtClass childClass = pool.makeClass(inputs[i]);
	            setSuperclass(childClass, superClass, pool);
	            childClass.writeFile(outputDir);
	        	
	         }
	         System.out.println("[DBG] write output to: " + outputDir);
	      
	      }
	
	     
	     
   }

   public void setSuperclass(CtClass curClass, String superClass, ClassPool pool) throws NotFoundException, CannotCompileException {
       curClass.setSuperclass(pool.get(superClass));
       System.out.println("[DBG] set superclass: " + curClass.getSuperclass().getName() + //
             ", subclass: " + curClass.getName());
    }
   
   public static String[] getInputs() {
      String input = scanner.nextLine();
      if (input.trim().equalsIgnoreCase("q")) {
         System.err.println("Terminated.");
         System.exit(0);
      }
      List<String> list = new ArrayList<String>();
      String[] inputArr = input.split(",");
      for (String iElem : inputArr) {
         list.add(iElem.trim());
      }
      return list.toArray(new String[0]);
   }
}
