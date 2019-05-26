package com.example.mybookvibez;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mybookvibez.AddBook.AddBookImagePopup;
import com.example.mybookvibez.Leaderboard.LeaderboardTabUsers;
import com.example.mybookvibez.Leaderboard.UsersLeaderAdapter;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * easy API for server
 */
public class ServerApi {
    private final static String USERS_DB = "users";
    private final static String BOOKS_DB = "books";
    private final static String USERS_PROFILES = "users_profile_pics/";
    private StorageReference storage = FirebaseStorage.getInstance().getReference();
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

    public void getBooksList(final ArrayList<BookItem> books, final BooksRecyclerAdapter adapt) {

        db.collection(BOOKS_DB)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ListOfBooks.clearBooksList();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                books.add(document.toObject(BookItem.class));
                                Log.d("getBooks", document.getId() + " => " + document.getData());
                            }

                            adapt.notifyDataSetChanged();
                        } else {
                            Log.d("getBooks", "Error getting documents: ", task.getException());
                        }
                    }
                })
            .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("getBooks", "FAILED_GET_BOOKS");

            }
        });
    }


    public void getUsersList(final ArrayList<User> users, final UsersLeaderAdapter adapt) {

        db.collection(USERS_DB)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            LeaderboardTabUsers.clearUsersList();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                users.add(document.toObject(User.class));
                                Log.d("getUsers", document.getId() + " => " + document.getData());
                            }

                            adapt.notifyDataSetChanged();
                        } else {
                            Log.d("getUsers", "Error getting documents: ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("getUsers", "FAILED_GET_USERS");

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
                        vibeString.setText(got.getVibePoints()+"");
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

    public void addNewBook(BookItem book, Uri uri)
    {
        DocumentReference addDocRef = db.collection(BOOKS_DB).document();
        String id = addDocRef.getId();
        book.setId(id);
        db.collection(BOOKS_DB).document(id).set(book).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("BOOk_ADDED_SUCCESSFULLY");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("BOOK_ADDING_FAILED");
            }
        });
        StorageReference filepath = AddBookImagePopup.mStorage.child(id).child(uri.getLastPathSegment());
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                AddBookImagePopup.mProgress.dismiss();
            }
        });
    }

    public void addUser(User user, String id)
    {
        user.setId(id);
        db.collection(USERS_DB).document(id).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("USER_ADDED_SUCCESSFULLY");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("USER_ADDING_FAILED");
            }
        });
    }


    public void downloadProfilePic(final ImageView img, final String userId) {
        try {
            StorageReference ref = storage.child(USERS_PROFILES + userId);

            final File localFile = File.createTempFile("Images", "bmp");

            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener < FileDownloadTask.TaskSnapshot >() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap my_image = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    img.setImageBitmap(my_image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Downloading photo: ", "Error downloading Image");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
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
