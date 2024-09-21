import AxiosConFig from "../axiosConfig";
const getAllBill = async (page = 0, size = 10) => {
  let response;
  try {
    response = await AxiosConFig.get(`bills?page=${page}&size=${size}`);
    return response?.data || {};
  } catch (error) {
    return error.response.data;
  }
};

const getBillById = async (id) => {
  let response;
  try {
    response = await AxiosConFig.get(`bill/${id}`);
    return response?.data || {};
  } catch (error) {
    return error.response.data;
  }
};

const createBill = async (bill) => {
  let response;
  try {
    response = await AxiosConFig.post(`bill`, bill);
    return response?.data || {};
  } catch (error) {
    return error.response.data;
  }
};

const updateBill = async (bill) => {
  let response;
  try {
    response = await AxiosConFig.put(`bill`, bill);
    return response?.data || {};
  } catch (error) {
    return error.response.data;
  }
};

const deleteBill = async (id) => {
  let response;
  try {
    response = await AxiosConFig.delete(`bill/${id}`);
    return response?.data || {};
  } catch (error) {
    return error.response.data;
  }
};

const searchBill = async (params) => {
  let response;
  try {
    response = await AxiosConFig.get(`search-bill`, { params });
    return response?.data || {};
  } catch (error) {
    return error.response.data;
  }
};

export {
  getAllBill,
  getBillById,
  createBill,
  updateBill,
  deleteBill,
  searchBill,
};
