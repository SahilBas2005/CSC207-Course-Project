package entities;

public class TournamentSets {

    private SetData[] ongoingSets;

    private SetData[] upcomingSets;

    public TournamentSets(SetData[] ongoingSets, SetData[] upcomingSets) {
        this.ongoingSets = ongoingSets;
        this.upcomingSets = upcomingSets;
    }
}
