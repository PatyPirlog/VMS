import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class VMS {
    List<Campaign> campaigns; //multimea de campanii existente
    List<User> users; //utilizatorii care pot primi diverse vouchere
    private static VMS obj = null;

    public VMS() {
        this.campaigns = new ArrayList<Campaign>();
        this.users = new ArrayList<User>();
    }

    public static VMS getInstance() {
        if(obj == null)
            obj = new VMS();
        return obj;
    }

    List<Campaign> getCampaigns() {
        return this.campaigns;
    }

    Campaign getCampaign(Integer id) {
        for(Campaign cmp : this.campaigns)
            if(cmp.campaignId.equals(id)) return cmp;
        return null;
    }

    void addCampaign(Campaign campaign) {
        this.campaigns.add(campaign);
    }

    //mai verific aici update-ul la campanie!!
    void updateCampaign(Integer id, Campaign campaign) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for(Campaign cmp : this.campaigns) {
            if(cmp.campaignId.equals(id)) {
                if(cmp.campaignStatusType.equals(CampaignStatusType.NEW)) {
                    if(cmp.totalVouchers < campaign.totalVouchers) {
                        cmp.totalVouchers = campaign.totalVouchers;
                        //cmp.currentVouchers = campaign.totalVouchers;
                    }
                    cmp.campaignName = campaign.campaignName;
                    cmp.campaignDescription = campaign.campaignDescription;
                    cmp.startDate = campaign.startDate;
                    cmp.endDate = campaign.endDate;
                    cmp.notifyAllObservers(new Notification(NotificationType.EDIT, LocalDateTime.now(), cmp.campaignId));

                }
                if(cmp.campaignStatusType.equals(CampaignStatusType.STARTED)) {
                    if(cmp.totalVouchers < campaign.totalVouchers) {
                        cmp.totalVouchers = campaign.totalVouchers;
                        cmp.currentVouchers += campaign.totalVouchers - cmp.totalVouchers;
                    }
                    cmp.endDate = campaign.endDate;
                    //trimit notificare la toti userii ca a fost editata campania cu campaignId
                    cmp.notifyAllObservers(new Notification(NotificationType.EDIT, LocalDateTime.now(), cmp.campaignId));
                }
            }
        }
    }

    void cancelCampaign(Integer id) {
        for(Campaign cmp : this.campaigns)
            if(cmp.campaignId.equals(id) &&
                    (cmp.campaignStatusType == CampaignStatusType.NEW || cmp.campaignStatusType == CampaignStatusType.STARTED)) {
                cmp.campaignStatusType = CampaignStatusType.CANCELLED;
                //pt fiecare user care are cel putin un voucher in campania cmp

                //trimit notificare la toti userii ca a fost editata campania cu campaignId
                cmp.notifyAllObservers(new Notification(NotificationType.CANCEL, LocalDateTime.now(), cmp.campaignId));
            }
    }

    User getUser(Integer id) {
        for(User usr : this.users)
            if(usr.userId.equals(id)) {
                return usr;
            }
        return null;
    }

    User getUser(String email) {
        for(User usr : this.users) {
            if(usr.email.equals(email))
                return usr;
        }
        return null;
    }

    List<User> getUsers() {
        return this.users;
    }

    void addUser(User user) {
        this.users.add(user);
    }


}
