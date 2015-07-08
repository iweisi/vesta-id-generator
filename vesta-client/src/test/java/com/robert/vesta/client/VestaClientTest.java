package com.robert.vesta.client;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.robert.vesta.service.bean.Id;
import com.robert.vesta.service.impl.IdConverter;
import com.robert.vesta.service.impl.IdConverterImpl;
import com.robert.vesta.service.impl.bean.IdType;
import com.robert.vesta.service.intf.IdService;

public class VestaClientTest extends TestCase {
	public VestaClientTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(VestaClientTest.class);
	}

	public void testSimple() {
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"spring/vesta-client-test.xml");

		IdService idService = (IdService) ac.getBean("idService");
		long id = idService.genId();
		Id ido = idService.expId(id);

		IdConverter idConverter = new IdConverterImpl(IdType.MAX_PEAK);
		long id1 = idConverter.convert(ido);
		
		assertEquals(id, id1);
		
		System.out.println(id);
		System.out.println(id1);
	}
}
