package app.christhoval.rugbypty.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.christhoval.rugbypty.RugbyPTY;
import app.christhoval.rugbypty.models.Match;
import app.christhoval.rugbypty.models.MatchDay;

/**
 * Created by christhoval on 04/05/17.
 */

public class RugbyPTYAPIHelpers {

    public static List<Match> convertMatchDays2Matches(List<MatchDay> matchDays, String direction) {
        return RugbyPTYAPIHelpers.convertMatchDays2Matches(matchDays, direction, null);
    }

    public static List<Match> convertMatchDays2Matches(List<MatchDay> matchDays, String direction, String team) {
        List<Match> matches = new ArrayList<>();

        switch (direction) {
            case "-":
            case "+":
                Collections.reverse(matchDays);
                break;
        }

        for (MatchDay matchDay : matchDays) {
            for (Match match : matchDay.getMatchs()) {
                match.setMatchDay(matchDay);
                if (team == null || team.equals("all")) {
                    matches.add(match);
                }else{
                    if(match.getTeamA().getTeamRef().getId().equals(team) || match.getTeamB().getTeamRef().getId().equals(team)){
                        matches.add(match);
                    }
                }
            }

        }
        return matches;
    }
}
