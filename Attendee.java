public class Attendee extends Person{
    private String emailAddress;

    
    public Attendee(String firstName, String lastName, String address, String phoneNumber, String emailAddress){
        super(firstName, lastName, address, phoneNumber);
        this.emailAddress=emailAddress;

    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress){
        this.emailAddress=emailAddress;
    }
}
