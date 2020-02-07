package insertmethodbody;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import util.UtilFile;
import util.UtilMenu;

public class InsertMethodBody {
   static String WORK_DIR = System.getProperty("user.dir");
   static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
   static String OUTPUT_DIR = WORK_DIR + File.separator + "output";
   static String TARGET_APP;
   static String FIELD_NAME;
   static String FIELD_PARAMETER;
   static String _L_ = System.lineSeparator();

   public static void main(String[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
	   while (true) {
	         UtilMenu.showMenuOptions();
	         int option = UtilMenu.getOption();
	         switch (option) {
	         case 1:
	            System.out.println("Please enter an application class name, a method name, and method parameter index. (e.g.., ComponentApp, foo, 1, or ServiceApp, bar, 2)");
	            String[] arguments = UtilMenu.getArguments();
	            if(arguments == null || arguments.length != 3)
	            {
	            	System.out.println("[WRN] Invalid Input!");
	            	break;
	            }
	         TARGET_APP = arguments[0].trim();
	         FIELD_NAME = arguments[1].trim();
	         FIELD_PARAMETER = arguments[2].trim();
	         try {
	        	 
	             ClassPool pool = ClassPool.getDefault();
	             pool.insertClassPath(INPUT_DIR);
	             CtClass cc = pool.get("target." + TARGET_APP);
	             cc.defrost();
	             CtMethod m = cc.getDeclaredMethod(FIELD_NAME);
	             String block1 = "";
	             if (Integer.valueOf(FIELD_PARAMETER) == 1)
	             {
	            	 block1 = "{ " + _L_ //
	  	                   + "System.out.println(\"[Inserted] target." + TARGET_APP + " " + FIELD_NAME + "'s parm 1: \" + $1); " + _L_ //
	  	                   + "}";
	             }
	             if (Integer.valueOf(FIELD_PARAMETER) == 2)
	             {
	            	 block1 = "{ " + _L_ //
		  	                   + "System.out.println(\"[Inserted] target." + TARGET_APP + " " + FIELD_NAME + "'s parm 2: \" + $2); " + _L_ //
		  	                   + "}"; 
	             }
	             
	             System.out.println(block1);
	             m.insertBefore(block1);
	             Class<?> c = cc.toClass(); 
	             c.getDeclaredMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { arguments }); 
	             cc.writeFile(OUTPUT_DIR);
	             System.out.println("[DBG] write output to: " + OUTPUT_DIR);
	             System.out.println("[DBG] \t" + UtilFile.getShortFileName(OUTPUT_DIR));
	             
	          } catch (NotFoundException | CannotCompileException | IOException e) {
	             e.printStackTrace();
	          }

	            break;
	         default:
	            break;
	         }
	      }
   }
}
	      
      