import java.lang.reflect.Field;

public class ComponentApp {
	public static void main(String[] args) throws Exception
	{
		System.out.println("Run...");
		ComponentApp localMyApp = new ComponentApp();
		localMyApp.runComponent();
		System.out.println("Field Name: " + Class.forName(args[0]).getField(args[1]).getName());
		System.out.println("Value: " + Class.forName(args[0]).getField(args[1]).get(Class.forName(args[0]).newInstance()));
		System.out.println("Done.");
	}
	
	public void runComponent()
	{
		System.out.println("Called runComponent.");
	}
}
