package com.robert.vesta.service.factory;

import java.beans.PropertyVetoException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.robert.vesta.service.impl.IdServiceImpl;
import com.robert.vesta.service.impl.provider.DbMachineIdProvider;
import com.robert.vesta.service.impl.provider.IpConfigurableMachineIdProvider;
import com.robert.vesta.service.impl.provider.PropertyMachineIdProvider;
import com.robert.vesta.service.intf.IdService;

public class IdServiceFactoryBean implements FactoryBean<IdService> {
	protected final Logger log = LoggerFactory
			.getLogger(IdServiceFactoryBean.class);

	public enum Type {
		PROPERTY, IP_CONFIGURABLE, DB
	};

	private Type type;

	private long machineId;

	private String ips;

	private String dbUrl;
	private String dbName;
	private String dbUser;
	private String dbPassword;

	private IdService idService;

	public void init() {
		if (type == null) {
			log.error("The type of Id service is mandatory.");
			throw new IllegalArgumentException(
					"The type of Id service is mandatory.");
		}

		switch (type) {
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
		idServiceImpl.init();

		return idServiceImpl;
	}

	private IdService constructIpConfigurableIdService(String ips) {
		log.info("Construct Ip Configurable IdService ips {}", ips);

		IpConfigurableMachineIdProvider ipConfigurableMachineIdProvider = new IpConfigurableMachineIdProvider(
				ips);

		IdServiceImpl idServiceImpl = new IdServiceImpl();
		idServiceImpl.setMachineIdProvider(ipConfigurableMachineIdProvider);
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
		idServiceImpl.init();

		return idServiceImpl;
	}

	public Class<?> getObjectType() {
		return IdService.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
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
}
