package com.wisethan.bestrefur1.OnlineOrder;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wisethan.bestrefur1.MainActivity;
import com.wisethan.bestrefur1.R;
import com.wisethan.bestrefur1.common.ApiService;
import com.wisethan.bestrefur1.databinding.ActivityUploadPdfBinding;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadOnlineOrderActivity extends AppCompatActivity {

    private static final int REQUEST_PDF = 1;
    private final List<Uri> selectedFiles = new ArrayList<>();
    private PDFFileAdapter PDFFileAdapter;
    private ApiService apiService;

    private ImageView mImageView;

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUploadPdfBinding binding = ActivityUploadPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = findViewById(R.id.recycler_view_pdf);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PDFFileAdapter = new PDFFileAdapter(selectedFiles, this);
        recyclerView.setAdapter(PDFFileAdapter);

        LayoutInflater mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        CoordinatorLayout mCoordinatorLayout = findViewById(R.id.pdf_upload);
        mImageView = (ImageView) mInflater.inflate(R.layout.add_pdf_image, mCoordinatorLayout, false);
        mCoordinatorLayout.addView(mImageView);
        mImageView.setOnClickListener(v -> {
            openFileChooser();
            mImageView.setVisibility(View.INVISIBLE);

        });

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.catalog_preview); // Set custom title
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES) // write timeout
                .readTimeout(1, TimeUnit.MINUTES) // read timeout
                .build();
        // Retrofit 인스턴스 생성
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.inventory.wisethan.com") // 서버의 베이스 URL
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        // API 서비스 생성
        apiService = retrofit.create(ApiService.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_pdf, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_select_pdf) {
            // UploadOnlineOrderActivity로 이동하는 코드
            openFileChooser();
            mImageView.setVisibility(View.INVISIBLE);
            item.getItemId();
            return true;
        } else if (item.getItemId() == R.id.action_upload_pdf) {
            // UploadOnlineOrderActivity로 이동하는 코드
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("PDF 업로드");
            builder.setMessage("현재 PDF파일을 올리시겠습니까?");
            builder.setPositiveButton("예", (dialog, which) -> uploadFiles());
            builder.setNegativeButton("아니오", null);
            builder.create().show();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // 인텐트에서 여러개의 파일 선택가능
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), REQUEST_PDF);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PDF && resultCode == RESULT_OK) {
            if (data != null) {                             // data값이 없을때
                if (data.getData() != null) {               // 이미지를 하나라도 선택한 경우
                    recyclerView.removeAllViews();
                    recyclerView.setAdapter(PDFFileAdapter);
                    selectedFiles.clear();
                    Uri uri = data.getData();
                    selectedFiles.add(uri);
                    Log.e(TAG, "PDF 파일 주소 경로입니다" + uri);
                    PDFFileAdapter.notifyDataSetChanged();

                }
            }
        } else {
            Toast.makeText(this, "파일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }


    private void uploadFiles() {
        if (selectedFiles.isEmpty()) {
            Toast.makeText(this, "파일을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        ProgressBar progressBar = findViewById(R.id.progressbar);
//      Log.e(TAG,"PDF 파일 주소 경로입니다" + selectedFiles.get(0).getPath());
//      파일 업로드를 위한 MultipartBody.Part 리스트 생성
        List<MultipartBody.Part> fileParts = new ArrayList<>();
        Log.e(TAG, "fileUritest////////////" + selectedFiles);
        MultipartBody.Part filePart;
        for (Uri uri : selectedFiles) {
            String filePath = getRealFilePath(uri);
            if (TextUtils.isEmpty(filePath)) {
                continue;
            }
            File file = new File(filePath);
            Log.e(TAG, "업로드 파일 주소 경로입니다" + file.getName());
            RequestBody requestBody = RequestBody.create(file, MediaType.parse("application/pdf"));
            filePart = MultipartBody.Part.createFormData("files", file.getName(), requestBody);
            fileParts.add(filePart);

            Log.e(TAG, "업로드 전 파일 주소 경로입니다" + filePart.body());
        }

        // 파일 업로드 API 호출
        Call<ResponseBody> call = apiService.uploadFiles(fileParts);

        progressBar.setVisibility(View.VISIBLE); // To hide the ProgressBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Toast.makeText(UploadOnlineOrderActivity.this, "파일 업로드 중 입니다", Toast.LENGTH_LONG).show();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Log.d("Call request", call.request().toString());
                Log.d("Call request header", call.request().headers().toString());
                Log.d("Response raw header", response.headers().toString());
                Log.d("Response raw", String.valueOf(response.raw().body()));
                Log.d("Response code", String.valueOf(response.code()));
                if (response.isSuccessful()) {
                    // 파일 업로드 성공
                    Toast.makeText(UploadOnlineOrderActivity.this, "파일 업로드 성공", Toast.LENGTH_SHORT).show();
                    ResponseBody responseBody = response.body();
                    String responseBodyString;
                    try {
                        assert responseBody != null;
                        responseBodyString = responseBody.string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.e("Response body", responseBodyString);
                    progressBar.setVisibility(View.INVISIBLE); // To hide the ProgressBar
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Intent intent = new Intent(UploadOnlineOrderActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    progressBar.setVisibility(View.INVISIBLE); // To hide the ProgressBar
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    // 요청 실패
                    Toast.makeText(UploadOnlineOrderActivity.this, "파일 업로드 실패", Toast.LENGTH_SHORT).show();
                    Log.d("Response errorBody", String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.INVISIBLE); // To hide the ProgressBar
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                // 네트워크 오류 또는 기타 오류 처리
                t.printStackTrace();
                Toast.makeText(UploadOnlineOrderActivity.this, "파일 업로드 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getRealFilePath(Uri uri) {
        String filePath = "";
        if (uri == null) {
            return filePath;
        }

        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                File file = createTemporalFileFrom(inputStream);
                filePath = file.getAbsolutePath();
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    @NonNull
    private File createTemporalFileFrom(@NonNull InputStream inputStream) throws IOException {
        File targetFile = new File(getCacheDir(), "temp.pdf");
        targetFile.createNewFile();
        OutputStream outputStream = Files.newOutputStream(targetFile.toPath());
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        return targetFile;
    }
}
