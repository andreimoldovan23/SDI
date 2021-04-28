import config.Config;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ui.Console;

public class ClientApp {
    
    public static void main(String[] args) {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Config.class);
        System.out.println("hey");

        Console clientConsole = context.getBean(Console.class);

        clientConsole.runConsole();

        System.out.println("bye");
    }

}
