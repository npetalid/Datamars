package gr.petalidis.datamars.inspections.domain;

import gr.petalidis.datamars.Moo;
import gr.petalidis.datamars.R;

public enum CommentType {
    EMPTY(Moo.getAppContext().getResources().getString(R.string.commentEmpty)),
    SOLD(Moo.getAppContext().getResources().getString(R.string.commentSale)),
    DEAD(Moo.getAppContext().getResources().getString(R.string.commentDeath)),
    SLAUGHTERED(Moo.getAppContext().getResources().getString(R.string.commentSlaughtered)),
    DOUBLE(Moo.getAppContext().getResources().getString(R.string.commentDouble)),
    SINGLE(Moo.getAppContext().getResources().getString(R.string.commentSingle)),
    FAULT(Moo.getAppContext().getResources().getString(R.string.commentError));

    final String title;

    CommentType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString()
    {
        return title;
    }

   public static CommentType fromString(String commentType)
   {

       if (commentType!=null) {
           if (commentType.isEmpty()) {
               return CommentType.EMPTY;
           } else if (commentType.trim().toUpperCase().equals(CommentType.SOLD.title)) {
               return CommentType.SOLD;
           } else if (commentType.trim().toUpperCase().equals(CommentType.DEAD.title)) {
               return CommentType.DEAD;
           } else if (commentType.trim().toUpperCase().equals(CommentType.SLAUGHTERED.title)) {
               return CommentType.SLAUGHTERED;
           } else if (commentType.trim().toUpperCase().equals(CommentType.DOUBLE.title)) {
               return CommentType.DOUBLE;
           } else if (commentType.trim().toUpperCase().equals(CommentType.SINGLE.title)) {
               return CommentType.SINGLE;
           } else if (commentType.trim().toUpperCase().equals(CommentType.FAULT.title)) {
               return CommentType.FAULT;
           }
       }
       throw new IllegalArgumentException("Read " + commentType + " as comment");
   }
}
