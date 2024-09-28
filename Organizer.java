public class Organizer extends Person{
    private String emailAddress;
    private String organizationName;

    public Organizer(String firstName, String lastName, String address, String phoneNumber, String emailAddress, String organizationName){
        super(firstName, lastName, address, phoneNumber);
        this.emailAddress=emailAddress;
        this.organizationName=organizationName;

    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress){
        this.emailAddress=emailAddress;
    }

    public String getOrganizationName(){ 
        return organizationName;
    }

    public void setOrganzationName(String organizationName){
        this.organizationName=organizationName;
    }
}
