package lk.transliterate;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * A better Transliterator for use in Sri Lanka
 *
 * Copyright (c) 2010 Asankha Perera (http://adroitlogic.org). All Rights Reserved.
 *
 * GNU Affero General Public License Usage
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program
 * If not, see http://www.gnu.org/licenses/agpl-3.0.html
 *
 * @author asankha perera (asankha AT gmail DOT com)
 * @since 6th November 2010
 *
 * Reuses rules from the ICTA transliterator which had severe code/architectural
 * issues making it unsuitable for any real work
 */
public class Transliterate {

    private static final ClassLoader CLS_LDR = Transliterate.class.getClassLoader();
    private static boolean DEBUG = Boolean.getBoolean("debug");

    public static final int ENGLISH = 0;
    public static final int SINHALA = 1;
    public static final int TAMIL   = 2;

    public static final int UNKNOWN = 0;
    public static final int MALE = 1;
    public static final int FEMALE = 2;

    private static final String END_VOVELS   = ".aeiou#";
    private static final String START_VOVELS = ".aeiou";

    private static final Map<String, String> engToSinNames = new HashMap<String, String>();
    private static final Map<String, String> engToSinOther = new HashMap<String, String>();

    private static final Map<String, String> sinToEngNames = new HashMap<String, String>();
    private static final Map<String, String> sinToEngOther = new HashMap<String, String>();

    private static final Map<String, String> engToTamNames = new HashMap<String, String>();
    private static final Map<String, String> engToTamOther = new HashMap<String, String>();

    private static final Map<String, String> tamToEngNames = new HashMap<String, String>();
    private static final Map<String, String> tamToEngOther = new HashMap<String, String>();

    private static final Map<String, String> sinToTamNames = new HashMap<String, String>();
    private static final Map<String, String> sinToTamOther = new HashMap<String, String>();

    private static final Map<String, String> tamToSinNames = new HashMap<String, String>();
    private static final Map<String, String> tamToSinOther = new HashMap<String, String>();

    private static lk.transliterate.LangToPhonetic[] enToPhRules;
    private static lk.transliterate.LangToPhonetic[] siToPhRules;
    private static lk.transliterate.LangToPhonetic[] taToPhRules;

    private static lk.transliterate.PhoneticToLang[] phToSiRules;
    private static lk.transliterate.PhoneticToLang[] phToTaRules;
    private static lk.transliterate.PhoneticToLang[] phToEnRules;

    static {
        try {
            enToPhRules = loadLangToPhoneticFile("lk/transliterate/rules-en.txt");
            siToPhRules = loadLangToPhoneticFile("lk/transliterate/rules-si.txt");
            taToPhRules = loadLangToPhoneticFile("lk/transliterate/rules-ta.txt");

            phToSiRules = loadPhoneticToLangFile("lk/transliterate/phonetic-si.txt");
            phToTaRules = loadPhoneticToLangFile("lk/transliterate/phonetic-ta.txt");
            phToEnRules = loadPhoneticToLangFile("lk/transliterate/phonetic-en.txt");

            loadMappingFile("lk/transliterate/en-to-si.txt", engToSinNames, engToSinOther, sinToEngNames, sinToEngOther);
            loadMappingFile("lk/transliterate/en-to-ta.txt", engToTamNames, engToTamOther, tamToEngNames, tamToEngOther);
            loadMappingFile("lk/transliterate/si-to-ta.txt", sinToTamNames, sinToTamOther, tamToSinNames, tamToSinOther);

            if (DEBUG) {
                System.out.println("En to Ph rules : " + enToPhRules.length);
                System.out.println("Si to Ph rules : " + siToPhRules.length);
                System.out.println("Ta to Ph rules : " + taToPhRules.length);
                System.out.println("Ph to Si rules : " + phToSiRules.length);
                System.out.println("Ph to Ta rules : " + phToTaRules.length);
                System.out.println("Ph to En rules : " + phToEnRules.length);
                System.out.println("En to Si mappings : " + engToSinNames.size() + " : " + engToSinOther.size());
                System.out.println("En to Ta mappings : " + engToTamNames.size() + " : " + engToTamOther.size());
                System.out.println("Si to Ta mappings : " + sinToTamNames.size() + " : " + sinToTamOther.size());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        //System.out.println(translateLine("\"Kumarasiri\" asankha-chamath, #Perera#", ENGLISH, SINHALA, MALE));

        int src = ENGLISH;
        int dst = SINHALA;
        int gender = UNKNOWN;

        System.out.println("java -jar transliterate.jar [-s si|ta|en] [-t si|ta|en] [-g m|f|u]");

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-s")) {
                if (i + 1 < args.length) {
                    src = "si".equals(args[i + 1]) ? SINHALA : "ta".equals(args[i + 1]) ? TAMIL : ENGLISH;
                }
            }
            if (args[i].equals("-t")) {
                if (i + 1 < args.length) {
                    dst = "si".equals(args[i + 1]) ? SINHALA : "ta".equals(args[i + 1]) ? TAMIL : ENGLISH;
                }
            }
            if (args[i].equals("-g")) {
                if (i + 1 < args.length) {
                    gender = "m".equals(args[i + 1]) ? MALE : "f".equals(args[i + 1]) ? FEMALE : UNKNOWN;
                }
            }
        }

        Scanner sc = new Scanner(System.in);
        String s = null;
        try {
            while ((s = sc.nextLine()) != null) {
                System.out.println(translateLine(s, src, dst, gender));
            }
        } catch (NoSuchElementException ignore) {
        }
    }

    private static lk.transliterate.LangToPhonetic[] loadLangToPhoneticFile(String filename) throws Exception {
        DataInputStream in = new DataInputStream(CLS_LDR.getResourceAsStream(filename));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String s;
        List<lk.transliterate.LangToPhonetic> list = new ArrayList<lk.transliterate.LangToPhonetic>();
        while ((s = br.readLine()) != null) {
            String[] p = s.split("\t");
            list.add(new lk.transliterate.LangToPhonetic(p[2].replaceAll("%", ".*"), p[0], p[3], p[4].replaceAll("%", "")));
        }
        in.close();
        return list.toArray(new lk.transliterate.LangToPhonetic[list.size()]);
    }

    private static lk.transliterate.PhoneticToLang[] loadPhoneticToLangFile(String filename) throws Exception {
        DataInputStream in = new DataInputStream(CLS_LDR.getResourceAsStream(filename));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String s;
        List<lk.transliterate.PhoneticToLang> list = new ArrayList<lk.transliterate.PhoneticToLang>();
        while ((s = br.readLine()) != null) {
            String[] p = s.split("\t");
            list.add(new lk.transliterate.PhoneticToLang(p[0].replaceAll("\\.", "\\\\.").replaceAll("%", ".*"), p[1].replaceAll("%", ""), p[2]));
        }
        in.close();
        return list.toArray(new lk.transliterate.PhoneticToLang[list.size()]);
    }

    private static void loadMappingFile(String filename,
        Map<String, String> namesOne, Map<String, String> otherOne,
        Map<String, String> namesTwo, Map<String, String> otherTwo) throws Exception {

        DataInputStream in = new DataInputStream(CLS_LDR.getResourceAsStream(filename));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String s;
        while ((s = br.readLine()) != null) {
            String[] p = s.toLowerCase().split("\t");
            if ("1".equals(p[2]) || "1".equals(p[3])) {
                // this is a person name
                if (!namesOne.containsKey(p[0])) {
                    namesOne.put(p[0], p[1]);
                }
                if (!namesTwo.containsKey(p[1])) {
                    namesTwo.put(p[1], p[0]);
                }
            } else {
                if (!otherOne.containsKey(p[0])) {
                    otherOne.put(p[0], p[1]);
                }
                if (!otherTwo.containsKey(p[1])) {
                    otherTwo.put(p[1], p[0]);
                }
            }
        }
        in.close();
    }

    //----------------------------- translate a phrase from one language to another ------------------------------------
    public static String translateLine(String s, int src, int dst, int gender) {

        if (DEBUG) {
            System.out.println("Src : " + src + " Target : " + dst + " Gender : " + gender);
        }

        StringTokenizer st = new StringTokenizer(s.toLowerCase(), " ,\\[]#'\"()", true);
        if (st.countTokens() == 1) {
            return translateWord(st.nextToken(), src, dst, gender);

        } else {
            StringBuilder sb = new StringBuilder();
            while (st.hasMoreTokens()) {
                String t = st.nextToken();
                if (t.length() > 1) {
                    sb.append(translateWord(t, src, dst, gender));
                } else {
                    sb.append(t);
                }
            }
            return sb.toString();
        }
    }

    // ---------------------------- translate one word from one language to another ------------------------------------
    public static String translateWord(String s, int src, int dst, int gender) {

        Map<String, String> otherMap = null;
        Map<String, String> namesMap = null;
        lk.transliterate.LangToPhonetic[] langToPhonetic = null;
        lk.transliterate.PhoneticToLang[] phoneticToLang = null;

        s = s.toLowerCase();

        switch (src) {
            case SINHALA:
                switch (dst) {
                    case TAMIL:
                        otherMap = sinToTamOther;
                        namesMap = sinToTamNames;
                        langToPhonetic = siToPhRules;
                        phoneticToLang = phToTaRules;
                        break;
                    case ENGLISH:
                        otherMap = sinToEngOther;
                        namesMap = sinToEngNames;
                        langToPhonetic = siToPhRules;
                        phoneticToLang = phToEnRules;
                        break;
                }
                break;
            case TAMIL:
                switch (dst) {
                    case SINHALA:
                        otherMap = tamToSinOther;
                        namesMap = tamToSinNames;
                        langToPhonetic = taToPhRules;
                        phoneticToLang = phToSiRules;
                        break;
                    case ENGLISH:
                        otherMap = tamToEngOther;
                        namesMap = tamToEngNames;
                        langToPhonetic = taToPhRules;
                        phoneticToLang = phToEnRules;
                        break;
                }
                break;
            case ENGLISH:
                switch (dst) {
                    case TAMIL:
                        otherMap = engToTamOther;
                        namesMap = engToTamNames;
                        langToPhonetic = enToPhRules;
                        phoneticToLang = phToTaRules;
                        break;
                    case SINHALA:
                        otherMap = engToSinOther;
                        namesMap = engToSinNames;
                        langToPhonetic = enToPhRules;
                        phoneticToLang = phToSiRules;
                        break;
                }
                break;
        }

        if (namesMap == null || otherMap == null) {
            System.out.println("Invalid language pair");
            return null;
        }

        String result = null;
        if (gender == UNKNOWN) {
            result = otherMap.get(s);
        } else {
            result = namesMap.get(s);
        }

        if (result != null) {
            return result;
        } else {
            if (DEBUG) {
                System.out.println("Dictionary lookup failed for : " + s);
            }
            return phoneticToLang(convertToPhonetic(s, gender, langToPhonetic), phoneticToLang);
        }
    }


    // --------------------- rules based translation to / from a language to phonetic ----------------------------------

    private static String convertToPhonetic(String word, int gender, lk.transliterate.LangToPhonetic[] rules) {

        StringBuilder in = new StringBuilder().append("#").append(word).append("#");
        StringBuilder out = new StringBuilder();

        while (in.length() > 0) {
            boolean found = false;
            String s = in.toString();

            for (lk.transliterate.LangToPhonetic l2p : rules) {
                boolean c = s.matches(l2p.getRule());
                if (c && (gender == l2p.getGender() || l2p.getGender() == UNKNOWN)) {
                    if (DEBUG) {
                        System.out.println("In : " + s + " matches : " + l2p.getRule() + " with : " + l2p.getPhonetic());
                    }
                    appendPhoneticWithCorrection(out, l2p.getPhonetic());
                    in.delete(0, l2p.getLength());
                    found = true;
                    break;
                }
            }
            if (!found) {
                out.append(in.charAt(0));
                in.delete(0, 1);
            }
        }

        if (DEBUG) {
            System.out.println("convertToPhonetic(" + word + ") = " + out.toString());
        }
        return out.toString();
    }

    private static String phoneticToLang(String word, lk.transliterate.PhoneticToLang[] rules) {

        StringBuilder in = new StringBuilder().append(word);
        StringBuilder out = new StringBuilder();

        while (in.length() > 0) {
            boolean found = false;
            String s = in.toString();
            for (lk.transliterate.PhoneticToLang p2l : rules) {
                boolean c = s.matches(p2l.getRule());
                if (c) {
                    if (DEBUG) {
                        System.out.println("In : " + s + " matches : " + p2l.getRule() + " with : " + p2l.getLang());
                    }
                    if (p2l.getLang() != null) {
                        out.append(p2l.getLang());
                    }
                    in.delete(0, p2l.getLength());
                    found = true;
                    break;
                }
            }
            if (!found) {
                out.append(in.charAt(0));
                in.delete(0, 1);
            }
        }

        if (DEBUG) {
            System.out.println("phoneticToLang(" + word + ") = " + out.toString().replaceAll("#", ""));
        }
        return out.toString().replaceAll("#", "");
    }

    private static void appendPhoneticWithCorrection(StringBuilder out, String ph) {
        final int len = out.length();
        if (len > 0) {
            String lastChar = out.substring(len - 1);
            if (END_VOVELS.indexOf(lastChar) == -1) {   // not a vowel
                if (ph.length() > 0) {
                    if (START_VOVELS.indexOf(ph.substring(0, 1)) == -1) { // not a vowel
                        out.append(".a");
                    }
                } else {
                    out.append(".a");
                }
            }
        }
        out.append(ph);
    }

}
