import java.util.*;

public class StrategyA implements Strategy {
    @Override
    public Voucher execute(Campaign cmp) {
        /*
        Aleg un user random cu Random si ii distribui un GiftVoucher in valoare de 100
         */
        Random rand = new Random();
        User randUsr;
        randUsr = cmp.observers.get(rand.nextInt(cmp.observers.size()));
        //System.out.println("Un user random este: " + randUsr);

        return cmp.generateVoucher(randUsr.email, "GiftVoucher", 100);
        //return null;
    }
}
