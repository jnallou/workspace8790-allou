package newexpr;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;
import util.UtilMenu;

public class NewExprAccess extends ClassLoader {
   static final String WORK_DIR = System.getProperty("user.dir");
   static final String CLASS_PATH = WORK_DIR + File.separator + "classfiles";
   static  String TARGET_MY_APP;
   static int arguments;
   static String _L_ = System.lineSeparator();

   public static void main(String[] args) throws Throwable {
	   while (true) {
			UtilMenu.showMenuOptions();
			switch (UtilMenu.getOption()) {
			case 1:
				System.out.println("Enter 1)Class name 2)# of parameters");
				String[] input = UtilMenu.getArguments();
				
				if (input.length != 2) {
					System.out.println("[WRN]: Invalid Input");
					continue;
				}
					  TARGET_MY_APP = "target." + input[0].trim();
					  arguments =  Integer.parseInt(input[1].trim());
				      NewExprAccess s = new NewExprAccess();
				      Class<?> c = s.loadClass(TARGET_MY_APP);
				      Method mainMethod = c.getDeclaredMethod("main", new Class[] { String[].class });
				      mainMethod.invoke(null, new Object[] { args });
				
				break;
			default:
				break;
			}
	   }

   }

   private ClassPool pool;

   public NewExprAccess() throws NotFoundException {
      pool = new ClassPool();
      pool.insertClassPath(new ClassClassPath(new java.lang.Object().getClass()));
      pool.insertClassPath(CLASS_PATH); // TARGET must be there.
   }

   /*
    * Finds a specified class. The bytecode for that class can be modified.
    */
   protected Class<?> findClass(String name) throws ClassNotFoundException {
      CtClass cc = null;
      try {
         cc = pool.get(name);
         cc.instrument(new ExprEditor() {
            public void edit(NewExpr newExpr) throws CannotCompileException {
               CtField fields[] = newExpr.getEnclosingClass().getDeclaredFields();
        	   String log = String.format("[Edited by ClassLoader] new expr: %s, " //
                       + "line: %d, signature: %s", newExpr.getEnclosingClass().getName(), //
                       newExpr.getLineNumber(), newExpr.getSignature());
                 System.out.println(log);
                 String block1 = "{ " + _L_ //
							+ "  $_ = $proceed($$);" + _L_;

               if (fields.length < arguments)
               {
                     for (int i = 0; i < fields.length; i++ )
                     {
                    	 String fieldName = fields[i].getName();
						 try {
							String fieldType = fields[i].getType().getName();
							block1 += ( //
									"{ " + _L_ //
											+ "  String cName = $_.getClass().getName();" + _L_ //
											+ "  String fName = $_.getClass().getDeclaredFields()[" + i + "].getName();"
											+ _L_ //
											+ "  String fieldFullName = cName + \".\" + fName;" + _L_ //
											+ fieldType + " fieldValue = $_." + fieldName + ";" + _L_ //
											+ "  System.out.println( \"[Instrument] \" +  fieldFullName + \" :\" + fieldValue);"
											+ _L_ //
											+ "}" + _L_ //
									); //
						} catch (NotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
							
                     }
            	   
               }
               else
               {
                     for (int i = 0; i < arguments; i++ )
                     {
                    	 String fieldName = fields[i].getName();
						 try {
							String fieldType = fields[i].getType().getName();
							block1 += ( //
									"{ " + _L_ //
											+ "  String cName = $_.getClass().getName();" + _L_ //
											+ "  String fName = $_.getClass().getDeclaredFields()[" + i + "].getName();"
											+ _L_ //
											+ "  String fieldFullName = cName + \".\" + fName;" + _L_ //
											+ fieldType + " fieldValue = $_." + fieldName + ";" + _L_ //
											+ "  System.out.println( \"[Instrument] \" +  fieldFullName + \" :\" + fieldValue);"
											+ _L_ //
											+ "}" + _L_ //
									); //
						} catch (NotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                     }
			            	   
               }
              
               

               block1 += "}";
               System.out.println(block1);
               newExpr.replace(block1);
            }
               
         });
         byte[] b = cc.toBytecode();
         return defineClass(name, b, 0, b.length);
      } catch (NotFoundException e) {
         throw new ClassNotFoundException();
      } catch (IOException e) {
         throw new ClassNotFoundException();
      } catch (CannotCompileException e) {
         e.printStackTrace();
         throw new ClassNotFoundException();
      }
   }
}