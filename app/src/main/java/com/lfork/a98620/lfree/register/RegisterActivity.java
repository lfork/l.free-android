package com.lfork.a98620.lfree.register;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.lfork.a98620.lfree.R;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.source.UserDataRepository;
import com.lfork.a98620.lfree.databinding.RegisterActBinding;
import com.lfork.a98620.lfree.util.UserValidation;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    public final ObservableField<String> username = new ObservableField<>();

    public final ObservableField<String> studentId = new ObservableField<>();

    public final ObservableField<String> password = new ObservableField<>();

    public final ObservableField<String> passwordConfirm = new ObservableField<>();

    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RegisterActBinding binding = DataBindingUtil.setContentView(this, R.layout.register_act);
        binding.setViewModel(this);
    }

    public void register(){


        user.setStudentId(studentId.get());
        user.setUserName(username.get());
        user.setUserPassword(password.get());

        if (dataValidate()) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    UserDataRepository repository = UserDataRepository.getInstance();
                    repository.register(new DataSource.GeneralCallback<String>() {
                        @Override
                        public void succeed(String data) {
                            showDealResult("注册成功, 请牢记你的账号:" + data);
                            finish();
                        }

                        @Override
                        public void failed(String log) {
                            showDealResult("注册失败:" + log);
                        }
                    }, user);
                }
            }).start();

        } else {
            showDealResult("请输入合法的注册信息信息");
        }

    }

    private boolean dataValidate(){
        Log.d(TAG, "validate s: " + user.getStudentId() + " u:" + user.getUserName() + " p:" + user.getUserPassword());
        return UserValidation.RegistValidation(studentId.get(), username.get(), password.get());
    }

    private void showDealResult(final String result) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        });
    }
}
