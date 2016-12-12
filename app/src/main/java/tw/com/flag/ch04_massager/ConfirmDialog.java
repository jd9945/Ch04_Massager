package tw.com.flag.ch04_massager;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Administrator on 2016/12/8.
 */

public class ConfirmDialog extends Dialog {

        Context context;
        public ConfirmDialog(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
            this.context = context;
        }
        public ConfirmDialog(Context context, int theme){
            super(context, theme);
            this.context = context;
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
            this.setContentView(R.layout.dialog);
        }

}
