import { Link } from "react-router-dom";
import "../../styles/manage-user.css";
import { useEffect, useRef, useState } from "react";// Hooks
import {
  getUsers,
  deleteUserById,
  getUserById,
  createUser,
  updateUser,
} from "../../services/api-user";
import { validateEmail, isVietnamesePhoneNumber } from "../../utils/utils-func";
import { toast } from "react-toastify";
import ReactPaginate from "react-paginate";
import CONST from "../../utils/utils-const";

// UI NGUOI DUNG
function ManageUser() {
  const handleBtn = useRef(null);
  const [data, setData] = useState({
    id: "",
    fullname: "",
    email: "",
    age: "",
    phone: "",
    password: "",
  });

  const [isDeleteUser, setIsDeleteUser] = useState(false);
  const [isSubmit, setIsSubmit] = useState(false);
  const [users, setUsers] = useState([]);
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

  // Hàm lấy dữ liệu người dùng
  const fetchDataUsers = async (page = 0) => {
    try {
      const response = await getUsers(page, CONST.ITEMS_PERPAGE); // Thay đổi để hỗ trợ phân trang
      if (response && response.error == null) {
        setUsers(response.data.data.content);
        setPageCount(
          Math.ceil(response.data.data.totalElements / CONST.ITEMS_PERPAGE)
        ); // Cập nhật số trang
      }
    } catch (error) {
      toast.error("An error occurred while fetching users");
    }
  };

  // Hàm xử lý chỉnh sửa người dùng
  const editUserById = async (id) => {
    try {
      const { data, error } = await getUserById(id);
      if (error == null) {
        handleBtn.current.textContent = "Edit User";
        handleBtn.current.style.background = "red";
        setData(data);
      } else {
        toast.warn(error.message);
      }
    } catch (error) {
      toast.error("An error occurred while fetching user details");
    }
  };

  // Hàm xử lý xóa người dùng
  const handleDeleteUserById = async (id) => {
    const isDelete = window.confirm("Bạn có chắc chắn muốn xóa không?");
    if (isDelete && id) {
      try {
        const response = await deleteUserById(id);
        if (response && response.code === 200) {
          toast.success(response.message);
          setIsSubmit((prev) => !prev);
        } else {
          toast.error(response.message);
        }
      } catch (error) {
        toast.error("An error occurred while deleting user");
      }
    } else if (!id) {
      toast.warn("ID does not exist");
    }
  };
  // UseEffect để lấy dữ liệu khi component mount hoặc khi có thay đổi từ delete hoặc submit
  useEffect(() => {
    fetchDataUsers(currentPage);
  }, [isDeleteUser, isSubmit, currentPage]);

  // Hàm xử lý submit
  const handleSubmit = async () => {
    const { id, email, password, age, phone, fullname } = data;
    if (!validateEmail(email)) {
      toast.warn("Email is not in correct format");
      return;
    }

    if (password.length < 6) {
      toast.warn("Password must be at least 6 characters");
      return;
    }

    if (fullname.length < 6) {
      toast.warn("Fullname must be at least 6 characters");
      return;
    }
    if (phone != "" && !isVietnamesePhoneNumber(phone)) {
      toast.warn("Invalid phone number, please re-enter");
      return;
    }
    if (age != "" && (age >= 150 || age <= 0)) {
      toast.warn("Invalid age, please try again");
      return;
    }
    if (handleBtn.current.textContent === "Edit User" && id !== "") {
      try {
        const response = await updateUser(data);
        if (response && response.code === 200) {
          toast.success(response.message);
          setIsSubmit((prev) => !prev);
          setData({
            id: "",
            fullname: "",
            email: "",
            age: "",
            phone: "",
            password: "",
          });
          handleBtn.current.textContent = "Thêm người dùng";
          handleBtn.current.style.background = "#008CBA";
        } else {
          toast.error(response.message);
        }
      } catch (error) {
        toast.error("An error occurred while updating user");
      }
    } else {
      try {
        const response = await createUser(data);
        if (response?.code === 201) {
          toast.success(response.message);
          setData({
            id: "",
            fullname: "",
            email: "",
            age: "",
            phone: "",
            password: "",
          });
          setIsSubmit((prev) => !prev);
        } else {
          toast.error(response.message);
        }
      } catch (error) {
        toast.error("An error occurred while creating user");
      }
    }
  };

  // Hàm xử lý thay đổi trang
  const handlePageClick = (event) => {
    const selectedPage = event.selected;
    setCurrentPage(selectedPage); // Cập nhật trang hiện tại
  };

  return (
    <>
      <h1 className="title-manage-user">Quản lý người dùng</h1>
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
        <button
          className="btn-submid-manage-user"
          onClick={handleSubmit}
          ref={handleBtn}>
          Thêm người dùng
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
                <th>Email</th>
                <th>Fullname</th>
                <th>Age</th>
                <th>Phone</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {users && users.length > 0 ? (
                users.map((user) => (
                  <tr key={user.id}>
                    <td>{user.id}</td>
                    <td>{user.email}</td>
                    <td>{user.fullname}</td>
                    <td>{user.age}</td>
                    <td>{user.phone}</td>
                    <td className="action-buttons">
                      <button
                        className="btn-edit"
                        onClick={() => editUserById(user.id)}>
                        Edit
                      </button>
                      <button
                        className="btn-delete"
                        onClick={() => handleDeleteUserById(user.id)}>
                        Delete
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td
                    colspan="6"
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
      </div>
    </>
  );
}

export default ManageUser;
