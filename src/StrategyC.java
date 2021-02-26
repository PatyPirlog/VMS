public class StrategyC implements Strategy{
    @Override
    public Voucher execute(Campaign cmp) {
        /*
        Caut userul cu cele mai putine vouchere si ii distribui un GiftVoucher de 100
         */
        int minVouchers = cmp.totalVouchers;
        String giftEmail = "";
        for(User usr : cmp.observers) {
            if(usr.getVouchers().size() < minVouchers) {
                minVouchers = usr.getVouchers().size();
                giftEmail = usr.email;
            }
        }
        return cmp.generateVoucher(giftEmail, "GiftVoucher", 100);

    }
}
