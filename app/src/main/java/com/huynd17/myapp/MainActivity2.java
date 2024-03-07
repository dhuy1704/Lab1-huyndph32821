package com.huynd17.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity2 extends AppCompatActivity {
    TextView tvKQ;
    FirebaseFirestore database;
    Context context=this;
    String strKQ="";
    ToDo toDo = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tvKQ=findViewById(R.id.tvKQ);
        database=FirebaseFirestore.getInstance();//Khoi tao database
        insert();
//        update();
//       select();
    }
    void insert(){
        String id = UUID.randomUUID().toString(); //lay chuoi ngau nhien
        toDo = new ToDo(id, "title 11", "content 11");// tao doi tuong moi
        database.collection("TODO")//truy cap den bang du lieu
                .document(id)//truy cap den dong du lieu
                .set(toDo.convertToHashMap())//dua du lieu vao dong
                .addOnSuccessListener(new OnSuccessListener<Void>() {//thanh cong
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "insert thanh cong", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {//thất bại
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "insert that bai", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    void update(){
        String id = ""; //copy id vao day
        toDo = new ToDo(id, "title 11 update", "content 11 update");// noi dung can update
        database.collection("TODO")//lay bang du lieu
                .document(id)//lay id
                .update(toDo.convertToHashMap())// thuc hien update
                .addOnSuccessListener(new OnSuccessListener<Void>() {//thanh cong
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "update thanh cong", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {//that bai
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "update that bai", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    void delete(){
        String id="";
        database.collection("TODO")//truy cap vao bang du lieu
                .document(id)//truy cap vao id
                .delete()//thuc hien xoa
                .addOnCompleteListener(new OnCompleteListener<Void>() {//xoa thanh cong
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Xoa thanh cong", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {//xoa that bai
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Xoa that bai", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    ArrayList<ToDo> select()
    {
        ArrayList<ToDo> list = new ArrayList<>();
        database.collection("TODO")
                .get()//lay ve du lieu
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            strKQ="";
                            for (QueryDocumentSnapshot doc: task.getResult()){
                                ToDo t = doc.toObject(ToDo.class);//chuyen du lieu doc duoc sang dang ToDo
                                list.add(t);
                                strKQ += "id: "+t.getId()+"\n";
                                strKQ += "title: "+t.getTitle()+"\n";
                                strKQ += "content: "+t.getContent()+"\n";
                            }
                            tvKQ.setText(strKQ);
                        }
                        else {
                            Toast.makeText(context, "select that bai", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return list;
    }
}