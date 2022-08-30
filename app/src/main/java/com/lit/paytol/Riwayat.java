package com.lit.paytol;

public class Riwayat {
    private String rute;
    private String tarif;
    private String tanggal;
    private String waktu;

    public Riwayat(String rute, String tarif, String tanggal, String waktu) {
        this.rute = rute;
        this.tarif = tarif;
        this.tanggal = tanggal;
        this.waktu = waktu;
    }

    public String getRute() {
        return rute;
    }

    public void setRute(String rute) {
        this.rute = rute;
    }

    public String getTarif() {
        return tarif;
    }

    public void setTarif(String tarif) {
        this.tarif = tarif;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}
