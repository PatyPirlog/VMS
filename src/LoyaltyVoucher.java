import java.time.LocalDateTime;
import java.util.Date;

public class LoyaltyVoucher extends Voucher {

    float discount;

    public LoyaltyVoucher () {

    }

    public LoyaltyVoucher(float giftSum, Integer id, String code, VoucherStatusType type, LocalDateTime dateUse, String email, Integer campaignId) {
        super.voucherId = id;
        super.code = code;
        super.statusType = type;
        super.dateOfUsage = dateUse;
        super.email = email;
        super.campaignId = campaignId;
        this.discount = discount;
    }
    public LoyaltyVoucher(float discount) {
        this.discount = discount;
    }

    void LoyaltyVoucher(VoucherStatusType statusType) {
        super.statusType = statusType;
    }

    public String toString() {
        return "[" + super.voucherId + ";" + super.statusType + ";" + super.email + ";" + this.discount +
                ";" + super.campaignId + ";" + super.dateOfUsage + "]";
    }

}
