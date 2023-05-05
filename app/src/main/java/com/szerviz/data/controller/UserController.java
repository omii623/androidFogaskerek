package com.szerviz.data.controller;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.szerviz.data.dao.UserDAO;
import com.szerviz.data.model.User;

import java.util.concurrent.CompletableFuture;

public class UserController {
    private static UserController instance = new UserController();
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference userCollection;
    public String reference = "";

    private UserController(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        userCollection = firebaseFirestore.collection("user");
    }

    public static UserController getInstance() {return instance;}

    public Task<User> getUserByEmail(String email) {
        final TaskCompletionSource<User> taskCompletionSource = new TaskCompletionSource<>();

        userCollection.whereEqualTo("email", email)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            QueryDocumentSnapshot documentSnapshot = (QueryDocumentSnapshot) querySnapshot.getDocuments().get(0);
                            User user = documentSnapshot.toObject(User.class);
                            reference = documentSnapshot.getReference().getId();
                            taskCompletionSource.setResult(user);
                        } else {
                            taskCompletionSource.setResult(null);
                            //taskCompletionSource.setException(new Exception("No user found with email: " + email));
                        }
                    } else {
                        taskCompletionSource.setException(task.getException());
                    }
                });

        return taskCompletionSource.getTask();
    }

    public void addUser(User user){
        if(user != null){
            userCollection.add(user);
        }
    }

    public void updateUser(String userName,String phone, String addres){
        if(!reference.equals("")){
            DocumentReference docRef = userCollection.document(reference);
            docRef.update("addres",addres);
            docRef.update("phoneNumber",phone);
            docRef.update("userName",userName);
        }
    }
}
