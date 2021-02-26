import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

enum NotificationType {
    EDIT, CANCEL
}

public class Notification {
    NotificationType notificationType;
    LocalDateTime notificationDate;
    Integer campaignId;
    List<String> codes = new ArrayList<String>();

    public Notification(NotificationType type, LocalDateTime date, Integer campaignId) {
        this.notificationType = type;
        this.notificationDate = date;
        this.campaignId = campaignId;
    }

    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "[" + this.campaignId + ";" + this.codes + ";" + this.notificationDate.format(formatter) + ";" + this.notificationType + "]";
    }

}
