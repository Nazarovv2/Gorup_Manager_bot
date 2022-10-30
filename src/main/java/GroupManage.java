import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.*;

public class GroupManage {
    private Bot bot;
    private TextController textController;
    public Map<Group, List<User>> inviteList = new HashMap<>();

    public GroupManage(Bot bot) {
        this.bot = bot;
        textController = new TextController(bot);
    }

    public void handle(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChat().getId());
        sendMessage.setParseMode(ParseMode.HTML);
        if (message.hasText()) {
            textController.handle(message);

        } else if (message.getNewChatMembers().size() != 0) {

            message.getFrom();
            welcome(message.getNewChatMembers(), sendMessage, message.getFrom(), message);

        } else if (message.getLeftChatMember() != null) {
            leftChatMember(message, sendMessage);
        } else {


            sendMessage.setChatId(message.getChat().getId());
            sendMessage.setText(message.getText());
            bot.send(sendMessage);

        }

    }

    private void leftChatMember(Message message, SendMessage sendMessage) {
        User leftChatMember = message.getLeftChatMember();

        if (message.getFrom().equals(leftChatMember)) {
            sendMessage.setText("<a href=\"tg://user?id=" + leftChatMember.getId() + "\">" + leftChatMember.getFirstName() + "ni" + "</a> guruhdan chiqib ketti");
            bot.send(sendMessage);

            Set<Group> groups = inviteList.keySet();
            for (Group group : groups) {
                if (group.getGroupId().equals(message.getChatId())) {
                    List<User> userList = inviteList.get(group);
                    if (userList.contains(leftChatMember)) {
                        userList.remove(leftChatMember);
                        return;
                    }
                }
            }
        }


    }

    private void welcome(List<User> chatMembers, SendMessage sendMessage, User user, Message message) {


        List<User> userList = new ArrayList<>();

        for (User newChatMember : chatMembers) {
            if (!newChatMember.getIsBot()) {

                sendMessage.setText("Guruhga xush kelibsiz <a href=\"tg://user?id="
                        + newChatMember.getId() + "\">" + newChatMember.getFirstName() + "</a>");
                bot.send(sendMessage);
                if (!newChatMember.getId().equals(user.getId())) {
                    userList.add(newChatMember);
                }
            }
        }


        Set<Group> groups = inviteList.keySet();
        for (Group group : groups) {
            if (group.getGroupId().equals(message.getChatId()) && group.getUser().equals(user)) {
                List<User> userList1 = inviteList.get(group);
                userList1.addAll(userList);
                return;
            }
        }

        Group group = new Group();
        group.setGroupId(message.getChatId());
        group.setUser(user);
        inviteList.put(group, userList);


    }


    public void requestAdmin(ChatMemberUpdated myChatMember) {

        User from = myChatMember.getFrom();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.setChatId(myChatMember.getChat().getId());

        switch (myChatMember.getNewChatMember().getStatus()) {
            case "member":
                sendMessage.setText("Menga admin bering");
                break;
            case "administrator":
                sendMessage.setText("Barakalllo <a href=\"tg://user?id=" + from.getId() + "\">" + from.getFirstName() + "</a> admin" +
                        " berganing uchun rahmat \n " +
                        "\uD83D\uDC6E\uD83C\uDFFB\u200D♂️ Men bu Guruhda ishlashga tayyorman !\n" +
                        "\uD83D\uDD0E Funkisyalar bilan tanishish - /help" + bot.getBotUsername());
                break;
            case "kicked":
                break;

            default:
                System.out.println("Defolt ishladi");
        }

        bot.send(sendMessage);
    }
}
