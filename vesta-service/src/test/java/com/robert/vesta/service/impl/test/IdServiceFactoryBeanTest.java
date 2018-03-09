package com.robert.vesta.service.impl.test;

import com.robert.vesta.service.bean.Id;
import com.robert.vesta.service.intf.IdService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

// There is only one factory bean in a single spring context, so we should create spring context for each test
public class IdServiceFactoryBeanTest {

    @Test(groups = {"idService"})
    public void testSimple() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "spring/vesta-service-factory-bean-property-test.xml");
        IdService idService = (IdService) applicationContext
                .getBean("idService");

        long id = idService.genId();
        Id ido = idService.expId(id);
        long id1 = idService.makeId(ido.getVersion(), ido.getType(),
                ido.getGenMethod(), ido.getTime(), ido.getSeq(),
                ido.getMachine());

        System.err.println(id + ":" + ido);

        AssertJUnit.assertEquals(id, id1);
    }

    @Test(groups = {"idService"})
    public void testIpConfigurable() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "spring/vesta-service-factory-bean-ip-configurable-test.xml");
        IdService idService = (IdService) applicationContext
                .getBean("idServiceIpConfigurable");

        long id = idService.genId();
        Id ido = idService.expId(id);
        long id1 = idService.makeId(ido.getVersion(), ido.getType(),
                ido.getGenMethod(), ido.getTime(), ido.getSeq(),
                ido.getMachine());

        System.err.println(id + ":" + ido);

        AssertJUnit.assertEquals(id, id1);
    }

    @Test(groups = {"idService"})
    public void testDb() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "spring/vesta-service-factory-bean-db-test.xml");
        IdService idService = (IdService) applicationContext
                .getBean("idServiceDb");

        long id = idService.genId();
        Id ido = idService.expId(id);
        long id1 = idService.makeId(ido.getVersion(), ido.getType(),
                ido.getGenMethod(), ido.getTime(), ido.getSeq(),
                ido.getMachine());

        System.err.println(id + ":" + ido);

        AssertJUnit.assertEquals(id, id1);
    }
}
