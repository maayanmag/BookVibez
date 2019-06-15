package com.example.mybookvibez;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mybookvibez.AddBook.AddBookImagePopup;
import com.example.mybookvibez.BookPage.Comment;
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
import java.util.concurrent.Callable;

import de.hdodenhof.circleimageview.CircleImageView;


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


    public void getBooksListForMap(final ArrayList<BookItem> books, final Callable<Void> AddMarkers) {

        db.collection(BOOKS_DB)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ListOfBooks.clearBooksList();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                books.add(document.toObject(BookItem.class));
                                Log.d("getBooksList", document.getId() + " => " + document.getData());
                            }
                            try {
                                AddMarkers.call();
                            } catch (Exception e) {
                                Log.d("getBooksList", "getBooks from getBooksListForMap failed");
                            }
                        } else {
                            Log.d("getBooksList", "Error getting documents: ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("getBooksList", "FAILED_GET_BOOKS");

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

    /**
     * the function uses to get and display data of particular user in ProfileFragment.
     * @param userId - string id of the user to display
     * @param user - User array to insert User object at index 0
     * @param image - CircleImageView to assign the profile picture of the user
     * @param name - the user name to display
     * @param vibe - the vibe string to display
     * @param points - vibe points of the user
     * @param func - a function to call when finished
     */
    public void getUserForProfileFragment(final String userId, final User[] user, final CircleImageView image,
                                          final TextView name, final TextView vibe, final TextView points,
                                          final Callable<Void> func) {
        DocumentReference docRef = db.collection(USERS_DB).document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document != null && document.exists()) {
                        User got =  document.toObject(User.class);
                        user[0] = got;
                        name.setText(got.getName());
                        vibe.setText(got.getVibeString());
                        points.setText(got.getVibePoints() + " Vibe Points");

                        try {
                            StorageReference ref = storage.child(USERS_PROFILES + userId);
                            final File localFile = File.createTempFile("Images", "bmp");

                            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener < FileDownloadTask.TaskSnapshot >() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap my_image = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    image.setImageBitmap(my_image);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Downloading photo: ", "Error downloading Image");
                                }
                            });
                        } catch (IOException e) { e.printStackTrace(); }

                        try { func.call(); }  catch (Exception e) { e.printStackTrace(); }
                    } else {
                        System.out.println("getUserForProfileFragment: something went wrong");
                    }
                }
            }
        });
    }


    public void getBooksByIdsList(final ArrayList<BookItem> books, final ArrayList<String> booksIds, final Callable<Void> func) {
        for(String id : booksIds){
            final DocumentReference docRef = db.collection(BOOKS_DB).document(id);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        DocumentSnapshot document = task.getResult();
                        if(document != null && document.exists()) {
                            books.add(document.toObject(BookItem.class));
                            Log.d("getBooksByIdsList: ", "added book "+ document.getData());
                            try {
                                func.call();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            System.out.println("no book found");
                        }
                    }
                }
            });
        }
    }



    public void getBook(final String bookId, final BookItem[] book){
        DocumentReference docRef = db.collection(BOOKS_DB).document(bookId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if(document != null && document.exists()) {
                        book[0] = document.toObject(BookItem.class);
                    }
                    else {
                        System.out.println("no book found");
                    }
                }
            }
        });
    }

    /**
     * the method returm a User object and assign it's name in a given textView. used in bookPage.
     * @param userId - the user id to display
     * @param user - the user object to assign
     * @param name - the user name
     */
    public void getUser(final String userId, final User[] user, final TextView name){
        if (user == null || userId == null) return;
        DocumentReference docRef = db.collection(USERS_DB).document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document != null && document.exists()) {
                        User got =  document.toObject(User.class);
                        user[0] = got;
                        if (name != null)
                            name.setText(got.getName());
                    }
                    else {
                        System.out.println("no user found");
                    }
                }
            }
        });
    }

    public void changeBookState(String id, boolean state){
        DocumentReference reference = db.collection(BOOKS_DB).document(id);
        reference.update("offered", state);

    }

    public void addComment(String bookId, Comment comment){
        DocumentReference docRef = db.collection(BOOKS_DB).document(bookId);
        //comment.setTime(FieldValue.serverTimestamp());
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

    /**
     * the method adds new book to firebase and assign it's id to the Book object.
     * the method also creates a folder in firebase storage and store a given image as "1" inside a
     * folder which named with the book id.
     * @param book - the BookItem to add.
     * @param uri - the book image to store
     */
    public void addNewBook(BookItem book, Uri uri) {
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
        StorageReference filepath = storage.child(id).child("1");
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                AddBookImagePopup.mProgress.dismiss();
            }
        });
    }


    /**
     * the method adds a new user to firebase and give the User object its generated new id.
     * @param user - the User object to store.
     * @param id - the user id (which given at outhintication pahse).
     */
    public void addUser(User user,final String id, final Uri uri, final Callable<Void> func) {
        user.setId(id);
        db.collection(USERS_DB).document(id).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("USER_ADDED_SUCCESSFULLY");

                StorageReference filepath = storage.child(USERS_PROFILES + id);
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        System.out.println("addUser: added photo");
                    }
                });

                try {
                    func.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("USER_ADDING_FAILED");
            }
        });
    }

    /**
     * the function assign the user profile picture to a given ImageView.
     * @param img - the image view to update
     * @param userId - the id of the user to pull his pic
     */
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

    /**
     * the function assign the book cover picture to a given ImageView.
     * @param img - the image view to update
     * @param bookId - the id of the book
     */
    public void downloadBookImage(final ImageView img, final String bookId){
        try {
            StorageReference ref = storage.child(bookId + "/1");

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





}
