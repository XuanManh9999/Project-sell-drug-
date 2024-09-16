import AxiosConFig from "../axiosConfig";

const login = async (body) => {
  try {
    const response = await AxiosConFig.post("/user/login", body, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    // Trả về dữ liệu trong đối tượng { data: ... } cho dù có dữ liệu từ server hay không
    return {
      data: response.data || {},
      error: null,
    };
  } catch (error) {
    return {
      data: {},
      error: error.response ? error.response.data : error.message,
    };
  }
};

const register = async (body) => {
  try {
    const response = await AxiosConFig.post("/user/register", body, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    return {
      data: response.data || {},
      error: null,
    };
  } catch (error) {
    return {
      data: {},
      error: error.response ? error.response.data : error.message,
    };
  }
};

const getUsers = async () => {
  try {
    const response = await AxiosConFig.get("/user/users");
    return {
      data: response.data.data || {},
      error: null,
    };
  } catch (error) {
    return {
      data: {},
      error: error.response ? error.response.data : error.message,
    };
  }
};

const getUserById = async (id) => {
  try {
    const response = await AxiosConFig.get(`user/${id}`);
    return {
      data: response.data.data || {},
      error: null,
    };
  } catch (error) {
    return {
      data: {},
      error: error.response ? error.response.data : error.message,
    };
  }
};

const deleteUserById = async (id) => {
  try {
    const response = await AxiosConFig.delete(`user/${id}`);
    return {
      data: response.data.data || {},
      error: null,
    };
  } catch (error) {
    return {
      data: {},
      error: error.response ? error.response.data : error.message,
    };
  }
};

export { login, register, getUsers, getUserById, deleteUserById };
