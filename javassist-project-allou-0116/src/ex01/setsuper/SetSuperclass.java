package ex01.setsuper;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;


public class SetSuperclass {
   static final String SEP = File.separator;
   static String workDir = System.getProperty("user.dir");
   static String outputDir = workDir + SEP + "output";

   public static void main(String[] args) {
      try {
         ClassPool pool = ClassPool.getDefault();
        
         if (args.length != 3) return;
         String[] classes = new String[3];
         for (int i = 0; i < 3; i++)
         {
        	classes[i] = args[i]; 
         }
         int commonInt = 0;
         int found = 0;
         String longestString = "";
         String superClass = "";
         for (int i = 0; i < classes.length; i++)
         {
        	 if (classes[i].startsWith("Common"))
        	 {
        		 commonInt++;
        		 found = i;
        	 }
        	 if (classes[i].length() > longestString.length()) longestString = classes[i];	 
         }
         if (commonInt == 0) superClass = classes[0];
         else if (commonInt > 1) superClass = longestString;
         else
         {
        	 superClass = classes[found];
         }
         CtClass ccSuperClass = pool.makeClass(superClass);
         ccSuperClass.writeFile(outputDir);
         for (int i = 0; i < classes.length; i++)
         {
        	if (classes[i] == superClass) continue; 
        	CtClass childClass = pool.makeClass(classes[i]);
            setSuperclass(childClass, superClass, pool);
            childClass.writeFile(outputDir);
        	
         }
         
         System.out.println("[DBG] write output to: " + outputDir);
      } catch (NotFoundException | CannotCompileException | IOException e) {
         e.printStackTrace();
      }
   }

   static void setSuperclass(CtClass curClass, String superClass, ClassPool pool) throws NotFoundException, CannotCompileException {
      curClass.setSuperclass(pool.get(superClass));
      System.out.println("[DBG] set superclass: " + curClass.getSuperclass().getName() + //
            ", subclass: " + curClass.getName());
   }
}
