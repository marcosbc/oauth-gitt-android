package luis.clientebanco;

public class Transacciones {

    private int id;
    private int cuentaid;
    private String to_descrip;
    private String from_descrip;
    private float quantity;
    private String from_iban;
    private String to_iban;
    private String date;
    private String status;




    public Transacciones(int id, int cuentaid, String to_descrip, String from_descrip, float quantity, String from_iban,
                         String to_iban, String date, String status) {
        this.id = id;
        this.cuentaid = cuentaid;
        this.to_descrip = to_descrip;
        this.from_descrip = from_descrip;
        this.quantity = quantity;
        this.from_iban = from_iban;
        this.to_iban = to_iban;
        this.status = status;
        this.date = date;

    }

    public int getID() {
        return id;
    }
    public void setID(int id) {
        this.id = id;
    }

    public int getCUENTAID() {
        return cuentaid;
    }
    public void setCUENTAID(int cuentaid) {
        this.cuentaid = cuentaid;
    }

    public String getTODESCRIP() {
        return to_descrip;
    }
    public void setTODESCRIP(String username) {
        this.to_descrip = to_descrip;
    }

    public String getFROMDESCRIP() {
        return from_descrip;
    }
    public void setFROMDESCRIP(String from_descrip) {
        this.from_descrip = from_descrip;
    }

    public float getQUANTITY() {
        return quantity;
    }
    public void setQUANTITY(float quantity) {
        this.quantity = quantity;
    }

    public String getFROMIBAN() {
        return from_iban;
    }
    public void setFROMIBAN(String from_iban) {
        this.from_iban = from_iban;
    }

    public String getTOIBAN() {
        return to_iban;
    }
    public void setTOIBAN(String to_iban) {
        this.to_iban = to_iban;
    }

    public String getSTATUS() {
        return status;
    }
    public void setSTATUS(String status) {
        this.status = status;
    }

    public String getDATE() {
        return date;
    }
    public void setDATE(String date) {
        this.date = date;
    }

}
