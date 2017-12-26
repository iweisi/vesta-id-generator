package com.robert.vesta.service.impl.provider;

public class PropertyMachineIdProvider implements MachineIdProvider {
    private long machineId;

    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }
}
