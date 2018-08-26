package com.lfork.a98620.lfree.data.user.remote;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.lfork.a98620.lfree.base.network.Result;
import com.lfork.a98620.lfree.base.network.api.UserApi;
import com.lfork.a98620.lfree.data.entity.School;
import com.lfork.a98620.lfree.data.entity.User;
import com.lfork.a98620.lfree.data.user.UserDataSource;
import com.lfork.a98620.lfree.util.JSONUtil;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 98620 on 2018/3/23.
 *
 * @author 98620
 */

public class UserRemoteDataSource implements UserDataSource {

    private static UserRemoteDataSource INSTANCE;

    private UserApi api;

    private UserRemoteDataSource() {
        api = UserApi.Companion.create();
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
        Call<Result<User>> call = api.login(user.getUserName(), user.getUserPassword());
        call.enqueue(new Callback<Result<User>>() {
            @Override
            public void onResponse(@NonNull Call<Result<User>> call, @NonNull Response<Result<User>> response) {
                Result<User> result = response.body();
                if (result != null) {
                    User u = result.getData();
                    if (u != null) {
                        callback.succeed(u);
                    } else {
                        callback.failed(result.getMessage());
                    }
                } else {
                    callback.failed("error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result<User>> call, @NonNull Throwable t) {
                callback.failed(t.getMessage());
            }
        });
    }

    @Override
    public void register(GeneralCallback<String> callback, User user) {
        Call<Result<?>> call = api.register(
                user.getStudentId(),
                user.getUserPassword(),
                user.getUserName(),
                user.getSchool().getId());

        call.enqueue(new Callback<Result<?>>() {
            @Override
            public void onResponse(@NonNull Call<Result<?>> call, @NonNull Response<Result<?>> response) {
                Result result = response.body();
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
            public void onFailure(@NonNull Call<Result<?>> call, @NonNull Throwable t) {
                callback.failed(t.toString());
            }
        });
    }

    @Override
    public void updateUserInfo(GeneralCallback<String> callback, User user) {

        if (TextUtils.isEmpty(user.getUserDesc())) {
            user.setUserDesc("");
        }
        if (TextUtils.isEmpty(user.getUserEmail())) {
            user.setUserEmail("");
        }
        if (TextUtils.isEmpty(user.getUserPhone())) {
            user.setUserPhone("");
        }

        Call<Result<User>> call = api.updateUserInfo(user.getUserId(), user.getUserName(), user.getSchool().getId(), user.getUserDesc(), user.getUserEmail(), user.getUserPhone());


        call.enqueue(new Callback<Result<User>>() {
            @Override
            public void onResponse(@NonNull Call<Result<User>> call, @NonNull Response<Result<User>> response) {
                Result<User> result =response.body();
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
            public void onFailure(@NonNull Call<Result<User>> call, @NonNull Throwable t) {
                callback.failed(t.toString());
            }
        });



    }

    @Override
    public void updateUserPortrait(GeneralCallback<String> callback, String studentId, String localFilePath) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), new File(localFilePath));
        MultipartBody.Part photo = MultipartBody.Part.createFormData("image", "image.png", fileBody);

        Call<Result<String>> call = api.updatePortrait(photo, RequestBody.create(null, studentId));
        call.enqueue(new Callback<Result<String>>() {
            @Override
            public void onResponse(@NonNull Call<Result<String>> call, @NonNull Response<Result<String>> response) {
                Result<String> result = response.body();
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
            public void onFailure(@NonNull Call<Result<String>> call, @NonNull Throwable t) {
                callback.failed(t.toString());
            }
        });


    }

    @Override
    public void getUserInfo(GeneralCallback<User> callback, int userId) {
        Call<Result<User>> call = api.getUserInfo(userId + "");
        call.enqueue(new Callback<Result<User>>() {
            @Override
            public void onResponse(@NonNull Call<Result<User>> call, @NonNull Response<Result<User>> response) {
                Result<User> result = response.body();
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
            public void onFailure(@NonNull Call<Result<User>> call, @NonNull Throwable t) {
                callback.failed(t.toString());
            }
        });
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
