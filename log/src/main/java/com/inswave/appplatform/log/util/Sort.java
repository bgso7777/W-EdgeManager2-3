package com.inswave.appplatform.log.util;

import com.inswave.appplatform.log.ConstantsLog;

import java.util.Comparator;

public class Sort implements Comparator {

    private int type1 = 1;
    private String type2 = ConstantsLog.SORT_ASCENDING;// "asc"; // desc

    /**
     *
     * @param type1 sort 기준  cpu memory
     * @param type2 내림:desc 오름:asc
     */
    public Sort(int type1, String type2) {
        this.type1 = type1;
        this.type2 = type2;
    }

    public int compare(Object o1, Object o2) {

//        if(o1 instanceof ClientProcessResourceLogData){
//            if( o1 instanceof ClientProcessResourceLogData) {
//                Long s1=-1L, s2=-1L;
//                if(type1==ConstantsLog.TYPE_OF_COLUMN_CPU) {
//                    s1 = ((ClientProcessResourceLogData) o1).getProcCpuUsage();
//                    s2 = ((ClientProcessResourceLogData) o2).getProcCpuUsage();
//                } else if(type1==ConstantsLog.TYPE_OF_COLUMN_MEMORY) {
//                    s1 = ((ClientProcessResourceLogData) o1).getProcMemoryUsage();
//                    s2 = ((ClientProcessResourceLogData) o2).getProcMemoryUsage();
//                } else if(type1==ConstantsLog.TYPE_OF_COLUMN_THREAD) {
//                    s1 = ((ClientProcessResourceLogData) o1).getProcThreadCount();
//                    s2 = ((ClientProcessResourceLogData) o2).getProcThreadCount();
//                } else if(type1==ConstantsLog.TYPE_OF_COLUMN_HANDLE) {
//                    s1 = ((ClientProcessResourceLogData) o1).getProcHandleCount();
//                    s2 = ((ClientProcessResourceLogData) o2).getProcHandleCount();
//                }
//                if(s1==null||s2==null)
//                    return 0;
//                if(type2.equals(ConstantsLog.SORT_DESENDING))
//                    return s1.compareTo(s2)*-1;
//                else if(type2.equals(ConstantsLog.SORT_ASCENDING))
//                    return s1.compareTo(s2);
//            }
//        }

        String s1 = (String)o1;
        String s2 = (String)o2;

        if( s1 != null && !s1.equals("") && isNumber(s1) &&
                s2 != null && !s2.equals("") &&	isNumber(s2) ) {
            int i1 = Integer.parseInt(s1);
            int i2 = Integer.parseInt(s2);

            if( type2.toLowerCase().indexOf(ConstantsLog.SORT_DESENDING) != -1 ) {
                if( i1 == i2 )
                    return 0;
                else if( i1 > i2 )
                    return -1;
                else if( i1 < i2 )
                    return 1;
            } else {
                if( i1 == i2 )
                    return 0;
                else if( i1 > i2 )
                    return 1;
                else if( i1 < i2 )
                    return -1;
            }
        } else {
            int r = s1.compareTo(s2);
            if( type2.toLowerCase().indexOf(ConstantsLog.SORT_DESENDING) != -1 ) {
                if( r == 0 )
                    return 0;
                else
                    return r*-1;
            } else {
                return r;
            }
        }

        return 0;
    }

    private boolean isNumber(String s) {
        for (int i = 0; i < s.length(); i++) {
            if( !isDigit(s.substring(i, i+1)) )
                return false;
        }
        return true;
    }

    private boolean isDigit(String s) {
        if( s.equals("0") || s.equals("1") || s.equals("2") ||
                s.equals("3") || s.equals("4") || s.equals("5") ||
                s.equals("6") || s.equals("7") || s.equals("8") ||
                s.equals("9") )
            return true;
        else
            return false;
    }
}