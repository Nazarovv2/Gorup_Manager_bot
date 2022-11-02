import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.LinkedList;
import java.util.List;

public class PersonalChatManager {
    private Bot bot;
    private TextController textController;

    public PersonalChatManager(Bot bot) {
        this.bot = bot;
        textController = new TextController(bot);
    }

    public void handle(Message message) {
        if (message.hasText() && message.getText().equals("/help")) {
            help(message);
            return;
        }

        start(message);

    }


    private void start(Message message) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("\u200ESalom Man guruppani boshqarishda yordam beraman  "
                        + " Man ishlashim uchun guruhizga qoshib admin berishiz kerak\uD83D\uDE04"+
                "⚠️Botdan foydalanish uchun qollanma: /help ");

        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText("Guruhga qoshish+");
        inlineKeyboardButton.setUrl("https://t.me/uzgroup_manager_bot?startgroup=ru");

        List<InlineKeyboardButton> row = new LinkedList<>();
        row.add(inlineKeyboardButton);

        List<List<InlineKeyboardButton>> keyboard = new LinkedList<>();
        keyboard.add(row);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        bot.send(sendMessage);
    }

    private void help(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Helpni bosdi");
        sendMessage.setChatId(message.getChatId());
        bot.send(sendMessage);

    }
}
