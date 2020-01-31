import java.lang.reflect.Field;

public class MyApp {

   public static void main(String[] paramArrayOfString) throws Exception {

      System.out.println("Run...");
      MyApp localMyApp = new MyApp();
      localMyApp.foo();
      System.out.println("------------------------------------------");
      
      System.out.println("[DBG] Case 1: Instance.getClass() is used");
      displayFieldVal(localMyApp.getClass());
      System.out.println("------------------------------------------");
      
      System.out.println("[DBG] Case 2: ClassName.class is used");
      displayFieldVal(MyApp.class);
      System.out.println("------------------------------------------");
      
      System.out.println("[DBG] Case 3: Class.forName() is used");
      displayFieldVal(Class.forName("MyApp"));
      Object o = Class.forName("MyApp").getField("hiddenValue").get(Class.forName("MyApp").newInstance());
      System.out.println("[DBG] v: " + o);
      System.out.println("------------------------------------------");
      
      System.out.println("Done.");
      System.out.println("==========================================");
   }

   public static void displayFieldVal(Class<?> c) throws Exception {
      Field field = c.getField("hiddenValue");
      String fName = field.getName();
      Object fVal = field.get(c.newInstance());
      System.out.println(" Field name: " + fName + ", value: " + fVal);
   }

   public void foo() {
      System.out.println("Called foo.");
   }
}
