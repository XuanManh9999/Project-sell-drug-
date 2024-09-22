import { useEffect, useState } from "react";
import "../../styles/box-model.css";
import { getAllMedicine } from "../../services/api-medicine";
import { createBill } from "../../services/api-bill";
import { toast } from "react-toastify";

function formatCurrencyVN(amount) {
  if (!amount) return "0 VNĐ";
  return amount.toLocaleString("vi-VN", { style: "currency", currency: "VND" });
}

function BoxModel({
  setIsShowModel,
  data: dataProps,
  setData: setDataProps,
  setIsSubmit,
}) {
  const [total, setTotal] = useState(0);
  const [checkedProducts, setCheckedProducts] = useState({}); // Lưu trạng thái checkbox của từng sản phẩm
  const [data, setData] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await getAllMedicine();
        if (response && response.error == null) {
          setData(response.data.content);
        } else {
          console.error(response.error?.message || "Failed to fetch data");
        }
      } catch (error) {
        console.error("An error occurred while fetching data: ", error);
      }
    };
    fetchData();
  }, []);

  // Hàm tính tổng tiền
  const calculateTotal = () => {
    let sum = 0;
    document.querySelectorAll(".product-row").forEach((row) => {
      const checkbox = row.querySelector("input[type='checkbox']");
      const quantityInput = row.querySelector("input[type='number']");
      const priceText = row
        .querySelector(".price")
        .textContent.replace(" VNĐ", "");

      if (checkbox.checked) {
        const quantity = parseInt(quantityInput.value) || 0;
        const price = parseInt(priceText) || 0;
        sum += quantity * price;
      }
    });
    setTotal(sum);
  };

  // Hàm thay đổi trạng thái checkbox
  const handleCheckboxChange = (id) => {
    setCheckedProducts((prevState) => ({
      ...prevState,
      [id]: !prevState[id], // Đổi trạng thái checkbox
    }));
    calculateTotal(); // Tính lại tổng tiền
  };

  const handleSubmit = async () => {
    const detailBills = [];
    data.forEach((product) => {
      const checkbox = checkedProducts[product.id];
      const quantityInput = document.querySelector(
        `input[type='number'][data-id='${product.id}']`
      );

      if (checkbox) {
        const quantity = parseInt(quantityInput.value) || 0;
        if (quantity > 0) {
          detailBills.push({ id_medicine: product.id, quantity });
        }
      }
    });
    const dataSubmid = {
      ...dataProps,
      detailBills,
    };
    const { code, message } = await createBill(dataSubmid);
    if (code === 200) {
      toast.success(message);
      setIsSubmit((prev) => !prev);
    } else {
      toast.error(message);
    }
    setIsShowModel(false); // Đóng model
    setDataProps({});
  };

  console.log("xuan manh check data", data);

  return (
    <div className="wrapper">
      <div className="main-container">
        <div className="container-header">
          <h2>Thêm sản phẩm vào hóa đơn</h2>
        </div>

        <div className="container-section-data">
          <table className="product-table">
            <thead>
              <tr>
                <th>Select Product</th>
                <th>Name</th>
                <th>Quantity</th>
                <th>Composition</th>
                <th>Dosage</th>
                <th>Usage Instructions</th>
                <th>Price</th>
              </tr>
            </thead>
            <tbody>
              {data && data.length > 0 ? (
                data.map((product) => (
                  <tr className="product-row" key={product.id}>
                    <td>
                      <input
                        type="checkbox"
                        onChange={() => handleCheckboxChange(product.id)}
                      />
                    </td>
                    <td>
                      <span>{product.name}</span>
                    </td>
                    <td>
                      <input
                        type="number"
                        placeholder="Quantity"
                        data-id={product.id} // Gán data-id để lấy giá trị trong handleSubmit
                        disabled={!checkedProducts[product.id]} // Chỉ cho phép nhập số lượng khi checkbox được chọn
                        onChange={(e) => {
                          const value = Number(e.target.value);
                          const max = product.quantity;
                          const min = 0;

                          // Kiểm tra và điều chỉnh giá trị
                          if (value > max) {
                            e.target.value = max; // Gán giá trị tối đa nếu vượt quá
                          } else if (value < min) {
                            e.target.value = min; // Gán giá trị tối thiểu nếu thấp hơn
                          }

                          // Gọi hàm tính tổng (nếu cần)
                          calculateTotal();
                        }}
                        max={product.quantity}
                        min={0}
                      />
                    </td>
                    <td>
                      <span>{product.composition}</span>
                    </td>
                    <td>
                      <span>{product.dosage}</span>
                    </td>
                    <td>
                      <span>{product.usage_instructions}</span>
                    </td>
                    <td className="price">
                      <span>{product.price || 0} VNĐ</span>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="7" style={{ textAlign: "center" }}>
                    Không có dữ liệu
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>

        {/* Hiển thị tổng tiền */}
        <div className="container-total">
          <h3>Tổng tiền: {formatCurrencyVN(total)}</h3>
        </div>

        <div className="container-footer">
          <button className="submit-btn" onClick={handleSubmit}>
            Submit
          </button>
          <button className="close-btn" onClick={() => setIsShowModel(false)}>
            Close
          </button>
        </div>
      </div>
    </div>
  );
}

export default BoxModel;
