package com.android.operatorapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.role.RoleManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.android.operatorapp.Api.APIClient;
import com.android.operatorapp.Api.ApiInterface;
import com.android.operatorapp.Checker.PermissionChecker;
import com.android.operatorapp.Common.Utils;
import com.android.operatorapp.DataBase.CallDB;
import com.android.operatorapp.Model.GBody;
import com.android.operatorapp.Service.CallReceiver;
import com.android.operatorapp.View.ConsoleView;
import com.android.operatorapp.databinding.ActivityMainBinding;
import com.jraska.console.Console;
import com.jraska.console.timber.ConsoleTree;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ID = 1;
    private ActivityMainBinding binding;
    private Context context = this;
    private static MainActivity INSTANCE;
    private ConsoleView consoleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        INSTANCE=this;
        consoleView = new ConsoleView(binding.consoleView, context);
        if(Utils.getSharedPreference(context,"active").equals("1")){
            binding.statusSwitch.setChecked(true);
        } else {
            binding.statusSwitch.setChecked(false);
        }
        setListener();
        checkPermissions();
    }

    public static MainActivity get(){
        return INSTANCE;
    }

    public ConsoleView getConsoleView(){
        return consoleView;
    }

    private void setListener() {
        binding.ip.setText(Utils.getSharedPreference(this, "ip"));
        binding.connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.ip.getText().toString().trim().isEmpty()) {
                    Toast.makeText(context, "Server salgysyny girizin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Utils.setPreference("ip", binding.ip.getText().toString(), context);
                ApiInterface apiInterface = APIClient.getClient(Utils.urlGenerator(binding.ip.getText().toString())).create(ApiInterface.class);
                Call<GBody<String>> call = apiInterface.testConnection();
                call.enqueue(new Callback<GBody<String>>() {
                    @Override
                    public void onResponse(Call<GBody<String>> call, Response<GBody<String>> response) {
                        if (response.isSuccessful()) {
                            consoleView.s("Test result: " + response.body().getBody());
                            consoleView.s("Test message: " + response.body().getMessage());
                        } else {
                            consoleView.e("Test result:" + "Can't connect to: " + binding.ip.getText().toString());
                            consoleView.e("Error code: " + response.code() + "");
                        }
                        binding.scroll.scrollTo(0, binding.scroll.getBottom());
                    }

                    @Override
                    public void onFailure(Call<GBody<String>> call, Throwable t) {
                        consoleView.e("Test result: " + "Can't connect to: " + binding.ip.getText().toString());
                        consoleView.e("Error message: " + t.getMessage());
                        binding.scroll.scrollTo(0, binding.scroll.getBottom());
                    }
                });

            }
        });

        binding.statusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Utils.setPreference("active","1",context);
                } else {
                    Utils.setPreference("active","0",context);
                }
            }
        });

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.setPreference("fullName","",context);
                Utils.setPreference("user_id","",context);
                Utils.setPreference("phoneNumber","",context);
                Utils.setPreference("role_name","",context);
                Utils.setPreference("token","",context);
                Utils.setPreference("unique_id","",context);
                Utils.setPreference("sell_point_id","",context);
                Utils.setPreference("active","0",context);
                finish();
                startActivity(new Intent(context,LoginPage.class));
            }
        });
    }

    private void checkPermissions() {
        boolean isNeedAsk = false;
        for (String permission : PermissionChecker.NEED_PERMISSIONS) {
            if (!PermissionChecker.checkPermission(this, permission)) {
                isNeedAsk = true;
                break;
            }
        }
        if (isNeedAsk) {
            PermissionChecker.askPermission(this, PermissionChecker.NEED_PERMISSIONS, PermissionChecker.ASK_PERMISSION_CODE);
        } else {
            start();
        }
    }

    public void requestRole() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            RoleManager roleManager =  roleManager = (RoleManager) getSystemService(ROLE_SERVICE);
            Intent intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING);
            someActivityResultLauncher.launch(intent);
        }

    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                    }
                }
            });

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionChecker.ASK_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //granted
                start();
            } else {
                //not granted
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void start() {
        requestRole();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE");
        context.registerReceiver(new CallReceiver(), filter);
    }

    public static String executeCmd(String cmd, boolean sudo) {
        try {

            Process p;
            if (!sudo)
                p = Runtime.getRuntime().exec(cmd);
            else {
                p = Runtime.getRuntime().exec(new String[]{"su", "-c", cmd});
            }
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String s;
            String res = "";
            while ((s = stdInput.readLine()) != null) {
                res += s + "\n";
            }
            p.destroy();
            Log.e("Res", res);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }


}