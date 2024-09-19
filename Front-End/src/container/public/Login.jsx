import { useEffect, useState } from "react";
import "../../styles/login.css";
import { Link, useNavigate } from "react-router-dom";
import MainToast from "../../utils/MainToast";
import { login } from "../../services/api-user";
import { validateEmail } from "../../utils/utils-func";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
function Login() {
  const [data, setData] = useState({});
  const nav = useNavigate();
  const handleOnChange = (event) => {
    const { name, value } = event.target;
    setData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };
  const handleLogin = async () => {
    const { email, password } = data;
    if (!email || !password) {
      toast.warning("Fields cannot be left blank");
    } else {
      let isSubmid = true;
      if (!validateEmail(email)) {
        toast.warning("Invalid email");
        isSubmid = false;
      }
      if (password?.length < 6) {
        toast.warning("Invalid password");
        isSubmid = false;
      }
      if (isSubmid) {
        const response = await login({
          email,
          password,
        });

        if (
          response &&
          Object.keys(response?.data).length >= 0 &&
          response?.data?.code === 200
        ) {
          toast.success(response.data?.message);
          localStorage.setItem("email", data.email);
          localStorage.setItem("password", data.password);
          setTimeout(() => {
            nav("/trang-chu");
          }, 1500);
        } else {
          toast.error(response.error?.message);
        }
      }
    }
  };

  return (
    <div className="container-login">
      <div className="main">
        <h2 className="title">Đăng nhập</h2>
        <div className="input">
          <label htmlFor="user-name">Email:</label>
          <input
            type="text"
            name="email"
            id="user-name"
            placeholder="Nhập vào email"
            onChange={handleOnChange}
          />
        </div>
        <div className="input">
          <label htmlFor="pass-word">Mật khẩu:</label>
          <input
            type="password"
            name="password"
            id="pass-word"
            placeholder="Nhập vào mật khẩu"
            onChange={handleOnChange}
          />
        </div>
        <button className="btn" onClick={handleLogin}>
          Đăng nhập
        </button>
        <div className="links">
          <Link to="/dang-ky">Đăng ký</Link>
          <span> | </span>
          <Link to="/quen-mat-khau">Quên mật khẩu?</Link>
        </div>
      </div>
    </div>
  );
}

export default Login;
