public class StrategyB implements Strategy {
    @Override
    public Voucher execute(Campaign cmp) {
        int maxUsedVouchers = 0;
        String loyalEmail = "";
        int usedVouchers = 0;
        /*
        Caut userul cu cele mai multe vouchere folosite si ii trimit un LoyaltyVoucher de 50
         */
        for(User usr: cmp.observers) {
            //if(usr.getVouchers().size() > maxVouchers) {
                usedVouchers = 0;
               for(Voucher v: usr.getVouchers()) {
                   if(v.statusType.equals(VoucherStatusType.USED))
                       usedVouchers++;
               }
               if(usedVouchers > maxUsedVouchers) {
                   maxUsedVouchers = usedVouchers;
                   loyalEmail = usr.email;
               }
            }

        return cmp.generateVoucher(loyalEmail, "LoyaltyVoucher", 50);
        //return null;
    }
}
