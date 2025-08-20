import controllers.AppController;
import exceptions.ApplicationException;

public class Main {
    public static void main(String[] args) {
        try {
            AppController app = new AppController();
            app.start();
        }
        catch (ApplicationException e) {
            System.out.println("We ran into an error. Please try again.");
        }
    }
}
