package com.lfork.a98620.lfree.data.user.remote;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.lfork.a98620.lfree.base.network.HttpService;
import com.lfork.a98620.lfree.base.network.Result;
import com.lfork.a98620.lfree.data.entity.School;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.user.UserDataSource;
import com.lfork.a98620.lfree.util.Config;
import com.lfork.a98620.lfree.util.JSONUtil;

import java.io.File;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 98620 on 2018/3/23.
 */

public class UserRemoteDataSource implements UserDataSource {

    private static UserRemoteDataSource INSTANCE;
//    private ArrayList<ResponseGetUser.UserInfo> mCachedUserList = new ArrayList<>();

    private static final String TAG = "IMRemoteDataSource";

    private UserRemoteDataSource() {
    }

    public static UserRemoteDataSource getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            INSTANCE = new UserRemoteDataSource();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void login(final GeneralCallback<User> callback, User user) {
        UserNetService service = HttpService.getNetWorkService(UserNetService.class);
        Call<Result<User>> call = service.login(user.getUserName(), user.getUserPassword());
        call.enqueue(new Callback<Result<User>>() {
            @Override
            public void onResponse(Call<Result<User>> call, Response<Result<User>> response) {
                Result<User> result = response.body();
                if (result != null) {
                    User u = result.getData();
                    if (u != null)
                        callback.succeed(u);
                    else {
                        callback.failed(result.getMessage());
                    }
                } else {
                    callback.failed("error");
                }
            }

            @Override
            public void onFailure(Call<Result<User>> call, Throwable t) {
                callback.failed(t.getMessage());
            }
        });

//        HttpService.getNetWorkService(UserNetService)
//
//        String url = Config.ServerURL + "/22y/user_login";
//
//        RequestBody requestbody = new FormBody.Builder()
//                .add("studentId", user.getUserName())
//                .add("userPassword", user.getUserPassword())
//                .build();
//
//        String responseData = HttpService.getInstance().sendPostRequest(url, requestbody);
//
//        Result<User> result = JSONUtil.parseJson(responseData, new TypeToken<Result<User>>() {
//        });
//
//        if (result != null) {
//            User u = result.getData();
//            if (u != null)
//                callback.succeed(u);
//            else {
//                callback.failed(result.getMessage());
//            }
//        } else {
//            callback.failed("error");
//        }
    }

    @Override
    public void register(GeneralCallback<String> callback, User user) {
        String url = Config.ServerURL + "/22y/user_regist";

        RequestBody requestbody = new FormBody.Builder()
                .add("studentId", user.getStudentId())
                .add("userPassword", user.getUserPassword())
                .add("userName", user.getUserName())
                .add("userSchool.id", user.getSchool().getId())
                .build();

        //Log.d(TAG, "validate s: " + user.getStudentId() + " u:" + user.getUserName() + " p:" + user.getUserPassword());

        String responseData = HttpService.getInstance().sendPostRequest(url, requestbody);

        Result result = JSONUtil.parseJson(responseData, new TypeToken<Result>() {
        });

        if (result != null) {
            //  Log.d(TAG, "register: ");
            if (result.getCode() == 1) {
                callback.succeed(result.getMessage());
            } else {
                callback.failed(result.getMessage());
            }
        } else {
            callback.failed("error 服务器异常");
        }
    }

    @Override
    public User getThisUser() {
        //do nothing here
        return null;
    }

    @Override
    public void getThisUser(GeneralCallback<User> callback) {
        //do nothing here
    }

    @Override
    public boolean saveThisUser(User user) {
        return false;
    }


    @Override
    public void updateThisUser(GeneralCallback<String> callback, User user) {

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("studentId", user.getUserId() + "")
                .addFormDataPart("userName", user.getUserName())
                .addFormDataPart("userSchool.id", user.getSchool().getId());

        if (!TextUtils.isEmpty(user.getUserDesc())) {
            builder.addFormDataPart("userDesc", user.getUserDesc());
        } else {
            builder.addFormDataPart("userDesc", "");
        }

        if (!TextUtils.isEmpty(user.getUserEmail())) {
            builder.addFormDataPart("userEmail", user.getUserEmail());
        } else {
            builder.addFormDataPart("userEmail", "");
        }

        if (!TextUtils.isEmpty(user.getUserPhone())) {
            builder.addFormDataPart("userPhone", user.getUserPhone());
        } else {
            builder.addFormDataPart("userPhone", "");
        }

        RequestBody requestBody = builder.build();

        String responseData = HttpService.getInstance().sendPostRequest("http://www.lfork.top/22y/user_save", requestBody);

        Result<User> result = JSONUtil.parseJson(responseData, new TypeToken<Result<User>>() {
        });

        if (result != null) {
            if (result.getCode() == 1) {
                callback.succeed(result.getMessage());
            } else {
                callback.failed(result.getMessage());
            }
        } else {
            callback.failed("error 服务器异常");
        }
    }

    @Override
    public void updateUserPortrait(GeneralCallback<String> callback, String studentId, String localFilePath) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), new File(localFilePath));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "image.png", fileBody)
                .addFormDataPart("studentId", studentId)
                .build();
        String responseData = HttpService.getInstance().sendPostRequest("http://www.lfork.top/22y/user_imageUpload", requestBody);
        Result<String> result = JSONUtil.parseJson(responseData, new TypeToken<Result<String>>() {
        });

        if (result != null) {
            if (result.getCode() == 1) {
                callback.succeed(result.getData());
            } else {
                callback.failed(result.getMessage());
            }
        } else {
            callback.failed("error 服务器异常");
        }
    }


    @Override
    public void getUserInfo(GeneralCallback<User> callback, int userId) {
        new Thread(() -> {
            RequestBody requestBody = new FormBody.Builder()
                    .add("studentId", userId + "")
                    .build();

            String responseData = HttpService.getInstance().sendPostRequest("http://www.lfork.top/22y/user_info", requestBody);

            Result<User> result = JSONUtil.parseJson(responseData, new TypeToken<Result<User>>() { //GSON
            });

            if (result != null) {
                if (result.getCode() == 1) {
                    callback.succeed(result.getData());
                } else {
                    callback.failed(result.getMessage());
                }
            } else {
                callback.failed("error 服务器异常");
            }

        }).start();

    }

    @Override
    public void getSchoolList(GeneralCallback<List<School>> callback) {

        // http://www.lfork.top/22y/school_getSchoolList
        String jsonData = "[{\"id\":1,\"schoolName\":\"四川大学\"},{\"id\":2,\"schoolName\":\"电子科技大学\"}," +
                "{\"id\":3,\"schoolName\":\"西南交通大学\"},{\"id\":4,\"schoolName\":\"西南财经大学\"}," +
                "{\"id\":5,\"schoolName\":\"西南民族大学\"},{\"id\":6,\"schoolName\":\"中国民用航空飞行学院\"}," +
                "{\"id\":7,\"schoolName\":\"西南石油大学\"},{\"id\":8,\"schoolName\":\"成都理工大学\"}," +
                "{\"id\":9,\"schoolName\":\"西南科技大学\"},{\"id\":10,\"schoolName\":\"成都信息工程大学\"}," +
                "{\"id\":11,\"schoolName\":\"四川理工学院\"},{\"id\":12,\"schoolName\":\"西华大学\"}," +
                "{\"id\":13,\"schoolName\":\"四川农业大学\"},{\"id\":14,\"schoolName\":\"四川师范大学\"}," +
                "{\"id\":15,\"schoolName\":\"西南医科大学\"},{\"id\":16,\"schoolName\":\"成都中医药大学\"}," +
                "{\"id\":17,\"schoolName\":\"川北医学院\"},{\"id\":18,\"schoolName\":\"西华师范大学\"}," +
                "{\"id\":19,\"schoolName\":\"成都师范学院\"},{\"id\":20,\"schoolName\":\"绵阳师范学院\"}," +
                "{\"id\":21,\"schoolName\":\"内江师范学院\"},{\"id\":22,\"schoolName\":\"成都学院\"}," +
                "{\"id\":23,\"schoolName\":\"成都工业学院\"},{\"id\":24,\"schoolName\":\"成都医学院\"}," +
                "{\"id\":25,\"schoolName\":\"乐山师范学院\"},{\"id\":26,\"schoolName\":\"成都体育学院\"}," +
                "{\"id\":27,\"schoolName\":\"四川音乐学院\"},{\"id\":28,\"schoolName\":\"宜宾学院\"}," +
                "{\"id\":29,\"schoolName\":\"四川文理学院\"},{\"id\":30,\"schoolName\":\"攀枝花学院\"}," +
                "{\"id\":31,\"schoolName\":\"四川旅游学院\"},{\"id\":32,\"schoolName\":\"四川警察学院\"}," +
                "{\"id\":33,\"schoolName\":\"四川民族学院\"},{\"id\":34,\"schoolName\":\"阿坝师范学院\"}," +
                "{\"id\":35,\"schoolName\":\"西昌学院\"},{\"id\":36,\"schoolName\":\"四川传媒学院\"}," +
                "{\"id\":37,\"schoolName\":\"四川电影电视学院\"},{\"id\":38,\"schoolName\":\"成都文理学院\"}," +
                "{\"id\":39,\"schoolName\":\"四川工商学院\"},{\"id\":40,\"schoolName\":\"成都东软学院\"}," +
                "{\"id\":41,\"schoolName\":\"四川工业科技学院\"},{\"id\":42,\"schoolName\":\"四川文化艺术学院\"}," +
                "{\"id\":43,\"schoolName\":\"四川大学锦城学院\"},{\"id\":44,\"schoolName\":\"四川大学锦江学院\"}," +
                "{\"id\":45,\"schoolName\":\"电子科技大学成都学院\"},{\"id\":46,\"schoolName\":\"西南财经大学天府学院\"}," +
                "{\"id\":47,\"schoolName\":\"西南交通大学希望学院\"},{\"id\":48,\"schoolName\":\"成都理工大学工程技术学院\"}," +
                "{\"id\":49,\"schoolName\":\"成都信息工程大学银杏酒店管理学院\"}," +
                "{\"id\":50,\"schoolName\":\"四川外国语大学成都学院\"},{\"id\":51,\"schoolName\":\"西南科技大学城市学院\"}]";

        List<School> schools = JSONUtil.parseJson(jsonData, new TypeToken<List<School>>() {
        });
        callback.succeed(schools);
    }
}
