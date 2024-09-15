import { Link } from "react-router-dom";
import "../../styles/not-found.css";

function NotFound() {
  return (
    <div className="not-found-container">
      <h1>404 - Trang không tìm thấy</h1>
      <p>Rất tiếc, trang bạn tìm không tồn tại.</p>
      <Link to="/" className="home-link">
        Trở về trang chủ
      </Link>
    </div>
  );
}

export default NotFound;
