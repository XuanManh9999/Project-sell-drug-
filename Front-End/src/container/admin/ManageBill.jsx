import { useEffect, useRef, useState } from "react";
import ReactPaginate from "react-paginate";
import { toast } from "react-toastify";
import "../../styles/manage-medicine.css";
import { Link } from "react-router-dom";
import { BoxModel } from "../public/index";
import {
  getAllBill,
  getBillById,
  createBill,
  updateBill,
  searchBill,
  deleteBill,
} from "../../services/api-bill";
import CONST from "../../utils/utils-const";
import PATH from "../../utils/utils-url-route";

// UI Hoa Don

function ManageBill() {
  const handleBtn = useRef(null);
  const [data, setData] = useState({});
  const [responseData, setResponseData] = useState([]);
  const [pageCount, setPageCount] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);
  const [isSubmit, setIsSubmit] = useState(false);
  const [isDelete, setIsDelete] = useState(false);
  const [detailBill, setDetailBill] = useState([]); // Lưu chi tiết hóa đơn
  const [isShowModel, setIsShowModel] = useState(false); // Trạng thái hiển thị model
  const [isSendCreate, setIsSendCreate] = useState(false); // Trạng thái gửi request tạo hóa đơn
  // Hàm xử lý thay đổi input
  const handleInputOnChange = (event) => {
    const { name, value } = event.target;
    setData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };
  // Hàm xử lý thay đổi trang
  const handlePageClick = (event) => {
    const selectedPage = event.selected;
    setCurrentPage(selectedPage);
  };

  useEffect(() => {
    // Hàm lấy dữ liệu người dùng
    const fetchDataCategories = async (page = 0) => {
      try {
        const response = await getAllBill(page, CONST.ITEMS_PERPAGE); // Thay đổi để hỗ trợ phân trang
        if (response && response.error == null) {
          setResponseData(response.data.content);
          setPageCount(
            Math.ceil(response.data.totalElements / CONST.ITEMS_PERPAGE)
          ); // Cập nhật số trang
        } else {
          toast.error(response.error?.message || "Failed to fetch categories");
        }
      } catch (error) {
        toast.error(
          "An error occurred while fetching categories: ",
          handleBtn.current.textContent
        );
      }
    };
    fetchDataCategories(currentPage);
  }, [currentPage, isSubmit, isDelete]);

  const handleEditBill = async (id) => {
    if (id) {
      const { code, data } = await getBillById(id);
      if (code === 200) {
        setData(data);
        handleBtn.current.textContent = "Cập nhật hóa đơn";
        handleBtn.current.style.backgroundColor = "red";
        return;
      }
    }
    toast.error("Vui lòng chọn hóa đơn để sửa");
  };

  const handleDeleteBill = async (id) => {
    const isConfirm = window.confirm("Bạn có chắc chắn muốn xóa hóa đơn này?");
    if (id && isConfirm) {
      const { code, message } = await deleteBill(id);
      if (code === 200) {
        toast.success(message);
        setIsDelete(!isDelete);
      } else {
        toast.error(message);
      }
    } else {
      toast.error("Vui lòng chọn hóa đơn để xóa");
    }
  };

  // add bill
  const handleSubmit = async () => {
    const { id, name, customer_name, customer_phone } = data;

    if (!name || !customer_name || !customer_phone) {
      toast.error("Vui lòng nhập đầy đủ thông tin để tiếp tục");
      return;
    }
    if (handleBtn.current.textContent == "Thêm hóa đơn" && id == null) {
      setIsShowModel(true);
    } else {
      const { code, message } = await updateBill(data);
      if (code === 200) {
        toast.success(message);
        setIsSubmit(!isSubmit);
      } else {
        toast.error(message);
      }
      handleBtn.current.textContent = "Thêm hóa đơn";
      handleBtn.current.style.backgroundColor = "#008CBA";
      setData({});
    }
  };

  // search bill
  const handleSearchBill = async () => {
    const data_search = await searchBill(data);
    if (data_search && data_search.error == null) {
      setResponseData(data_search?.data);
      setPageCount(
        Math.ceil(data_search.data.totalElements / CONST.ITEMS_PERPAGE)
      );
    } else {
      toast.error(data_search.error?.message || "Failed to fetch categories");
    }
  };

  return (
    <>
      <h1 className="title-manage-user">Quản lý hóa đơn</h1>
      <div className="container-manager-user">
        <div className="flex-row-container-manager-user">
          <label>ID: </label>
          <input
            type="text"
            readOnly
            name="id"
            onChange={handleInputOnChange}
            value={data.id || ""}
          />
        </div>
        <div className="flex-row-container-manager-user">
          <label>Name: </label>
          <input
            type="text"
            name="name"
            placeholder="Nhập vào name..."
            onChange={handleInputOnChange}
            value={data.name || ""}
          />
        </div>
        <div className="flex-row-container-manager-user">
          <label>Customer_name: </label>
          <input
            type="text"
            name="customer_name"
            placeholder="Nhập vào customer_name..."
            onChange={handleInputOnChange}
            value={data.customer_name || ""}
          />
        </div>
        <div className="flex-row-container-manager-user">
          <label>Customer_phone: </label>
          <input
            type="text"
            name="customer_phone"
            placeholder="Nhập vào customer_phone..."
            onChange={handleInputOnChange}
            value={data.customer_phone || ""}
          />
        </div>
      </div>
      <div className="action-manage-user">
        <button
          className="btn-submid-manage-user"
          onClick={handleSubmit}
          ref={handleBtn}>
          Thêm hóa đơn
        </button>
        <button className="btn-submid-manage-user" onClick={handleSearchBill}>
          Tìm kiếm hóa đơn
        </button>
        <Link
          className="btn-submid-manage-user back-to-home-manage-user"
          to={"/trang-chu"}>
          Trở lại trang chủ
        </Link>
      </div>
      <div className="manage-user-wrapper">
        <div className="table-manage-user">
          <table className="table-manage-user">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Name_Customer</th>
                <th>Phone_Customer</th>
                <th>Quantity_Product</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {responseData && responseData.length > 0 ? (
                responseData?.map((bill) => (
                  <tr key={bill.id}>
                    <td>{bill.id}</td>
                    <td>{bill.name}</td>
                    <td>{bill.customer_name}</td>
                    <td>{bill.customer_phone}</td>
                    <td>
                      {Array.isArray(bill?.detailBill)
                        ? bill.detailBill.length
                        : bill.detailBill}
                    </td>
                    <td className="action-buttons">
                      <button
                        className="btn-edit"
                        onClick={() => handleEditBill(bill.id)}>
                        Edit
                      </button>
                      <Link
                        to={`${PATH.DETAIL_BILL}?bill-id=${bill.id}`}
                        className="btn-viewdetail">
                        View Details
                      </Link>
                      <button
                        className="btn-delete"
                        onClick={() => handleDeleteBill(bill.id)}>
                        Delete
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td
                    colSpan="6"
                    style={{
                      textAlign: "center",
                    }}>
                    Không có dữ liệu
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
        <div className="pagination">
          <ReactPaginate
            pageCount={pageCount}
            onPageChange={handlePageClick}
            containerClassName={"pagination-container"}
            pageClassName={"pagination-page"}
            previousClassName={"pagination-previous"}
            nextClassName={"pagination-next"}
            disabledClassName={"pagination-disabled"}
            activeClassName={"pagination-active"}
          />
        </div>
        {isShowModel && (
          <BoxModel
            setIsShowModel={setIsShowModel}
            data={data}
            setIsSubmit={setIsSubmit}
            setData={setData}
          />
        )}
      </div>
    </>
  );
}

export default ManageBill;
