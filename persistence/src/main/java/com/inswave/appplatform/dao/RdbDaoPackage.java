package com.inswave.appplatform.dao;

import com.inswave.appplatform.Constants;
import com.inswave.appplatform.domain.*;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Hashtable;

@Component
public class RdbDaoPackage implements CommandLineRunner {

    private static RdbDaoPackage allDaoPackage;
    private static Hashtable package_ = null;
    private static Hashtable domainPackage_ = null;

    //@PersistenceContext
    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    @Autowired
    @Qualifier("wemEntityManager")
    private EntityManager entityManager;
//    //@PersistenceUnit(unitName = "db")
//    private EntityManagerFactory entityManagerFactory;

    @Autowired private AdminUserDao adminUserDao;
    @Autowired private ConfigDao configDao1;
    @Autowired private ConfigManagerDao configManagerDao;
    @Autowired private ConfigLogDao configLogDao;
    @Autowired private LicenseDao licenseDao;
    @Autowired private RoleDao roleDao;
    @Autowired private LanguageDao languageDao;
    @Autowired private SiteDao siteDao;
    @Autowired private CountryDao countryDao;
    @Autowired private MenuDao menuDao;
    @Autowired private MenuRoleDao menuRoleDao;

    public static RdbDaoPackage getInstance() {
        if(allDaoPackage==null)
            allDaoPackage = new RdbDaoPackage();
        return allDaoPackage;
    }

    public RdbDaoPackage() {
        package_ = new Hashtable();
        domainPackage_ = new Hashtable();
    }

    public Object getDao(String tableEntityName) {
        return package_.get(tableEntityName);
    }

    public Object getDomain(String tableEntityName) {
        return domainPackage_.get(tableEntityName);
    }

    public void putDao(String tableEntityName, Object daoServiceObject, Object domain) {
        package_.put(tableEntityName,daoServiceObject);
        domainPackage_.put(tableEntityName,domain);
    }
//
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//
//        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
//        lef.setPackagesToScan("com.inswave.appplatform");
//        lef.setDataSource(dataSource());
//        lef.setJpaVendorAdapter(jpaVendorAdapter());
//
//        Properties properties = new Properties();
//        properties.setProperty("hibernate.show_sql", "true");
//        properties.setProperty("hibernate.format_sql", "true");
//        properties.setProperty("hibernate.use_sql_comments", "true");
//        properties.setProperty("hibernate.globally_quoted_identifiers", "true");
//        properties.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");
//        properties.setProperty("hibernate.jdbc.fetch_size", "100");
//        properties.setProperty("hibernate.hbm2ddl.auto", "update");
//
//        lef.setJpaProperties(properties);
//        return lef;
//    }
//
//    public DataSource dataSource() {
//        final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
//        dataSource.setDriver(new org.mariadb.jdbc.Driver());
//        dataSource.setUrl("jdbc:mysql://192.168.79.100:3306/WHUB?autoReconnect=true");
//        dataSource.setUsername("whub");
//        dataSource.setPassword("1eldptm))&$");
//        return dataSource;
//    }
//
//    public JpaVendorAdapter jpaVendorAdapter() {
//        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
//        jpaVendorAdapter.setGenerateDdl(true);
//        jpaVendorAdapter.setShowSql(true);
//        jpaVendorAdapter.setDatabasePlatform("org.hibernate.dialect.MariaDBDialect");
//        return jpaVendorAdapter;
//    }

    @Override
    public void run(String... args) throws Exception {

        RdbDaoPackage.getInstance().putDao(Config.class.getSimpleName(),configDao1,Config.class);
        RdbDaoPackage.getInstance().putDao(ConfigManager.class.getSimpleName(),configManagerDao,ConfigManager.class);
        RdbDaoPackage.getInstance().putDao(ConfigLog.class.getSimpleName(),configLogDao, ConfigLog.class);
        RdbDaoPackage.getInstance().putDao(Language.class.getSimpleName(),languageDao,Language.class);
        RdbDaoPackage.getInstance().putDao(Country.class.getSimpleName(),countryDao,Country.class);

        initConfig();

        RdbDaoPackage.getInstance().putDao(AdminUser.class.getSimpleName(),adminUserDao,AdminUser.class);
        RdbDaoPackage.getInstance().putDao(License.class.getSimpleName(),licenseDao,License.class);
        RdbDaoPackage.getInstance().putDao(Role.class.getSimpleName(),roleDao,Role.class);
        RdbDaoPackage.getInstance().putDao(Site.class.getSimpleName(),siteDao,Site.class);
        RdbDaoPackage.getInstance().putDao(Menu.class.getSimpleName(),menuDao,Menu.class);
        RdbDaoPackage.getInstance().putDao(MenuRole.class.getSimpleName(),menuRoleDao,MenuRole.class);

        try {
            Session session = (Session)entityManager.getDelegate();
            RdbDaoPackage.getInstance().putDao(Session.class.getSimpleName(),session,Session.class);
            RdbDaoPackage.getInstance().putDao(EntityManager.class.getSimpleName(),entityManager,EntityManager.class);

            //entityManagerFactory = Persistence.createEntityManagerFactory("db"); // hibernate.xml정의해야 함.

//            LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = entityManagerFactory();
//            DataSource dataSource = localContainerEntityManagerFactoryBean.getDataSource();
            RdbDaoPackage.getInstance().putDao(Session.class.getSimpleName(),session,Session.class);

            //EntityManagerFactory entityManagerFactory = localContainerEntityManagerFactoryBean.getObject();
            //RdbDaoPackage.getInstance().putDao(EntityManagerFactory.class.getSimpleName(),entityManagerFactory);

            System.out.println("");
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 기본 환경설정 값 셋팅
     */
    private void initConfig() {
        try {
            Class<?> cls = Class.forName(Constants.CLASS_OF_CONFIG_INITIAL_BATCH_SERVICE);
            Constructor<?> constructor = cls.getConstructor();
            Object node = constructor.newInstance();
            Method method = cls.getMethod(Constants.METHOD_OF_INTERNAL_EXCUTE_SERVICE);
            Object object = method.invoke(node);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
