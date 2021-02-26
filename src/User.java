import java.util.*;
enum UserType {
    ADMIN, GUEST
}

public class User {
    Integer userId;
    String userName, email, password;
    UserType userType;
    UserVoucherMap voucherDict = new UserVoucherMap();
    ArrayList<Notification> notifications = new ArrayList<Notification>();

    public User(Integer id, String userName, String password, String email, UserType type) {
        this.userId = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.userType = type;
    }

    /*
    adaug notificarea in lista notificarilor userului
     */
    public void update(Notification notification) {
        this.notifications.add(notification);
    }

    //creez un arraylist pe care il intorc cu voucherele userului curent
    public ArrayList<Voucher> getVouchers() {
        List<Voucher> vouchers = new ArrayList<Voucher>();
        for(Map.Entry<Integer, ArrayList<Voucher>> entry : this.voucherDict.entrySet()) {
            vouchers.addAll(entry.getValue());
        }
        return (ArrayList<Voucher>) vouchers;
    }

    public String toString() {
        return "[" + this.userId + ";" + this.userName + ";" + this.email + ";" + this.userType + "]";
    }


}
