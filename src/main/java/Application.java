import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;


public class Application {
    public static void main(String[] args) {
        ApiContextInitializer.init();

        TelegramBotsApi api = new TelegramBotsApi();

        try{
            api.registerBot(new PointBot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
