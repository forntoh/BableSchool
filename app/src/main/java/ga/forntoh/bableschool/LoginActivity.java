package ga.forntoh.bableschool;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import ga.forntoh.bableschool.model.User;
import ga.forntoh.bableschool.network.ApiService;
import ga.forntoh.bableschool.network.RetrofitBuilder;
import ga.forntoh.bableschool.store.MyStores;
import ga.forntoh.bableschool.store.StorageUtil;
import ga.forntoh.bableschool.store.StoreRepository;
import ga.forntoh.bableschool.utils.Utils;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings("unchecked")
public class LoginActivity extends AppCompatActivity {

    private PopupWindow popupWindow;
    private EditText userName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.et_uname);
        password = findViewById(R.id.et_pass);
    }

    @SuppressLint({"CheckResult", "InflateParams"})
    public void login(View view) {

        String matriculation = userName.getText().toString();
        String pass = password.getText().toString();

        if (TextUtils.isEmpty(matriculation) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        popupWindow = Utils.startPopUpWindow(
                LayoutInflater.from(this).inflate(R.layout.dialog_loading, null),
                findViewById(R.id.parent), null);

        ApiService service = RetrofitBuilder.createService(ApiService.class);
        StoreRepository repository = new StoreRepository(this.getApplication());
        repository
                .create(service.getUserProfile(matriculation, pass), User.class)
                .fetch(MyStores.USER)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        user -> {
                            StorageUtil.with(this).saveMatriculation(((User) user).getProfileData().get("Matriculation"));
                            StorageUtil.with(this).savePassword(((User) user).getProfileData().get("Password"));
                            runOnUiThread(() -> {
                                startActivity(new Intent(this, MainActivity.class));
                                finish();
                            });
                        },
                        t -> runOnUiThread(() -> {
                                    popupWindow.dismiss();
                                    Toast.makeText(this, "Account does not exist or has been deactivated", Toast.LENGTH_LONG).show();
                                    ((Throwable) t).printStackTrace();
                                }
                        )
                );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
}
