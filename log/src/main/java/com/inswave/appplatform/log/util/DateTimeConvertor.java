package com.inswave.appplatform.log.util;

import com.inswave.appplatform.Config;
import com.inswave.appplatform.transaver.ConstantsTranSaver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * date의 TimeZone이 적용되지 않아 이 클래스를 임시로 생성함.
 * 해결 후 삭제 해도 됨.
 */
public class DateTimeConvertor {

    private static String timeZoneForThisClass = "Asia/Seoul";

    /**
     * jeus에서 utc로 저장되는 것 확인 해야함. ??????? WAS의 설정을 바꿀 수 없을경우 9시를 더해 줘야 함 ....
     *
     */
    public static Date getTimeRegistered(){
        Date ret = null;
        Calendar cal = Calendar.getInstance();
        try{
            //cal.setTimeZone(TimeZone.getTimeZone("UTC"));//안먹힘.
            cal.setTime(new Date());
//            cal.add(Calendar.HOUR, 9); // WAS TimeZone에 따라 다르게 설정해야 할 수 있음.. ????
            ret = new Date(cal.getTimeInMillis());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static Date getTimeRegistered2(){
        Date ret = null;
        Calendar cal = Calendar.getInstance();
        try{
            //cal.setTimeZone(TimeZone.getTimeZone("UTC"));//안먹힘.
            cal.setTime(new Date());
            cal.add(Calendar.HOUR, 9);
            ret = new Date(cal.getTimeInMillis());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
    /**
     * elasticsearch에서 search한 데이터를 다시 elasticsearch에 넣을 경우 date를 보정해야 함.
     * @param date
     * @return
     */
    public static Date changeElasticsearchDate(int addHour, Date date) {
        Date ret = null;
        if(date==null)
            return ret;
        Calendar cal = Calendar.getInstance();
        try{
            cal.setTime(date);
            cal.add(Calendar.HOUR,addHour);
            ret = new Date(cal.getTimeInMillis());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String getStatisticsValue(Date timeCurrent) {
        if(timeCurrent == null)
            return "";
        SimpleDateFormat formatter = new SimpleDateFormat(Config.getInstance().getLog().getStatisticsValue());
        formatter.setTimeZone(TimeZone.getTimeZone(timeZoneForThisClass));
        String strdate =  formatter.format(timeCurrent);
        // 10분
        if(Config.getInstance().getLog().getStatisticsValue().equals("yyyyMMddHHmm"))
            if(strdate.length()>0)
                strdate = strdate.substring(0,strdate.length()-1);
        return strdate;
    }

    public static String getSaveValue(Date timeCurrent) {
        if(timeCurrent == null)
            return "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
        formatter.setTimeZone(TimeZone.getTimeZone(timeZoneForThisClass));
        String strdate =  formatter.format(timeCurrent);
        if(strdate.length()>0)
            strdate = strdate.substring(0,strdate.length()-1);
        return strdate;
    }

    public static String getPrintDate(Date date) {
        String ret = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ConstantsTranSaver.TAG_DATE_PATTERN_OF_ELASTICSEARCH_INDEX);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZoneForThisClass));
        ret = simpleDateFormat.format(date);
        return ret;
    }

    public static String getIndexName(String className, Date timeCurrent) {
        String ret = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ConstantsTranSaver.TAG_DATE_PATTERN_OF_ELASTICSEARCH_INDEX);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZoneForThisClass));  // ??
        ret = className.toLowerCase() +"_"+ simpleDateFormat.format(timeCurrent);

        /*
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);

        TimeZone tz;
        tz = TimeZone.getTimeZone("Asia/Seoul");
        df.setTimeZone(tz);
        System.out.println(tz.getDisplayName() + ":" + df.format(timeCurrent));

        tz = TimeZone.getTimeZone("Asia/Shanghai");
        df.setTimeZone(tz);
        System.out.println(tz.getDisplayName() + ":" + df.format(timeCurrent));

        tz = TimeZone.getTimeZone("America/New_York");
        df.setTimeZone(tz);
        System.out.println(tz.getDisplayName() + ":" + df.format(timeCurrent));

        tz = TimeZone.getTimeZone("UTC");
        df.setTimeZone(tz);
        System.out.println(tz.getDisplayName() + ":" + df.format(timeCurrent));
        */

        return ret;
    }

    public static String getIndexName2(String className, String strTimeCurrent) {
        String ret = "";
        try{
            ret = strTimeCurrent.substring(0,10);
            ret = ret.replaceAll("-","");
            ret = className.toLowerCase() +"_"+ ret;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static Date replaceDate(Date date){
        Date retDate = null;
        try{
            if(date!=null) {
//                String strDate = "";
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z);
//                simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZoneForThisClass));
//                strDate = simpleDateFormat.format(date);
//                retDate = simpleDateFormat.parse(strDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.HOUR, 9);
                retDate = new Date(calendar.getTimeInMillis());
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return retDate;
    }

    public static Date getDateCurrentDate() {
        return replaceDate(new Date());
    }

    public static String getCurrentDateElasticsearch(String datePattern){
        String ret = null;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(datePattern);//, Locale.KOREA);
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            ret = sdf.format(new Date());
        } catch(Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static Date getDate(String datePattern, String strDate){
        Date retDate = null;
        try{
            SimpleDateFormat df = new SimpleDateFormat(datePattern);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            retDate = df.parse(strDate);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return retDate;
    }

    public static Date getDateForInstallDate(String datePattern, String strDate){
        Date retDate = null;
        try{
            if(strDate==null || strDate.equals("") )
                return null;
            try {
                Long.parseLong(strDate);
            } catch (NumberFormatException ne) {
                return null;
            }
            SimpleDateFormat df = new SimpleDateFormat(datePattern);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            retDate = df.parse(strDate);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return retDate;
    }

    public static Date getDate(String datePattern, Date date) {
        Date retDate = null;
        try{
            SimpleDateFormat df = new SimpleDateFormat(datePattern);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            df.format(date);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return retDate;
    }

    public static void main(String argv[]) {

        try {
//            String day = "2021-10-20T20:19:00.123Z";
//            SimpleDateFormat df = new SimpleDateFormat(ConstantsTranSaver.TAG_DATE_PATTERN_YYYY_MM_DD_T_HH_MM_SS_SSS_Z);
//            Date date = df.parse(day);
//            System.out.println(date);

//            TimeZone tz;
//            tz = TimeZone.getTimeZone("Asia/Seoul");
//            df.setTimeZone(tz);
//            System.out.println(tz.getDisplayName() + ":" + df.format(date));
//
//            tz = TimeZone.getTimeZone("Asia/Shanghai");
//            df.setTimeZone(tz);
//            System.out.println(tz.getDisplayName() + ":" + df.format(date));
//
//            tz = TimeZone.getTimeZone("America/New_York");
//            df.setTimeZone(tz);
//            System.out.println(tz.getDisplayName() + ":" + df.format(date));
//
//            tz = TimeZone.getTimeZone("UTC");
//            df.setTimeZone(tz);
//            System.out.println(tz.getDisplayName() + ":" + df.format(date));

            String strDate = "2022/02/25";
            Date date = getDateForInstallDate("yyyyMMdd", null);
            System.out.print(date);
        }catch(Exception e){
            e.printStackTrace();
        }

    }


}
