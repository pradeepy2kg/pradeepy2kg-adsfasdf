package lk.rgd.prs;

import lk.rgd.AppConstants;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.prs.api.domain.Address;
import lk.rgd.prs.api.domain.Marriage;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.service.PopulationRegistry;
import lk.transliterate.Transliterate;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * This is NOT a main class of the system, although its defined under the main source
 */
public class PopulationGenerator {

    private final PopulationRegistry popreg;
    private final RaceDAO raceDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final Race SINHALA;
    private final User system;
    private final Random rand = new Random(System.currentTimeMillis());
    private Map<Character, List<String[]>> names = new HashMap<Character, List<String[]>>(8000);
    private List<String> lastNames = new ArrayList<String>(90000);

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public PopulationGenerator(PopulationRegistry popreg, RaceDAO raceDAO, DSDivisionDAO dsDivisionDAO,
        UserManager userManager) throws Exception {

        System.out.println("\n\nStarting the Population Generator\n\n");
        for (char c = 'a'; c <= 'z'; c++) {
            names.put(c, new ArrayList());
        }

        DataInputStream in = new DataInputStream(this.getClass().getClassLoader().getResourceAsStream("names.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String name;
        while ((name = br.readLine()) != null) {
            char c = Character.toLowerCase(name.charAt(0));
            String[] n =
                {name,
                    Transliterate.translateWord(name, Transliterate.ENGLISH, Transliterate.SINHALA, Transliterate.MALE),
                    Transliterate.translateWord(name, Transliterate.ENGLISH, Transliterate.SINHALA, Transliterate.FEMALE),
                    Transliterate.translateWord(name, Transliterate.ENGLISH, Transliterate.TAMIL, Transliterate.MALE),
                    Transliterate.translateWord(name, Transliterate.ENGLISH, Transliterate.TAMIL, Transliterate.FEMALE)};
            names.get(c).add(n);
        }
        in.close();

        in = new DataInputStream(this.getClass().getClassLoader().getResourceAsStream("lastnames.txt"));
        br = new BufferedReader(new InputStreamReader(in));
        while ((name = br.readLine()) != null) {
            lastNames.add(name);
        }
        in.close();

        this.popreg = popreg;
        this.raceDAO = raceDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        system = userManager.getSystemUser();

        SINHALA = raceDAO.getRace(1);
    }

    public void init() throws Exception {
        Thread t;
        t = new Thread(new Worker(1, 7401, 100000));
        t.start();
        t = new Thread(new Worker(2, 102701, 200000));
        t.start();
        t = new Thread(new Worker(3, 202701, 300000));
        t.start();
        t = new Thread(new Worker(4, 304701, 400000));
        t.start();
        t = new Thread(new Worker(5, 402701, 500000));
        t.start();
        t = new Thread(new Worker(6, 505101, 600000));
        t.start();
        t = new Thread(new Worker(7, 609801, 700000));
        t.start();
        t = new Thread(new Worker(8, 704501, 800000));
        t.start();
        t = new Thread(new Worker(9, 811701, 900000));
        t.start();
        t = new Thread(new Worker(10, 903501, 1000000));
        t.start();
        t = new Thread(new Worker(11, 1005701, 1100000));
        t.start();
    }

    class Worker implements Runnable {
        private int id;
        private int start;
        private int stop;
        private Connection sltConn;

        Worker(int id, int start, int stop) {
            this.id = id;
            this.start = start;
            this.stop = stop;
            try {
                sltConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SLT", "root", "srilanka");
                sltConn.setAutoCommit(false);
                sltConn.setReadOnly(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() {
            int row = 0;
            System.out.println("Starting Thread : " + id);
            
            try {
                Statement s = sltConn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                s.setFetchSize(Integer.MIN_VALUE);
                ResultSet rs = s.executeQuery("select * from tp");
                
                while (rs.next()) {
                    if (row++ % 100 == 0) {
                        System.out.println("Thread : " + id + " row : " + row);
                    }
                    
                    if (row < start) continue;
                    if (row > stop)  break;

                    String lastname = rs.getString("lastname");
                    String firstname = rs.getString("firstname");
                    String address = rs.getString("address");
                    String phone = rs.getString("phone");
                    String areaCode    = phone.substring(0,2);
                    String phoneNumber = phone.substring(4);

                    generateFamily(id, lastname, firstname, address, phone);
                    generateFamily(id, getRandomLastname(), getRandomInitials(), getRandomAddress(address), areaCode + "4" + phoneNumber);
                    generateFamily(id, getRandomLastname(), getRandomInitials(), getRandomAddress(address), areaCode + "5" + phoneNumber);
                    generateFamily(id, getRandomLastname(), getRandomInitials(), getRandomAddress(address), getRandomMobile());
                    generateFamily(id, getRandomLastname(), getRandomInitials(), getRandomAddress(address), getRandomMobile());
                    generateFamily(id, getRandomLastname(), getRandomInitials(), getRandomAddress(address), getRandomMobile());
                    generateFamily(id, getRandomLastname(), getRandomInitials(), getRandomAddress(address), getRandomMobile());
                    generateFamily(id, getRandomLastname(), getRandomInitials(), getRandomAddress(address), getRandomMobile());
                    generateFamily(id, getRandomLastname(), getRandomInitials(), getRandomAddress(address), null);
                    generateFamily(id, getRandomLastname(), getRandomInitials(), getRandomAddress(address), null);
                }
                sltConn.close();
                System.out.println("Ending Thread : " + id);

            } catch (Exception e) {
                System.out.println("Thread : " + id + " failed at : " + row + new Date());
                e.printStackTrace();
            }
        }
    }

    private String getRandomMobile() {
        StringBuilder sb = new StringBuilder(12);
        sb.append("07");
        for (int i=0; i<8; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    private String getRandomInitials() {
        // decide on some random initials
        StringBuilder sb = new StringBuilder(12);
        int initials = 1 + rand.nextInt(5);
        for (int i = 0; i < initials; i++) {
            sb.append((char) ('A' + rand.nextInt(26))).append(' ');
        }
        if (rand.nextInt(10) < 4) {
            sb.append("Mrs");
        }
        return sb.toString();
    }

    private String getRandomLastname() {
        int pos = rand.nextInt(lastNames.size());
        return lastNames.get(pos);
    }

    private String getRandomAddress(String address) {
        if (address != null && address.length() > 5) {
            try {
                StringBuilder sb = new StringBuilder(60);
                char[] chars = address.toCharArray();
                int i=0;
                for (; i<chars.length; i++) {
                    if (Character.isDigit(chars[i])) {
                        continue;
                    } else {
                        break;
                    }
                }

                sb.append(rand.nextInt(300));
                if (rand.nextInt(10) < 2) {
                    sb.append('/');
                    sb.append(rand.nextInt(10));
                }
                if (rand.nextInt(10) < 2) {
                    sb.append( (char) ('A' + rand.nextInt(26)));
                }

                for (; i<chars.length; i++) {
                    sb.append(chars[i]);
                }
                return sb.toString();
            } catch (Exception e) {
                return address;
            }

        } else {
            return address;
        }
    }

    private void generateFamily(int id, String lastname, String firstname, String address, String phone) {

        try {
            // select gender
            int gender = 0;
            int spouseGender = 1;
            if (firstname.contains("Ms") || firstname.contains("Mrs") || firstname.contains("Miss")) {
                gender = 1;
                spouseGender = 0;
            }

            // decide race
            Race race = getRace(lastname);

            // pick a DOB
            int age = 25 + rand.nextInt(60);
            int day = rand.nextInt(365);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 2010 - age);
            cal.set(Calendar.DAY_OF_YEAR, day);
            String nic = generateNIC(gender, cal.get(Calendar.YEAR) % 100, day);

            // is married?
            Person.CivilStatus civilStatus;
            int r = rand.nextInt(100);
            if (r < 75) {
                civilStatus = Person.CivilStatus.MARRIED;
            } else if (r < 85) {
                civilStatus = Person.CivilStatus.NEVER_MARRIED;
            } else if (r < 90) {
                civilStatus = Person.CivilStatus.WIDOWED;
            } else if (r < 95) {
                civilStatus = Person.CivilStatus.DIVORCED;
            } else if (r < 97) {
                civilStatus = Person.CivilStatus.SEPARATED;
            } else {
                civilStatus = Person.CivilStatus.ANNULLED;
            }

            // generate a name
            StringBuilder sbOfficialName = new StringBuilder();
            StringBuilder sbEnglishName = new StringBuilder();

            String initString = firstname.replaceAll("Mr", "").
                replaceAll("Ms", "").replaceAll("Miss", "").replaceAll("Mrs", "");
            String[] initials = initString.split(" ");

            for (String letter : initials) {
                List<String[]> nameList = null;
                if (letter.length() == 0) {
                    nameList = names.get((char) ('a' + rand.nextInt(26)));
                } else {
                    nameList = names.get(Character.toLowerCase(letter.charAt(0)));
                    if (nameList == null) {
                        nameList = names.get((char) ('a' + rand.nextInt(26)));
                    }
                }
                int index = rand.nextInt(nameList.size());
                String[] nameFound = nameList.get(index);
                sbOfficialName.append(race == SINHALA ?
                    nameFound[gender == 0 ? 1 : 2] : nameFound[gender == 0 ? 3 : 4]).append(" ");
                sbEnglishName.append(nameFound[0]).append(" ");
            }

            sbEnglishName.append(lastname.toUpperCase());
            sbOfficialName.append(Transliterate.translateWord(lastname, Transliterate.ENGLISH,
                race == SINHALA ? Transliterate.SINHALA : Transliterate.TAMIL,
                gender == 0 ? Transliterate.MALE : Transliterate.FEMALE));

            Person person = new Person();
            person.setFullNameInEnglishLanguage(sbEnglishName.toString().trim());
            //TODO removed person.setInitialsInEnglish(firstname);
            person.setFullNameInOfficialLanguage(sbOfficialName.toString().trim());
            person.setDateOfBirth(cal.getTime());
            person.setRace(race);
            person.setGender(gender);
            person.setNic(nic);
            person.setCivilStatus(civilStatus);
            person.setStatus(nic != null ? Person.Status.SEMI_VERIFIED : Person.Status.UNVERIFIED);
            person.setPersonPhoneNo(phone);

    //        System.out.println("@@ Person " + lastname + " " + firstname + " | " +
    //            person.getFullNameInEnglishLanguage() + " | " + person.getFullNameInOfficialLanguage() + " | " +
    //            (gender == 0 ? "M" : "F") + " | " + person.getDateOfBirth() + " | " + nic);
            popreg.addPerson(person, system);

            Address addr = splitAddress(address);
            addr.setPerson(person);
    //        System.out.println("\tAddress : " + addr.getLine1() + " | " + addr.getCity());
            popreg.addAddress(addr, system);

            if (civilStatus == Person.CivilStatus.MARRIED) {
                generateSpouseAndChildren(person, spouseGender, age, race, lastname, addr);
            }
        } catch (Exception e) {
            System.out.println("Thread : " + id + " encountered an error : " + e.getMessage() + " at : " + new Date());
            e.printStackTrace();
        }
    }

    private void generateSpouseAndChildren(Person person, int spouseGender, int age, Race race, String lastname, Address addr) {

        Person father, mother;

        // decide spouse age
        if (rand.nextBoolean()) {
            age = age + rand.nextInt(6);
        } else {
            age = age - rand.nextInt(6);
        }
        Person spouse = generatePerson(spouseGender, age, race, lastname, addr);
        spouse.setCivilStatus(Person.CivilStatus.MARRIED);
//        System.out.println("\tSpouse : " +
//            spouse.getFullNameInEnglishLanguage() + " | " + spouse.getFullNameInOfficialLanguage() + " | " +
//            (spouse.getGender() == 0 ? "M" : "F") + " | " + spouse.getDateOfBirth() + " | " + spouse.getNic());
        popreg.addPerson(spouse, system);

        Marriage m = new Marriage();
        m.setPreferredLanguage(race == SINHALA ? AppConstants.SINHALA : AppConstants.TAMIL);
        m.setState(Marriage.State.MARRIED);
        if (spouseGender == 1) {
            m.setBride(spouse);
            m.setGroom(person);
            father = person;
            mother = spouse;
        } else {
            m.setBride(person);
            m.setGroom(spouse);
            father = spouse;
            mother = person;
        }

        // pick a date for marriage
        int yearsMarried = age - (20 + rand.nextInt(5));
        int day = rand.nextInt(365);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2010 - yearsMarried);
        cal.set(Calendar.DAY_OF_YEAR, day);
        m.setDateOfMarriage(cal.getTime());

        // we have 326 DS divisions, pick one randomly
        m.setPlaceOfMarriage(
            dsDivisionDAO.getNameByPK(1 + rand.nextInt(325), race == SINHALA ? AppConstants.SINHALA : AppConstants.TAMIL));
//        System.out.println("\tMarriage at : " + m.getPlaceOfMarriage() + " on : " + m.getDateOfMarriage());
        popreg.addMarriage(m, system);

        person.specifyMarriage(m);
        popreg.updatePerson(person, system);
        spouse.specifyMarriage(m);
        popreg.updatePerson(spouse, system);

        // decide on the number of children
        int children = rand.nextInt(10);
        if (children == 5 || children == 6) {
            children = 1;
        } else if (children == 7 || children == 8 || children == 9) {
            children = 2;
        }

        // minimum parent age will be 19
        int parentAge = age;
        for (int i = 0; i < children; i++) {
            age = parentAge - (15 + rand.nextInt(20));
            if (age < 0) {
                age = 0;
            }
            Person child = generatePerson(spouseGender, age, race, lastname, addr);
//            System.out.println("\tChild : " +
//                child.getFullNameInEnglishLanguage() + " | " + child.getFullNameInOfficialLanguage() + " | " +
//                (child.getGender() == 0 ? "M" : "F") + " | " + child.getDateOfBirth() + " | " + child.getNic());
            child.setFather(father);
            child.setMother(mother);
            popreg.addPerson(child, system);
        }
    }

    private Person generatePerson(int gender, int age, Race race, String lastname, Address addr) {

        Person person = new Person();
        StringBuilder sbOfficialName = new StringBuilder();
        StringBuilder sbEnglishName = new StringBuilder();
        StringBuilder initialsInEnglish = new StringBuilder();

        // decide on some random initials
        int initials = 1 + rand.nextInt(5);
        for (int i = 0; i < initials; i++) {
            List<String[]> nameList = names.get((char) (97 + rand.nextInt(26)));
            int index = rand.nextInt(nameList.size());
            String[] nameFound = nameList.get(index);
            sbOfficialName.append(race == SINHALA ?
                nameFound[gender == 0 ? 1 : 2] : nameFound[gender == 0 ? 3 : 4]).append(" ");
            sbEnglishName.append(nameFound[0]).append(" ");
            initialsInEnglish.append(nameFound[0].charAt(0)).append(" ");
        }

        sbEnglishName.append(lastname.toUpperCase());
        sbOfficialName.append(Transliterate.translateWord(lastname, Transliterate.ENGLISH,
            race == SINHALA ? Transliterate.SINHALA : Transliterate.TAMIL,
            gender == 0 ? Transliterate.MALE : Transliterate.FEMALE));

        person.setFullNameInEnglishLanguage(sbEnglishName.toString().trim());
        // TODO removed person.setInitialsInEnglish(initialsInEnglish.toString().toUpperCase().trim());
        person.setFullNameInOfficialLanguage(sbOfficialName.toString().trim());

        person.setGender(gender);
        person.setRace(race);

        // pick DOB
        int day = rand.nextInt(365);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2010 - age);
        cal.set(Calendar.DAY_OF_YEAR, day);
        person.setDateOfBirth(cal.getTime());
        String nic = generateNIC(gender, cal.get(Calendar.YEAR) % 100, day);

        person.setNic(nic);
        person.setStatus(nic != null ? Person.Status.SEMI_VERIFIED : Person.Status.UNVERIFIED);
        return person;
    }


    private Race getRace(String lastname) {
        if (lastname.endsWith("deen") || lastname.endsWith("abdul") || lastname.endsWith("eez")) {
            return raceDAO.getRace(4); // moor
        }
        if (lastname.endsWith("am") || lastname.endsWith("an") || lastname.endsWith("esh")
            || lastname.endsWith("esu") || lastname.endsWith("elu") || lastname.endsWith("rani")) {
            return raceDAO.getRace(2); // sri lankan or indian tamil
        }
        return raceDAO.getRace(1);
    }

    private static Address splitAddress(String line) {
        Address addr = new Address();
        int space = line.trim().lastIndexOf(" ");
        // if last part is a number, city is the last two
        if (space != -1) {
            String city = line.substring(space + 1);
            try {
                Integer.parseInt(city);
                // get last two words
                space = line.substring(0, space).lastIndexOf(" ");
                if (space != -1) {
                    addr.setCity(line.substring(space));
                    addr.setLine1(line.substring(0, space));
                }
            } catch (NumberFormatException ignore) {
                addr.setCity(city);
                addr.setLine1(line.substring(0, space));
            }
        } else {
            addr.setLine1(line);
        }

        // if somehow this resulted in a null line1, set the original address as line1
        if (addr.getLine1() == null || addr.getLine1().length() == 0) {
            addr.setLine1(line);
            addr.setCity(null);
        }
        return addr;
    }

    private String generateNIC(int gender, int year, int dayOfBirth) {
        if (year < 1995 && rand.nextInt(100) < 60) {
            // 60% has some old NIC
            StringBuilder sb = new StringBuilder(10);
            sb.append(year);

            if (gender == 1) {
                dayOfBirth += 500;
            }
            int zeros = 3 - Integer.toString(dayOfBirth).length();
            for (int i = 0; i < zeros; i++) {
                sb.append("0");
            }
            sb.append(dayOfBirth);

            String serial = Integer.toString(rand.nextInt(10000));
            zeros = 4 - serial.length();
            for (int i = 0; i < zeros; i++) {
                sb.append("0");
            }
            sb.append(serial);
            sb.append(rand.nextInt(100) < 80 ? "V" : "X");
            return sb.toString();
        }
        return null;
    }
}
