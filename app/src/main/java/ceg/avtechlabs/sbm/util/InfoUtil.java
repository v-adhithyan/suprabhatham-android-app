package ceg.avtechlabs.sbm.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * Created by adhithyan-3592 on 07/02/16.
 */

public class InfoUtil {

    public static String getEmailAddress(Context context){

        Pattern emailPattern = Patterns.EMAIL_ADDRESS;

        Account[] accounts = AccountManager.get(context).getAccounts();

        String email = "";
        for(Account account : accounts){
            if(emailPattern.matcher(account.name).matches()){
                email = account.name;
            }
        }

        ToastUtil.showToast(context, email);
        return email;
    }
}
