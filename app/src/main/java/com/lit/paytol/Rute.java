package com.lit.paytol;

public class Rute {
    private String id;
    private String gateMasuk;
    private String gateKeluar;
    private String tarifI;
    private String tarifII;
    private String tarifIII;
    private String tarifIV;
    private String tarifV;

    public Rute(String id, String gateMasuk, String gateKeluar, String tarifI, String tarifII, String tarifIII, String tarifIV, String tarifV) {
        this.id = id;
        this.gateMasuk = gateMasuk;
        this.gateKeluar = gateKeluar;
        this.tarifI = tarifI;
        this.tarifII = tarifII;
        this.tarifIII = tarifIII;
        this.tarifIV = tarifIV;
        this.tarifV = tarifV;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGateMasuk() {
        return gateMasuk;
    }

    public void setGateMasuk(String gateMasuk) {
        this.gateMasuk = gateMasuk;
    }

    public String getGateKeluar() {
        return gateKeluar;
    }

    public void setGateKeluar(String gateKeluar) {
        this.gateKeluar = gateKeluar;
    }

    public String getTarifI() {
        return tarifI;
    }

    public void setTarifI(String tarifI) {
        this.tarifI = tarifI;
    }

    public String getTarifII() {
        return tarifII;
    }

    public void setTarifII(String tarifII) {
        this.tarifII = tarifII;
    }

    public String getTarifIII() {
        return tarifIII;
    }

    public void setTarifIII(String tarifIII) {
        this.tarifIII = tarifIII;
    }

    public String getTarifIV() {
        return tarifIV;
    }

    public void setTarifIV(String tarifIV) {
        this.tarifIV = tarifIV;
    }

    public String getTarifV() {
        return tarifV;
    }

    public void setTarifV(String tarifV) {
        this.tarifV = tarifV;
    }
}
