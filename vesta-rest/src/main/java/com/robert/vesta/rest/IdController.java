package com.robert.vesta.rest;

import com.robert.vesta.service.bean.Id;
import com.robert.vesta.service.intf.IdService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IdController {

    private final IdService idService;

    public IdController() {
        ApplicationContext ac = new ClassPathXmlApplicationContext(
                "spring/vesta-rest-main.xml");

        idService = (IdService) ac.getBean("idService");
    }

    @RequestMapping("/genid")
    public long genId() {

        return idService.genId();
    }

    @RequestMapping("/expid")
    public Id explainId(@RequestParam(value = "id", defaultValue = "0") long id) {
        return idService.expId(id);
    }

    @RequestMapping("/transtime")
    public String transTime(
            @RequestParam(value = "time", defaultValue = "-1") long time) {
        return idService.transTime(time).toString();
    }

    @RequestMapping("/makeid")
    public long makeId(
            @RequestParam(value = "version", defaultValue = "-1") long version,
            @RequestParam(value = "type", defaultValue = "-1") long type,
            @RequestParam(value = "genMethod", defaultValue = "-1") long genMethod,
            @RequestParam(value = "machine", defaultValue = "-1") long machine,
            @RequestParam(value = "time", defaultValue = "-1") long time,
            @RequestParam(value = "seq", defaultValue = "-1") long seq) {

        long madeId = -1;
        if (time == -1 || seq == -1)
            throw new IllegalArgumentException(
                    "Both time and seq are required.");
        else if (version == -1) {
            if (type == -1) {
                if (genMethod == -1) {
                    if (machine == -1) {
                        madeId = idService.makeId(time, seq);
                    } else {
                        madeId = idService.makeId(machine, time, seq);
                    }
                } else {
                    madeId = idService.makeId(genMethod, machine, time, seq);
                }
            } else {
                madeId = idService.makeId(type, genMethod, machine, time, seq);
            }
        } else {
            madeId = idService.makeId(version, type, genMethod, time,
                    seq, machine);
        }

        return madeId;
    }
}