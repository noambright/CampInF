package com.campin.Utils;

/**
 * Created by Igor on 7/24/2017.
 */

public class TripComments
{
    private String _tripComment;
    private Double _commentScore;

    public TripComments()
    {

    }

    public TripComments(String _tripComment, Double _commentScore)
    {
        this._tripComment = _tripComment;
        this._commentScore = _commentScore;
    }

    public String get_tripComment()
    {
        return _tripComment;
    }

    public void set_tripComment(String _tripComment)
    {
        this._tripComment = _tripComment;
    }

    public Double get_commentScore()
    {
        return _commentScore;
    }

    public void set_commentScore(Double _commentScore)
    {
        this._commentScore = _commentScore;
    }
}
