package com.androidsx.rateme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidsx.libraryrateme.R;
import com.androidsx.rateme.RateMeDialog.RateMeAction;
import com.androidsx.rateme.RateMeDialog.RateMeOnActionListener;

/**
 * Dialog to ask the user for feedback after a low rating.
 */
public class FeedbackDialog extends DialogFragment {
    private static final String TAG = FeedbackDialog.class.getSimpleName();
    
    private static final String EXTRA_EMAIL = "email";
    private static final String EXTRA_DIALOG_TITLE_COLOR = "dialog-title-color";
    private static final String EXTRA_DIALOG_COLOR = "dialog-color";
    private static final String EXTRA_TEXT_COLOR = "text-color";
    private static final String EXTRA_HEADER_TEXT_COLOR = "header-text-color";
    private static final String EXTRA_LOGO = "icon";
    private static final String EXTRA_RATE_BUTTON_TEXT_COLOR = "button-text-color";
    private static final String EXTRA_RATE_BUTTON_BG_COLOR = "button-bg-color";
    private static final String EXTRA_TITLE_DIVIDER = "color-title-divider";
    private static final String EXTRA_RATING_BAR = "get-rating";
    
    // Views
    private View confirmDialogTitleView;
    private View confirmDialogView;
    private Button cancel;
    private Button yes;
    
    private RateMeOnActionListener onActionListener = new RateMeOnActionListener() {  
        @Override
        public void onHandleRateMeAction(RateMeAction action, float rating)
        {
            Log.d(TAG, "Action " + action + " (rating: " + rating + ")");
        }
    };
    
    public static FeedbackDialog newInstance (String email, int titleBackgroundColor, int dialogColor, int headerTextColor, int textColor, int logoResId, int rateButtonTextColor, int rateButtonBackgroundColor, int lineDividerColor, float getRatingBar){
        FeedbackDialog feedbackDialog = new FeedbackDialog();
        Bundle args = new Bundle();
        args.putString(EXTRA_EMAIL, email);
        args.putInt(EXTRA_DIALOG_TITLE_COLOR, titleBackgroundColor);
        args.putInt(EXTRA_DIALOG_COLOR, dialogColor);
        args.putInt(EXTRA_HEADER_TEXT_COLOR, headerTextColor);
        args.putInt(EXTRA_TEXT_COLOR, textColor);
        args.putInt(EXTRA_LOGO, logoResId);
        args.putInt(EXTRA_RATE_BUTTON_TEXT_COLOR, rateButtonTextColor);
        args.putInt(EXTRA_RATE_BUTTON_BG_COLOR, rateButtonBackgroundColor);
        args.putInt(EXTRA_TITLE_DIVIDER, lineDividerColor);
        args.putFloat(EXTRA_RATING_BAR, getRatingBar);
        feedbackDialog.setArguments(args);
        return feedbackDialog;
        
    }
    
    public FeedbackDialog()
    {
        super();
    }
    
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        
        try {
            onActionListener = (RateMeOnActionListener) activity;
        } catch (ClassCastException e) {
            // throw new ClassCastException("An activity hosting a DialogGoToMail fragment must implement the necessary listener interface: " + RateMeOnActionListener.class.getName());
        }
        
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        initializeUiFieldsDialogGoToMail();
        Log.d(TAG, "All components were initialized successfully");
        
        cancel.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View v) {
                dismiss();
                onActionListener.onHandleRateMeAction(RateMeAction.LOW_RATING_REFUSED_TO_GIVE_FEEDBACK, getArguments().getFloat(EXTRA_RATING_BAR));
                Log.d(TAG, "Canceled the feedback dialog");
            }
        });  
        
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMail();
                onActionListener.onHandleRateMeAction(RateMeAction.LOW_RATING_GAVE_FEEDBACK, getArguments().getFloat(EXTRA_RATING_BAR));
                Log.d(TAG, "Agreed to provide feedback");
                dismiss();
            }
        });
        
        return builder.setCustomTitle(confirmDialogTitleView).setView(confirmDialogView).create();
    }
    
    private void initializeUiFieldsDialogGoToMail() {
        confirmDialogTitleView = View.inflate(getActivity(), R.layout.feedback_dialog_title, null);
        confirmDialogView = View.inflate(getActivity(), R.layout.feedback_dialog_message, null);
        confirmDialogTitleView.setBackgroundColor(getArguments().getInt(EXTRA_DIALOG_TITLE_COLOR));
        confirmDialogView.setBackgroundColor(getArguments().getInt(EXTRA_DIALOG_COLOR));
        if (getArguments().getInt(EXTRA_LOGO) == 0) {
            confirmDialogView.findViewById(R.id.app_icon_dialog_mail).setVisibility(View.GONE);
        } else {
            ((ImageView) confirmDialogView.findViewById(R.id.app_icon_dialog_mail)).setImageResource(getArguments().getInt(EXTRA_LOGO));
            confirmDialogView.findViewById(R.id.app_icon_dialog_mail).setVisibility(View.VISIBLE);
        }
        ((TextView) confirmDialogTitleView.findViewById(R.id.confirmDialogTitle)).setTextColor(getArguments().getInt(EXTRA_HEADER_TEXT_COLOR));
        ((TextView) confirmDialogView.findViewById(R.id.mail_dialog_message)).setTextColor(getArguments().getInt(EXTRA_TEXT_COLOR));
        cancel = (Button) confirmDialogView.findViewById(R.id.buttonCancel);
        yes = (Button) confirmDialogView.findViewById(R.id.buttonYes);
        cancel.setTextColor(getArguments().getInt(EXTRA_RATE_BUTTON_TEXT_COLOR));
        yes.setTextColor(getArguments().getInt(EXTRA_RATE_BUTTON_TEXT_COLOR));
        cancel.setBackgroundColor(getArguments().getInt(EXTRA_RATE_BUTTON_BG_COLOR));
        yes.setBackgroundColor(getArguments().getInt(EXTRA_RATE_BUTTON_BG_COLOR));
    }
    
    private void goToMail() {
        final String subject = getResources().getString(R.string.rateme_subject_email, getResources().getString(R.string.app_name));
        final String gmailPackageName = "com.google.android.gm";
        
        try {
            if (isPackageInstalled(gmailPackageName)) {
                Intent sendMailWithGmail = new Intent(Intent.ACTION_SEND);
                sendMailWithGmail.setType("plain/text");
                sendMailWithGmail.putExtra(Intent.EXTRA_EMAIL, new String[]{getArguments().getString(EXTRA_EMAIL)});
                sendMailWithGmail.putExtra(Intent.EXTRA_SUBJECT, subject);
                sendMailWithGmail.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                startActivity(Intent.createChooser(sendMailWithGmail, ""));
            } else {
                sendGenericMail(subject);
            }
        } catch (android.content.ActivityNotFoundException ex) {
            sendGenericMail(subject);
        }
    }
    
    @Override
    public void onStart() {
        super.onStart();
        final int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
        final View titleDivider = getDialog().findViewById(titleDividerId);
        if (titleDivider != null) {
            titleDivider.setBackgroundColor(getArguments().getInt(EXTRA_TITLE_DIVIDER));
        }
    }

    private boolean isPackageInstalled(String packageName) {
        PackageManager pm = getActivity().getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    
    private void sendGenericMail(String subject) {
        Log.w(TAG, "Cannot send the email with GMail. Will use the generic chooser");
        Intent sendGeneric = new Intent(Intent.ACTION_SEND);
        sendGeneric.setType("plain/text");
        sendGeneric.putExtra(Intent.EXTRA_EMAIL, new String[] { getArguments().getString(EXTRA_EMAIL) });
        sendGeneric.putExtra(Intent.EXTRA_SUBJECT, subject);
        startActivity(Intent.createChooser(sendGeneric, ""));
    } 
}