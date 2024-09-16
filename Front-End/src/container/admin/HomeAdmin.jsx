import { Link, NavLink, Outlet, useLocation } from "react-router-dom";
import "../../styles/home-admin.css";
import PATH from "../../utils/utils-url-route";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function HomeAdmin() {
  const location = useLocation(); // Lấy thông tin location

  const handleClickLogOut = () => {
    localStorage.clear();
  };

  // Kiểm tra nếu path là root thì hiển thị đoạn text "Chào mừng đến với trang quản trị"
  const isRootPath = location.pathname === "/trang-chu";

  return (
    <div className="home-admin-container">
      <nav className="navbar">
        <Link to={"/trang-chu"} className="navbar-brand">
          <h1>HomeAdmin</h1>
        </Link>
        <div className="navbar-menu">
          <Link to={PATH.MANAGE_USER}>Quản lý người dùng</Link>
          <Link to={PATH.MANAGE_BILL}>Quản lý đơn thuốc</Link>
          <Link to={PATH.MANAGE_BILL}>Quản lý hóa đơn</Link>
        </div>
        <div className="navbar-user">
          <span className="username">Xin chào, Admin</span>
          <Link to="/dang-nhap" className="logout" onClick={handleClickLogOut}>
            Đăng xuất
          </Link>
        </div>
      </nav>
      <main className="main-content">
        {isRootPath && <h2>Chào mừng đến với trang quản trị</h2>}
        {/* Chỉ hiển thị khi path là root */}
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
