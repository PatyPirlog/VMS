import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Test {
    public static void main(String[] args) throws FileNotFoundException, ParseException {
        //instanta obiectului de care ma folosesc pt modelarea datelor
        VMS voucherManagementService = VMS.getInstance();

        //CAMPAIGNS
        File campaignsFile = new File("D:\\Paty\\POLI\\POO\\VMSTests\\test07\\input\\campaigns.txt");
        Scanner campaignSc = new Scanner(campaignsFile);
        //aici retin nr de campanii din fisier
        int nrOfCampaigns = Integer.parseInt(campaignSc.nextLine());
        System.out.println(nrOfCampaigns);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime currentAppDate = LocalDateTime.parse(campaignSc.nextLine(), formatter);
        System.out.println(currentAppDate);

        //adaug campaniile create din fisier in voucherManagementService
        for(int i = 0; i < nrOfCampaigns; i++) {
            String currentLine = campaignSc.nextLine();
            //retin in words datele de pe fiecare linie
            String[] words = currentLine.split(";");
            //id, nume, descr, start, end, budget x2, strategy
            LocalDateTime startDate = LocalDateTime.parse(words[3], formatter);
            LocalDateTime endDate = LocalDateTime.parse(words[4], formatter);

            Campaign newCampaign = new Campaign(Integer.parseInt(words[0]), words[1], words[2], startDate, endDate,
                    Integer.parseInt(words[5]), Integer.parseInt(words[5]), words[6]);

            if(currentAppDate.isBefore(startDate))
                newCampaign.campaignStatusType = CampaignStatusType.NEW;
            else if(currentAppDate.isAfter(endDate) || currentAppDate.isEqual(endDate))
                newCampaign.campaignStatusType = CampaignStatusType.EXPIRED;
            else
                newCampaign.campaignStatusType = CampaignStatusType.STARTED;

            voucherManagementService.campaigns.add(newCampaign);
        }

        //USERS
        File usersFile = new File("D:\\Paty\\POLI\\POO\\VMSTests\\test07\\input\\users.txt");
        Scanner usersSc = new Scanner(usersFile);

        int nrOfUsers = Integer.parseInt(usersSc.nextLine());
        //adaug userii din fisierul user.txt in voucherManagementService
        for(int i = 0; i < nrOfUsers; i++) {
            String currentLine = usersSc.nextLine();
            String[] userWords = currentLine.split(";");
            User newUser = new User(Integer.parseInt(userWords[0]), userWords[1], userWords[2], userWords[3], UserType.valueOf(userWords[4]));
            voucherManagementService.users.add(newUser);
        }

        //EVENTS
        File eventsFile = new File("D:\\Paty\\POLI\\POO\\VMSTests\\test07\\input\\events.txt");
        Scanner eventsSc = new Scanner(eventsFile);

        LocalDateTime currentAppDateEvents = LocalDateTime.parse(eventsSc.nextLine(), formatter);

        int nrOfEvents = Integer.parseInt(eventsSc.nextLine());
        //retin liniile din events
        List<String> lines = new ArrayList<String>();

        while(eventsSc.hasNextLine()) {
            String currentLine = eventsSc.nextLine();
            lines.add(currentLine);
        }
        /*
        parcurg fiecare linie din events.txt si in functie de ce e pe pozitia eventWords[1] efectuez operatia
        corespunzatoare
         */
        for(int i = 0; i < nrOfEvents; i++) {
            String[] eventWords = lines.get(i).split(";");
            int userId = Integer.parseInt(eventWords[0]);

            switch(eventWords[1]) {
                case "addCampaign":
                    LocalDateTime startDate = LocalDateTime.parse(eventWords[5], formatter);
                    LocalDateTime endDate = LocalDateTime.parse(eventWords[6], formatter);
                    String status;

                    //in functie de data curenta a aplicatiei setez statusul campaniei
                    if(currentAppDateEvents.isBefore(startDate))
                        status = "NEW";
                    else if((currentAppDateEvents.isEqual(startDate) || currentAppDateEvents.isBefore(startDate))
                            && currentAppDateEvents.isBefore(endDate))
                        status = "STARTED";
                    else
                        //if(currentAppDateEvents.isAfter(endDate) || currentAppDateEvents.isEqual(endDate))
                        status = "EXPIRED";

                    //adaug campania in voucherManagementService
                    if(voucherManagementService.getUser(userId).userType.equals(UserType.ADMIN)) {
                        voucherManagementService.addCampaign(new Campaign(Integer.parseInt(eventWords[2]), eventWords[3],
                                eventWords[4], startDate, endDate, Integer.parseInt(eventWords[7]),
                                Integer.parseInt(eventWords[7]), eventWords[8], CampaignStatusType.valueOf(status)));
                    }
                    continue;

                case "editCampaign":
                    if(voucherManagementService.getUser(userId).userType.equals(UserType.ADMIN)) {
                        //editez campania cu id-ul dat in functie de atributele date in event
                        Campaign editCampaign = new Campaign();
                        editCampaign.campaignId = Integer.parseInt(eventWords[2]);
                        editCampaign.campaignName = eventWords[3];
                        editCampaign.campaignDescription = eventWords[4];
                        editCampaign.startDate = LocalDateTime.parse(eventWords[5], formatter);
                        editCampaign.endDate = LocalDateTime.parse(eventWords[6], formatter);
                        editCampaign.totalVouchers = Integer.parseInt(eventWords[7]);
                        editCampaign.currentVouchers = Integer.parseInt(eventWords[7]);

                        voucherManagementService.updateCampaign(Integer.parseInt(eventWords[2]), editCampaign);

                    }
                    continue;

                case "cancelCampaign":
                    //daca userul e admin, inchid campania cu id-ul dat
                    if(voucherManagementService.getUser(userId).userType.equals(UserType.ADMIN)) {
                        voucherManagementService.cancelCampaign(Integer.parseInt(eventWords[2]));
                    }
                    continue;

                case "generateVoucher":
                    /*
                    daca userul e admin, sunt suficiente vouchere pt a se genera unul noi iar campania nu este
                    CANCELLED, atunci generez un voucher pe care il distribui utilizatorului cu emailul dat.
                    daca userul nu este in observatorii campaniei respective, il adaug
                    */
                    if(voucherManagementService.getUser(userId).userType.equals(UserType.ADMIN)

                            && (voucherManagementService.getCampaign(Integer.parseInt(eventWords[2])).currentVouchers > 0)
                    && !((voucherManagementService.getCampaign((Integer.parseInt(eventWords[2]))).campaignStatusType.equals(CampaignStatusType.CANCELLED)))){
                        if(!voucherManagementService.getCampaign(Integer.parseInt(eventWords[2])).existUser(voucherManagementService.getUser(eventWords[3]))) {
                            voucherManagementService.getCampaign(Integer.parseInt(eventWords[2])).addObserver(voucherManagementService.getUser(eventWords[3]));
                        }
                        voucherManagementService.getCampaign(Integer.parseInt(eventWords[2])).generateVoucher(eventWords[3],
                                eventWords[4], Float.parseFloat(eventWords[5]));
                    }
                    continue;

                case "redeemVoucher":
                    if(voucherManagementService.getUser(userId).userType.equals(UserType.ADMIN)) {
                        LocalDateTime date = LocalDateTime.parse(eventWords[4], formatter);
                        voucherManagementService.getCampaign(Integer.parseInt(eventWords[2])).redeemVoucher(Integer.parseInt(eventWords[3]), date);
                    }
                    //daca apelez metoda getVouchers si imi intoarce un array gol atunci nu mai are userul vouchere
                    if(voucherManagementService.getUser(userId).getVouchers().isEmpty()) {
                        voucherManagementService.getCampaign(Integer.parseInt(eventWords[2])).removeObserver(voucherManagementService.getUser(userId));
                    }
                    continue;

                case "getVouchers":
                    //intorc voucherele userului cu id-ul dat in event
                    if(voucherManagementService.getUser(userId).userType.equals(UserType.GUEST)) {
                        System.out.println(voucherManagementService.getUser(userId).getVouchers());
                    }
                    continue;

                case "getObservers":
                    //intorc observatorii campaniei date in event
                    if(voucherManagementService.getUser(userId).userType.equals(UserType.ADMIN)) {
                        System.out.println(voucherManagementService.getCampaign(Integer.parseInt(eventWords[2])).getObservers());
                    }
                    continue;

                case "getNotifications":
                    //intorc notificarile primite de userul specificat
                    if(voucherManagementService.getUser(userId).userType.equals(UserType.GUEST)) {
                        for(Notification n : voucherManagementService.getUser(userId).notifications) {
                            System.out.println(n);
                        }
                    }
                    continue;

                case "getVoucher":
                    //intorc voucherul distribuit specific strategiei campaniei in cadrul careia este distribuit
                    if(voucherManagementService.getUser(userId).userType.equals(UserType.ADMIN) &&
                    !(voucherManagementService.getCampaign(Integer.parseInt(eventWords[2])).campaignStatusType.equals(CampaignStatusType.CANCELLED))) {
                        System.out.println(voucherManagementService.getCampaign(Integer.parseInt(eventWords[2])).strategy + " " + voucherManagementService.getCampaign(Integer.parseInt(eventWords[2])).executeStrategy());
                    }
            }
        }
    }
}
