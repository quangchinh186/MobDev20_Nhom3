package com.example.myapplication.activities.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.activities.ApplicationActivity;
import com.example.myapplication.activities.Authentication.LoginActivity;
import com.example.myapplication.schema.ChatMessage;
import com.example.myapplication.schema.ChatRoom;
import com.example.myapplication.schema.Profile;
import com.example.myapplication.system.BatoSystem;

import org.bson.types.ObjectId;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.annotations.Required;
import io.realm.mongodb.App;


public class ProfileFragment extends Fragment {
    App app = ApplicationActivity.app;

    String[] hoTenVietNam = {
            "Nguyễn Văn A", "Trần Thị Bích", "Lê Văn Cường", "Phạm Thị Diệu", "Hoàng Văn Eo",
            "Vũ Thị Fương", "Bùi Văn Giang", "Đặng Thị Hà", "Lý Văn Hải", "Đỗ Thị Khánh",
            "Ngô Văn Linh", "Hồ Thị Mai", "Chu Văn Nam", "Võ Thị Oanh", "Đinh Văn Phúc",
            "Trương Thị Quỳnh", "Phan Văn Ròm", "Hoàng Thị Sáu", "Nguyễn Văn Tuấn", "Lê Thị Uyên",
            "Mai Văn Vui", "Đào Thị Xinh", "Trịnh Văn Yên", "Đoàn Thị Zụ", "Lương Văn Mười",
            "Lâm Thị Năm", "Nguyễn Văn Sáu", "Phạm Thị Tám", "Hoàng Văn Chín"
    };
    String[] soThich = {
            "Du lịch", "Đọc sách", "Nghe nhạc", "Chơi thể thao", "Nấu ăn",
            "Lập trình", "Học ngoại ngữ", "Ngắm cảnh", "Chụp ảnh", "Gardening",
            "Hội họa", "Thể dục yoga", "Xem phim", "Chơi game", "Meditation",
            "Thể dục ngoại ô", "Chơi nhạc cụ", "Thiền", "Nghiên cứu khoa học", "Du lịch nước ngoài",
            "Học lịch sử", "Đi bơi", "Thể dục dụng cụ", "Viết blog", "Chăm sóc thú cưng",
            "Dã ngoại", "Chơi bóng đá", "Lập kế hoạch sự kiện", "Tập luyện thể hình", "Ghi nhật ký"
    };
    String[] moTaBanThan = {
            "Người yêu thiên nhiên, thích dã ngoại và ngắm cảnh.",
            "Đam mê lập trình và khám phá công nghệ mới.",
            "Người thích nghe nhạc và thường xuyên tham gia các sự kiện âm nhạc.",
            "Yêu thích nấu ăn và thử nghiệm các món ăn mới.",
            "Mọt sách, luôn dành thời gian đọc sách mỗi ngày.",
            "Người yêu thể thao và thường xuyên tham gia các hoạt động thể dục.",
            "Năng động, sáng tạo và luôn tìm kiếm cơ hội mới.",
            "Người thích đi du lịch và khám phá văn hóa mới.",
            "Nghiên cứu viên khoa học, đam mê giải quyết vấn đề.",
            "Người yêu nghệ thuật, thường xuyên tham gia hoạt động hội họa.",
            "Thích thể dục yoga để duy trì sức khỏe tốt.",
            "Đam mê viết blog và chia sẻ kinh nghiệm.",
            "Tự học ngoại ngữ và muốn biết đến nhiều ngôn ngữ hơn.",
            "Không Muốn Chia Sẻ.",
            "Đi bơi để giữ dáng và rèn luyện sức khỏe.",
            "Người yêu thiền, tìm kiếm sự yên bình trong tâm hồn.",
            "Thích chơi nhạc cụ như guitar hoặc piano.",
            "Người sáng tạo, luôn tìm cách mới để làm việc.",
            "Chăm sóc thú cưng với tình yêu và trách nhiệm.",
            "Đam mê viết và xuất bản sách điện tử.",
            "Nghiên cứu về lịch sử và văn hóa các quốc gia.",
            "Thích tập luyện thể hình và tham gia các cuộc thi.",
            "Thành thạo trong việc lập kế hoạch sự kiện.",
            "Người yêu thích môi trường làm việc sáng tạo.",
            "Người yêu động vật và thường xuyên tham gia các hoạt động bảo vệ môi trường.",
            "Yêu thích nghệ thuật và thường xuyên tham gia triển lãm nghệ thuật.",
            "Thích làm việc nhóm và giao tiếp hiệu quả.",
            "Người thích trải nghiệm văn hóa qua ẩm thực.",
            "Đam mê thể dục dụng cụ và thường xuyên tham gia các lớp tập luyện.",
            "Năng động, nhiệt huyết và có tinh thần lạc quan."
    };
    String[] ngheNghiep = {
            "Lập trình viên", "Nhà thiết kế đồ họa", "Kỹ sư cơ khí", "Không Muốn Chia Sẻ",
            "Chuyên viên tài chính", "Nhà hàng chủ", "Giáo viên", "Nhà nghiên cứu khoa học",
            "Nhà sản xuất phim", "Nghệ sĩ hội họa", "Diễn viên", "Nhà báo",
            "Chuyên gia tiếp thị số", "Nhân viên kinh doanh", "Nhà phân tích dữ liệu", "Kiến trúc sư",
            "Nhà vận động viên", "Quản lý dự án", "Nhà xuất bản", "Nhà thơ",
            "Nhà thiết kế web", "Chuyên viên tư vấn tâm lý", "Nhà khoa học dữ liệu", "Nhà hát biểu diễn",
            "Nhà làm phim tài liệu", "Nhà thiết kế thời trang", "Nhà văn", "Nhà tổ chức sự kiện",
            "Nhà thực hiện video trên mạng", "Nhà nghiên cứu thị trường"
    };
    String[] pics = {
            "https://fastly.picsum.photos/id/65/200/200.jpg?hmac=pZrTO_F80X2VYUVpgorpj6xM_WABGhjIXYieK__8B50",
            "https://fastly.picsum.photos/id/960/200/200.jpg?hmac=jBtZLcx2FwawGC7rwl0dNWTD3q1uuB7CjJmALIF9pIg",
            "https://fastly.picsum.photos/id/236/200/200.jpg?hmac=BntZCNnsVBEqCPw_5q7AhpFIO2vQrHXcSLFR_C1i4fo",
            "https://fastly.picsum.photos/id/338/200/200.jpg?hmac=5S5SeR5xW8mbN3Ml7wTTJPePX392JafhcFMGm7IFNy0",
            "https://fastly.picsum.photos/id/680/200/200.jpg?hmac=jlYrp1rK5RIQZTIaHkTW2llzrRXtuYJLeprOC8i40os",
            "https://fastly.picsum.photos/id/849/200/200.jpg?hmac=LwsdGn2endKvoLY10FPqtfqKYCVMbPEp5J6S_tUN1Yg",
            "https://fastly.picsum.photos/id/1008/200/200.jpg?hmac=I0T_cpYR-61pUlB0jVB4I5B7tL0fvzN5MgslAOirM50",
            "https://fastly.picsum.photos/id/991/200/200.jpg?hmac=zdrg88cOolAGaFqNdqLZG2ECaXB0AjpYxU8PNyq3dBw",
            "https://fastly.picsum.photos/id/735/200/200.jpg?hmac=XML4Wv2mXSMlQw0EtA5w4gbgWnEexc-t-ulwq8lAu1o",
            "https://fastly.picsum.photos/id/930/200/200.jpg?hmac=RFuPrtDvQpcnLHYqLKXd8mbb6jxqDE1g0387zdxBVNg",
            "https://fastly.picsum.photos/id/268/200/200.jpg?hmac=I5JAWzISJc5x0jlDhEngvCIwyM0zxRh22iIIzHqOT80",
            "https://fastly.picsum.photos/id/564/200/200.jpg?hmac=uExb18W9rplmCwAJ9SS5NVsLaurpaCTCBuHZdhsW25I",
            "https://fastly.picsum.photos/id/865/200/200.jpg?hmac=YU_vd-bJ6z9YfrQwr6tDOm--FtikU1rNpyxItCoIOgQ",
            "https://fastly.picsum.photos/id/321/200/200.jpg?hmac=V8qQPhFl_8KjI8JgGI74LQepgBOnxdXOuZmBclxHU90",
            "https://fastly.picsum.photos/id/208/200/200.jpg?hmac=J1BdqRgAAAId9wernbPINrW38haBGOtrpEqn3m2wjlY",
            "https://fastly.picsum.photos/id/402/200/200.jpg?hmac=9PZqzeq_aHvVAxvDPNfP6GuD58m4rilq-TUrG4e7V80",
            "https://fastly.picsum.photos/id/336/200/200.jpg?hmac=VZ7MzNM30jINYNf5Oj_8zqPLTDAyKDk6eXWTGnNb4bU",
            "https://fastly.picsum.photos/id/546/200/200.jpg?hmac=qPx0UfEYgljp1xwhEAy3t7xCT8uLxWCGibv7hu6EkwQ",
            "https://fastly.picsum.photos/id/351/200/200.jpg?hmac=E2C8OwTRNgbEan5RzifMH73ENtpcsHSr45mGFQk5mPU",
            "https://fastly.picsum.photos/id/983/200/200.jpg?hmac=dWGIQKhPUTlF4pkeYDou10SJkQTJDRGf4usmJS38cNY",
            "https://fastly.picsum.photos/id/175/200/200.jpg?hmac=5rzD884Hqi9oWr_n5vg0XSc9f6yMlPotvKa8Y0cVzd4",
            "https://fastly.picsum.photos/id/608/200/200.jpg?hmac=-p1htX-mFieavdRDr9vUIJKyDHCXZAY5B35nhdcgIgQ",
            "https://fastly.picsum.photos/id/790/200/200.jpg?hmac=Y1d81XFNx8LJhlNsiwDoDgIn4mF3SK9nTdIVqkkHS9I",
            "https://fastly.picsum.photos/id/3/200/200.jpg?hmac=N5yYUNYl5gOUcaMmTtnNNtx839TN2qaNM4SaXhQl65U",
            "https://fastly.picsum.photos/id/716/200/200.jpg?hmac=IF3XZCw6rDCs7xOWawb1RJaXLQ6ajQuqxQcbwZM1rbE",
            "https://fastly.picsum.photos/id/243/200/200.jpg?hmac=fW5ZwzzyTBy2t2MROp988oq12mZnKwN0coFLhZEE87s",
            "https://fastly.picsum.photos/id/358/200/200.jpg?hmac=Jz0Wznjq-WZNc6RCtBlacITT7zzNvwlMxNnRhP2i6jM",
            "https://fastly.picsum.photos/id/914/200/200.jpg?hmac=7y7KxpocRNcNDWaeoHP1QJslAOGqg-BzFfLyqLHTbpM",
            "https://fastly.picsum.photos/id/581/200/200.jpg?hmac=l2PTQyeWhW42zIrR020S5LHZ2yrX63cSOgZqpeWM0BA",
            "https://fastly.picsum.photos/id/50/200/200.jpg?hmac=Tz-5Oumk5gfW4P4hAiYNsHDjmBVhOzedd8gy4aEsumY",
    };

    String[] thanhTichCaNhan = {
            "Đạt giải Nhất trong cuộc thi Tin học cấp trường.",
            "Tham gia đội tuyển bóng đá trường và đạt giải Á quân giải đấu.",
            "Hoàn thành dự án lớn về trí tuệ nhân tạo trong khóa học Điện toán.",
            "Được nhận chứng chỉ Anh ngữ quốc tế (IELTS) với điểm số 7.5.",
            "Thành viên tích cực trong nhóm tình nguyện, tham gia xây dựng nhà cho người nghèo.",
            "Thiết kế và triển khai website cho một doanh nghiệp nhỏ trong thời gian 2 tháng.",
            "Đồng sáng tạo một ứng dụng di động giáo dục được chấp nhận trong cuộc thi Start-up.",
            "Làm việc nhóm và giành giải Nhất trong cuộc thi Khoa học kỹ thuật cấp trường.",
            "Đạt học bổng xuất sắc năm học vì thành tích học tập đặc biệt.",
            "Thành công trong việc tự học tiếng Pháp và đạt chứng chỉ DELF B1.",
            "Thiết kế và triển khai chiến lược quảng cáo trực tuyến cho một doanh nghiệp khởi nghiệp.",
            "Đạt giải Nhất trong cuộc thi Văn hóa nghệ thuật cấp trường.",
            "Làm việc tình nguyện trong các dự án xây dựng cộng đồng.",
            "Được chọn làm đại diện sinh viên trong cuộc họp với hội đồng trường.",
            "Đồng sáng lập và điều hành clb đọc sách tại trường.",
            "Tham gia đội tuyển thể dục dụng cụ và đạt giải Nhất tại giải đấu trường.",
            "Thực hiện nghiên cứu khoa học về biến đổi khí hậu và đăng bài báo tại hội nghị sinh viên.",
            "Thành công trong việc làm việc từ xa và duy trì hiệu suất cao.",
            "Được mời tham gia chương trình đào tạo quốc tế tại Pháp về kỹ thuật máy tính.",
            "Thành tích cao trong các khóa học trực tuyến về khoa học dữ liệu và máy học.",
            "Nhận giải Thưởng Tài năng Trẻ năm học vì đóng góp tích cực cho cộng đồng.",
            "Thiết kế và triển khai chiến lược truyền thông cho chiến dịch xã hội quan trọng.",
            "Đạt giải Nhất trong cuộc thi Sáng tạo và Khởi nghiệp cấp trường.",
            "Thành viên chủ chốt của đội tình nguyện cứu trợ thảm họa tự nhiên.",
            "Nhận giải Nhất trong cuộc thi Nghiên cứu Khoa học Sinh viên cấp quốc gia.",
            "Tham gia các khóa học quản lý thời gian và đạt kết quả xuất sắc.",
            "Không có gì đặc biệt.",
            "Đạt giải Nhất trong cuộc thi Tiếng Anh cấp trường.",
            "Thành công trong việc làm thêm và đạt danh hiệu Nhân viên xuất sắc của tháng.",
            "Thành lập và quản lý blog cá nhân về chủ đề Khoa học và Công nghệ."
    };

    String[] camNhanVeNguoi = {
            "Sáng tạo và có khả năng đưa ra những ý kiến mới mẻ.",
            "Thành thạo trong giao tiếp và tạo nên môi trường làm việc tích cực.",
            "Tự tin và không ngần ngại đối mặt với thách thức.",
            "Thường xuyên thể hiện sự quan tâm và chăm sóc đến đồng đội.",
            "Rất hứng thú và có sự nhiệt huyết với những dự án mới.",
            "Thể hiện lòng tin và tôn trọng đối với ý kiến của người khác.",
            "Có khả năng giải quyết xung đột và tìm ra giải pháp hiệu quả.",
            "Sáng tạo và có tinh thần nghệ sĩ trong công việc.",
            "Thể hiện sự chính trực và trung thực trong mọi giao tiếp.",
            "Luôn duy trì tinh thần lạc quan và tích cực.",
            "Thích hợp với vai trò lãnh đạo và có khả năng tạo động lực.",
            "Có tâm huyết với các hoạt động xã hội và từ thiện.",
            "Thể hiện sự quyết tâm và kiên nhẫn trong mọi hành động.",
            // Cảm nhận tiêu cực
            "",
            "Thường xuyên trễ giờ và không giữ được thời gian.",
            "Thiếu sự linh hoạt và khả năng thích ứng với thay đổi.",
            "Thể hiện sự kiểm soát và thái độ làm việc cá nhân quá mức.",
            "Thiếu sự chăm chỉ và nỗ lực trong công việc.",
            "Hay than phiền và không chấp nhận được áp lực công việc.",
            "Gặp khó khăn trong việc làm việc nhóm và giữ đồng thuận.",
            "Không thể tin cậy trong việc hoàn thành công việc đúng hạn.",
            "Thiếu sự kiên nhẫn và dễ nổi giận trong tình huống khó khăn.",
            "Thể hiện sự quan ngại quá mức về rủi ro và thất bại.",
            "Khó chấp nhận phản hồi và thích giữ ý kiến cá nhân.",
            "Thiếu lòng tin vào khả năng của đồng đội và chủ nghĩa cá nhân quá mức.",
            "Thường xuyên lạc quan quá mức và không nhận thức rủi ro.",
            "Hay tránh trách nhiệm và đổ lỗi cho người khác.",
            "Không thể nhận phản hồi một cách xây dựng và cởi mở.",
            "Thể hiện sự đắc ý và không linh hoạt trong thay đổi.",
            "Hay thể hiện sự quá mức tự tin và thiếu sự khiêm tốn."
    };

    String[] gender = {"Nữ", "Nam"};
    String[] interest = {"Nam", "Nữ"};

    List<String> names, hobbies, desc, jobs, archive, avts, reviews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button = view.findViewById(R.id.test_btn);
        Button logout = view.findViewById(R.id.logoutBtn);
        names = Arrays.asList(hoTenVietNam);
        hobbies = Arrays.asList(soThich);
        desc = Arrays.asList(moTaBanThan);
        jobs = Arrays.asList(ngheNghiep);
        avts = Arrays.asList(pics);
        archive = Arrays.asList(thanhTichCaNhan);
        reviews = Arrays.asList(camNhanVeNguoi);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < 5; i++){
                    generate();
                }

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogout();
            }
        });
    }

    public void onLogout() {
        BatoSystem.writeBoolean("login", false);
        BatoSystem.writeString("email", "");
        Intent loginActivity = new Intent(getContext(), LoginActivity.class);
        startActivity(loginActivity);
        app.currentUser().logOutAsync(res -> {
            if(res.isSuccess()){
                ApplicationActivity.queryHelper.closeRealm();
                ApplicationActivity.user = null;
                ApplicationActivity.queryHelper = null;
                Log.v("realm", "log out success");
            } else {
                Log.v("realm", "fail: " + res.getError().toString());
            }
        });
    }

    public void generate(){
        Random random = new Random();
        // Generating a random integer
        int randomNumber = random.nextInt(100) % 30;

        Profile profile = new Profile();
        profile.setDob(random());
        profile.setName(names.get(randomNumber));
        profile.setGender(gender[randomNumber % 2]);
        profile.setInterest(interest[randomNumber % 2]);
        profile.setOccupy(jobs.get(randomNumber));
        profile.setDescription(desc.get(randomNumber));
        profile.getAchievement().add(archive.get(randomNumber));
        profile.getReview().add(reviews.get(randomNumber));
        profile.getPhoto().add(avts.get(randomNumber));

        for(int i = 0; i < randomNumber % 4; i++){
            Collections.shuffle(hobbies);
            Collections.shuffle(avts);
            profile.getHobby().add(hobbies.get(randomNumber));
            profile.getPhoto().add(avts.get(randomNumber));
        }

        ApplicationActivity.queryHelper.createUser(profile);

    }

    private Date random(){
        Date startDate = createDate(1980, 1, 1);
        Date endDate = createDate(2005, 12, 31);

        // Calculate the time range in milliseconds
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long range = endTime - startTime;

        // Generate a random time within the range
        long randomTime = ThreadLocalRandom.current().nextLong(range + 1) + startTime;

        // Create a random date from the random time
        return new Date(randomTime);
    }

    private static Date createDate(int year, int month, int day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(year + "-" + month + "-" + day);
        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }

}