package lab.schneppd.restapp.rest.github;

/**
 * Created by schneppd on 6/22/17.
 */

public class Contributor {

    public String login;
    public int contributions;

    public Contributor(String login, int contributions) {
        this.login = login;
        this.contributions = contributions;
    }

}
