import { useState } from "react";
import ReactPaginate from "react-paginate";
import { toast } from "react-toastify";
import "../../styles/manage-medicine.css";
import { Link } from "react-router-dom";
function ManageBill() {
  const [data, setData] = useState({});
  const [pageCount, setPageCount] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);
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
    setCurrentPage(selectedPage); // Cập nhật trang hiện tại
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
          <label>Email: </label>
          <input
            type="email"
            name="email"
            placeholder="Nhập vào email..."
            onChange={handleInputOnChange}
            value={data.email || ""}
          />
        </div>
        <div className="flex-row-container-manager-user">
          <label>Fullname: </label>
          <input
            type="text"
            name="fullname"
            placeholder="Nhập vào fullname..."
            onChange={handleInputOnChange}
            value={data.fullname || ""}
          />
        </div>
        <div className="flex-row-container-manager-user">
          <label>Age: </label>
          <input
            type="number"
            name="age"
            placeholder="Nhập vào age..."
            onChange={handleInputOnChange}
            value={data.age || ""}
          />
        </div>
        <div className="flex-row-container-manager-user">
          <label>Phone: </label>
          <input
            type="text"
            name="phone"
            placeholder="Nhập vào phone..."
            onChange={handleInputOnChange}
            value={data.phone || ""}
          />
        </div>
        <div className="flex-row-container-manager-user">
          <label>Password: </label>
          <input
            type="password"
            name="password"
            placeholder="Nhập vào password..."
            onChange={handleInputOnChange}
            value={data.password || ""}
          />
        </div>
      </div>
      <div className="action-manage-user">
        <button className="btn-submid-manage-user">Add User</button>
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
                <th>Email</th>
                <th>Fullname</th>
                <th>Age</th>
                <th>Phone</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody></tbody>
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
      </div>
    </>
  );
}

export default ManageBill;
