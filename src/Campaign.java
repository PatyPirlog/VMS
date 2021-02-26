import java.time.LocalDateTime;
import java.util.*;

enum CampaignStatusType{
    NEW, STARTED,
    EXPIRED, CANCELLED
}

public class Campaign {
    Integer campaignId; //tre sa fie unic
    String campaignName, campaignDescription;
    LocalDateTime startDate;
    LocalDateTime endDate;
    int totalVouchers, currentVouchers;
    CampaignStatusType campaignStatusType;
    CampaignVoucherMap voucherDict = new CampaignVoucherMap(); //dictionar de vouchere
    //era ArrayList<User> observers; inainte
    List<User> observers = new ArrayList<User>();//utilizatorii cu cel putin un voucher
    //totalVouchers - currentVouchers
    String strategy;
    Strategy strategyType;

    public Campaign() {
        observers = new ArrayList<User>();
    }

    public Campaign(Integer id, String name, String description, LocalDateTime start, LocalDateTime end, int total, int current, String strategy, CampaignStatusType type) {
        this.campaignId = id;
        this.campaignName = name;
        this.campaignDescription = description;
        this.startDate = start;
        this.endDate = end;
        this.totalVouchers = total;
        this.currentVouchers = current;
        this.strategy = strategy;
        this.campaignStatusType = type;
    }

    public Campaign(Integer id, String name, String description, LocalDateTime start, LocalDateTime end, int total, int current, String strategy) {
        this.campaignId = id;
        this.campaignName = name;
        this.campaignDescription = description;
        this.startDate = start;
        this.endDate = end;
        this.totalVouchers = total;
        this.currentVouchers = current;
        this.strategy = strategy;
    }

    public ArrayList<Voucher> getVouchers() {
        ArrayList<Voucher> campaignVouchers = new ArrayList<Voucher>();

        for(Map.Entry<String, ArrayList<Voucher>> entry : voucherDict.entrySet()) {
            campaignVouchers.addAll(entry.getValue());
        }

        return campaignVouchers;
    }

    Voucher getVoucher(String code) {
        for(Map.Entry<String, ArrayList<Voucher>> entry : voucherDict.entrySet()) {
            for(Voucher v : entry.getValue()) {
                if(v.code.equals(code))
                    return v;
            }
        }
        return null;
    }

    /*
    functie pt generarea id-ului unui voucher pornind de la 1
    Generez id-ul voucherelor scazand nr total de vouchere si nr de vouchere disponibile si apoi adunand 1
     */
    Integer generateId() {
        Integer id;
        id = this.totalVouchers - this.currentVouchers + 1;
        //scad 1 din voucherele disponibile pt ca am generat un voucher
        this.currentVouchers--;
        return id;
    }

    Voucher generateVoucher(String email, String voucherType, float value) {
        if(voucherType.equals("GiftVoucher")) {
            GiftVoucher giftVoucher = new GiftVoucher(value);
            giftVoucher.voucherId = generateId();
            giftVoucher.email = email;
            giftVoucher.code = giftVoucher.voucherId.toString();
            giftVoucher.statusType = VoucherStatusType.UNUSED;
            giftVoucher.campaignId = this.campaignId;
            //distribui userului cu emailul dat voucherul generat
            for(User usr : this.observers) {
                if (usr.email.equals(email))
                    usr.voucherDict.addVoucher(giftVoucher);
            }
            //adaug voucherul generat in dictionarul de vouchere distribuite
            this.voucherDict.addVoucher(giftVoucher);

            return giftVoucher;
        }
        else {
            LoyaltyVoucher loyaltyVoucher = new LoyaltyVoucher(value);
            loyaltyVoucher.voucherId = generateId();
            loyaltyVoucher.email = email;
            loyaltyVoucher.code = loyaltyVoucher.voucherId.toString();
            loyaltyVoucher.statusType = VoucherStatusType.UNUSED;
            loyaltyVoucher.campaignId = this.campaignId;

            for(User usr : this.observers) {
                if(usr.email.equals(email)) {
                    usr.voucherDict.addVoucher(loyaltyVoucher);
                }
            }
            this.voucherDict.addVoucher(loyaltyVoucher);
            return loyaltyVoucher;
        }
    }

    void redeemVoucher(Integer id, LocalDateTime date) {
        for(Map.Entry<String, ArrayList<Voucher>> entry : voucherDict.entrySet()) {
            for(Voucher v : entry.getValue()) {
                //daca agsesc voucherul cu codul dat si data e intre start si end si campania nu e cancelled
                if(v.voucherId.equals(id) && date.isAfter(startDate) && date.isBefore(endDate)
                        && !(this.campaignStatusType.equals(CampaignStatusType.CANCELLED)) && v.statusType.equals(VoucherStatusType.UNUSED)) {
                    v.statusType = VoucherStatusType.USED;
                    v.dateOfUsage = date;
                }
                    //VERIFIC DACA MAI ARE VOUCHERE USERUL DUPE CE FOLOSESTE UNUL
                //!!!!!
            }
        }
    }

    ArrayList<User> getObservers() {
        return (ArrayList<User>) this.observers;
    }

    boolean existUser(User user) {
        for(User usr : this.observers) {
            if(usr.equals(user))
                return true;
        }
        return false;
    }

    void addObserver(User user) {
        if(!existUser(user))
            this.observers.add(user);
    }

    void removeObserver(User user) {
        this.observers.remove(user);
    }

    void notifyAllObservers(Notification notification) {
        for(User usr : this.observers) {
            Notification newNotif = new Notification(notification.notificationType, notification.notificationDate, notification.campaignId);

            List<String> codes = new ArrayList<String>();
            //daca userul are vouchere in cadrul campaniei
                for(Map.Entry<Integer, ArrayList<Voucher>> entry : usr.voucherDict.entrySet() ) {
                    if(entry.getKey().equals(this.campaignId)) {
                        for(Voucher v : entry.getValue()) {
                            codes.add(v.getCode());
                        }
                    }
                }
            /*
             Trimit notificare tuturor userilor care au vouchere in cadrul campaniei in cazul in care se modifica
             o campanie sau devine CANCELLED
             */

            newNotif.codes.addAll(codes);
            usr.update(newNotif);
            codes.clear();
        }
    }

    public Voucher executeStrategy() {
        if(this.strategy.equals("A")) {
            strategyType = new StrategyA();
            return strategyType.execute(this);
        }
        if(this.strategy.equals("B")) {
            strategyType = new StrategyB();
            return strategyType.execute(this);
        }
        if(this.strategy.equals("C")) {
            strategyType = new StrategyC();
            return strategyType.execute(this);
        }
        return null;
    }

    public String toString() {
        return this.campaignId + " " + this.campaignName + " " + this.campaignDescription + " " + this.startDate + " " + this.endDate
                + " " + this.totalVouchers + " " + this.currentVouchers + " " + this.campaignStatusType + " " + this.strategy;
    }

}
