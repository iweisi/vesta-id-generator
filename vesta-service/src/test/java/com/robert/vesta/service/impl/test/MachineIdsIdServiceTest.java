package com.robert.vesta.service.impl.test;

import com.robert.vesta.service.bean.Id;
import com.robert.vesta.service.intf.IdService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

@ContextConfiguration(locations = "/spring/vesta-service-machineids-test.xml")
public class MachineIdsIdServiceTest extends AbstractTestNGSpringContextTests {

    @Test(groups = {"idService"})
    public void testSimple() {
        IdService idService = (IdService) applicationContext
                .getBean("idService");
        for (int i = 0; i < 2; i++) {
            long id = idService.genId();
            Id ido = idService.expId(id);
            long id1 = idService.makeId(ido.getVersion(), ido.getType(),
                    ido.getGenMethod(), ido.getTime(), ido.getSeq(),
                    ido.getMachine());
            System.out.println(id + ":" + ido);

            AssertJUnit.assertEquals(id, id1);

            try {
                System.out.println("You can change the system time in 10 seconds");
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
        }

    }
}
