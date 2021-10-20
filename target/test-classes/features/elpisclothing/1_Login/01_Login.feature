@login 
Feature: 01_Đăng nhập 
Background: 
	Given HomePage: mở trang
	When HomePage: đóng chatbox nếu hiển thị 
	And Menu: nhấn hyperlink Tài khoản 
	Then LoginPage: kiểm tra mở trang Đăng nhập với header "Đăng nhập" được hiển thị 

@sucessLogin
Scenario: 01_Đăng nhập thành công 
	Given LoginPage: nhập thông tin đăng nhập 
		| case                         | email                   | matKhau       | 
		| Nhập [Email],[Mật khẩu] đúng | quynhkudo1998@gmail.com | Matkhaula9998 | 
	When LoginPage: nhấn button Đăng nhập 
	Then AccountPage: kiểm tra login thành công với tên acc "QUYNH VUONG" được hiển thị 
	
@unLogin 
Scenario Outline: 02_Đăng nhập không thành công 
	Given LoginPage: nhập thông tin đăng nhập 
		| email   | matKhau   | 
		| <email> | <matKhau> |    
	And LoginPage: nhấn button Đăng nhập 
	Then LoginPage: kiểm tra hiển thị thông báo lỗi 
		| thongBaoLoi   | 
		| <thongBaoLoi> | 
	Examples: 
		| case                         | email    | matKhau       | thongBaoLoi                | 
		| Không nhập email             |          | Matkhaula9998 | Please fill out this field.| 
		
		#		| Nhập [Email]không đúng                | quynhkudo@gmail.com     | Matkhaula9998 | Thông tin đăng nhập không hợp lệ. | 
		#		| Nhập [Mật khẩu] không đúng            | quynhkudo1998@gmail.com | Matkhaula     | Thông tin đăng nhập không hợp lệ. | 
		#		| Nhập [Email],[Mật khẩu] không đúng    | quynhkudo@gmail.com     | Matkhaula     | Thông tin đăng nhập không hợp lệ. | 
		#  