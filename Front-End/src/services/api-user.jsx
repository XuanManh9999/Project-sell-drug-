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

export { login, register };
