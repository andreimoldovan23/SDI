import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ui.ClientConsole;

public class ClientApp {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("config");
        ClientConsole console = context.getBean(ClientConsole.class);
        console.runConsole();
        System.out.println("bye ");
    }
}
