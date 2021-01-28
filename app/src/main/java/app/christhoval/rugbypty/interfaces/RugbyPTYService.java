package app.christhoval.rugbypty.interfaces;

import java.util.Map;

import app.christhoval.rugbypty.models.response.FixturesResults;
import app.christhoval.rugbypty.models.response.NewsList;
import app.christhoval.rugbypty.models.response.PositionsTable;
import app.christhoval.rugbypty.models.response.TournamentList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by christhoval on 03/19/17.
 */

public interface RugbyPTYService {

    @GET("news/list")
    Call<NewsList> listNews(@QueryMap Map<String, String> options);

    @GET("tournament/list")
    Call<TournamentList> listTournament(@QueryMap Map<String, String> options);

    @GET("positions-table")
    Call<PositionsTable> getPositions(@QueryMap Map<String, String> options);

    @GET("fixtures-results/matchs")
    Call<FixturesResults> getMatchs(@QueryMap Map<String, String> options);
}
