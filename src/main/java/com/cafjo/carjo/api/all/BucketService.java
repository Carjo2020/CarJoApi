package com.cafjo.carjo.api.all;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;


@Service
public class BucketService {
    //    private static String APP_NAME = "rasheeqat";
    private static String BUCKET_MAIN_PATH_NAME = "src";
    private static String PROJECT_NAME = "rasheeqat-47924";
    private static String BUCKET_NAME = "rasheeqat-47924.appspot.com";
//    private static String PATH_CREDENTIALS = "ServiceAccountKey.json";


    public String getBucketPathFromURL(String url) {
        String split_url = url.substring(0, url.indexOf("?"));
        return split_url.substring(split_url.lastIndexOf("appspot.com/") + 12, split_url.length());
    }

    public boolean deleteFromURL(String url) {
        String name = getBucketPathFromURL(url);
        System.out.println("delete name: " + name);
        Bucket bucket = StorageClient.getInstance().bucket(BUCKET_NAME);
        BlobId blobId = BlobId.of(BUCKET_NAME, name);
        return bucket.getStorage().delete(blobId);
    }

    public String getFileUrl(MultipartFile file) throws IOException {
        String name_file = file.getOriginalFilename();
        String type_file = name_file.substring(name_file.lastIndexOf("."), name_file.length());
        InputStream stream = new ByteArrayInputStream(file.getBytes());
        String blobString = BUCKET_MAIN_PATH_NAME + "/" + file.getContentType() + "/" + System.currentTimeMillis() + type_file;
//        System.out.println("blobString: " + blobString);
        Bucket bucket = StorageClient.getInstance().bucket(BUCKET_NAME);
        Blob blob = bucket.create(blobString, stream, file.getContentType(), Bucket.BlobWriteOption.userProject(PROJECT_NAME));
//        FileInputStream serviceAccount = new FileInputStream(PATH_CREDENTIALS);
        String initialString = "{ \"type\": \"service_account\", \"project_id\": \"rasheeqat-47924\", \"private_key_id\": \"57295e3c24d7b82bfc215c0a1f319d9f27c83458\", \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDLKuUDIjIufque\\nQsFgGMB/ankotG5WnaWn5c+TeN3p23Cl525zOLbg0CknI/gAnWT93Hl+liqUaN0p\\n5mgDPS8taD2KRzMBUYiGgOU9wDAxdf4Qi7J18fhnIhU47MCAxOcxkeATSE11uiN/\\nhJNIipHXZiYSnJ4WTM8hIx+WgqrPEsAYMBNqnFg89rvjdwakEjfSxl5aGe0/U0Fg\\nC/ufGhe1BnkfB05BFF6OqUK+o4p0bLjfP44L5REaHMbJD/VErMKNy//6DUUQ6HYZ\\nnHfw8Bf04ewUKX4OZGSL7mYxO5Np2jC93zWuOum2L8biEg3Z/ymj0VF13+xQE0hI\\nQbPM77DtAgMBAAECggEAAk62E9IImzSUbb4OWjFjG6SxmZUVsmV5+IKIrVZwbB6H\\nlKrLO1hyjfJvSo2DBzwXuzZiKYjlFOhtUp4rokBzT7hhB+Kvy/JeyEBLL5IGOhmV\\nTXTODb7gy7MRu01yZkQA6DQ4RPUg7sGyyNj69JZLpRROQbnbpcnbibckvbM1jhfV\\nk8l7fw8xjHFflF98uGRNWaQSRHuZsu37S5IUvrFPSpLfh4H1iLspgjOZtD7ACH0A\\nvn2HAJ3sqf0m669Oukq03ei9QpXRms017HUCjOpXXOpcl9DPTaSjb0KXrps3EI/M\\ng5NkdnCDxlB24df4SpybeCIGIWElJBEPDIyYkPl8wQKBgQDmDIJQ5kq88n3AxYc3\\nVbLYflfQJxz6R9JUPuvXjjoRQbQ3/TBc3U3P9wvPKOu9yixwNJ86gx4Hx0HY0aIw\\npseFzCDmU59aNdjp9Y/Q9mTz2B7IDFgsljgKe+n4j2cCxTr+eQ/sBqDoziDFoFjd\\nhMOKpiVqJsVSPINyTPv7eeQBvQKBgQDiFhc/3Z5S2KhvkVw7HPQKKz6NF2eBZtFx\\nlSFoeLlH88fX/MGa01BkKdz/KOEXGgQ+wItoAYXScGHIMKwbbOhkjjbLAeNcto2F\\nYF/+77CIrnRvSmbjQWS622LqLoirUTU9Qqnmi6LfQouOKbLzmdMnDEp68kMfGvHu\\na77b5agm8QKBgQCovxpET3kZOmMGKZ6y91j2N08gr+AwsshU5cbH2ls7109kFoQI\\nSolOeHAgKGssT4XjVKZMHGvF1uK1MajgOSZ4PbEQYtysJn2TuH4jEOoieDC0G2H+\\nar526khI/J/aR2Srz4/fWUllG/asIJ+u9jkdlhwxznjFhwAAsvnPkKywnQKBgQDU\\npyipJsADdsw/XH9fDqWwZuqH9UqjGV3JMxdgV6UWu4Luckx8X2SPQh0fV4kYnJz/\\nlUXKxxvjS1d93+ISTcbsxiNdoWIE06SbnoHMnhDzDvxvrZDHErw3UaFhWfGtCk5E\\nV87NDexdiQ8Q4cHzhW1pnhL0P6jIuiRzOJ8l4IbgUQKBgEukuCGY2tZiR+nvGv7g\\nNNHaAJag/BJ6sEFog+AUvGlzophGpKQyE4cS++ACtyQZTMOglhZDXzEDn6+zPFGk\\nNwwFes6yYC5R4yV8THiu37zdPGwocwQ2WvjhWvkXljhaKK82eF0BRpUKKuL/TOeJ\\nWY/sjY9mgivY5QXP1NBeNb2C\\n-----END PRIVATE KEY-----\\n\", \"client_email\": \"firebase-adminsdk-fw08q@rasheeqat-47924.iam.gserviceaccount.com\", \"client_id\": \"109358984472615558649\", \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\", \"token_uri\": \"https://oauth2.googleapis.com/token\", \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\", \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-fw08q%40rasheeqat-47924.iam.gserviceaccount.com\" }";
        InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
        java.net.URL signedUrl = blob.signUrl(14, TimeUnit.DAYS, Storage.SignUrlOption.signWith(ServiceAccountCredentials.fromStream(targetStream)));
        System.out.println(signedUrl + "");
        return signedUrl.toString();
    }

}
