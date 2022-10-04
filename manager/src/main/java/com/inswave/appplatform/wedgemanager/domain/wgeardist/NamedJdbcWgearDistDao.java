package com.inswave.appplatform.wedgemanager.domain.wgeardist;

import com.inswave.appplatform.wedgemanager.domain.device.Device;
import com.inswave.appplatform.wedgemanager.domain.device.DeviceATM;
import com.inswave.appplatform.wedgemanager.dto.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.*;

public class NamedJdbcWgearDistDao extends NamedParameterJdbcDaoSupport implements WgearDistDao {

    private static final String DIST_INSERT_SQL = "INSERT INTO em_dists " +
            "(APPID, VERSION, EXECUTOR, EXECUTEDATE, STATUS, DESCRIPTION, CREATEDATE, UPDATEDATE, ADMISSION) " +
            "VALUES (:appId, :version, :executor, :executeDate, :status, :description, " +
            ":createDate, :updateDate, :admission)";

//    private static final String DIST_UPDATE_STATUS_SQL = "UPDATE em_dists " +
//            "SET STATUS = :resetStatus, " +
//            "UPDATEDATE = :updateDate " +
//            "WHERE LATEST = :latest ";

//    private static final String DIST_UPDATE_STATUS_BY_SCOPE_SQL = "UPDATE em_dists " +
//            "SET STATUS = :resetStatus, " +
//            "UPDATEDATE = :updateDate " +
//            "WHERE SCOPE = :scope " +
//            "AND LATEST = :latest ";

    private static final String DIST_DEPARTMENTS_INSERT_SQL = "INSERT INTO em_dists_departments " +
            "(APPID, VERSION, TARGET, CREATEDATE, UPDATEDATE) " +
            "VALUES (:appId, :version, :target, :createDate, :updateDate)";

    private static final String DIST_ADVANCE_SQL =
            "WITH RECURSIVE_DEPARTMENTS( ID, DEPARTCODE) AS ( " +
                    "    select ID, DEPARTCODE " +
                    "    from em_devices " +
                    "    where ID = :deviceId " +
                    "    union all " +
                    "    select ed.DEPARTCODE, ed.UPPERCODE " +
                    "    from em_department ed, RECURSIVE_DEPARTMENTS " +
                    "    where RECURSIVE_DEPARTMENTS.DEPARTCODE = ed.DEPARTCODE " +
                    "      and ed.DEPARTLEVEL > 0 " +
                    ") " +
                    "SELECT distVersion, EXECUTEDATE, STATUS, TARGET, UPDATEDATE FROM ( " +
                    "	select dist.VERSION AS distVersion, dist.EXECUTEDATE, dist.STATUS, edd.TARGET, dist.UPDATEDATE " +
                    "	from RECURSIVE_DEPARTMENTS d " +
                    "	join em_dists_departments edd on d.DEPARTCODE=edd.TARGET " +
                    "	join em_dists dist on dist.VERSION=edd.VERSION and dist.APPID=edd.APPID " +
                    "	where dist.APPID=:appId and dist.ADMISSION='DE' " +
                    "	order by dist.UPDATEDATE desc " +
                    " ) x " +
                    "WHERE ROWNUM <= 1 ";

    private static final String DIST_ADVANCE_ATM_SQL =
            "WITH RECURSIVE_DEPARTMENTS( ID, DEPARTCODE) AS ( " +
                    "    select ID, DEPARTCODE " +
                    "    from em_devices_atm " +
                    "    where ID = :deviceId " +
                    "    union all " +
                    "    select ed.DEPARTCODE, ed.UPPERCODE " +
                    "    from em_department ed, RECURSIVE_DEPARTMENTS " +
                    "    where RECURSIVE_DEPARTMENTS.DEPARTCODE = ed.DEPARTCODE " +
                    "      and ed.DEPARTLEVEL > 0 " +
                    ") " +
                    "SELECT distVersion, EXECUTEDATE, STATUS, TARGET, UPDATEDATE FROM ( " +
                    "	select dist.VERSION AS distVersion, dist.EXECUTEDATE, dist.STATUS, edd.TARGET, dist.UPDATEDATE " +
                    "	from RECURSIVE_DEPARTMENTS d " +
                    "	join em_dists_departments edd on d.DEPARTCODE=edd.TARGET " +
                    "	join em_dists dist on dist.VERSION=edd.VERSION and dist.APPID=edd.APPID " +
                    "	where dist.APPID=:appId and dist.ADMISSION='DE' " +
                    "	order by dist.UPDATEDATE desc " +
                    " ) x " +
                    "WHERE ROWNUM <= 1 ";

    private static final String DIST_LATEST_SQL =
            "SELECT APPID, VERSION AS distVersion, EXECUTEDATE, STATUS, '@all' AS TARGET " +
                    "FROM em_dists " +
                    "WHERE APPID = :appId and ADMISSION='DE' " +
                    "order by UPDATEDATE desc " +
                    "limit 1; ";

    private static final String DIST_FINDALL_BY_CREATEDATE =
            "select APPID, VERSION, EXECUTOR, EXECUTEDATE, STATUS, ADMISSION, DESCRIPTION as description, CREATEDATE, UPDATEDATE " +
            "from em_dists ed where ed.APPID=:appId order by ed.CREATEDATE desc";

    private static final String DIST_FILE_INSERT_SQL = "INSERT INTO em_dists_files " +
            "(APPID, VERSION, PATH, COMPAREKEY, STATE, RULEINDEX) " +
            "VALUES (:appId, :version, :path, :compareKey, :state, :ruleIndex)";

//    private static final String DIST_FILES_INFO_INSERT_SQL = "INSERT INTO WSQ_DISTS_FILES_INFO " +
//            "(PATH, DESTPATH, TYPE, ORDEROFEXECUTIOM) " +
//            "VALUES (:path, :destPath, :type, :orderOfExecution)";

    private static final String DIST_FILE_LIST_SQL =
            "SELECT d.APPID, d.VERSION, d.PATH, d.COMPAREKEY, d.STATE, i.DESTPATH, i.DEVICECODE as deviceType, i.TYPE, i.ORDEROFEXECUTION, d.REEXECUTE as reExecute " +
                "FROM em_dists_files d " +
                "LEFT OUTER JOIN em_dists_files_info i " +
                "ON d.PATH = i.PATH and d.APPID = i.APPID " +
                "WHERE VERSION = :distVersion " +
                "AND d.APPID = :appId ";

    private static final String DIST_FILES_LIST_SQL =
            "SELECT d.APPID, d.VERSION, d.PATH, d.COMPAREKEY as compareKey, d.STATE as state, i.DESTPATH, i.DEVICECODE as deviceType, i.TYPE, d.REEXECUTE as reExecute, i.ORDEROFEXECUTION " +
                    "FROM em_dists_files d " +
                    "LEFT OUTER JOIN em_dists_files_info i " +
                    "ON d.PATH = i.PATH and d.APPID = i.APPID " +
                    "WHERE VERSION = :distVersion " +
                    "AND d.APPID = :appId ";

    private static final String DIST_FILE_LIST_BY_DEVICETYPE_SQL =
            "SELECT d.APPID, d.VERSION, d.PATH, d.COMPAREKEY, d.STATE, i.DESTPATH, i.DEVICECODE as deviceType, i.TYPE, i.ORDEROFEXECUTION, d.REEXECUTE as reExecute " +
                    "FROM em_dists_files d " +
                    "LEFT OUTER JOIN em_dists_files_info i " +
                    "ON d.PATH = i.PATH and d.APPID = i.APPID " +
                    "WHERE VERSION = :distVersion " +
                    "AND d.APPID = :appId " +
                    "AND i.DEVICECODE = :deviceType";

    private static final String DIST_FILE_INFO_LIST_SQL =
            "SELECT APPID, PATH, DESTPATH, DEVICECODE as deviceType, TYPE, ORDEROFEXECUTION FROM em_dists_files_info WHERE PATH = :path and APPID = :appId";

	 private static final String DIST_FILE_LATEST_LIST_SQL =
            "SELECT B.APPID, B.VERSION, B.PATH, B.COMPAREKEY, B.REEXECUTE as reExecute FROM em_dists A, em_dists_files B\n" +
                    "WHERE A.APPID = B.APPID AND A.VERSION = B.VERSION and A.ADMISSION='DE' ORDER BY B.PATH ASC";

    //scopelist 추가 시작
    private static final String DIST_SCOPE_TOP10_LIST_SQL =
            "select APPID, VERSION, EXECUTOR, EXECUTEDATE, STATUS from em_dists where @ROWNUM < 11 and ADMISSION='DE' order by EXECUTEDATE DESC";
    //scopelist 추가 끝

    //이력 추가 시작
    private static final String DIST_LIST_SQL =
            "select APPID, VERSION, EXECUTOR, EXECUTEDATE, STATUS, DESCRIPTION from em_dists where ADMISSION='DE' and EXECUTEDATE between :sexecuteDate and :eexecuteDate  ORDER BY EXECUTEDATE DESC";
    //이력 추가 끝

    private static final String DIST_LIST_BYAPPID_SQL =
            "select APPID, VERSION, EXECUTOR, EXECUTEDATE, STATUS, DESCRIPTION from em_dists where APPID = :appId and ADMISSION='DE'  ORDER BY EXECUTEDATE DESC";

    //deployState add
    private static final String SELECT_DEPLOY_STATE_BY_VERION_SQL =
            "select version, LISTAGG(DEPARTCODE, ',') within group(order by DEPARTCODE) as targets, EXECUTEDATE, UPDATEDATE " +
                    "from ( " +
                    "    select DISTINCT dm.DEPARTCODE, ddm.VERSION, d.UPDATEDATE, d.EXECUTEDATE " +
                    "         from (select DEPARTCODE, UPDATEDATE from EM_DEPARTMENT sd where sd.DEPARTCODE not in (select UPPERCODE from EM_DEPARTMENT where UPPERCODE is not null)) dm " +
                    "              left join em_dists_departments ddm on dm.DEPARTCODE = ddm.TARGET " +
                    "              left join em_dists d on d.APPID = ddm.APPID and d.VERSION = ddm.VERSION " +
                    "              left join em_devices device on device.DEPARTCODE = ddm.TARGET  " +
                    "         where d.APPID=:appId and d.ADMISSION='DE' " +
                    "         order by d.UPDATEDATE desc " +
                    ") tt " +
                    "group by version, EXECUTEDATE, UPDATEDATE";

    private static final String SELECT_DEPLOY_STATE_BY_DEPARTMENT_SQL =
            "select departcode, departname, version, updatedate, executeDate, ( " +
                    "select COUNT(d.ID) " +
                    "from em_devices d " +
                    "where t.DEPARTCODE=d.DEPARTCODE " +
                    "  and d.APPID=:appId) as totalDevice, ( " +
                    "   select COUNT(d.ID) " +
                    "   from em_devices d " +
                    "   where d.DEPARTCODE=t.DEPARTCODE " +
                    " and d.STATE='DistributionCompleted' and d.APPID=:appId and d.DISTVERSION=t.VERSION) as completeCount " +
                    "from ( " +
                    "  select dm.DEPARTCODE, dm.DEPARTNAME, ddm.VERSION, d.UPDATEDATE, d.EXECUTEDATE " +
                    "  from em_department dm " +
                    "   left join em_dists_departments ddm on dm.DEPARTCODE = ddm.TARGET " +
                    "   left join em_dists d on d.APPID = ddm.APPID and d.VERSION = ddm.VERSION " +
                    "  where (dm.DEPARTCODE, d.UPDATEDATE) in ( " +
                    "  select DEPARTCODE, max(d.UPDATEDATE) as UPDATEDATE " +
                    "  from (select DEPARTCODE, UPDATEDATE from EM_DEPARTMENT sd where sd.DEPARTCODE not in (select UPPERCODE from EM_DEPARTMENT where UPPERCODE is not null)) dm " +
                    "   left join em_dists_departments ddm on dm.DEPARTCODE = ddm.TARGET " +
                    "   left join em_dists d on d.APPID = ddm.APPID and d.VERSION = ddm.VERSION " +
                    "  where d.APPID=:appId and d.ADMISSION='DE' " +
                    "  group by  dm.DEPARTCODE " +
                    "  ) " +
                    "order by d.UPDATEDATE desc " +
                    ") t";

    private static final String SELECT_ATM_DEPLOY_STATE_BY_VERION_SQL =
            "select version, LISTAGG(DEPARTCODE, ',') within group(order by DEPARTCODE) as targets, EXECUTEDATE, UPDATEDATE " +
                    "from ( " +
                    "         select DISTINCT dm.DEPARTCODE, ddm.VERSION, d.UPDATEDATE, d.EXECUTEDATE " +
                    "         from (select DEPARTCODE, UPDATEDATE from EM_DEPARTMENT sd where sd.DEPARTCODE not in (select UPPERCODE from EM_DEPARTMENT where UPPERCODE is not null)) dm " +
                    "                  left join em_dists_departments ddm on dm.DEPARTCODE = ddm.TARGET " +
                    "                  left join em_dists d on d.APPID = ddm.APPID and d.VERSION = ddm.VERSION " +
                    "                  left join em_devices_atm device on device.DEPARTCODE = ddm.TARGET  " +
                    "         where d.APPID=:appId and d.ADMISSION='DE' " +
                    "         order by d.UPDATEDATE desc " +
                    "     ) tt " +
                    "group by version, EXECUTEDATE,UPDATEDATE";

    private static final String SELECT_ATM_DEPLOY_STATE_BY_DEPARTMENT_SQL =
            "select departcode, departname, version, updatedate, executeDate, ( " +
                    "select COUNT(d.ID) " +
                    "from em_devices_atm d " +
                    "where t.DEPARTCODE=d.DEPARTCODE " +
                    "  and d.APPID=:appId) as totalDevice, ( " +
                    "   select COUNT(d.ID) " +
                    "   from em_devices_atm d " +
                    "   where d.DEPARTCODE=t.DEPARTCODE " +
                    " and d.STATE='DistributionCompleted' and d.APPID=:appId and d.DISTVERSION=t.VERSION) as completeCount " +
                    "from ( " +
                    "  select dm.DEPARTCODE, dm.DEPARTNAME, ddm.VERSION, d.UPDATEDATE, d.EXECUTEDATE " +
                    "  from em_department dm " +
                    "   left join em_dists_departments ddm on dm.DEPARTCODE = ddm.TARGET " +
                    "   left join em_dists d on d.APPID = ddm.APPID and d.VERSION = ddm.VERSION " +
                    "  where (dm.DEPARTCODE, d.UPDATEDATE) in ( " +
                    "  select DEPARTCODE, max(d.UPDATEDATE) as UPDATEDATE " +
                    "  from (select DEPARTCODE, UPDATEDATE from EM_DEPARTMENT sd where sd.DEPARTCODE not in (select UPPERCODE from EM_DEPARTMENT where UPPERCODE is not null)) dm " +
                    "   left join em_dists_departments ddm on dm.DEPARTCODE = ddm.TARGET " +
                    "   left join em_dists d on d.APPID = ddm.APPID and d.VERSION = ddm.VERSION " +
                    "  where d.APPID=:appId and d.ADMISSION='DE' " +
                    "  group by  dm.DEPARTCODE " +
                    "  ) " +
                    "order by d.UPDATEDATE desc " +
                    ") t";

    private static final String SELECT_DEPLOY_STATE_BYDEPARTMENT_TOTAL_COMPLETE_SQL =
            "select COUNT(d.ID) as totalDevice, COUNT(case when d.STATE='DistributionCompleted' and d.DISTVERSION=:distVersion then 1 end) as completeCount " +
            "from em_devices d where d.DEPARTCODE=:departCode and d.APPID=:appId";

    private static final String SELECT_ATM_DEPLOY_STATE_BYDEPARTMENT_TOTAL_COMPLETE_SQL =
            "select COUNT(d.ID) as totalDevice, COUNT(case when d.STATE='DistributionCompleted' and d.DISTVERSION=:distVersion then 1 end) as completeCount " +
            "from em_devices_atm d where d.DEPARTCODE=:departCode and d.APPID=:appId";

    private static final String SELECT_FILELIST_BY_FILEPATH_SQL =
            "select * from em_dists_files edf " +
            "join em_dists_files_info edfi on edf.PATH = edfi.PATH and edf.APPID = edfi.APPID " +
            "where edf.APPID=:appId and edf.PATH like :fileName";

    private static final String SELECT_TOTAL_DEVICE_SQL =
            "select d.APPID, COUNT(d.ID) as count from em_devices d where d.APPID=:appId;";

    private static final String SELECT_TOTAL_ATM_DEVICE_SQL =
            "select d.APPID, COUNT(d.ID) as count from em_devices_atm d where d.APPID=:appId;";
    //end

    private static final String SELECT_DIST_FILES_LAST_VERSION =
            "select edf.VERSION as version, edf.APPID as appId, edf.PATH as path, STATE, COMPAREKEY, ed.UPDATEDATE, edf.REEXECUTE as reExecute from em_dists_files edf " +
                    "join em_dists ed on ed.APPID=edf.APPID and ed.VERSION=edf.VERSION " +
                    "where edf.APPID=:appId and PATH=:path and ed.ADMISSION='DE' " +
                    "order by ed.UPDATEDATE desc";

    private static final String SELECT_ADMIN_USER_SQL =
            "select NAME, USERNAME, EMAIL, ROLE_ID " +
                    "from admin_user where ROLE_ID=:roleId";

    private static final String SELECT_DISTS_ADMISSION_LIST_BY_ADMINID_SQL =
            "select APPID, ed.VERSION, EXECUTOR, EXECUTEDATE, STATUS, DESCRIPTION, CREATEDATE, UPDATEDATE, ADMISSION from em_dists ed " +
                    "join em_dists_admission eda on ed.VERSION=eda.VERSION " +
                    "where ed.APPID=:appId and eda.ADMINID=:adminId and ed.ADMISSION='PA'";

    private static final String SELECT_DISTS_ADMISSION_COMPLETE_LIST_BY_ADMINID_SQL =
            "select APPID, ed.VERSION, EXECUTOR, EXECUTEDATE, STATUS, DESCRIPTION, CREATEDATE, UPDATEDATE, ADMISSION " +
                "from em_dists ed join em_dists_admission eda on ed.VERSION=eda.VERSION " +
                "where ed.APPID=:appId and eda.ADMINID=:adminId and ed.ADMISSION='CA' " +
            "union " +
            "select APPID, ed.VERSION, EXECUTOR, EXECUTEDATE, STATUS, DESCRIPTION, CREATEDATE, UPDATEDATE, ADMISSION " +
                "from em_dists ed join em_dists_admission eda on ed.VERSION=eda.VERSION " +
                "where ed.APPID=:appId and eda.ADMINID=:adminId and ed.ADMISSION='RA';";

    private static final String SELECT_DISTS_FILE_LIST_BY_VERSION_SQL =
            "select edf.APPID, edf.VERSION, edf.PATH, edf.COMPAREKEY, edf.STATE, edf.RULEINDEX, edfi.DEVICECODE, edfi.DESTPATH, edfi.TYPE, edfi.ORDEROFEXECUTION, edf.REEXECUTE as reExecute " +
                    "from em_dists_files edf " +
                    "join em_dists_files_info edfi on edf.PATH = edfi.PATH and edf.APPID = edfi.APPID " +
                    "where edf.APPID = :appId and VERSION = :version";

    private static final String SELECT_DISTS_DEPARTMENT_LIST_BY_VERSION_SQL =
            "select APPID, VERSION, TARGET, CREATEDATE, UPDATEDATE " +
                    "from em_dists_departments " +
                    "where APPID = :appId and VERSION = :version";

    private static final String SELECT_DISTINFO_APPID_DEPARTMENT_SQL =
            "select departcode, departname, ed.version, ed.updatedate, ed.executeDate ,( " +
                    "select COUNT(d.ID) " +
                    "from em_devices d " +
                    "where department.DEPARTCODE=d.DEPARTCODE " +
                    "  and d.APPID=:appId) as totalDevice, ( " +
                    "select COUNT(d.ID) " +
                    "from em_devices d " +
                    "where d.DEPARTCODE=edd.TARGET " +
                    "  and d.STATE='DistributionCompleted' and d.APPID=:appId and d.DISTVERSION=:version) as completeCount " +
                    "from em_dists ed " +
                    "join em_dists_departments edd on ed.APPID = edd.APPID and ed.VERSION = edd.VERSION " +
                    "join em_department department on edd.TARGET=department.DEPARTCODE " +
                    "where ed.APPID =:appId and ed.VERSION = :version";

    private static final String SELECT_DISTINFOATM_APPID_DEPARTMENT_SQL =
            "select departcode, departname, ed.version, ed.updatedate, ed.executeDate ,( " +
                    "select COUNT(d.ID) " +
                    "from em_devices_atm d " +
                    "where department.DEPARTCODE=d.DEPARTCODE " +
                    "  and d.APPID=:appId) as totalDevice, ( " +
                    "select COUNT(d.ID) " +
                    "from em_devices_atm d " +
                    "where d.DEPARTCODE=edd.TARGET " +
                    "  and d.STATE='DistributionCompleted' and d.APPID=:appId and d.DISTVERSION=:version) as completeCount " +
                    "from em_dists ed " +
                    "join em_dists_departments edd on ed.APPID = edd.APPID and ed.VERSION = edd.VERSION " +
                    "join em_department department on edd.TARGET=department.DEPARTCODE " +
                    "where ed.APPID =:appId and ed.VERSION = :version";

    private static final String SELECT_DISTINFO_APPID_DEPARTMENT_DEVICE_SQL =
            "select ed.id as deviceId, ed.appid, ed.ostype, ed.ip, ed.userid, ed.sessionstate, ed.updateversion, ed.updatestate, ed.updatescope, ed.updateupdatedate, ed.distversion, ed.distscope, ed.diststate, ed.distupdatedate, ed.state, ed.createdate, ed.updatedate, ed.lastdate, ed.departcode, ed.statemsg from em_devices ed " +
                    "join em_dists_departments edd on ed.APPID = edd.APPID and ed.DEPARTCODE = edd.TARGET " +
                    "where ed.APPID = :appId and edd.VERSION like :version and ed.DEPARTCODE like :departCode " +
                    "group by ed.ID;";

    private static final String SELECT_DISTINFO_APPID_DEPARTMENT_DEVICEATM_SQL =
            "select ed.id as deviceId, ed.appid, ed.ostype, ed.ip, ed.userid, ed.sessionstate, ed.updateversion, ed.updatestate, ed.updatescope, ed.updateupdatedate, ed.distversion, ed.distscope, ed.diststate, ed.distupdatedate, ed.state, ed.createdate, ed.updatedate, ed.lastdate, ed.departcode, ed.statemsg from em_devices_atm ed " +
                    "join em_dists_departments edd on ed.APPID = edd.APPID and ed.DEPARTCODE = edd.TARGET " +
                    "where ed.APPID = :appId and edd.VERSION like :version and ed.DEPARTCODE like :departCode " +
                    "group by ed.ID;";

    @Override
    public void insert(WgearDistDTO dto) {
        try {
            getNamedParameterJdbcTemplate().update(DIST_INSERT_SQL, new BeanPropertySqlParameterSource(dto));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

//    @Override
//    public void endDist(WgearDistDTO dto) {
//        getNamedParameterJdbcTemplate().update(DIST_UPDATE_STATUS_SQL,
//                new BeanPropertySqlParameterSource(dto));
//    }

//    @Override
//    public void endDistByScope(WgearDistDTO dto) {
//        getNamedParameterJdbcTemplate().update(DIST_UPDATE_STATUS_BY_SCOPE_SQL,
//                new BeanPropertySqlParameterSource(dto));
//    }

    @Override
    public void insertTargets(Collection<WgearDistDepartments> wgearDistDepartments) {
        try {
            SqlParameterSource[] sources = wgearDistDepartments.stream()
                    .map(d -> new BeanPropertySqlParameterSource(d))
                    .toArray(size -> new SqlParameterSource[size]);
            getNamedParameterJdbcTemplate().batchUpdate(DIST_DEPARTMENTS_INSERT_SQL, sources);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public ClientUpdateCheckDTO checkAdvance(Device device) {
        try {
            return getNamedParameterJdbcTemplate().queryForObject(DIST_ADVANCE_SQL,
                    new BeanPropertySqlParameterSource(device),
                    BeanPropertyRowMapper.newInstance(ClientUpdateCheckDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public ClientUpdateCheckDTO checkAdvance(DeviceATM device) {
        try {
            return getNamedParameterJdbcTemplate().queryForObject(DIST_ADVANCE_ATM_SQL,
                    new BeanPropertySqlParameterSource(device),
                    BeanPropertyRowMapper.newInstance(ClientUpdateCheckDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public ClientUpdateCheckDTO getLatestDist(ClientUpdateCheckDTO dto) {
        try {
            return getNamedParameterJdbcTemplate().queryForObject(DIST_LATEST_SQL,
                    new BeanPropertySqlParameterSource(dto),
                    BeanPropertyRowMapper.newInstance(ClientUpdateCheckDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void insertFiles(Collection<WgearDistFiles> wgearDistFiles) {
        SqlParameterSource[] sources = wgearDistFiles.stream()
                .map(f -> new BeanPropertySqlParameterSource(f))
                .toArray(size -> new SqlParameterSource[size]);
        getNamedParameterJdbcTemplate().batchUpdate(DIST_FILE_INSERT_SQL, sources);
    }

    @Override
    public List<WgearDistFiles> getFileListByVersion(ClientUpdateCheckDTO checkUpdate) {
        try {
            return getNamedParameterJdbcTemplate().query(DIST_FILE_LIST_SQL,
                    new BeanPropertySqlParameterSource(checkUpdate),
                    BeanPropertyRowMapper.newInstance(WgearDistFiles.class));
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<DeployFileListDTO> getFilesByVersion(ClientUpdateCheckDTO checkUpdate) {
        try {
            return getNamedParameterJdbcTemplate().query(DIST_FILES_LIST_SQL,
                    new BeanPropertySqlParameterSource(checkUpdate),
                    BeanPropertyRowMapper.newInstance(DeployFileListDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<WgearDistFiles> getFileListByVersionAndDeviceType(ClientUpdateCheckDTO checkUpdate) {
        try {
            return getNamedParameterJdbcTemplate().query(DIST_FILE_LIST_BY_DEVICETYPE_SQL,
                    new BeanPropertySqlParameterSource(checkUpdate),
                    BeanPropertyRowMapper.newInstance(WgearDistFiles.class));
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }
	
	//scopeList 추가 시작
    @Override
    public List<WgearDist> getScopeList(WgearDistDTO param) {
        try {
            return getNamedParameterJdbcTemplate().query(DIST_SCOPE_TOP10_LIST_SQL,
                    new BeanPropertySqlParameterSource(param),
                    BeanPropertyRowMapper.newInstance(WgearDist.class));
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<WgearDist> getAllDistList(WgearDistDTO param) {
        try {
            return getNamedParameterJdbcTemplate().query(DIST_LIST_BYAPPID_SQL,
                    new BeanPropertySqlParameterSource(param),
                    BeanPropertyRowMapper.newInstance(WgearDist.class));
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }
    //scopeList 추가 끝

    //이력 추가 시작
    @Override
    public List<WgearDist> getDeployList(WgearDistDTO param) {
        try {
            return getNamedParameterJdbcTemplate().query(DIST_LIST_SQL,
                    new BeanPropertySqlParameterSource(param),
                    BeanPropertyRowMapper.newInstance(WgearDist.class));
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }
    //이력 추가 끝

    @Override
    public List<WgearDistFiles> getLastestFileList() {
        try {
            return getNamedParameterJdbcTemplate().query(DIST_FILE_LATEST_LIST_SQL,
                    BeanPropertyRowMapper.newInstance(WgearDistFiles.class));
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<WgearDist> findAllByOrderByCreateDateAsc(String appId) {
        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);

        try {
            return getNamedParameterJdbcTemplate().query(DIST_FINDALL_BY_CREATEDATE,
                    params,
                    BeanPropertyRowMapper.newInstance(WgearDist.class));
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public WgearDistFilesInfo checkFilesInfo(WgearDistFilesInfo path){
        try{
            return getNamedParameterJdbcTemplate().queryForObject(DIST_FILE_INFO_LIST_SQL,
                    new BeanPropertySqlParameterSource(path),
                    BeanPropertyRowMapper.newInstance(WgearDistFilesInfo.class));
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<DeployStateDTO> getDeployStateByVersion(DataHeader dataHeader) {
        try {
            return getNamedParameterJdbcTemplate().query(SELECT_DEPLOY_STATE_BY_VERION_SQL,
                    new BeanPropertySqlParameterSource(dataHeader),
                    BeanPropertyRowMapper.newInstance(DeployStateDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<DeployStateDTO> getDeployStateByDepartment(DataHeader dataHeader) {
        try {
            return getNamedParameterJdbcTemplate().query(SELECT_DEPLOY_STATE_BY_DEPARTMENT_SQL,
                    new BeanPropertySqlParameterSource(dataHeader),
                    BeanPropertyRowMapper.newInstance(DeployStateDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public DeployStateDTO getTotalAndCompleteByDepartment(DeployStateDTO deployStateDTO) {
        try {
            return getNamedParameterJdbcTemplate().queryForObject(SELECT_DEPLOY_STATE_BYDEPARTMENT_TOTAL_COMPLETE_SQL,
                    new BeanPropertySqlParameterSource(deployStateDTO),
                    BeanPropertyRowMapper.newInstance(DeployStateDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<DeployStateDTO> getATMDeployStateByVersion(DataHeader dataHeader) {
        try {
            return getNamedParameterJdbcTemplate().query(SELECT_ATM_DEPLOY_STATE_BY_VERION_SQL,
                    new BeanPropertySqlParameterSource(dataHeader),
                    BeanPropertyRowMapper.newInstance(DeployStateDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<DeployStateDTO> getATMDeployStateByDepartment(DataHeader dataHeader) {
        try {
            return getNamedParameterJdbcTemplate().query(SELECT_ATM_DEPLOY_STATE_BY_DEPARTMENT_SQL,
                    new BeanPropertySqlParameterSource(dataHeader),
                    BeanPropertyRowMapper.newInstance(DeployStateDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public DeployStateDTO getATMTotalAndCompleteByDepartment(DeployStateDTO deployStateDTO) {
        try {
            return getNamedParameterJdbcTemplate().queryForObject(SELECT_ATM_DEPLOY_STATE_BYDEPARTMENT_TOTAL_COMPLETE_SQL,
                    new BeanPropertySqlParameterSource(deployStateDTO),
                    BeanPropertyRowMapper.newInstance(DeployStateDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<DeployFileListDTO> getFileList(String appId, String fileName) {
        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);
        params.put("fileName", fileName);

        try {
            return getNamedParameterJdbcTemplate().query(SELECT_FILELIST_BY_FILEPATH_SQL,
                    params,
                    BeanPropertyRowMapper.newInstance(DeployFileListDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public TotalDeviceDTO getTotalDevice(String appId) {
        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);
        try {
            return getNamedParameterJdbcTemplate().queryForObject(SELECT_TOTAL_DEVICE_SQL,
                    params,
                    BeanPropertyRowMapper.newInstance(TotalDeviceDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public TotalDeviceDTO getTotalATMDevice(String appId) {
        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);
        try {
            return getNamedParameterJdbcTemplate().queryForObject(SELECT_TOTAL_ATM_DEVICE_SQL,
                    params,
                    BeanPropertyRowMapper.newInstance(TotalDeviceDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public DistFiles getLastVersion(String appId, String fileName) {
        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);
        params.put("path", fileName);
        try {
            return getNamedParameterJdbcTemplate().queryForObject(SELECT_DIST_FILES_LAST_VERSION,
                    params,
                    BeanPropertyRowMapper.newInstance(DistFiles.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<AdminUserDTO> getAdminUSer(String roleId) {
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", roleId);

        try {
            return getNamedParameterJdbcTemplate().query(SELECT_ADMIN_USER_SQL,
                    params,
                    BeanPropertyRowMapper.newInstance(AdminUserDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<WgearDistFiles> getFileListByVersion(String appId, String version) {
        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);
        params.put("version", version);

        try {
            return getNamedParameterJdbcTemplate().query(SELECT_DISTS_FILE_LIST_BY_VERSION_SQL,
                    params,
                    BeanPropertyRowMapper.newInstance(WgearDistFiles.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<WgearDistDepartments> getDepartmentListByVersion(String appId, String version) {
        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);
        params.put("version", version);

        try {
            return getNamedParameterJdbcTemplate().query(SELECT_DISTS_DEPARTMENT_LIST_BY_VERSION_SQL,
                    params,
                    BeanPropertyRowMapper.newInstance(WgearDistDepartments.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Dist> getAdmissionByAdminId(String appId, String adminId) {
        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);
        params.put("adminId", adminId);

        try {
            return getNamedParameterJdbcTemplate().query(SELECT_DISTS_ADMISSION_LIST_BY_ADMINID_SQL,
                    params,
                    BeanPropertyRowMapper.newInstance(Dist.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    @Override
    public List<Dist> getAdmissionCompleteByAdminId(String appId, String adminId) {
        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);
        params.put("adminId", adminId);

        try {
            return getNamedParameterJdbcTemplate().query(SELECT_DISTS_ADMISSION_COMPLETE_LIST_BY_ADMINID_SQL,
                    params,
                    BeanPropertyRowMapper.newInstance(Dist.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<DeployStateDTO> getDistInfoByDepartCode(String appId, String version) {
        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);
        params.put("version", version);

        try {
            return getNamedParameterJdbcTemplate().query(SELECT_DISTINFO_APPID_DEPARTMENT_SQL,
                    params,
                    BeanPropertyRowMapper.newInstance(DeployStateDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<DeployStateDTO> getDistInfoATMByDepartCode(String appId, String version) {
        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);
        params.put("version", version);

        try {
            return getNamedParameterJdbcTemplate().query(SELECT_DISTINFOATM_APPID_DEPARTMENT_SQL,
                    params,
                    BeanPropertyRowMapper.newInstance(DeployStateDTO.class));
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Device> getDeviceByVersionAndDepartCode(String appId, String version, String departCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);
        params.put("version", version);
        params.put("departCode", departCode);

        try {
            return getNamedParameterJdbcTemplate().query(SELECT_DISTINFO_APPID_DEPARTMENT_DEVICE_SQL,
                    params,
                    BeanPropertyRowMapper.newInstance(Device.class));
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<DeviceATM> getDeviceATMByVersionAndDepartCode(String appId, String version, String departCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);
        params.put("version", version);
        params.put("departCode", departCode);

        try {
            return getNamedParameterJdbcTemplate().query(SELECT_DISTINFO_APPID_DEPARTMENT_DEVICEATM_SQL,
                    params,
                    BeanPropertyRowMapper.newInstance(DeviceATM.class));
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

//    @Override
//    public void addFilesInfo(Collection<WgearDistFilesInfo> wgearDistFilesInfo){
//        SqlParameterSource[] sources = wgearDistFilesInfo.stream()
//                .map(f -> new BeanPropertySqlParameterSource(f))
//                .toArray(size -> new SqlParameterSource[size]);
//        getNamedParameterJdbcTemplate().batchUpdate(DIST_FILES_INFO_INSERT_SQL, sources);
//    }
}
