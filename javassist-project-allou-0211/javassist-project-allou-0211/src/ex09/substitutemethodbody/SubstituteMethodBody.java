package ex09.substitutemethodbody;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import util.UtilMenu;

public class SubstituteMethodBody extends ClassLoader {
   static final String WORK_DIR = System.getProperty("user.dir");
   static final String INPUT_PATH = WORK_DIR + File.separator + "classfiles";

   static String TARGET_MY_APP;
   static String METHOD;
   static String INDEX;
   static String VALUE;
   static String DRAW_METHOD = "draw";
   static ArrayList<String> modified = new ArrayList<>();

   static String _L_ = System.lineSeparator();

   public static void main(String[] args) throws Throwable {
	   while (true) {
	         UtilMenu.showMenuOptions();
	         int option = UtilMenu.getOption();
	         switch (option) {
	         case 1:
	            System.out.println("Please enter an application class name, a method name, method parameter index, and value to be assigned to the parameter index. (e.g.., ComponentApp, move, 1, o, or ServiceApp, fill, 2, 10)");
	            String[] arguments = UtilMenu.getArguments();
	            if(arguments == null || arguments.length != 4)
	            {
	            	System.out.println("[WRN] Invalid Input size!");
	            	break;
	            }
	            TARGET_MY_APP = arguments[0].trim();
	            METHOD = arguments[1].trim();
	            INDEX = arguments[2].trim();
	            VALUE = arguments[3].trim();
	            
	            if (modified.contains(METHOD)) 
	            {
	            	System.out.println("[WRN] This method " + METHOD + " has been modified!!");
	            	break;
	            }
	            
	            SubstituteMethodBody s = new SubstituteMethodBody();
	            Class<?> c = s.loadClass("target." + TARGET_MY_APP);
	            Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
	            mainMethod.invoke(null, new Object[] { args });
	            break;
	         default:
	            break;
	         }
	      }
     
   }

   private ClassPool pool;

   public SubstituteMethodBody() throws NotFoundException {
      pool = new ClassPool();
      pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
      pool.insertClassPath(INPUT_PATH); // "target" must be there.
      System.out.println("[DBG] Class Pathes: " + pool.toString());
   }

   /*
    * Finds a specified class. The bytecode for that class can be modified.
    */
   protected Class<?> findClass(String name) throws ClassNotFoundException {
      CtClass cc = null;
      try {
    
         cc = pool.get(name);
         cc.defrost();
         if (!cc.getName().equals("target." + TARGET_MY_APP)) {
        	 System.out.println("Here");
            return defineClass(name, cc.toBytecode(), 0, cc.toBytecode().length);
         }

         cc.instrument(new ExprEditor() {
            public void edit(MethodCall call) throws CannotCompileException {
               String className = call.getClassName();
               String methodName = call.getMethodName();

              if (className.equals("target." +TARGET_MY_APP) && methodName.equals(DRAW_METHOD)) {
                  String block1 = "{" + _L_ //
                        + "$proceed($$); " + _L_ //
                        + "}";
                  call.replace(block1);
               } else if (className.equals("target." +TARGET_MY_APP) && methodName.equals(METHOD)) {
            	   modified.add(METHOD);
                  String block2 = "{" + _L_ //
                          + "System.out.println(\"Reset param " + INDEX + " to " + VALUE + ".\"); " + _L_ //
                        + "$" + INDEX + " = " + VALUE + ";" + _L_ //
                        + "$proceed($$); " + _L_ //
                        + "}";
                  call.replace(block2);
               }
            }
         });
         byte[] b = cc.toBytecode();
     
         return defineClass(name, b, 0, b.length);
      } catch (NotFoundException e) {
         throw new ClassNotFoundException();
      } catch (IOException e) {
         throw new ClassNotFoundException();
      } catch (CannotCompileException e) {
    	 System.out.println("[WRN] This method has been modified!");
         throw new ClassNotFoundException();
      }
   }
}
