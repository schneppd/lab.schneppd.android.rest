package lab.schneppd.restapp.rest.github;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by schneppd on 6/22/17.
 */

public interface GitHubService {
    public static String API_URL = "https://api.github.com";

    @GET("/repos/{owner}/{repo}/contributors")
    public Observable<List<Contributor>> GetContributors(@Path("owner") String owner, @Path("repo") String repo);

}
