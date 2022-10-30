import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.groupadministration.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;


public class Bot extends TelegramLongPollingBot {

    public GroupManage groupManage;
    private PersonalChatManager personalChatManager;

    public Bot() {
        groupManage = new GroupManage(this);
        personalChatManager = new PersonalChatManager(this);
    }

    @Override
    public String getBotUsername() {
        return "@uzgroup_manager_bot";
    }

    @Override
    public String getBotToken() {
        return "5553748612:AAHmAsS2S9o9IktmoZ8Kss7OQG-l5QMzPJY";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.getFrom().getId().equals(message.getChatId())) {
                personalChatManager.handle(message);
            } else {
                groupManage.handle(message);
            }

        } else if (update.hasMyChatMember()) {

            groupManage.requestAdmin(update.getMyChatMember());

        }


    }

    public void send(SendMessage sms) {
        try {
            execute(sms);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(BanChatMember sms) {
        try {
            execute(sms);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public ChatMember send(GetChatMember sms) {
        try {
            return execute(sms);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(UnbanChatMember sms) {
        try {
            execute(sms);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(RestrictChatMember sms) {
        try {
            execute(sms);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(PromoteChatMember sms) {
        try {
            execute(sms);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ChatMember> send(GetChatAdministrators sms) {
        try {
            return execute(sms);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
