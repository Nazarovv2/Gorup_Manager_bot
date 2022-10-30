import org.telegram.telegrambots.meta.api.objects.User;


public class Group {
    private Long groupId;
    private User user;


    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
