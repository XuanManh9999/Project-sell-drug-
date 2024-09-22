import { useEffect, useState } from "react";
import "../../styles/revenue-statistics.css";
import { getStatistics } from "../../services/api-statistics";
function RevenueStatistics() {
  const [statistics, setStatistics] = useState({
    totalBills: 0,
    totalRevenue: 0,
    totalProductsSold: 0,
    totalAccounts: 0,
  });

  useEffect(() => {
    const fetchData = async () => {
      const { code, message, data } = await getStatistics();
      if (code !== 200) {
        alert(message);
        return;
      }
      setStatistics(data);
    };

    fetchData();
  }, []);

  return (
    <div className="revenue-statistics-wrapper">
      <h1>Thống kê doanh thu</h1>
      <div className="statistics-info">
        <p>
          <strong>Số hóa đơn:</strong> {statistics.totalBills}
        </p>
        <p>
          <strong>Doanh thu:</strong> {statistics.totalRevenue.toLocaleString()}{" "}
          VND
        </p>
        <p>
          <strong>Số sản phẩm đã bán:</strong> {statistics.totalProductsSold}
        </p>
        <p>
          <strong>Số tài khoản trong hệ thống:</strong>{" "}
          {statistics.totalAccounts}
        </p>
      </div>
    </div>
  );
}

export default RevenueStatistics;
