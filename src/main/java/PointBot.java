import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PointBot extends TelegramLongPollingBot{
    private Users users;
    {users = Users.getUsers();}

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            SendMessage message = new SendMessage()
                    .setChatId(chat_id)
                    .setText("error");


            String[] message_array = message_text.split(" ");

            if(!message_text.startsWith("/start") || message_array.length<=1){
                message.setText("Que?");
            } else {
                String[] stringMessage = message_array[1].split("_");
                String stringCredentials = stringMessage[0];
                int credentials = -1;
                boolean registration = false;
                try {
                    credentials = Integer.parseInt(stringCredentials);
                } catch(NumberFormatException e){
                    e.printStackTrace();
                }
                if(stringMessage.length==2)
                    if(stringMessage[1].equals("registration"))
                        registration = true;

                try {
                if (stringCredentials.isEmpty() || credentials==-1) {
                    message.setText("Hello");
                } else if (registration){
                    message.setText((String.valueOf(users.getCode(credentials))));
                } else{

                        if (users.isRegisteredUser(credentials)) {


                            if (users.isSameChat(credentials, chat_id)) {
                                message.setText((String.valueOf(users.getCode(credentials))));

                            } else {
                                message.setText("Uh-oh. Seems that it is a wrong account.");
                            }
                        } else {
                            int code = users.addUser(credentials, chat_id);
                            message.setText(String.valueOf(code));
                        }
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }
            }
            try {
                execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

    public String getBotUsername() {
        return Constants.BOT_NAME;
    }

    public String getBotToken() {
        return Constants.TOKEN;
    }
}
