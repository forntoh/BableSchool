package ga.forntoh.bableschool.model;

@SuppressWarnings("unused")
public class Score {

    private double firstSequenceMark, secondSequenceMark;
    private Course course;
    private String rank;
    private String termRank;

    public double getFirstSequenceMark() {
        return firstSequenceMark;
    }

    public double getSecondSequenceMark() {
        return secondSequenceMark;
    }

    public Course getCourse() {
        return course;
    }

    public String getRank() {
        return rank;
    }

    public String getTermRank() {
        return termRank;
    }

    public double getScoreAverage() {
        if (firstSequenceMark < 0 || secondSequenceMark < 0) return -1;
        return (firstSequenceMark + secondSequenceMark) / 2.0;
    }
}
