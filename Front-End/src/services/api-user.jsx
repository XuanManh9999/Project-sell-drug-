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

// const getUsers = async () => {
//   try {
//     const response = await AxiosConFig.get("/user/users");
//     return {
//       data: response.data.data || {},
//       error: null,
//     };
//   } catch (error) {
//     return {
//       data: {},
//       error: error.response ? error.response.data : error.message,
//     };
//   }
// };

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
    return response.data || {};
  } catch (error) {
    console.log(error);

    return response.data || {};
  }
};
const createUser = async (body) => {
  try {
    const response = await AxiosConFig.post("user", body);
    return response?.data || {};
  } catch (error) {
    return error.response.data;
  }
};

const updateUser = async (body) => {
  let response;
  try {
    response = await AxiosConFig.put("user", body);
    return response?.data || {};
  } catch (error) {
    return error.response.data;
  }
};

const getUsers = async (page = 0, size = 10) => {
  try {
    const response = await AxiosConFig.get(
      `/user/test/users?page=${page}&size=${size}`
    );
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

export {
  login,
  register,
  getUsers,
  getUserById,
  deleteUserById,
  createUser,
  updateUser,
};
