import { useState } from "react";
import "../../styles/register.css";
import { Link } from "react-router-dom";
import { register } from "../../services/api-user";
import { validateEmail } from "../../utils/utils-func";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
function Register() {
  const [data, setData] = useState({});
  const onChangeInput = (event) => {
    const { name, value } = event.target;
    setData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };
  const handleRegister = async () => {
    const { email, fullname, password } = data;
    console.log(email, fullname, password);

    if (!email || !password || !fullname) {
      toast.warning("Fields cannot be left blank");
    } else {
      let isSubmid = true;
      if (!validateEmail(email)) {
        toast.warning("Invalid email");
        isSubmid = false;
      }
      if (fullname?.length <= 6) {
        toast.warning("Invalid fullname");
        isSubmid = false;
      }
      if (password?.length < 6) {
        toast.warning("Invalid password");
        isSubmid = false;
      }
      if (isSubmid) {
        const response = await register({
          fullname,
          email,
          password,
        });
        if (
          response &&
          Object.keys(response?.data).length >= 0 &&
          response?.data?.code === 201
        ) {
          toast.success(response.data?.message);
        } else {
          toast.error(response.error?.message);
        }
      }
    }
  };
  return (
    <div className="container-register">
      <div className="register-main">
        <h2 className="register-title">Đăng ký tài khoản</h2>
        <div className="register-input">
          <label htmlFor="email">Email:</label>
          <input
            type="email"
            name="email"
            id="email"
            placeholder="Nhập vào email"
            onChange={onChangeInput}
          />
        </div>
        <div className="register-input">
          <label htmlFor="fullname">Họ và tên:</label>
          <input
            type="text"
            name="fullname"
            id="fullname"
            placeholder="Nhập vào họ và tên"
            onChange={onChangeInput}
          />
        </div>
        <div className="register-input">
          <label htmlFor="password">Mật khẩu:</label>
          <input
            type="password"
            name="password"
            id="password"
            placeholder="Nhập vào mật khẩu"
            onChange={onChangeInput}
          />
        </div>
        <button className="register-btn" onClick={handleRegister}>
          Đăng ký
        </button>
        <div className="register-links">
          <p>
            Đã có tài khoản? <Link to="/dang-nhap">Đăng nhập</Link>
          </p>
        </div>
      </div>
    </div>
  );
}

export default Register;
