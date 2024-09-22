import { useLocation } from "react-router-dom";
import "../../styles/detail-bill.css";
import { useEffect, useState } from "react";
import { getDetailBillById } from "../../services/api-bill";

function DetailBill() {
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const [data, setData] = useState({});
  const id = queryParams.get("bill-id");
  const [totalPrice, setTotalPrice] = useState(0);

  useEffect(() => {
    const fetchData = async () => {
      const response = await getDetailBillById(id);
      if (response && response.error == null) {
        setData(response.data);
        calculateTotalPrice(response.data.detailBills); // Tính tổng giá
      } else {
        console.log(response.error?.message || "Failed to fetch bill");
      }
    };
    fetchData();
  }, [id]);

  const calculateTotalPrice = (detailBills) => {
    const total = detailBills.reduce((acc, product) => {
      return acc + product.medicineDTO.price * product.quantity;
    }, 0);
    setTotalPrice(total);
  };

  return (
    <div className="detail-bill-wrapper">
      <h1>Chi tiết hóa đơn</h1>
      <div className="detail-bill-content">
        <div className="customer-info">
          <h2>Thông tin khách hàng</h2>
          <p>
            <strong>Mã hóa đơn:</strong> {data ? data.id : "User Id"}
          </p>
          <p>
            <strong>Tên khách hàng:</strong>{" "}
            {data ? data.customer_name : "User Name"}
          </p>
          <p>
            <strong>Số điện thoại:</strong>{" "}
            {data ? data.customer_phone : "User Phone"}
          </p>
        </div>

        <div className="product-info">
          <h2>Sản phẩm mua</h2>
          <table>
            <thead>
              <tr>
                <th>Mã sản phẩm</th>
                <th>Tên sản phẩm</th>
                <th>Giá</th>
                <th>Số lượng</th>
                <th>Tổng tiền</th>
              </tr>
            </thead>
            <tbody>
              {data &&
                data.detailBills &&
                data.detailBills.length > 0 &&
                data.detailBills.map((product) => (
                  <tr key={product.id}>
                    <td>{product.medicineDTO.id}</td>
                    <td>{product.medicineDTO.name}</td>
                    <td>{product.medicineDTO.price.toLocaleString()} VND</td>
                    <td>{product.quantity}</td>
                    <td>
                      {(
                        product.medicineDTO.price * product.quantity
                      ).toLocaleString()}{" "}
                      VND
                    </td>
                  </tr>
                ))}
            </tbody>
          </table>
        </div>

        <div className="total-price">
          <h2>
            Tổng tiền hóa đơn:{" "}
            <strong>{totalPrice.toLocaleString()} VND</strong>
          </h2>
        </div>
      </div>
    </div>
  );
}

export default DetailBill;
