package field;

import java.lang.reflect.Field;
import java.util.Arrays;
import static java.lang.System.out;

enum Tweedle {
   DEE, DUM
}

public class Book {
   public int chapters = 0;
   public String[] characters = { "Alice", "White Rabbit" };

   public static void main(String... args) {
      Book book = new Book();

      try {
         Class<?> c = book.getClass();
         modifyField(book, book.chapters, c);
         modifyField(book, book.characters, c);
      } catch (NoSuchFieldException e) {
         e.printStackTrace();
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      }
   }

   private static void modifyField(Book book, int chapters, Class<?> c) throws NoSuchFieldException, IllegalAccessException {
      String fmt = "%6S:  %-12s = %s%n";
      Field chap = c.getDeclaredField("chapters");
      out.format(fmt, "before", "chapters", chapters);
      chap.setInt(book, 12);
      out.format(fmt, "after", "chapters", chap.getInt(book));
   }

   private static void modifyField(Book book, String[] characters, Class<?> c) throws NoSuchFieldException, IllegalAccessException {
      String fmt = "%6S:  %-12s = %s%n";
      Field chars = c.getDeclaredField("characters");
      out.format(fmt, "before", "characters", Arrays.asList(characters));
      chars.set(book, new String[] { "Queen", "King" });
      out.format(fmt, "after", "characters", Arrays.asList((String[]) chars.get(book)));
   }
}