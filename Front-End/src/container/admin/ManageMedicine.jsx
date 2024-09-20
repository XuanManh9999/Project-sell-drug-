import { useEffect, useRef, useState } from "react";
import ReactPaginate from "react-paginate";
import { toast } from "react-toastify";
import {
  getAllMedicine,
  createMedicine,
  deleteMedicineById,
  searchMedicine,
  getMedicineById,
  updateMedicineById,
} from "../../services/api-medicine";
import { getCategoriesForm } from "../../services/api-categories";
import "../../styles/manage-medicine.css";
import { Link } from "react-router-dom";
import CONST from "../../utils/utils-const";
function ManageMedicine() {
  const btnSubmid = useRef();
  const [data, setData] = useState({
    name: "",
    quantity: "",
    composition: "",
    dosage: "",
    formulation: "",
    usage_instructions: "",
    url_image: "",
    id_category: "",
  });

  const optionCategories = useRef();

  const [dataResponse, setDataResponse] = useState([]);
  const [categories, setCategories] = useState([]);
  const [pageCount, setPageCount] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);
  const [isSubmid, setIsSubmid] = useState(false);

  const [isDelete, setIsDelete] = useState(false);
  // Hàm xử lý thay đổi input
  const handleInputOnChange = (event) => {
    const { name, value } = event.target;
    setData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  useEffect(() => {
    const fetchData = async () => {
      const response = await getAllMedicine(currentPage, CONST.ITEMS_PERPAGE);
      setDataResponse(response?.data?.content ?? []);
      setPageCount(
        Math.ceil(response.data.totalElements / CONST.ITEMS_PERPAGE)
      ); // Cập nhật số trang
    };
    const fetchDataCategories = async () => {
      const response = await getCategoriesForm();
      setCategories(response?.data);
    };
    fetchData();
    fetchDataCategories();
  }, [isDelete, isSubmid, currentPage]);

  // Hàm xử lý thay đổi trang
  const handlePageClick = (event) => {
    const selectedPage = event.selected;
    setCurrentPage(selectedPage); // Cập nhật trang hiện tại
  };

  const handleEditMedicineByID = async (id) => {
    if (id) {
      const { code, data } = await getMedicineById(id);

      if (code === 200) {
        if (data?.categoryDTO?.id) {
          optionCategories.current.value = data?.categoryDTO?.id;
        }
        setData({ ...data });
        btnSubmid.current.textContent = "Sửa thuốc";
        btnSubmid.current.style.background = "red";
      } else {
        toast.warn("Not found category by id");
      }
    } else {
      toast.warn("id category is not found");
    }
  };

  const handleDeleteMedicineById = async (id) => {
    if (id) {
      const { code, message } = await deleteMedicineById(id);

      if (code === 200) {
        toast.success(message);
        setIsDelete((prev) => !prev);
      } else if (code === 404) {
        toast.error(message);
      }
      return;
    } else {
      toast.error("Id is not found. Please check my email address");
    }
  };

  const handleSubmid = async () => {
    const {
      id,
      name,
      quantity,
      composition,
      dosage,
      formulation,
      usage_instructions,
      url_image,
    } = data;

    if (!name || name.length == 0) {
      toast.warn("Name category must be at least 3 characters");
      return;
    }

    if (quantity <= 0) {
      toast.warn("quantity must be > 0");
      return;
    }
    if (
      !composition ||
      !dosage ||
      !formulation ||
      !usage_instructions ||
      !url_image ||
      composition.length == 0 ||
      dosage.length == 0 ||
      formulation.length == 0 ||
      usage_instructions.length == 0 ||
      url_image.length == 0
    ) {
      toast.warn("data is not empty");
      return;
    }

    if ((btnSubmid.current.textContent = "Sửa thuốc") && id) {
      const { code, message } = await updateMedicineById(data);
      if (code === 200) {
        toast.success(message);
        btnSubmid.current.textContent = "Thêm thuốc";
        btnSubmid.current.style.background = "#008CBA";
        setIsSubmid((prev) => !prev);
      } else if (code === 404) {
        toast.warn(message);
        setIsSubmid((prev) => !prev);
      } else {
        toast.error(message);
        setIsSubmid((prev) => !prev);
      }
      setData({});
    } else if ((btnSubmid.current.textContent = "Thêm thuốc") && !id) {
      const { code, message } = await createMedicine(data);
      if (code === 201) {
        toast.success(message);
        setIsSubmid((prev) => !prev);
      } else if (code === 409) {
        toast.error(message);
      } else {
        toast.error(message);
      }
      setData({});
    }
  };
  const handleSearch = async () => {
    const { code, message, data: dataSearch } = await searchMedicine(data);

    if (code && code == 200) {
      setDataResponse(dataSearch);
    } else {
      toast.error(message);
    }
  };

  return (
    <>
      <h1 className="title-manage-user">Quản lý thuốc</h1>
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
          <label>Quantity: </label>
          <input
            type="number"
            name="quantity"
            placeholder="Nhập vào quantity..."
            onChange={handleInputOnChange}
            value={data.quantity || ""}
          />
        </div>
        <div className="flex-row-container-manager-user">
          <label>Composition: </label>
          <input
            type="text"
            name="composition"
            placeholder="Nhập vào composition..."
            onChange={handleInputOnChange}
            value={data.composition || ""}
          />
        </div>
        <div className="flex-row-container-manager-user">
          <label>Dosage: </label>
          <input
            type="text"
            name="dosage"
            placeholder="Nhập vào dosage..."
            onChange={handleInputOnChange}
            value={data.dosage || ""}
          />
        </div>
        <div className="flex-row-container-manager-user">
          <label>Formulation: </label>
          <input
            type="text"
            name="formulation"
            placeholder="Nhập vào formulation..."
            onChange={handleInputOnChange}
            value={data.formulation || ""}
          />
        </div>
        <div className="flex-row-container-manager-user">
          <label>Usage_instructions: </label>
          <textarea
            type="text"
            name="usage_instructions"
            placeholder="Nhập vào usage_instructions..."
            onChange={handleInputOnChange}
            value={data.usage_instructions || ""}
          />
        </div>

        <div className="flex-row-container-manager-user">
          <label>Url_image: </label>
          <input
            type="text"
            name="url_image"
            placeholder="Nhập vào url_image..."
            onChange={handleInputOnChange}
            value={data.url_image || ""}
          />
        </div>
        <div className="flex-row-container-manager-user">
          <label>Type Category: </label>
          <select
            ref={optionCategories}
            name="id_category"
            onChange={handleInputOnChange}>
            <option value="">------</option>
            {categories &&
              categories?.map((category) => (
                <option key={category.id} value={category?.id}>
                  {category?.name}
                </option>
              ))}
          </select>
        </div>
      </div>
      <div className="action-manage-user">
        <button
          className="btn-submid-manage-user"
          ref={btnSubmid}
          onClick={handleSubmid}>
          Thêm thuốc
        </button>
        <button className="btn-submid-manage-user" onClick={handleSearch}>
          Tìm kiếm
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
                <th>Quantity</th>
                <th>Composition</th>
                <th>Dosage</th>
                <th>Formulation</th>
                <th>Image</th>
                <th>Type Category</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {dataResponse && dataResponse.length > 0 ? (
                dataResponse.map((medicine) => (
                  <tr key={medicine.id}>
                    <td>{medicine.id}</td>
                    <td>{medicine.name}</td>
                    <td>{medicine.quantity}</td>
                    <td>{medicine.composition}</td>
                    <td>{medicine.dosage}</td>
                    <td>{medicine.formulation}</td>
                    <td>
                      <img
                        className="img-thuoc-config"
                        src={medicine.url_image}
                        alt={medicine.name}
                      />
                    </td>
                    <td>
                      {medicine?.categoryDTO?.name ?? medicine?.name_category}
                    </td>
                    <td className="action-buttons">
                      <button
                        className="btn-edit"
                        onClick={() => handleEditMedicineByID(medicine.id)}>
                        Edit
                      </button>
                      <button
                        className="btn-delete"
                        onClick={() => handleDeleteMedicineById(medicine.id)}>
                        Delete
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td
                    colSpan="9"
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

export default ManageMedicine;
