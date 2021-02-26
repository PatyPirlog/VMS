import java.util.*;

public class UserVoucherMap extends ArrayMap<Integer, ArrayList<Voucher>> {
    //key = id-ul unei campanii
    //value = colectia de vouchere primite in cadrul campaniei resp
    /*
    asemanator cu CampaignVoucherMap doar ca key e reprezentat acum de id-ul campaniei
     */
    boolean addVoucher(Voucher v) {
        if(containsKey(v.campaignId)) {
            get(v.campaignId).add(v);
            return true;
        } else {
            ArrayList<Voucher> vouchers = new ArrayList<Voucher>();
            vouchers.add(v);
            put(v.campaignId, vouchers);
            return true;
        }
        //return false;
    }
}
