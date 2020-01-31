package classloader;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;
import util.UtilMenu;

public class SampleLoader extends ClassLoader {
   static String WORK_DIR = System.getProperty("user.dir");
   static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
   static String TARGET_APP;
   static String FIELD_NAME;
   private ClassPool pool;

   public static void main(String[] args) throws Throwable {
	   String[] arguments = new String[2];
	   while (true) {
	         UtilMenu.showMenuOptions();
	         int option = UtilMenu.getOption();
	         switch (option) {
	         case 1:
	            System.out.println("Please enter an application class name followed by a comma and a field name. Ex: ComponentApp, f1");
	            arguments = UtilMenu.getArguments();
	            if(arguments == null || arguments.length != 2)
	            {
	            	System.out.println("[WRN] Invalid Input!");
	            	break;
	            }
	         TARGET_APP = arguments[0];
	         FIELD_NAME = arguments[1];
           	 SampleLoader loader = new SampleLoader();
             Class<?> c = loader.loadClass(TARGET_APP);
             c.getDeclaredMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { arguments });

	            break;
	         default:
	            break;
	         }
	      }
	   }      
   

   public SampleLoader() throws NotFoundException {
      pool = new ClassPool();
      pool.insertClassPath(INPUT_DIR); // Search MyApp.class in this path.
   }

   /* 
    * Find a specified class, and modify the bytecode.
    */
   protected Class<?> findClass(String name) throws ClassNotFoundException {
      try {
         CtClass cc = pool.get(name);
         if (name.equals(TARGET_APP)) {
            CtField f = new CtField(CtClass.doubleType, FIELD_NAME, cc);
            f.setModifiers(Modifier.PUBLIC);
            cc.addField(f, CtField.Initializer.constant(0.0));
         }
         byte[] b = cc.toBytecode();
         return defineClass(name, b, 0, b.length);
      } catch (NotFoundException e) {
         throw new ClassNotFoundException();
      } catch (IOException e) {
         throw new ClassNotFoundException();
      } catch (CannotCompileException e) {
         throw new ClassNotFoundException();
      }
   }
}
