import { Link } from "react-router-dom";
import "../../styles/manage-user.css";
import { useEffect, useRef, useState } from "react";
import { getUsers, deleteUserById, getUserById } from "../../services/api-user";
import { toast } from "react-toastify";
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
  const [users, setUsers] = useState([]);

  const handleInputOnchange = (event) => {
    const { name, value } = event.target;
    setData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const editUserById = async (id) => {
    async function editData() {
      const { data, error } = await getUserById(id);
      if (error == null) {
        handleBtn.current.textContent = "Edit User";
        handleBtn.current.style.background = "red";
        setData({ ...data });
      } else {
        toast.warn(error.message);
      }
    }
    editData();
  };

  const deleteUserById = (id) => {
    if (!id) {
      toast.warn("id does not exist");
    } else {
      async function deleteUserById() {
        const response = await deleteUserById(id);
        console.log("Xuan manh check response", response);
      }
      deleteUserById();
    }
  };

  useEffect(() => {
    async function fetchDataUsers() {
      const response = await getUsers();
      if (response && response?.error == null) {
        setUsers(response.data);
      }
    }
    fetchDataUsers();
  }, []);

  return (
    <>
      <h1 className="title-manage-user">Manage User</h1>
      <div className="container-manager-user">
        {/* Form nhập liệu */}
        <div className="flex-row-container-manager-user">
          <label htmlFor="">ID: </label>
          <input
            type="text"
            readOnly
            onChange={handleInputOnchange}
            value={data.id}
          />
        </div>
        <div className="flex-row-container-manager-user">
          <label htmlFor="">Email: </label>
          <input
            type="email"
            name="email"
            placeholder="Nhập vào email..."
            onChange={handleInputOnchange}
            value={data.email}
          />
        </div>
        <div className="flex-row-container-manager-user">
          <label htmlFor="">Fullname: </label>
          <input
            type="text"
            name="fullname"
            placeholder="Nhập vào fullname..."
            onChange={handleInputOnchange}
            value={data.fullname}
          />
        </div>
        <div className="flex-row-container-manager-user">
          <label htmlFor="">Age: </label>
          <input
            type="number"
            name="age"
            placeholder="Nhập vào age..."
            onChange={handleInputOnchange}
            value={data.age}
          />
        </div>
        <div className="flex-row-container-manager-user">
          <label htmlFor="">Phone: </label>
          <input
            type="number"
            name="phone"
            placeholder="Nhập vào phone..."
            onChange={handleInputOnchange}
            value={data.phone}
          />
        </div>
        <div className="flex-row-container-manager-user">
          <label htmlFor="">Password: </label>
          <input
            type="password"
            name="password"
            placeholder="Nhập vào password..."
            onChange={handleInputOnchange}
            value={data.password}
          />
        </div>
      </div>
      <div className="action-manage-user">
        <button className="btn-add-user" ref={handleBtn}>
          Add User
        </button>
        <Link to={"/trang-chu"} className="btn-back-home">
          Back to Home
        </Link>
      </div>
      <div className="manage-user-wrapper">
        <h1 className="title-manage-user">Table User</h1>
        <table className="table-manage-user">
          <thead>
            <tr>
              <th>ID</th>
              <th>Email</th>
              <th>Fullname</th>
              <th>Age</th>
              <th>Phone</th>
              <th>Password</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {users &&
              users.map((item) => (
                <tr key={item?.id}>
                  <td>{item?.id}</td>
                  <td>{item?.email}</td>
                  <td>{item?.fullname}</td>
                  <td>{item?.age}</td>
                  <td>{item?.phone}</td>
                  <td>{item?.password}</td>
                  <td className="action-buttons">
                    <button
                      className="btn-edit"
                      onClick={() => editUserById(item?.id)}>
                      Edit
                    </button>
                    <button
                      className="btn-delete"
                      onClick={() => deleteUserById(item?.id)}>
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
          </tbody>
        </table>
      </div>
    </>
  );
}

export default ManageUser;
