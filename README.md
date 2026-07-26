# Quản lý cửa hàng bán điện thoại

## Giới thiệu

Ứng dụng Java Desktop quản lý các mẫu điện thoại đang bán tại cửa hàng. Chương
trình có giao diện tiếng Việt, tổ chức theo MVC đơn giản và tự lưu dữ liệu sau
mỗi thay đổi.

## Công nghệ sử dụng

- Java 7 trở lên
- Java Swing
- Object Serialization
- Collection Framework (`ArrayList`)
- MVC

Không sử dụng cơ sở dữ liệu hoặc thư viện bên ngoài.

## Chức năng

- Thêm, sửa và xóa điện thoại
- Tìm kiếm không phân biệt hoa thường
- Lọc kết hợp theo loại, hãng, hệ điều hành, tồn kho và khoảng giá
- Sắp xếp theo tên, giá và số lượng
- Thống kê số mẫu, tồn kho, giá trị kho, mẫu đắt nhất và hãng phổ biến
- Lưu và đọc danh sách qua `data/phones.dat`
- Tự tạo 8 bản ghi mẫu khi chưa có dữ liệu

## Cấu trúc project

- `src/model`: lớp trừu tượng `Phone` và hai lớp con
- `src/view`: cửa sổ Swing chính với ba tab quản lý, tìm kiếm và thống kê
- `src/controller`: tiếp nhận sự kiện và điều phối view/service
- `src/service`: nghiệp vụ quản lý điện thoại
- `src/repository`: đọc, ghi dữ liệu serialization
- `src/util`: kiểm tra dữ liệu và định dạng tiền
- `src/exception`: các ngoại lệ nghiệp vụ
- `src/main`: điểm khởi động chương trình

## Cách chạy

### IntelliJ IDEA

1. Chọn **File > Open**, mở thư mục project này.
2. Đánh dấu thư mục `src` là **Sources Root** nếu IDE chưa tự nhận.
3. Chọn Project SDK Java 7 hoặc mới hơn.
4. Mở `src/main/Main.java`, chọn **Run 'Main.main()'**.
5. Đặt working directory là thư mục gốc project để dữ liệu nằm đúng trong `data`.

### NetBeans

1. Tạo project Java Application mới, bỏ chọn tạo Main Class.
2. Sao chép các package trong `src` vào Source Packages, hoặc dùng
   **File > Open Project** nếu NetBeans nhận project.
3. Chọn `main.Main` làm Main Class rồi chạy.

### Eclipse

1. Chọn **File > Import > Existing Projects into Workspace**.
2. Thêm `src` vào Java Build Path nếu cần.
3. Chạy lớp `main.Main` dưới dạng Java Application.

### Dòng lệnh

Windows PowerShell:

```powershell
New-Item -ItemType Directory -Force out
$sources = Get-ChildItem src -Recurse -Filter *.java | ForEach-Object FullName
javac -encoding UTF-8 -source 7 -target 7 -d out $sources
java -cp out main.Main
```

Lớp cần chạy là **`main.Main`**.

## Dữ liệu

Toàn bộ danh sách được serialization vào `data/phones.dat`. Thư mục và tệp được
tạo tự động. Nếu chưa có dữ liệu, chương trình tạo đúng một lần 8 điện thoại mẫu.
Không nên chỉnh sửa tệp nhị phân bằng trình soạn thảo văn bản.

Để đặt lại dữ liệu mẫu, đóng chương trình rồi đổi tên hoặc di chuyển
`data/phones.dat` sang nơi sao lưu. Khi chạy lại, chương trình sẽ tạo file và
tám bản ghi mẫu mới. Không xóa file khi chương trình đang chạy.

## Thành viên

| STT | Họ và tên | Mã sinh viên | Công việc chính | Tỷ lệ |
| --- | --- | --- | --- | --- |
| 1 | Trịnh Minh Đức | B23DCCN192 | Model, OOP, repository, lưu dữ liệu, validation và exception | 50% |
| 2 | Trịnh Xuân Thắng | B23DCCN757 | View, controller, tìm kiếm, lọc, sắp xếp và thống kê | 50% |

## OOP đã áp dụng

- **Đóng gói:** thuộc tính model là `private`, truy cập qua getter/setter.
- **Kế thừa:** `Smartphone` và `FeaturePhone` kế thừa `Phone`.
- **Trừu tượng:** `Phone` khai báo hành vi tính thuế, loại và thông tin riêng.
- **Đa hình:** service/view quản lý `ArrayList<Phone>` và gọi các hành vi được
  override qua tham chiếu `Phone`.

## Kiểm thử thủ công

| STT | Tình huống | Kết quả mong đợi |
| --- | ---------- | ---------------- |
| 1 | Thêm điện thoại hợp lệ | Thêm và lưu thành công |
| 2 | Thêm trùng mã (khác cả hoa/thường) | Báo mã đã tồn tại |
| 3 | Thêm với giá âm | Báo giá phải lớn hơn 0 |
| 4 | Nhập số lượng không phải số | Báo số lượng phải là số nguyên |
| 5 | Chọn dòng và sửa điện thoại | Bản ghi được cập nhật |
| 6 | Chọn dòng và xóa điện thoại | Hỏi xác nhận rồi xóa |
| 7 | Tìm kiếm theo tên | Hiện các tên phù hợp |
| 8 | Tìm kiếm theo hãng | Hiện các hãng phù hợp |
| 9 | Lọc trạng thái hết hàng | Chỉ hiện số lượng bằng 0 |
| 10 | Lọc trạng thái sắp hết | Chỉ hiện số lượng từ 1 đến 5 |
| 11 | Lọc khoảng giá | Chỉ hiện giá trong khoảng |
| 12 | Sắp xếp giá tăng dần | Giá tăng dần, dữ liệu gốc không đổi |
| 13 | Sắp xếp số lượng giảm dần | Số lượng giảm dần |
| 14 | Đóng và mở lại ứng dụng | Dữ liệu đã thay đổi vẫn còn |
| 15 | Thống kê khi có dữ liệu | Các chỉ số đúng với danh sách |
| 16 | Thống kê khi danh sách rỗng | Các mục đặc biệt hiện “Chưa có dữ liệu” |
