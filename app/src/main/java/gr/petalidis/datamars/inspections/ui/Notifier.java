package gr.petalidis.datamars.inspections.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class Notifier {
    public enum NOTIFICATION_MESSAGE_TYPE {
        ERROR_MESSAGE("Λάθος"),
        WARNING_MESSAGE("Προειδοποίηση"),
        INFO_MESSAGE("Πληροφορία");

        private String message = "";

        NOTIFICATION_MESSAGE_TYPE(String message)
        {
            this.message = message;
        }

        public String getMessage()
        {
            return this.message;
        }
    };
    public static void notify(Context context, String msg, NOTIFICATION_MESSAGE_TYPE type)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(type.getMessage());
        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                    }
                })
                ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
