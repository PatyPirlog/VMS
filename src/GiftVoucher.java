import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class GiftVoucher extends Voucher {
    float giftSum;

    public GiftVoucher() {

    }
    public GiftVoucher(float giftSum, Integer id, String code, VoucherStatusType type, LocalDateTime dateUse, String email, Integer campaignId) {
        super.voucherId = id;
        super.code = code;
        super.statusType = type;
        super.dateOfUsage = dateUse;
        super.email = email;
        super.campaignId = campaignId;
        this.giftSum = giftSum;
    }
    public GiftVoucher(float giftSum) {
        this.giftSum = giftSum;
    }

    void modifyStatus(VoucherStatusType statusType) {
        super.statusType = statusType;
    }

    public String toString() {
        return "[" + super.voucherId + ";" + super.statusType + ";" + super.email + ";" + this.giftSum +
                ";" + super.campaignId + ";" + super.dateOfUsage + "]";
    }

}
