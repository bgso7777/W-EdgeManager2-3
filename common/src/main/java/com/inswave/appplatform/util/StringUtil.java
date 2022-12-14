package com.inswave.appplatform.util;

import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class StringUtil {

    static public String getRandomString(int length) {
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        String chars[] = "1,2,3,4,5,6,7,8,9,0,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");
        for (int i = 0; i < length; i++) buffer.append(chars[random.nextInt(chars.length)]);
        return buffer.toString();
    }

    public static String rebuildPath(String delimeter, String... value) {
        return concat(delimeter,
                      Arrays.stream(value)
                            .filter(s -> !delimeter.equals(s))
                            .map(s -> StringUtils.replaceChars(StringUtils.replaceChars(s, "//", delimeter), "\\", delimeter))
                            .collect(Collectors.joining(delimeter)));
    }

    public static String concat(String delimeter, String... value) {
        return Arrays.stream(value)
                     .filter(s -> !delimeter.equals(s))
                     .map(s -> String.join(delimeter, StringUtils.split(s, delimeter)))
                     .collect(Collectors.joining(delimeter));
    }

    public static String getShinhanbankGoldwingEUCKRString(String orgData) {
        try {
            orgData = URLEncoder.encode(orgData, "EUC-KR").replace("+", "%20");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orgData;
    }

    public static String getHexCode(String input) {
        byte[] buffer = input.getBytes(StandardCharsets.UTF_8);
        StringBuffer data = new StringBuffer();
        int hex;
        for (int k = 0; k < buffer.length; k++) {
            hex = buffer[k];
            if (hex < 0)
                hex = 256 + hex;
            if (hex >= 16)
                data.append(Integer.toHexString(hex).toUpperCase());
            else
                data.append("0" + Integer.toHexString(hex).toUpperCase());
        }
        return data.toString();
    }

    public static StringBuffer replaceElasticsearchContent(StringBuffer data) {
        StringBuffer ret = new StringBuffer(data);
        //        ret = new StringBuffer( ret.toString().replaceAll("\r\n","") );
        //        ret = new StringBuffer( ret.toString().replaceAll("\"","\\\\\"") );
        return ret;
    }

    public static Hashtable<String, Integer> lastNameIndex = new Hashtable<String, Integer>();
    public static Hashtable<Integer, String> indexLastName = new Hashtable<Integer, String>();
    public static String[] lastNames  = {"???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???", "???"};
    public static int[] lastNameIdxes = {463,108,45,142,29,167,216,307,412,214,94,268,235,299,405,134,494,230,54,379,332,328,421,182,280,2,422,315,202,117,362,92,86,310,279,49,518,343,76,510,124,397,109,15,196,382,477,337,229,118,353,451,144,13,455,165,274,322,240,151,501,23,46,183,213,492,392,81,90,227,103,60,3,119,430,396,224,70,395,236,429,351,265,367,184,75,253,375,120,491,250,141,278,408,462,416,246,306,20,349,445,121,295,341,437,424,172,161,0,433,194,502,495,400,190,506,505,483,110,386,271,132,11,411,116,513,434,272,319,497,508,348,225,128,485,181,410,245,309,473,24,334,503,350,145,504,53,126,150,383,251,234,294,8,484,17,21,407,321,269,249,61,266,197,490,170,360,442,212,474,443,173,77,259,481,409,427,293,330,147,102,302,342,248,331,185,64,95,304,221,289,426,57,72,440,511,239,507,164,480,223,435,496,136,37,373,372,493,345,290,58,153,516,287,448,403,123,158,159,237,466,500,166,220,388,14,457,143,296,27,112,344,255,452,301,192,282,381,398,203,180,438,352,325,256,99,316,231,96,276,384,67,446,171,243,73,43,444,374,313,354,241,146,78,169,52,36,262,205,498,261,101,439,515,7,338,133,50,47,9,218,314,406,160,281,80,242,423,211,333,308,201,155,63,204,97,187,489,198,270,189,380,154,62,488,291,486,454,100,31,114,22,71,346,415,365,371,399,436,509,297,356,467,357,465,450,468,479,1,88,419,247,478,209,320,347,459,277,55,59,193,98,391,369,200,263,129,66,326,514,460,222,298,267,336,233,175,417,111,162,56,107,355,226,401,324,425,174,137,105,33,125,273,368,148,6,323,431,447,286,140,41,199,378,188,340,191,305,168,252,370,264,210,413,44,339,131,93,5,12,469,470,238,359,91,402,476,82,127,84,138,38,517,366,285,335,122,393,106,292,139,16,42,35,487,195,74,329,394,472,275,186,482,420,458,157,18,461,471,327,87,303,25,65,464,432,404,104,28,39,232,217,68,130,441,358,453,376,260,389,377,244,26,19,449,177,284,428,258,283,387,363,499,215,300,317,385,51,48,115,418,254,311,257,228,85,30,69,414,135,149,178,475,4,179,163,318,456,219,512,288,83,10,208,152,113,89,207,361,176,79,156,32,312,40,364,34,206,390};

    public static String[] firstNames = {"???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???"};
    public static int[] firstNameIdxes = {430,198,415,288,27,455,193,172,313,217,121,274,188,349,351,167,420,50,475,203,165,350,63,401,231,142,30,355,283,23,76,451,149,26,114,320,476,433,422,461,119,259,47,213,116,194,72,437,34,216,413,186,302,452,60,90,242,399,404,87,297,410,253,146,285,276,398,328,287,405,317,179,468,157,53,96,432,414,196,169,38,57,155,79,279,450,136,464,39,265,89,278,305,466,183,244,85,228,103,303,332,290,182,25,123,7,225,10,68,289,321,358,239,480,266,308,1,12,418,208,168,41,115,469,291,51,195,465,309,473,223,234,263,314,224,295,346,438,32,337,329,304,249,80,91,312,199,238,326,233,18,81,294,42,86,436,49,178,449,92,148,267,368,130,440,271,24,357,388,120,286,365,131,204,174,417,151,175,374,111,335,458,361,446,101,113,258,78,372,382,54,334,232,84,180,343,260,164,408,201,3,100,20,192,315,277,108,296,75,43,64,381,190,229,230,395,212,254,184,159,338,412,59,373,4,378,444,215,150,166,2,153,88,176,478,243,36,359,99,65,163,248,300,221,477,105,340,257,307,439,400,454,356,310,177,140,246,363,110,324,44,353,345,37,35,463,256,145,129,247,262,74,393,427,156,102,141,219,348,21,272,347,402,275,457,366,55,134,125,185,407,93,181,264,95,52,154,387,200,377,280,470,261,160,22,227,391,139,409,459,124,126,384,421,462,364,236,426,360,187,67,255,214,273,6,370,218,241,143,31,16,5,369,138,474,467,419,171,106,424,173,162,245,82,66,456,98,250,107,386,327,112,40,15,104,222,73,299,425,352,416,375,435,70,46,392,325,132,118,445,284,403,268,429,306,367,205,380,397,97,342,191,17,472,109,428,311,460,423,14,385,77,19,471,122,170,270,293,371,298,376,281,333,292,269,448,137,144,69,161,226,33,147,211,71,135,158,9,301,127,207,152,189,117,330,251,48,197,94,202,319,45,411,390,83,316,11,441,240,58,210,442,237,56,318,62,354,322,443,61,431,406,379,389,341,479,128,394,8,13,28,323,362,282,252,339,206,383,29,220,331,344,336,434,209,0,447,235,453,396,133};

    public static void shuffle() {
        Map<Integer, Integer> lastNameIdxes = new HashMap<>();
        for (int i = 0; i < firstNames.length; i++)
            lastNameIdxes.put(i, i);
        List<Map.Entry<Integer, Integer>> shuffledEntries = new ArrayList<>(lastNameIdxes.entrySet());
        Collections.shuffle(shuffledEntries);
        List<Integer> shuffled = shuffledEntries.stream().map(Map.Entry::getValue).collect(Collectors.toList());
        for (Iterator<Integer> iter = shuffled.iterator(); iter.hasNext(); ) {
System.out.print(","+iter.next());
        }
System.out.print("};");
    }

    public static String replaceLastName(String name) {
        if(name==null || name.equals("") || name.indexOf("?")!=-1 )
            return name;
        String inLastName = name.substring(0,1);
        String replaceLastName = "";
        int i = 0;
        for (; i < lastNames.length; i++)
            if(inLastName.equals(lastNames[i]))
                break;
        if(i==lastNames.length)
            return name;
        else
            replaceLastName = lastNames[lastNameIdxes[i]];
        return name.replaceAll(inLastName,replaceLastName);
//        if( lastNameIndex.size()==0 ) {
//            for (int i = 0; i < lastNames.length; i++) {
//                lastNameIndex.put(lastNames[i],lastNameIdxes[i]);
//                indexLastName.put(lastNameIdxes[i],lastNames[i]);
//            }
//        }
//        String lastName="",replaceLastName="";
//        try {
//            if( name==null || name.equals("") )
//                return "";
//            lastName = name.substring(0,1);
//            int idx = lastNameIndex.get(lastName);
//            replaceLastName = indexLastName.get(idx);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return name.replaceAll(lastName,replaceLastName);
    }

    public static String replaceFirstName(String name) {
        if(name==null || name.equals("") || name.indexOf("?")!=-1 )
            return name;
        String firstName = name.substring(name.length()-1,name.length());
        String replaceLastName = "";
        int i = 0;
        for (; i < firstNames.length; i++)
            if(firstName.equals(firstNames[i]))
                break;
        if(i==firstNames.length)
            return name;
        else
            replaceLastName = firstNames[firstNameIdxes[i]];
        return name.replaceAll(firstName,replaceLastName);
    }

    public static String replaceLastName2(String data) {
        if( lastNameIndex.size()==0 ) {
            for (int i = 0; i < lastNames.length; i++) {
                lastNameIndex.put(lastNames[i],lastNameIdxes[i]);
                indexLastName.put(lastNameIdxes[i],lastNames[i]);
            }
        }
        for (int i = 0; i < lastNames.length; i++) {
            String originalStr = "\"userName\":\""+lastNames[i];
            String replaceStr = "\"userName\":\""+lastNames[lastNameIdxes[i]];
            if(data.indexOf(originalStr)!=-1) {
                data = data.replaceAll(originalStr,replaceStr);
            }
        }
        return data;
    }

    public static void main(String argv[]) {

//        String data = "........\"userName\":\"?????????\",\"JKGP\":\"........";
//        String userNameBeginStr = "\"userName\":\""; String userNameEndStr = "\",\"JKGP\":\"";
////        String userName = data.substring(data.indexOf(userNameBeginStr)+userNameBeginStr.length(),data.indexOf(userNameEndStr));
////        String lastName = userName.substring(0,1);
////System.out.println( userName+"-->>"+replaceLastName(userName) );
//
//        String[] nameData =  {"???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???"};
//        String[] nameData2 = {"???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???","???"};
//
//        System.out.println( replaceLastName2(data) );

//        shuffle();
        String name = "?????????";
        name = replaceLastName(name);
        System.out.println(name);
        name = replaceFirstName(name);
        System.out.println(name);
    }

}