package ex05.javassistloader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import javassist.NotFoundException;
import util.UtilMenu;

public class JavassistLoaderExample3 {
   static final String SEP = File.separator;
   static String workDir = System.getProperty("user.dir");
   static String outputDir = workDir + SEP + "output";
   private static final String WORK_DIR = System.getProperty("user.dir");
   private static final String TARGET_POINT = "target.Point";
   private static final String TARGET_RECTANGLE = "target.Rectangle";
   private static final String TARGET_CIRCLE = "target.Circle";   
   
   static HashSet<String> checkIfModified;
	  
   public static void main(String[] args) {

	  String firstMethod = null;
	  String secondMethod = null;
	  String thirdMethod = null;
	 checkIfModified =  new HashSet<>();
	 
      while (true) {
    	  
         UtilMenu.showMenuOptions();
         int option = UtilMenu.getOption();
         switch (option) {
         case 1:
            System.out.println("Please enter three method names, (1) a usage method, (2) an increment method, (3) a getter method, with the following format: e.g., add, incX, getX, or remove, incY, getY");
            String[] arguments = UtilMenu.getArguments();
            if(arguments == null || arguments.length != 3)
            {
            	System.out.println("[WRN] Invalid Input!");
            	break;
            }
            else
            {
            	firstMethod = arguments[0];
            	secondMethod = arguments[1];
            	thirdMethod = arguments[2];
            	if (checkIfModified.contains(firstMethod))
            	{
            		System.out.println("[WRN] This method \'" + firstMethod + "\' has been modified!!");
            		break;
            	}
            }
            
            System.out.println("Also, please enter three classes: ");
            findSub(UtilMenu.getArguments());
            
            analysisProcess(firstMethod, secondMethod, thirdMethod, TARGET_RECTANGLE);
            analysisProcess(firstMethod, secondMethod, thirdMethod, TARGET_CIRCLE);
            break;
         default:
            break;
         }
      }
   }
   
   public static void findSub(String[] args) {
	      try {
	         ClassPool pool = ClassPool.getDefault();
	        
	         if (args.length != 3)
	         {
	        	 System.out.println("Please enter three classes with a space separating each one next time");
	        	 return;
	         }
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

   static void analysisProcess(String firstMethod, String secondMethod, String thirdMethod, String subclass) {
      try {
         ClassPool cp = ClassPool.getDefault();
         insertClassPath(cp);

         CtClass cc = cp.get(subclass);
         cc.setSuperclass(cp.get(TARGET_POINT));
         CtMethod m1 = cc.getDeclaredMethod(firstMethod);
         String BLK1 = "{ " 
                 + secondMethod + "();" 
                 + "System.out.println(\"[TR] "+ thirdMethod + " result : \" + " + thirdMethod + "()); }";
         System.out.println("[DBG] Block: " + BLK1);
         m1.insertBefore(BLK1);

         Loader cl = new Loader(cp);
         Class<?> c = cl.loadClass(subclass);
         Object rect = c.newInstance();
         System.out.println("[DBG] Created an object.");

         Class<?> rectClass = rect.getClass();
         Method m = rectClass.getDeclaredMethod(firstMethod, new Class[] {});
         System.out.println("[DBG] Called getDeclaredMethod.");
         Object invoker = m.invoke(rect, new Object[] {});
         System.out.println("[DBG]" + thirdMethod + " result: " + invoker);
         checkIfModified.add(firstMethod);
         cc.defrost();
         
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   static void insertClassPath(ClassPool pool) throws NotFoundException {
      String strClassPath = WORK_DIR + File.separator + "classfiles";
      pool.insertClassPath(strClassPath);
      System.out.println("[DBG] insert classpath: " + strClassPath);
   }
   
   static void setSuperclass(CtClass curClass, String superClass, ClassPool pool) throws NotFoundException, CannotCompileException {
	      curClass.setSuperclass(pool.get(superClass));
	      System.out.println("[DBG] set superclass: " + curClass.getSuperclass().getName() + //
	            ", subclass: " + curClass.getName());
	   }
}
