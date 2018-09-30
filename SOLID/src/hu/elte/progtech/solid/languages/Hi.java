package hu.elte.progtech.solid.languages;

public class Hi {

    Languages language;

    public Hi(Languages language) {
        this.language = language;
    }

    private void salute() {
        switch (language) {
        case JAPANESE:
            System.out.println("Harōwārudo");
            break;
        case ENGLISH:
            System.out.println("Hello world");
            break;
        case HUNGARIAN:
            System.out.println("Hello világ");
            break;
        default:
            break;
        }

    }

    public static void main(String[] args) {
        Hi hi = new Hi(Languages.JAPANESE);
        hi.salute();
    }

}
