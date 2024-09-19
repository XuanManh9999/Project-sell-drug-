import AxiosConFig from "../axiosConfig";
const getAllCategories = async (page = 0, size = 10) => {
  let response;
  try {
    response = await AxiosConFig.get(`categories?page=${page}&size=${size}`);
    return response?.data || {};
  } catch (error) {
    return error.response.data;
  }
};

const createCategory = async (body) => {
  let response;
  try {
    response = await AxiosConFig.post("category", body);
    return response?.data || {};
  } catch (error) {
    return error.response.data;
  }
};

const updateCategory = async (body) => {
  let response;
  try {
    response = await AxiosConFig.put("category", body);
    return response?.data || {};
  } catch (error) {
    return error.response.data;
  }
};

const deleteCategory = async (id) => {
  let response;
  try {
    response = await AxiosConFig.delete(`category/${id}`);
    return response?.data || {};
  } catch (error) {
    return error.response.data;
  }
};

const getCategoryById = async (id) => {
  let response;
  try {
    response = await AxiosConFig.get(`category/${id}`);
    return response?.data || {};
  } catch (error) {
    return error.response.data;
  }
};

export {
  getAllCategories,
  createCategory,
  updateCategory,
  deleteCategory,
  getCategoryById,
};
