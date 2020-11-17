package br.unisc.pdm.trabalho.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import br.unisc.pdm.trabalho.R;

public class MailHelper {

    static public void sendEmail(Context ctx, String content, String subject) {
        sendEmail(ctx, content, subject, null);
    }

    static public void sendEmail(Context ctx, String content, String subject, Uri attachment) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setAction(Intent.ACTION_SEND);
        // The intent does not have a URI, so declare the "text/plain" MIME type
        emailIntent.setType("message/rfc822"); //rfc822 para forçar a abrir somente clientes de e-mail
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { content });
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

        if (attachment != null) {
            emailIntent.putExtra(Intent.EXTRA_STREAM, attachment);
        }

        ctx.startActivity(Intent.createChooser(emailIntent, ctx.getString(R.string.chooser_email)));
    }

}
