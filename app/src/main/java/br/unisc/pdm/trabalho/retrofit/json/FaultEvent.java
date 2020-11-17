package br.unisc.pdm.trabalho.retrofit.json;

import java.util.List;

/**
 * Created by José on 24/06/2015.
 */
public class FaultEvent {

    private List<Fault> faults;

    public List<Fault> getFaults() {
        return faults;
    }

    public void setFaults(List<Fault> faults) {
        this.faults = faults;
    }

}