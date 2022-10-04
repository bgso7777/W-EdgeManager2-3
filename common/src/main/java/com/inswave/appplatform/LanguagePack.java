package com.inswave.appplatform;

import java.util.Hashtable;

public class LanguagePack {

    private Hashtable messages = new Hashtable();
    private static LanguagePack language;

    public static LanguagePack getInstance() {
        if (language == null)
            language = new LanguagePack();
        return language;
    }

    private LanguagePack() {
    }

    /**
     * @param countryId
     * @param key
     * @param value
     */
    public void put(Long countryId, String key, String value) {
        // TODO Auto-generated method stub
        Message message = new Message();
        message.setCountry(countryId);
        message.setKey(key);
        message.setValue(value);
        messages.put(countryId+"_"+key, message);
    }

    public String get(Long countryId, String key) {
        // TODO Auto-generated method stub
        if(countryId==null) {
            Object object = messages.get( Constants.LANGUAGEPACK_COUNTRY_ID+"_"+Config.getInstance().getCountry() );
            if(object==null)
                countryId = 1L;
            else {
                Message languagePack = (Message)object;
                countryId = Long.parseLong(languagePack.getValue());
            }
        }
        Object obj = messages.get(countryId+"_"+key);
        String value = "";
        if(obj!=null) {
            Message message = (Message)obj;
            value = message.getValue();
        }
        if(value==null || value.equals(""))
            value=key;
        return value;
    }

    /**
     * @return
     */
    public int getMessagesSize() {
        // TODO Auto-generated method stub
        return messages.size();
    }

    public static void main(String argv[]) {
        String key = "korea_22554";
        String country = "japan";
        key = country+"_"+key.substring(key.indexOf("_")+1, key.length());
        System.out.println(key);
    }

    public class Message {

        private Long countryId;
        private String key;
        private String value;

        public Long getCountryId() {
            return countryId;
        }
        public void setCountry(Long country) {
            this.countryId = countryId;
        }
        public String getKey() {
            return key;
        }
        public void setKey(String key) {
            this.key = key;
        }
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
    }
}