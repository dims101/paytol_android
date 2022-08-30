package com.lit.paytol;

public class Informasi {
    private String namaInformasi;
    private String keterangan;

    public Informasi(String namaInformasi, String keterangan) {
        this.namaInformasi = namaInformasi;
        this.keterangan = keterangan;
    }

    public String getNamaInformasi() {
        return namaInformasi;
    }

    public void setNamaInformasi(String namaInformasi) {
        this.namaInformasi = namaInformasi;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
