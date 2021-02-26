import java.time.LocalDateTime;
import java.util.*;

enum VoucherStatusType {
    USED, UNUSED
}

public abstract class Voucher {
    Integer voucherId;  //unic in cadrul campaniei
    String code;  //unic
    VoucherStatusType statusType;
    LocalDateTime dateOfUsage;
    String email;  //catre cine a fost ditribuit
    Integer campaignId;  //id-ul companiei in cadrul caruia a fost ditribuit

    public String getCode() {
        return this.code;
    }

}



