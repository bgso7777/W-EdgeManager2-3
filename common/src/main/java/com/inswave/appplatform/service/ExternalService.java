package com.inswave.appplatform.service;

import com.inswave.appplatform.data.IData;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public interface ExternalService {

    public IData excuteGet(HashMap<String,Object> params);
    public IData excutePost(IData reqIData,IData resIData);
    public IData excutePost(IData reqIData,IData resIData, Object object);

}
