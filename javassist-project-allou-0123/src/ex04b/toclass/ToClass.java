package ex04b.toclass;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import target.CommonComponentB;
import util.UtilMenu;

public class ToClass {
   private static final String PKG_NAME = "target" + ".";

   public static void main(String[] args) {
      while (true) {
         UtilMenu.showMenuOptions();
         switch (UtilMenu.getOption()) {
         case 1:
            System.out.println("Please enter one class name from the target package:");
            String[] inputs = UtilMenu.getArguments();
            if (inputs.length != 1)
            {
            	System.out.println("[WRN] Invalid Input");
            	continue;
            }
            process(inputs[0]);
            break;
         default:
            break;
         }
      }
      // process("HelloA", "iA", "jA");
      // System.out.println("------------------------------------------");
      // process("HelloB", "iB", "jB");
      // System.out.println("==========================================");
   }

   static void process(String clazz) {
      try {
         ClassPool cp = ClassPool.getDefault();
         CtClass cc = cp.get(PKG_NAME + clazz);
         CtField[] fields = cc.getFields();
         CtConstructor declaredConstructor = cc.getDeclaredConstructor(new CtClass[0]);
         for (int i = 0; i < fields.length; i++)
         {
        	 String block = "{ " //
                     + "System.out.println(\"[TR] " + fields[i].getName() + ": \" + " + fields[i].getName() + "); }";
               System.out.println("[DBG] BLOCK" + i +": " + block);
               declaredConstructor.insertAfter(block);
         }


         Class<?> c = cc.toClass();
          c.newInstance(); 
       
      } catch (NotFoundException | CannotCompileException | //
            InstantiationException | IllegalAccessException e) {
         System.out.println(e.toString());
      }
   }
}
