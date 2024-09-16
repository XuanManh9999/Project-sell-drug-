import { Link, NavLink, Outlet } from "react-router-dom";
import "../../styles/home-admin.css";
import PATH from "../../utils/utils-url-route";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
function HomeAdmin() {
  const handleClickLogOut = () => {
    localStorage.clear();
  };

  return (
    <div className="home-admin-container">
      <nav className="navbar">
        <Link to={"/trang-chu"} className="navbar-brand">
          <h1>HomeAdmin</h1>
        </Link>
        <div className="navbar-menu">
          <NavLink to="/quan-ly-tai-khoan">Quản lý tài khoản</NavLink>
          <NavLink to="/quan-ly-don-thuoc">Quản lý đơn thuốc</NavLink>
          <NavLink to="/quan-ly-hoa-don">Quản lý hóa đơn</NavLink>
          <NavLink to={PATH.MANAGE_USER}>Quản lý người dùng</NavLink>
        </div>
        <div className="navbar-user">
          <span className="username">Xin chào, Admin</span>
          <Link to="/dang-nhap" className="logout" onClick={handleClickLogOut}>
            Đăng xuất
          </Link>
        </div>
      </nav>
      <main className="main-content">
        <h2>Chào mừng đến với trang quản trị</h2>
        <Outlet />
        {/* Nội dung chính của trang sẽ được thêm vào đây */}
      </main>
      <ToastContainer
        position="top-right"
        autoClose={1500}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="light"
      />
    </div>
  );
}

export default HomeAdmin;
