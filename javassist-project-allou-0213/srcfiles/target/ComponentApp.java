package target;

public class ComponentApp{
   int comX = 11, comY = 11;
   String comID = "Component";

   public void move(int dx, int dy, String str) {
      setX(dx);
      setY(dy);
      setString(str);
   }

   int setX(int dx) {
      this.comX = dx;
      return comX;
   }

   int setY(int dy) {
      this.comY = dy;
      return comY;
   }
   
   String setString(String str)
   {
	 this.comID = str;
	 return comID;
   }

   public static void main(String[] args) throws Exception {
      System.out.println("[ComponentApp] Run...");
      ComponentApp a = new ComponentApp();
      a.move(0,0,null);
      System.out.println("[ComponentApp] Done. ");
   }
}
