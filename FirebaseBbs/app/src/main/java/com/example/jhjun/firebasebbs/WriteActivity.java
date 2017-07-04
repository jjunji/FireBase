package com.example.jhjun.firebasebbs;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jhjun.firebasebbs.domain.Bbs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class WriteActivity extends AppCompatActivity {
    EditText editTitle, editAuthor, editContent;
    TextView textImage;
    // 데이터베이스
    FirebaseDatabase database;
    DatabaseReference bbsRef;
    // 스토리지
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 데이터베이스 레퍼런스
        database = FirebaseDatabase.getInstance();
        bbsRef = database.getReference("bbs");

        // onCreate안에서 스토리지 레퍼런스 만들기
        mStorageRef = FirebaseStorage.getInstance().getReference();

        setContentView(R.layout.activity_detail);

        editTitle = (EditText) findViewById(R.id.editTitle);
        editAuthor = (EditText) findViewById(R.id.editAuthor);
        editContent = (EditText) findViewById(R.id.editContent);
        textImage = (TextView) findViewById(R.id.textImage);

    }

    public void uploadFile(String filePath){
        // 스마트폰에 있는 파일의 경로
        File file = new File(filePath);  // 경로를 가지고 파일 객체만듬.
        Uri uri = Uri.fromFile(file);

        // 파이어베이스에 있는 파일 경로, 경로를 /로 구분하게 되면 첫번째 것을 디렉토리로 쓰겠다는 뜻
        String fileName = file.getName(); // 시간값 or UUID 추가해서 만듬
        // 데이터베이스의 키가 값과 동일한 구조
        StorageReference fileRef = mStorageRef.child(fileName);

        // 파일명 자체를 레퍼런스로 잡고 그곳에 파일을 업로드.
        fileRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // 파이어베이스 스토리지에 방금 업로드한 파일의 경로
                        @SuppressWarnings("VisibleForTests")
                        Uri uploadedUri = taskSnapshot.getDownloadUrl();
                        afterUploadFile(uploadedUri);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    // 데이터 전송
    public void postData(View view) {
        String imagePath = textImage.getText().toString();
        // 이미지가 있으면 이미지 경로를 받아서 저장해야 되기 때문에
        // 이미지를 먼저 업로드 한다.
        if(imagePath != null && !"".equals(imagePath)){
            uploadFile(imagePath);
        }else{
            afterUploadFile(null);
        }
    }

    public void afterUploadFile(Uri imageUri){
        String title = editTitle.getText().toString();
        String author = editAuthor.getText().toString();
        String content = editContent.getText().toString();

        // 파이어베이스 데이터베이스에 데이터 넣기
        // 1. 데이터 객체 생성
        Bbs bbs = new Bbs(title, author, content);

        if(imageUri != null){
            bbs.fileUriString = imageUri.toString();
        }

        // 2. 입력할 데이터의 키 생성
        String bbsKey = bbsRef.push().getKey(); // 자동생성된 키를 가져온다
        // 3. 생성된 키를 레퍼런스로 데이터를 입력
        bbsRef.child(bbsKey).setValue(bbs);
        // 데이터 입력후 창 닫기
        finish();
    }

    // 화면의 갤러리 버튼에서 자동 링크
    public void openGallery(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 가. 이미지 선택창 호출
        startActivityForResult(Intent.createChooser(intent, "앱을 선택하세요"),100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode) {
                // 나. 이미지 선택창에서 선택된 이미지의 경로 추출
                case 100:
                    Uri imageUri = data.getData();
                    String filePath = getPathFromUri(this, imageUri);
                    textImage.setText(filePath);
                    break;
            }
        }
    }

    // Uri에서 실제 경로 꺼내는 함수
     public static String getPathFromUri(Context context, Uri uri){
        String realPath = "";
        Cursor cursor = context.getContentResolver().query(uri,null,null,null,null);
         if(cursor.moveToNext()){
            realPath = cursor.getString(cursor.getColumnIndex("_data"));
         }
        cursor.close();
        return realPath;
    }
}
