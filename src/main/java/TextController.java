import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.groupadministration.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class TextController {
    private Bot bot;

    public TextController(Bot bot) {
        this.bot = bot;
    }

    public void handle(Message message) {
        String text = message.getText();

        if (!text.endsWith(bot.getBotUsername()) || !text.startsWith("/")) {
            return;
        }
        if (text.equals("/admins" + bot.getBotUsername())) {
            admins(message);
            return;

        }
        if (text.equals("/countinvites" + bot.getBotUsername())) {
            inviteCount(message);
            return;

        }

        if (!checkMemberStatus(message)) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("Siz admin emassiz");
            sendMessage.setReplyToMessageId(message.getMessageId());
            bot.send(sendMessage);
            return;
        }

        if (text.equals("/help" + bot.getBotUsername())) {
            help(message);
            return;
        }

        if (message.getReplyToMessage() == null) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("❓Bu buyruqni kimga nisbatan ishlatish  kerak bolsa shuning biron bir xabariga \n javob qaytargan holda buyruqni ishlating❓ ");
            sendMessage.setReplyToMessageId(message.getMessageId());
            bot.send(sendMessage);
            return;
        }

        if (text.equals("/ban" + bot.getBotUsername())) {
            banUser(message);
        } else if (text.equals("/unban" + bot.getBotUsername())) {
            unbanUser(message);
        } else if (text.equals("/mute" + bot.getBotUsername())) {
            mute(message);
        } else if (text.equals("/unmute" + bot.getBotUsername())) {
            unmute(message);
        } else if (text.equals("/admin" + bot.getBotUsername())) {
            admin(message);
        } else if (text.equals("/superadmin" + bot.getBotUsername())) {
            superAdmin(message);
        }


    }

    private void help(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText("Yordam nimag kere sanga ");
        bot.send(sendMessage);
    }

    private void inviteCount(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setParseMode(ParseMode.HTML);

        if (message.getReplyToMessage() == null) {
            User from = message.getFrom();
            Set<Group> groups = bot.groupManage.inviteList.keySet();
            for (Group group : groups) {
                if (group.getGroupId().equals(message.getChatId()) && group.getUser().equals(from)) {
                    StringBuilder text;
                    text = new StringBuilder();
                    List<User> userList = bot.groupManage.inviteList.get(group);
                    for (User user : userList) {

                        text.append("<a href=\"tg://user?id=").append(user.getId()).append("\">").append(user.getFirstName()).append("</a> \n");

                    }
                    sendMessage.setText("Siz jami " + userList.size() + " ta odam qoshgansiz \n Ularning royxati: \n" + text);
                    bot.send(sendMessage);
                    return;
                }
            }
        }

    }

    private void unbanUser(Message message) {
        User member = message.getReplyToMessage().getFrom();

        UnbanChatMember unbanChatMember = new UnbanChatMember();
        unbanChatMember.setChatId(message.getChatId());
        unbanChatMember.setUserId(member.getId());
        bot.send(unbanChatMember);
    }

    private void admins(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.setChatId(message.getChatId());

        GetChatAdministrators getChatAdministrators = new GetChatAdministrators();
        getChatAdministrators.setChatId(message.getChatId());
        ArrayList<ChatMember> admins = bot.send(getChatAdministrators);
        if (admins.size() == 0) {
            sendMessage.setText("\uD83D\uDC6E\u200D♂️Bu guruhda adminlar yo'q");
            return;
        }

        StringBuilder text;
        text = new StringBuilder("\uD83D\uDC6E\u200D♂️Adminlar royxati: \n");
        for (ChatMember admin : admins) {
            text.append("<a href=\"tg://user?id=").
                    append(admin.getUser().getId()).
                    append("\">").append(admin.getUser().getFirstName()).append("</a> \n");
        }

        sendMessage.setText(String.valueOf(text));

        bot.send(sendMessage);
    }

    private void admin(Message message) {
        User from = message.getReplyToMessage().getFrom();

        PromoteChatMember promoteChatMember = new PromoteChatMember();
        promoteChatMember.setChatId(message.getChatId());
        promoteChatMember.setUserId(from.getId());
        promoteChatMember.setCanPromoteMembers(false);
        promoteChatMember.setIsAnonymous(false);

        promoteChatMember.setCanChangeInformation(true);
        promoteChatMember.setCanDeleteMessages(true);
        promoteChatMember.setCanRestrictMembers(true);
        promoteChatMember.setCanInviteUsers(true);
        promoteChatMember.setCanPinMessages(true);
        promoteChatMember.setCanManageVideoChats(true);

        bot.send(promoteChatMember);
    }

    private void superAdmin(Message message) {
        User from = message.getReplyToMessage().getFrom();

        PromoteChatMember promoteChatMember = new PromoteChatMember();
        promoteChatMember.setChatId(message.getChatId());
        promoteChatMember.setUserId(from.getId());

        promoteChatMember.setCanPromoteMembers(true);
        promoteChatMember.setIsAnonymous(true);
        promoteChatMember.setCanChangeInformation(true);
        promoteChatMember.setCanDeleteMessages(true);
        promoteChatMember.setCanRestrictMembers(true);
        promoteChatMember.setCanInviteUsers(true);
        promoteChatMember.setCanPinMessages(true);
        promoteChatMember.setCanManageVideoChats(true);


        SetChatAdministratorCustomTitle administratorCustomTitle = new SetChatAdministratorCustomTitle();
        administratorCustomTitle.setChatId(message.getChatId());
        administratorCustomTitle.setUserId(from.getId());
        administratorCustomTitle.setCustomTitle("super admin");
        try {
            bot.execute(administratorCustomTitle);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        bot.send(promoteChatMember);
    }

    private void unmute(Message message) {
        User user = message.getReplyToMessage().getFrom();

        RestrictChatMember restrictChatMember = new RestrictChatMember();
        restrictChatMember.setUserId(user.getId());
        restrictChatMember.setChatId(message.getChatId());
        restrictChatMember.setPermissions(new ChatPermissions(
                true, true,
                true, true,
                true, true, true, true));
        bot.send(restrictChatMember);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.setText("<a href=\"tg://user?id=" + message.getFrom().getId() + "\">" + message.getFrom().getFirstName() + " </a>"
                + "<a href=\"tg://user?id=" + user.getId() + "\">" + user.getFirstName() + "dan" + "</a> mute rejimini olib tashladi✅️");

        bot.send(sendMessage);
    }

    private boolean checkMemberStatus(Message message) {
        GetChatMember getChatMember = new GetChatMember();
        getChatMember.setChatId(message.getChatId());
        getChatMember.setUserId(message.getFrom().getId());
        ChatMember send = bot.send(getChatMember);
        ChatPermissions chatPermissions = new ChatPermissions();
        RestrictChatMember restrictChatMember = new RestrictChatMember();
        restrictChatMember.setPermissions(chatPermissions);

        System.out.println(send.getStatus());

        return send.getStatus().equals("creator") || send.getStatus().equals("administrator");
    }

    private void banUser(Message message) {

        User member = message.getReplyToMessage().getFrom();
        BanChatMember banChatMember = new BanChatMember();
        banChatMember.setChatId(message.getChatId());
        banChatMember.setUserId(member.getId());
        banChatMember.setRevokeMessages(true);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.setText("<a href=\"tg://user?id=" + message.getFrom().getId() + "\">" + message.getFrom().getFirstName() + " </a>"
                + "<a href=\"tg://user?id=" + member.getId() + "\">" + member.getFirstName() + "ni" + "</a> guruhdan chopti⛔️");

        bot.send(banChatMember);

        bot.send(sendMessage);
    }

    private void mute(Message message) {
        User user = message.getReplyToMessage().getFrom();

        RestrictChatMember restrictChatMember = new RestrictChatMember();
        restrictChatMember.setUserId(user.getId());
        restrictChatMember.setChatId(message.getChatId());
        restrictChatMember.setPermissions(new ChatPermissions(
                false, false,
                false, false,
                false, false, false, false));

        bot.send(restrictChatMember);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.setText("<a href=\"tg://user?id=" + message.getFrom().getId() + "\">" + message.getFrom().getFirstName() + " </a>"
                + "<a href=\"tg://user?id=" + user.getId() + "\">" + user.getFirstName() + "ni" + "</a> mute  rejimiga otkazdi⛔️");

        bot.send(sendMessage);

    }

}
