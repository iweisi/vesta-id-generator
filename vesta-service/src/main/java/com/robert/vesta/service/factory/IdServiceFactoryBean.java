package com.robert.vesta.service.factory;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.robert.vesta.service.impl.IdServiceImpl;
import com.robert.vesta.service.impl.provider.DbMachineIdProvider;
import com.robert.vesta.service.impl.provider.IpConfigurableMachineIdProvider;
import com.robert.vesta.service.impl.provider.PropertyMachineIdProvider;
import com.robert.vesta.service.intf.IdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.beans.PropertyVetoException;

public class IdServiceFactoryBean implements FactoryBean<IdService> {
    protected final Logger log = LoggerFactory
            .getLogger(IdServiceFactoryBean.class);

    public enum Type {
        PROPERTY, IP_CONFIGURABLE, DB
    }

    ;

    private Type providerType;

    private long machineId;

    private String ips;

    private String dbUrl;
    private String dbName;
    private String dbUser;
    private String dbPassword;

    private long genMethod = -1;
    private long type = -1;
    private long version = -1;

    private IdService idService;

    public void init() {
        if (providerType == null) {
            log.error("The type of Id service is mandatory.");
            throw new IllegalArgumentException(
                    "The type of Id service is mandatory.");
        }

        switch (providerType) {
            case PROPERTY:
                idService = constructPropertyIdService(machineId);
                break;
            case IP_CONFIGURABLE:
                idService = constructIpConfigurableIdService(ips);
                break;
            case DB:
                idService = constructDbIdService(dbUrl, dbName, dbUser, dbPassword);
                break;
        }
    }

    public IdService getObject() throws Exception {
        return idService;
    }

    private IdService constructPropertyIdService(long machineId) {
        log.info("Construct Property IdService machineId {}", machineId);

        PropertyMachineIdProvider propertyMachineIdProvider = new PropertyMachineIdProvider();
        propertyMachineIdProvider.setMachineId(machineId);

        IdServiceImpl idServiceImpl = new IdServiceImpl();
        idServiceImpl.setMachineIdProvider(propertyMachineIdProvider);
        if (genMethod != -1)
            idServiceImpl.setGenMethod(genMethod);
        if (type != -1)
            idServiceImpl.setType(type);
        if (version != -1)
            idServiceImpl.setVersion(version);
        idServiceImpl.init();

        return idServiceImpl;
    }

    private IdService constructIpConfigurableIdService(String ips) {
        log.info("Construct Ip Configurable IdService ips {}", ips);

        IpConfigurableMachineIdProvider ipConfigurableMachineIdProvider = new IpConfigurableMachineIdProvider(
                ips);

        IdServiceImpl idServiceImpl = new IdServiceImpl();
        idServiceImpl.setMachineIdProvider(ipConfigurableMachineIdProvider);
        if (genMethod != -1)
            idServiceImpl.setGenMethod(genMethod);
        if (type != -1)
            idServiceImpl.setType(type);
        if (version != -1)
            idServiceImpl.setVersion(version);
        idServiceImpl.init();

        return idServiceImpl;
    }

    private IdService constructDbIdService(String dbUrl, String dbName,
                                           String dbUser, String dbPassword) {
        log.info(
                "Construct Db IdService dbUrl {} dbName {} dbUser {} dbPassword {}",
                dbUrl, dbName, dbUser, dbPassword);

        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();

        String jdbcDriver = "com.mysql.jdbc.Driver";
        try {
            comboPooledDataSource.setDriverClass(jdbcDriver);
        } catch (PropertyVetoException e) {
            log.error("Wrong JDBC driver {}", jdbcDriver);
            log.error("Wrong JDBC driver error: ", e);
            throw new IllegalStateException("Wrong JDBC driver ", e);
        }

        comboPooledDataSource.setMinPoolSize(5);
        comboPooledDataSource.setMaxPoolSize(30);
        comboPooledDataSource.setIdleConnectionTestPeriod(20);
        comboPooledDataSource.setMaxIdleTime(25);
        comboPooledDataSource.setBreakAfterAcquireFailure(false);
        comboPooledDataSource.setCheckoutTimeout(3000);
        comboPooledDataSource.setAcquireRetryAttempts(50);
        comboPooledDataSource.setAcquireRetryDelay(1000);

        String url = String
                .format("jdbc:mysql://%s/%s?useUnicode=true&amp;characterEncoding=UTF-8&amp;autoReconnect=true",
                        dbUrl, dbName);

        comboPooledDataSource.setJdbcUrl(url);
        comboPooledDataSource.setUser(dbUser);
        comboPooledDataSource.setPassword(dbPassword);

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setLazyInit(false);
        jdbcTemplate.setDataSource(comboPooledDataSource);

        DbMachineIdProvider dbMachineIdProvider = new DbMachineIdProvider();
        dbMachineIdProvider.setJdbcTemplate(jdbcTemplate);
        dbMachineIdProvider.init();

        IdServiceImpl idServiceImpl = new IdServiceImpl();
        idServiceImpl.setMachineIdProvider(dbMachineIdProvider);
        if (genMethod != -1)
            idServiceImpl.setGenMethod(genMethod);
        if (type != -1)
            idServiceImpl.setType(type);
        if (version != -1)
            idServiceImpl.setVersion(version);
        idServiceImpl.init();

        return idServiceImpl;
    }

    public Class<?> getObjectType() {
        return IdService.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public Type getProviderType() {
        return providerType;
    }

    public void setProviderType(Type providerType) {
        this.providerType = providerType;
    }

    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public long getGenMethod() {
        return genMethod;
    }

    public void setGenMethod(long genMethod) {
        this.genMethod = genMethod;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
