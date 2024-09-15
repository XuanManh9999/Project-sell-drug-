import { Link, NavLink } from "react-router-dom";
import "../../styles/home-admin.css";

function HomeAdmin() {
  return (
    <div className="home-admin-container">
      <nav className="navbar">
        <div className="navbar-brand">
          <h1>HomeAdmin</h1>
        </div>
        <div className="navbar-menu">
          <NavLink to="/quan-ly-tai-khoan">Quản lý tài khoản</NavLink>
          <NavLink to="/quan-ly-don-thuoc">Quản lý đơn thuốc</NavLink>
          <NavLink to="/quan-ly-hoa-don">Quản lý hóa đơn</NavLink>
        </div>
        <div className="navbar-user">
          <span className="username">Xin chào, Admin</span>
          <Link to="/dang-nhap" className="logout">
            Đăng xuất
          </Link>
        </div>
      </nav>
      <main className="main-content">
        <h2>Chào mừng đến với trang quản trị</h2>
        {/* Nội dung chính của trang sẽ được thêm vào đây */}
      </main>
    </div>
  );
}

export default HomeAdmin;
