import java.util.List;

public class NotificationQuery extends NotificationManager {
    public NotificationQuery(){}

    public List<String> getNotificationMessages(){
        return getNotificationMessagesInternal();
    }


}
