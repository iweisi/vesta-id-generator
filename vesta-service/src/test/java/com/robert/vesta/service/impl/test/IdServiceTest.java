package com.robert.vesta.service.impl.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.robert.vesta.service.bean.Id;
import com.robert.vesta.service.intf.IdService;

public class IdServiceTest extends TestCase {
	public IdServiceTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(IdServiceTest.class);
	}

	public void testSimple() {
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"spring/vesta-service-test.xml");
		IdService idService = (IdService) ac.getBean("idService");

		long id = idService.genId();
		Id ido = idService.expId(id);
		long id1 = idService.makeId(ido.getVersion(), ido.getType(),
				ido.getGenMethod(), ido.getMachine(), ido.getTime(),
				ido.getSeq());
		
		System.out.println(id + ":" + ido);

		assertEquals(id, id1);
	}

}
