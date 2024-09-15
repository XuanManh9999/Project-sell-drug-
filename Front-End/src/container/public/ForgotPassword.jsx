import { Link } from "react-router-dom";
import "../../styles/forgot-password.css"
function ForgotPassword() {
  return (
    <div className="container-forgot-password">
      <div className="forgot-password-main">
        <h2 className="forgot-password-title">Quên mật khẩu</h2>
        <p className="forgot-password-description">
          Nhập địa chỉ email của bạn để nhận hướng dẫn đặt lại mật khẩu.
        </p>
        <div className="forgot-password-input">
          <label htmlFor="email">Email:</label>
          <input
            type="email"
            name="email"
            id="email"
            placeholder="Nhập vào email"
          />
        </div>
        <button className="forgot-password-btn">Gửi hướng dẫn</button>
        <div className="forgot-password-links">
          <p>
            Quay lại <Link to="/dang-nhap">Đăng nhập</Link>
          </p>
        </div>
      </div>
    </div>
  );
}

export default ForgotPassword;
