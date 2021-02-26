import java.util.*;

public class CampaignVoucherMap extends ArrayMap<String, ArrayList<Voucher>> {
    /*
    key = emailul userului
    value = colectia de vouchere distribuite
    Daca exista userul in dictionar, adaug voucherul in lista voucherelor care i-au fost distribuite; daca nu exista
    creez un arraylist in care voi adauga voucherul distribuit userului(ulterior alte vouchere daca ii vor mai fi
    distribuite).

     */
    boolean addVoucher(Voucher v) {
        if(containsKey(v.email)) {
            get(v.email).add(v);
            return true;
        } else {
            ArrayList<Voucher> vouchers = new ArrayList<Voucher>();
            vouchers.add(v);
            put(v.email, vouchers);
            return true;
        }

       // return false;
    }

}
