package com.inswave.appplatform.service;

import com.inswave.appplatform.dao.Domain;
import com.inswave.appplatform.data.IData;

import java.util.List;

public interface ExternalRdbDaoService {

    public List<Domain> select(String tableEntityName, long id);
    public long insert(String tableEntityName, List<Domain> tableEntities);
    public long update(String tableEntityName, List<Domain> tableEntities);
    public long delete(String tableEntityName, long id);

    public IData select(IData reqIData, IData resIData);
    public IData select2(IData reqIData, IData resIData);

    public IData insert(IData reqIData,IData resIData);
    public IData update(IData reqIData,IData resIData);
    public IData delete(IData reqIData,IData resIData);
    public IData updateColumn(IData reqIData, IData resIData);
}
