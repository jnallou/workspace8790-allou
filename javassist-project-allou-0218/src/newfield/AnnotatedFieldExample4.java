package newfield;

import java.io.File;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationImpl;
import javassist.bytecode.annotation.MemberValue;
import util.UtilMenu;

public class AnnotatedFieldExample4 {
   static String workDir = System.getProperty("user.dir");
   static String inputDir = workDir + File.separator + "classfiles";
   static String outputDir = workDir + File.separator + "output";
   static ClassPool pool;
   static String TARGET_MY_APP;
   static String FIRST;
   static String SECOND;

   public static void main(String[] args) {
      try {
    	  while (true) {
 	         UtilMenu.showMenuOptions();
 	         int option = UtilMenu.getOption();
 	         switch (option) {
 	         case 1:
 	            System.out.println("Please enter an application class name, first annotation name, and second annotation name,  (e.g.., ComponentApp, Column, Author  or ServiceApp, Row, Author)");
 	            String[] arguments = UtilMenu.getArguments();
 	            if(arguments == null || arguments.length != 3)
 	            {
 	            	System.out.println("[WRN] Invalid Input size!");
 	            	break;
 	            }
 	            TARGET_MY_APP = arguments[0].trim();
 	            FIRST = arguments[1].trim();
 	            SECOND = arguments[2].trim();
 	           
 	           pool = ClassPool.getDefault();
 	          pool.insertClassPath(inputDir);

 	          CtClass ct = pool.get("target."+ TARGET_MY_APP);
 	          CtField fields[] = ct.getDeclaredFields();
 	         // CtField cf = ct.getField("x");

 	          process(ct.getAnnotations());
 	          System.out.println();
 	          for (int i = 0; i < fields.length; i++)
 	        	  process(fields[i].getAnnotations());
 	      
 	          
 	            break;
 	         default:
 	            break;
 	         }
 	      }
      } catch (NotFoundException | ClassNotFoundException e) {
	          e.printStackTrace();
	       }
     
   }

   static void process(Object[] annoList) {

      boolean firstAnnFound = false;
      boolean secondAnnFound = false;
    
      for (int i = 0; i < annoList.length; i++) {
         Annotation annotation = getAnnotation(annoList[i]);
         
         if (annotation.getTypeName().equals("target." + FIRST)) {
        	 
        	 firstAnnFound = true;
        	 if (secondAnnFound)
        		 showAnnotation(annotation);
         }
         else if (annotation.getTypeName().equals("target." + SECOND)) {
        	 secondAnnFound = true;
        	 if (firstAnnFound)
        		 showAnnotation(annotation);
         }
         
      }
      
   }

   static Annotation getAnnotation(Object obj) {
      // Get the underlying type of a proxy object in java
      AnnotationImpl annotationImpl = //
            (AnnotationImpl) Proxy.getInvocationHandler(obj);
      return annotationImpl.getAnnotation();
   }

   static void showAnnotation(Annotation annotation) {
      Iterator<?> iterator = annotation.getMemberNames().iterator();
      while (iterator.hasNext()) {
         Object keyObj = (Object) iterator.next();
         MemberValue value = annotation.getMemberValue(keyObj.toString());
         System.out.print( keyObj + ": " + value );
        if (iterator.hasNext())
        		System.out.print(", ");
      }
      System.out.println();

   }
}
