package com.example.mybookvibez;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mybookvibez.AddBook.AddBookImagePopup;
import com.example.mybookvibez.BookPage.BookPageFragment;
import com.example.mybookvibez.BookPage.BookPageTabDetails;
import com.example.mybookvibez.BookPage.Comment;
import com.example.mybookvibez.BookPage.CommentAdapter;
import com.example.mybookvibez.Leaderboard.LeaderboardTabUsers;
import com.example.mybookvibez.Leaderboard.UsersLeaderAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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


    /**
     * get all the book from server which are currently available for exchange/giveaway
     * @param books - the list to be filled with books
     * @param AddMarkers - the function which should be called whenever the list is filled with books.
     */
    public void getBooksListForMap(final ArrayList<BookItem> books, final Callable<Void> AddMarkers) {

        db.collection(BOOKS_DB)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ListOfBooks.clearBooksList();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                BookItem book = document.toObject(BookItem.class);
                                if(book.getOffered())
                                    books.add(book);
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


    /**
     * this func returns a list of users which will be shown in the LeaderBoard
     * @param users - the list to fill
     * @param adapt - the adapter to notify when the list changes
     * @param func - the function to call whenever the process is finished
     */
    public void getUsersList(final ArrayList<User> users, final UsersLeaderAdapter adapt, final Callable<Void> func) {
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
                            try { func.call(); } catch (Exception e) { e.printStackTrace(); }
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
     * this method is used to get and display data of particular user in ProfileFragment.
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


    /**
     * this func is used when there's a need to get books list by their IDs (used in profiles)
     * @param books - the list to fill with books
     * @param booksIds - the IDs to search for
     * @param func - the function to call whenever the process is finished
     */
    public void getBooksByIdsList(final ArrayList<BookItem> books, final ArrayList<String> booksIds,
                                  final Callable<Void> func) {
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

    /**
     * the method returm a User object and assign it's name in a given textView. used in bookPage.
     * @param userId - string id of the user to display
     * @param user - User array to insert User object at index 0
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

// <<<<<<< HEAD
//     /**
//      * this method is used to get and display data of user and his books in ChooseFromMyBooks.
//      * @param userId the user's id
//      * @param user an array to
//      * @param func - a function to call when finished
//      */
//     public void getUserForChooseFromMyBooks(final String userId, final User[] user, final Callable<Void> func) {
//         DocumentReference docRef = db.collection(USERS_DB).document(userId);
//         docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//             @Override
//             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                 if(task.isSuccessful()) {
//                     DocumentSnapshot document = task.getResult();
//                     if(document != null && document.exists()) {
//                         User got =  document.toObject(User.class);
//                         user[0] = got;
//                         try { func.call(); }  catch (Exception e) { e.printStackTrace(); }
//                     } else {
//                         System.out.println("getUserForProfileFragment: something went wrong");
//                     }
//                 }
//             }
//         });
//     }


    /**
     * this func updates a given book and users whenever a swap has occurred. the func updates their
     * vibePoints and changes ownership details
     * @param bookId - the book which had swap
     * @param state - a boolean flag which indicates whether the book is available for exchange or not
     * @param newOwnerId - new owner's id
     * @param pastOwnerId - past owner's id
     */
    public void changeBookState(String bookId, boolean state, final String newOwnerId, final String pastOwnerId){
        DocumentReference reference = db.collection(BOOKS_DB).document(bookId);
        reference.update("offered", state);
        reference.update("ownerId", newOwnerId);
        reference.update("points", FieldValue.increment(2));
        reference.update("ownedBy", FieldValue.increment(1));

        /* add this book to the past owner's list of booksIREAD and remove it from his MYbooks */
        DocumentReference pastOwner = db.collection(USERS_DB).document(pastOwnerId);
        pastOwner.update("booksIRead", FieldValue.arrayUnion(bookId));
        pastOwner.update("myBooks", FieldValue.arrayRemove(bookId));
        reference.update("vibePoints", FieldValue.increment(2));

        /* add this book to the new owner's list of MYbooks */
        DocumentReference newOwner = db.collection(USERS_DB).document(newOwnerId);
        newOwner.update("myBooks", FieldValue.arrayUnion(bookId));
        newOwner.update("vibePoints", FieldValue.increment(2));

    }


    /**
     * the func add points to user and book whenever they deserves it
     * @param bookId - the book to update
     * @param userId - the user to update
     */
    public void addPoints(final String bookId, final String userId){
        /* add points to user */
        DocumentReference userRef = db.collection(USERS_DB).document(userId);
        userRef.update("vibePoints", FieldValue.increment(2));

        /* add points to book */
        DocumentReference bookRef = db.collection(BOOKS_DB).document(bookId);
        bookRef.update("points", FieldValue.increment(2));
    }

    /**
     * the func adds a new comment to a given book
     * @param bookId - the book to update
     * @param comment - the Comment object
     * @param commentsList - the book's comments so far
     * @param commentAdapter - commentsList' adapter
     */
    public void addComment(final String bookId, final Comment comment, final ArrayList<Comment>
            commentsList, final CommentAdapter commentAdapter){
        DocumentReference docRef = db.collection(BOOKS_DB).document(bookId);
        Date date = new Date();
        String [] temp = date.toString().split(" ");
        String timeToSave = temp[1]+" "+temp[2]+", "+temp[5];
        comment.setTime(timeToSave);
        docRef.update("comments", FieldValue.arrayUnion(comment)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                commentsList.add(comment);
                commentAdapter.notifyDataSetChanged();
                System.out.println("COMMENT_ADDED_SUCCESSFULLY");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("COMMENT_ADDING_FAILED");
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
