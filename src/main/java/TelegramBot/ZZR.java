package TelegramBot;



public class ZZR {
    private String name;
    private String dv;
    private String vid;
    private String virobnik;
    private String normaVneseniy;
    private String kultura;
    private String spectr;

    public ZZR(String name, String dv, String vid, String virobnik, String normaVneseniy, String kultura, String spectr) {
        this.name = name;
        this.dv = dv;
        this.vid = vid;
        this.virobnik = virobnik;
        this.normaVneseniy = normaVneseniy;
        this.kultura = kultura;
        this.spectr = spectr;
    }

    @Override
    public String toString() {
        return "ZZR{" +
                "name='" + name + '\'' +
                ", dv='" + dv + '\'' +
                ", vid='" + vid + '\'' +
                ", virobnik='" + virobnik + '\'' +
                ", normaVneseniy='" + normaVneseniy + '\'' +
                ", kultura='" + kultura + '\'' +
                ", spectr='" + spectr + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDv() {
        return dv;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getVirobnik() {
        return virobnik;
    }

    public void setVirobnik(String virobnik) {
        this.virobnik = virobnik;
    }

    public String getNormaVneseniy() {
        return normaVneseniy;
    }

    public void setNormaVneseniy(String normaVneseniy) {
        this.normaVneseniy = normaVneseniy;
    }

    public String getKultura() {
        return kultura;
    }

    public void setKultura(String kultura) {
        this.kultura = kultura;
    }

    public String getSpectr() {
        return spectr;
    }

    public void setSpectr(String spectr) {
        this.spectr = spectr;
    }
}
