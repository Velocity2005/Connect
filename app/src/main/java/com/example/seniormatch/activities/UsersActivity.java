package com.example.seniormatch.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;



import com.example.seniormatch.adapters.UsersAdapter;
import com.example.seniormatch.databinding.ActivityUsersBinding;
import com.example.seniormatch.listeners.UserListener;
import com.example.seniormatch.models.User;
import com.example.seniormatch.utilities.Constants;
import com.example.seniormatch.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity implements UserListener {

    private ActivityUsersBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        getUsers();
    }

    private void setListeners(){
        binding.imageBack.setOnClickListener(view -> onBackPressed());
    }

    private void getUsers(){
        loading(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    String currentZipcode = preferenceManager.getString(Constants.KEY_ZIPCODE);
                    System.out.println(currentZipcode);
                    if(task.isSuccessful() && task.getResult() != null){
                        List<User> users = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            System.out.println(Integer.parseInt(queryDocumentSnapshot.getString(Constants.KEY_ZIPCODE)) == Integer.parseInt(currentZipcode));
                            if(currentUserId.equals(queryDocumentSnapshot.getId()) || (currentZipcode != null && Integer.parseInt(queryDocumentSnapshot.getString(Constants.KEY_ZIPCODE)) != Integer.parseInt(currentZipcode))){
                                continue;
                            }
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.zipcode = queryDocumentSnapshot.getString(Constants.KEY_ZIPCODE);
                            user.id = queryDocumentSnapshot.getId();
                            System.out.println(user.name);
                            users.add(user);
                        }
                        if(users.size() > 0){
                            UsersAdapter usersAdapter = new UsersAdapter(users,this);
                            binding.usersRecyclerView.setAdapter(usersAdapter);
                            binding.usersRecyclerView.setVisibility(View.VISIBLE);
                        }else{
                            showErrorMessage();
                        }
                    }else{
                        showErrorMessage();
                    }
                });

        System.out.println(db.collection(Constants.KEY_COLLECTION_USERS).get());
    }

    private void showErrorMessage(){
        binding.textErrorMessage.setText(String.format("%s","No User Avaiable"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading){
        if(isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
        intent.putExtra(Constants.KEY_USER,user);
        startActivity(intent);
        finish();
    }
}