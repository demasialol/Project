package com.androidapp.youjigom;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.os.Environment.DIRECTORY_PICTURES;

public class CameraActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info__list_);


        // 권한 체크 - 일정 android 버전에서 어플들을 이용하는데 필요하다
        TedPermission.with(getApplicationContext()) //context해주는것
                .setPermissionListener(permissionListener) //권한 체크에 관련된 인터페이스를 호출한다
                .setRationaleMessage("카메라 권한이 필요합니다.") //권한 사용을 허락했을 때 나오는 메세지
                .setDeniedMessage("거부하셨습니다.") //권한 사용을 거부했을 때 나오는 메세지
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                // 안드로이드 외부저장소와 카메라 사용에 대한 권한을 지정
                .check(); //실제 권한 체크는 .check()에서 이뤄짐


        //버튼 클릭시 작동할 작업을 지정하는 온클릭 리스너
        findViewById(R.id.btn_capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //버튼클릭 정상확인
                Toast.makeText(getApplicationContext(), "버튼눌리긴함",Toast.LENGTH_SHORT).show();

                // 외부저장소를 사용하는 카메라 촬영 화면으로 전환해줌
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                //인텐트를 수신할 앱이 있는지 확인하기 위해 Intent 객체에서 resolveActivity()를 호출한다.
                // 결과가 null이 아닌 경우, 인텐트를 처리할 수 있는 앱이 최소한 하나는 있다는 뜻
                if (intent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;  // 촬영한 사진을 저장할 파일 생성
                    try {
                        photoFile = createImageFile(); //임시로 사용할 캐시폴더 경로의 파일 사용
                    } catch (IOException e) {

                    }
                    //파일이 정상적으로 생성되었으면 진행
                    if (photoFile != null) {
                        //URI 가져오기
                        photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), photoFile);
                        //인텐트에 이미지가 저장될 URI담기
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        //인텐트 실행
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });


    }

    //이미지가 저장될 캐쉬 파일을 만듬
    private File createImageFile() throws IOException {
        // 데이터 이름의 중복으로 인한 충돌을 막기위해 SimpleDateFormat을 이용하여 어플이 동작할 때의
        // 시간을 기록 변수에 기록
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //임시로 사용될 파일이름을 만들어줌
        String imageFileName = "TEST_" + timeStamp + "_";
        //외부 저장소를 사용하는 것이기 때문에 다른 어플과 충돌을 막기 위하여 getExternalFilesDir이용
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //실제 임시 파일이름 지정
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        // 파일 저장 : ACTION_VIEW 인텐트에 사용할 경로
        imageFilePath = image.getAbsolutePath();
        return image;
    }


    @Override
    //메인화면-카메라촬영-촬영한 사진 순으로 인텐트가 전환하기 때문에 onActivityResult사용
    //int requestCode, int resultCode, Intent data는 onActivityResult사용시 따라오는 변수
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //휴대폰 안에 파일 형태로 저장된 이미지를 Bitmap으로 만들 때 사용합니다.
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);

            //Exif는 Exchangedable image file format의 줄임말로 사진정보 라고 알아두면 편하다
            ExifInterface exif = null;

            try {
                //exif에 파일을 저장함
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            /* 핸드폰 내부의 카메라를 사용하여 촬영을 하고 사진정보를 받아와 인텐트에 보여줄 때
               보여주고자 하는 사진이 회전되어 의도와는 다르게 표시되는 오류나 사진자체를 가로로 찍을 때
               이미지 회전에 대한 역활을 수행하기 위해서 2개의 변수를 선언해 준다.*/
            int exifOrientation; //회전된 각도
            int exifDegree; // exifOrientation의 값을 받음

            if (exif != null) {
                // TAG_ORIENTATION, ORIENTATION_NORMAL 는 이미지가 얼마나 회전되었는지를 나타낸다.
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegress(exifOrientation);
            } else {
                exifDegree = 0;
            }

            String result = "";
            // 데이터 이름의 중복으로 인한 충돌을 막기위해 SimpleDateFormat을 이용하여 어플이 동작할 때의 시간을 기록
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HHmmss", Locale.getDefault());
            // 데이터 이름의 중복으로 인한 충돌을 막기위해 SimpleDateFormat을 이용하여 어플이 동작할 때의 날짜을 기록
            Date curDate = new Date(System.currentTimeMillis());
            //날짜와 시간을 조합하여 저장할 파일의 이름을 만들어 준다
            String filename = formatter.format(curDate);
            // getExternalStoragePublicDirectory는 공개 디렉터리를 뜻한다. 파일을 저장할 내부 폴더를
            // 공개 디렉터리에 속하는 NEWFOLDER라는 이름의 폴더로 설정하는 구문이다
            String strFolderName = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES)
                    + File.separator + "NEWFOLDER" + File.separator;

            //위의 strFolderName의 경로/이름을 가진 폴더를 지정하는 구문이다.
            File file = new File(strFolderName);
            //디렉토리가 존재하지 않을 경우, 상위 디렉토리까지 생성
            if (!file.exists())
                file.mkdirs();

            // 인스턴스를 생성하여 f 변수에 저장될 이미지 경로/이름을 지정하는 구문이다.
            File f = new File(strFolderName + "/" + filename + ".png");
            result = f.getPath();

            //자바의 입출력 구문이다.
            FileOutputStream fOut = null;
            try {
                //FileOutputStream로 지정한 위에서 생성한 File 인스턴스의 변수f의 파일을 생성
                fOut = new FileOutputStream(f);
                // FileOutputStream 객체 생성시 파일 경로가 유효하지 않으면 FileNotFoundException 발생
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                result = "Save Error fOut";
            }

            // 비트맵 사진 폴더 경로에 저장
            rotate(bitmap, exifDegree).compress(Bitmap.CompressFormat.PNG, 70, fOut);
            try {
                fOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 이미지 뷰에 비트맵을 set하여 이미지 표현
            ((ImageView) findViewById(R.id.iv_result)).setImageBitmap(rotate(bitmap, exifDegree));

        }
    }

    //exifOrientation의 값을 exifdegree에 리턴
    private int exifOrientationToDegress(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
    //입력받은 degree값을 기반으로 원래 보여주고자 했던 이미지로 회전시켜주는 구문
    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    //권한 허용or거부에 따라서 toast 메세지를 출력하는 함수에 대한 코드
    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(getApplicationContext(), "권한이 허용됨",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), "권한이 거부됨",Toast.LENGTH_SHORT).show();
        }
    };


}
