package fr.gouv.impots.entreprises.domain;

public class ImpotCalculator {

    public Integer calculerImpotsEntrpriseIndivuduelle(Integer ca) {
        return ca / 4;
    }

    public Integer calculerImpotsSAS(Integer ca) {
        return Double.valueOf(ca * 0.33).intValue();
    }
}
