import { useEffect, useState, useRef } from "react";
import ReactPaginate from "react-paginate";
import { toast } from "react-toastify";
import "../../styles/manage-medicine.css";
import { Link } from "react-router-dom";
import {
  getAllCategories,
  createCategory,
  deleteCategory,
  getCategoryById,
  updateCategory,
} from "../../services/api-categories";
import CONST from "../../utils/utils-const";
function ManageCategory() {
  const handleBtn = useRef(null);
  const [isDeleteUser, setIsDeleteUser] = useState(false);
  const [isSubmit, setIsSubmit] = useState(false);
  const [data, setData] = useState({
    id: "",
    name: "",
    description: "",
  });
  const [pageCount, setPageCount] = useState(0);
  const [responseData, setResponseData] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);

  // Hàm xử lý thay đổi input
  const handleInputOnChange = (event) => {
    const { name, value } = event.target;
    setData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  useEffect(() => {
    // Hàm lấy dữ liệu người dùng
    const fetchDataCategories = async (page = 0) => {
      try {
        const response = await getAllCategories(page, CONST.ITEMS_PERPAGE); // Thay đổi để hỗ trợ phân trang
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
  }, [currentPage, isSubmit, isDeleteUser]);

  const editCategoryById = async (id) => {
    if (id) {
      const { code, data } = await getCategoryById(id);
      if (code === 200) {
        setData({ ...data });
        handleBtn.current.textContent = "Sửa loại thuốc";
        handleBtn.current.style.background = "red";
      } else {
        toast.warn("Not found category by id");
      }
    } else {
      toast.warn("id category is not found");
    }
  };

  const handleDeleteCategoryById = async (id) => {
    const isDelete = window.confirm("Bạn có chắc chắn muốn xóa không?");
    if (isDelete && id) {
      try {
        const response = await deleteCategory(id);
        if (response && response.code === 200) {
          toast.success(response.message);
          setIsDeleteUser((prev) => !prev); // Cập nhật để trigger useEffect
        } else {
          toast.error(response.message);
        }
      } catch (error) {
        toast.error("An error occurred while deleting category");
      }
    }
  };

  const handleSubmit = async () => {
    const { id, name, description } = data;

    if (name?.length <= 3) {
      toast.warn("Name category must be at least 3 characters");
      return;
    }
    if (handleBtn.current.textContent == "Thêm loại thuốc") {
      try {
        const responseCreateCategory = await createCategory(data);
        if (responseCreateCategory?.code === 201) {
          toast.success(responseCreateCategory.message);
          setData({
            id: "",
            name: "",
            description: "",
          });
          setIsSubmit((prev) => !prev);
        } else {
          toast.error(responseCreateCategory.message);
        }
      } catch (ex) {
        toast.error(
          responseCreateCategory.error?.message ||
            "An error occurred while creating category "
        );
      }
    } else if (id && handleBtn.current.textContent == "Sửa loại thuốc") {
      const responseUpdate = await updateCategory(data);
      if (
        responseUpdate &&
        responseUpdate?.code &&
        responseUpdate?.code === 200
      ) {
        setIsSubmit((prev) => !prev);
        setData({
          id: "",
          name: "",
          description: "",
        });
        toast.success("Update category success");
        handleBtn.current.textContent = "Thêm loại thuốc";
        handleBtn.current.style.background = "#008CBA";
      } else {
        toast.warning("Update category is not success");
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
      <h1 className="title-manage-user">Quản lý loại thuốc</h1>
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
          <label>Mô tả: </label>
          <input
            type="text"
            name="description"
            placeholder="Nhập vào description..."
            onChange={handleInputOnChange}
            value={data.description || ""}
          />
        </div>
      </div>
      <div className="action-manage-user">
        <button
          className="btn-submid-manage-user"
          onClick={handleSubmit}
          ref={handleBtn}>
          Thêm loại thuốc
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
                <th>Description</th>
                <th>Action </th>
              </tr>
            </thead>
            <tbody>
              {responseData &&
                responseData.length > 0 &&
                responseData?.map((category) => (
                  <tr key={category.id}>
                    <td>{category.id}</td>
                    <td>{category.name}</td>
                    <td>{category.description}</td>
                    <td className="action-buttons">
                      <button
                        className="btn-edit"
                        onClick={() => editCategoryById(category.id)}>
                        Edit
                      </button>
                      <button
                        className="btn-delete"
                        onClick={() => handleDeleteCategoryById(category.id)}>
                        Delete
                      </button>
                    </td>
                  </tr>
                ))}
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

export default ManageCategory;
