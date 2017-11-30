package spider.app.sportsfete.FireBaseServices;

/**
 * Created by praba1110 on 1/2/17.
 */
public class Comment {

    String eventId, comment, username, timestamp;
    long commentId;

    public String getEventId() {
        return eventId;
    }

    public long getCommentId() {
        return commentId;
    }

    public String getUsername() {
        return username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getComment() {
        return comment;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }
}
