package luis.clientebanco;

public class Cuentas {

    private int id;
    private String username;
    private String name;
    private String lastname;
    private String dni;
    private String iban;
    private float balance;
    private int permiso;



    public Cuentas(int id, String username, String name, String lastname,String dni,String iban,
                   float balance,int permiso) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.lastname = lastname;
        this.dni = dni;
        this.iban = iban;
        this.balance = balance;
        this.permiso = permiso;

    }


    public int getID() {
        return id;
    }
    public void setID(int id) {
        this.id = id;
    }

    public String getUSERNAME() {
        return username;
    }
    public void setUSERNAME(String username) {
        this.username = username;
    }

    public String getNAME() {
        return name;
    }
    public void setNAME(String name) {
        this.name = name;
    }

    public String getLASTNAME() {
        return lastname;
    }
    public void setLASTNAME(String lastname) {
        this.lastname = lastname;
    }

    public String getDNI() {
        return dni;
    }
    public void setDNI(String dni) {
        this.dni = dni;
    }

    public String getIBAN() {
        return iban;
    }
    public void setIBAN(String iban) {
        this.iban = iban;
    }

    public float getBALANCE() {
        return balance;
    }
    public void setBALANCE(float balance) {
        this.balance = balance;
    }

    public int getPERMISO() {
        return permiso;
    }
    public void setPERMISO(int permiso) {
        this.permiso = permiso;
    }
}
