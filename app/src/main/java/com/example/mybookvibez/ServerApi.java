package com.example.mybookvibez;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * easy API for server
 */
public class ServerApi {
    private final static String USERS_DB = "users";
    private final static String BOOKS_DB = "books";
    private FirebaseFirestore db;
    private final static ServerApi instance = new ServerApi();


    /**
     * constructor
     */
    private ServerApi(){
        db = FirebaseFirestore.getInstance();
    }

    /**
     * get singleton instance of this class
     * @return instance of this server
     */
    public static ServerApi getInstance(){
        return instance;
    }

    public void getBooksList(final ArrayList<BookItem> ListOfBooks, final BooksRecyclerAdapter adapt) {

        db.collection(BOOKS_DB)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ListOfBooks.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ListOfBooks.add(document.toObject(BookItem.class));
                        Log.d("getBooks", document.getId() + " => " + document.getData());
                    }
                    adapt.notifyDataSetChanged();
                } else {
                    Log.d("getBooks", "Error getting documents: ", task.getException());
                }
                }
            });
    }

    public void getUser(final String userId, final User[] user, final TextView name,
                        final TextView vibeString, final TextView langs){

        DocumentReference docRef = db.collection(USERS_DB).document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if(document != null && document.exists())
                    {
                        User got =  document.toObject(User.class);
                        user[0] = got;
                        name.setText(got.getName());
                        vibeString.setText(got.getVibePointsString());
//                        langs.setText(Profile.getLangsString(got.getLangs()));

                    }
                    else
                    {
                        System.out.println("no user found");
                    }
                }
            }
        });

    }


    public void addComment(String bookId, Comment comment){
        DocumentReference docRef = db.collection(BOOKS_DB).document(bookId);
        docRef.update("comments", FieldValue.arrayUnion(comment)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                System.out.println("BOOK_ADDED_SUCCESSFULLY");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("BOOK_ADDING_FAILED");
            }
        });
    }

    public void addNewBook(HashMap<String, Object> book)
    {
        db.collection(BOOKS_DB).add(book).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference DocumentReference) {
                System.out.println("BOOK_ADDED_SUCCESSFULLY");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("BOOK_ADDING_FAILED");
            }
        });
    }
//
//    public void updateBook(String id, String field, Object val)
//    {
//        db.collection(BOOKS_DB).document(id).update(field, val).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                System.out.println("BOOK_ADDED_SUCCESSFULLY");
//            }
//        })
//            .addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    System.out.println("BOOK_ADDED_FAILED");
//                }
//            });
//    }


//    public void addPointsUserBus(String busid, final int points){
//
//        DocumentReference busRef = db.collection(USERS_DB).document(busid);
//
//        busRef.update("vibePoints", FieldValue.increment(points)); // raise by points
//    }

}
