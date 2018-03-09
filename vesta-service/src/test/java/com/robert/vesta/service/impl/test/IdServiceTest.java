package com.robert.vesta.service.impl.test;

import com.robert.vesta.service.bean.Id;
import com.robert.vesta.service.intf.IdService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

@ContextConfiguration(locations = "/spring/vesta-service-test.xml")
public class IdServiceTest extends AbstractTestNGSpringContextTests {

    @Test(groups = {"idService"})
    public void testSimple() {
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
